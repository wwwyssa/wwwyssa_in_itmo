"""Генерация по H2 и сравнение с исходной последовательностью.

Запуск: python generated_sequence.py
Другая реализация: python generated_sequence.py --seed 7
Результаты: generated_results/seed_42 (или папка другого seed).
Matplotlib только рисует PNG; характеристики вычисляются вручную.
random.Random даёт равномерные числа, преобразование в H2 написано вручную.
"""

import argparse
import json
import math
import random
from pathlib import Path

from main import (
    DATA_PATH, SAMPLE_SIZES, CONFIDENCE_LEVELS, CONFIDENCE_COEFFICIENTS,
    read_data, dispersion, confidence_interval,
)
from distribution_approximation import fit_hyperexponential, initial_moments
from sequence_analysis import MAX_LAG, autocorrelation, correlation


DEFAULT_SEED = 42
OUTPUT_ROOT = Path(__file__).resolve().with_name("generated_results")


def generate_value(parameters, rng):
    # Два отдельных равномерных числа: выбор компоненты и значение внутри неё.
    branch_uniform = rng.random()
    if branch_uniform < parameters["p"]:
        rate = parameters["lambda1"]
    else:
        rate = parameters["lambda2"]

    value_uniform = rng.random()  # 0 <= u < 1, поэтому 1-u > 0.
    return -math.log(1 - value_uniform) / rate


def generate_sequence(parameters, count, seed):
    if count < 2:
        raise ValueError("Для анализа нужно минимум два значения")
    if not 0 < parameters["p"] < 1:
        raise ValueError("Вероятность компоненты должна быть между 0 и 1")
    for key in ("lambda1", "lambda2"):
        if not math.isfinite(parameters[key]) or parameters[key] <= 0:
            raise ValueError("Интенсивности должны быть конечными и положительными")
    rng = random.Random(seed)
    values = []
    for _ in range(count):
        values.append(generate_value(parameters, rng))
    return values


def characteristics(data):
    m1, m2 = initial_moments(data)
    corrected_variance = dispersion(data)
    standard_deviation = corrected_variance ** 0.5
    cv = standard_deviation / abs(m1) if m1 != 0 else math.nan
    return {
        "m1": m1,
        "m2": m2,
        "Дисперсия": corrected_variance,
        "СКО": standard_deviation,
        "КВ": cv,
    }


def percent_difference(value, reference):
    if reference == 0:
        return math.nan
    return abs(value - reference) / abs(reference) * 100


def number(value):
    return f"{value:.6f}" if math.isfinite(value) else "не определено"


def write_table(path, headers, rows):
    # Сохраняются только числовые результаты с подписями, без выводов.
    widths = [len(header) for header in headers]
    for row in rows:
        for i, cell in enumerate(row):
            widths[i] = max(widths[i], len(cell))
    lines = [" | ".join(header.ljust(width) for header, width in zip(headers, widths))]
    lines.append("-+-".join("-" * width for width in widths))
    for row in rows:
        lines.append(" | ".join(cell.rjust(width) for cell, width in zip(row, widths)))
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def common_histogram(original, generated):
    # Общие границы для честного сравнения двух гистограмм.
    bins = math.ceil(1 + math.log2(max(len(original), len(generated))))
    left = min(min(original), min(generated))
    right = max(max(original), max(generated))
    if left == right:
        return [left - 0.5, right + 0.5], [len(original)], [len(generated)]
    width = (right - left) / bins
    edges = [left + i * width for i in range(bins + 1)]
    edges[-1] = right
    frequencies = []
    for data in (original, generated):
        counts = [0] * bins
        for value in data:
            for i in range(bins):
                if value < edges[i + 1] or i == bins - 1:
                    counts[i] += 1
                    break
        frequencies.append(counts)
    return edges, frequencies[0], frequencies[1]


def save_tables(original, generated, lags, original_acf, generated_acf,
                edges, original_counts, generated_counts, output_dir):
    original_reference = characteristics(original)
    generated_reference = characteristics(generated)
    rows = []
    interval_rows = []
    correlation_rows = []
    for n in SAMPLE_SIZES:
        x = original[:n]
        y = generated[:n]
        original_stats = characteristics(x)
        generated_stats = characteristics(y)
        for key in original_stats:
            x_value = original_stats[key]
            y_value = generated_stats[key]
            rows.append([
                str(n), key, number(x_value), number(y_value),
                number(percent_difference(y_value, x_value)),
                number(percent_difference(x_value, original_reference[key])),
                number(percent_difference(y_value, generated_reference[key])),
            ])
        for confidence in CONFIDENCE_LEVELS:
            x_lower, x_upper = confidence_interval(x, confidence)
            y_lower, y_upper = confidence_interval(y, confidence)
            interval_rows.append([
                str(n), str(confidence), number(x_lower), number(x_upper),
                number(y_lower), number(y_upper),
            ])
        correlation_rows.append([str(n), number(correlation(x, y))])

    write_table(output_dir / "characteristics.txt", [
        "N", "Характеристика", "Исходная X", "Генерированная Y", "|Y-X|/|X|, %",
        "X к X300, %", "Y к Y300, %",
    ], rows)
    write_table(output_dir / "confidence_intervals.txt", [
        "N", "Вероятность", "Нижняя X", "Верхняя X", "Нижняя Y", "Верхняя Y",
    ], interval_rows)
    write_table(output_dir / "correlation.txt", ["N", "Корреляция X и Y"], correlation_rows)
    write_table(output_dir / "autocorrelation.txt", [
        "Сдвиг", "Число пар", "Автокорреляция X", "Автокорреляция Y",
    ], [[str(k), str(len(original) - k), number(rx), number(ry)]
        for k, rx, ry in zip(lags, original_acf, generated_acf)])
    write_table(output_dir / "histogram.txt", [
        "Левая граница", "Правая граница", "Частота X", "Частота Y",
    ], [[number(edges[i]), number(edges[i + 1]), str(original_counts[i]), str(generated_counts[i])]
        for i in range(len(original_counts))])


def plot_comparison(original, generated, lags, original_acf, generated_acf,
                    edges, original_counts, generated_counts, output_dir, seed):
    import matplotlib
    matplotlib.use("Agg")
    import matplotlib.pyplot as plt

    plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 11})
    n = len(original)
    indices = list(range(1, n + 1))
    labels = ("Исходная последовательность", "Сгенерированная последовательность")
    colors = ("#2667ad", "#a54579")

    fig, axes = plt.subplots(2, 1, figsize=(12, 7), sharex=True, sharey=True, layout="constrained")
    for ax, data, label, color in zip(axes, (original, generated), labels, colors):
        ax.plot(indices, data, color=color, linewidth=0.9)
        ax.set(title=label, ylabel="Значение")
        ax.grid(alpha=0.2)
    axes[-1].set_xlabel("Номер элемента")
    fig.suptitle(f"Сравнение значений · N = {n} · seed = {seed}")
    fig.savefig(output_dir / "sequences.png", dpi=160)
    plt.close(fig)

    fig, axes = plt.subplots(2, 1, figsize=(12, 7), sharex=True, sharey=True, layout="constrained")
    for ax, values, label, color in zip(axes, (original_acf, generated_acf), labels, colors):
        ax.vlines(lags, 0, values, color=color, linewidth=1)
        ax.plot(lags, values, ".", color=color, markersize=3)
        ax.axhline(0, color="#555555", linewidth=0.8)
        ax.set(title=label, ylabel="r(k)")
        ax.grid(alpha=0.2)
    axes[-1].set_xlabel("Сдвиг k, элементов")
    fig.suptitle(f"Сравнение автокорреляций · N = {n} · seed = {seed}")
    fig.savefig(output_dir / "autocorrelation.png", dpi=160)
    plt.close(fig)

    fig, ax = plt.subplots(figsize=(12, 6), layout="constrained")
    widths = [edges[i + 1] - edges[i] for i in range(len(original_counts))]
    ax.bar(edges[:-1], original_counts, width=widths, align="edge", color=colors[0],
           alpha=0.45, edgecolor=colors[0], label=labels[0])
    ax.stairs(generated_counts, edges, color=colors[1], linewidth=2, label=labels[1])
    ax.set(title=f"Сравнение гистограмм частот · N = {n} · seed = {seed}",
           xlabel="Значение", ylabel="Число попаданий в интервал")
    ax.legend()
    ax.grid(axis="y", alpha=0.2)
    fig.savefig(output_dir / "histograms.png", dpi=160)
    plt.close(fig)


def run(seed=DEFAULT_SEED):
    original = read_data(DATA_PATH)[:max(SAMPLE_SIZES)]
    parameters = fit_hyperexponential(original)
    generated = generate_sequence(parameters, len(original), seed)
    lags = list(range(1, min(MAX_LAG, len(original) - 2) + 1))
    original_acf = [autocorrelation(original, k) for k in lags]
    generated_acf = [autocorrelation(generated, k) for k in lags]
    edges, original_counts, generated_counts = common_histogram(original, generated)

    output_dir = OUTPUT_ROOT / f"seed_{seed}"
    output_dir.mkdir(parents=True, exist_ok=True)
    (output_dir / "generated_data.txt").write_text(
        "\n".join(repr(value) for value in generated) + "\n", encoding="utf-8"
    )
    metadata = {
        "seed": seed, "sample_size": len(original), "max_lag": lags[-1],
        "distribution": "H2, balanced means", "parameters": parameters,
        "statistics_variance_denominator": "N-1",
        "fit_variance_denominator": "N",
        "confidence_coefficients": CONFIDENCE_COEFFICIENTS,
    }
    (output_dir / "parameters.json").write_text(
        json.dumps(metadata, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
    )
    save_tables(original, generated, lags, original_acf, generated_acf,
                edges, original_counts, generated_counts, output_dir)
    plot_comparison(original, generated, lags, original_acf, generated_acf,
                    edges, original_counts, generated_counts, output_dir, seed)
    return generated


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--seed", type=int, default=DEFAULT_SEED,
                        help="Начальное состояние генератора (по умолчанию 42)")
    args = parser.parse_args()
    run(args.seed)


if __name__ == "__main__":
    main()
