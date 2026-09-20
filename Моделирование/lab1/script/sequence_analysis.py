"""График последовательности, автокорреляция и гистограмма частот.

Запуск и сохранение PNG: python sequence_analysis.py
Зависимость для рисования: python -m pip install matplotlib
Все статистические расчёты выполняются вручную.
"""

import math
from pathlib import Path

from main import DATA_PATH, average, read_data


SAMPLE_SIZE = 300
MAX_LAG = 200
OUTPUT_DIR = Path(__file__).resolve().with_name("sequence_results")


def correlation(x, y):
    """Коэффициент Пирсона по формуле со слайда 4 методички."""
    if len(x) != len(y) or len(x) < 2:
        raise ValueError("Нужны две выборки одинаковой длины, минимум по 2 значения")
    mean_x = average(x)
    mean_y = average(y)
    numerator = 0
    sum_x = 0
    sum_y = 0
    for i in range(len(x)):
        dx = x[i] - mean_x
        dy = y[i] - mean_y
        numerator += dx * dy
        sum_x += dx ** 2
        sum_y += dy ** 2
    denominator = (sum_x * sum_y) ** 0.5
    if denominator == 0:
        raise ValueError("Корреляция постоянной последовательности не определена")
    return numerator / denominator


def autocorrelation(data, lag):
    # Для сдвига k остаётся N-k пар. У двух срезов свои средние.
    if not 0 <= lag <= len(data) - 2:
        raise ValueError("Сдвиг должен оставлять минимум две пары значений")
    return correlation(data[:len(data) - lag], data[lag:])


def linear_trend(data):
    """Прямая a + b*i методом наименьших квадратов."""
    mean_index = (len(data) + 1) / 2
    mean_value = average(data)
    numerator = 0
    denominator = 0
    for i in range(len(data)):
        dx = i + 1 - mean_index
        numerator += dx * (data[i] - mean_value)
        denominator += dx ** 2
    b = numerator / denominator
    a = mean_value - b * mean_index
    print(a, b)
    return a, b


def histogram_frequencies(data):
    # Число интервалов по правилу Стерджеса: ceil(1 + log2(N)).
    bins = math.ceil(1 + math.log2(len(data)))
    left = min(data)
    right = max(data)
    if left == right:
        return [left - 0.5, right + 0.5], [len(data)]
    width = (right - left) / bins
    edges = [left + i * width for i in range(bins + 1)]
    edges[-1] = right
    counts = [0] * bins
    for value in data:
        # [a, b), кроме последнего интервала, включающего максимум.
        for i in range(bins):
            if value < edges[i + 1] or i == bins - 1:
                counts[i] += 1
                break
    return edges, counts


def analyze(data):
    lags = list(range(1, MAX_LAG + 1))
    correlations = [autocorrelation(data, lag) for lag in lags]
    return lags, correlations


def draw_plots(data, lags, correlations, edges, counts):
    import matplotlib
    # Рисуем в PNG без оконного интерфейса и зависимости от Tcl/Tk.
    matplotlib.use("Agg")
    import matplotlib.pyplot as plt

    plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 11})
    indices = list(range(1, len(data) + 1))
    a, b = linear_trend(data)

    fig, ax = plt.subplots(figsize=(12, 5), layout="constrained")
    ax.plot(indices, data, color="#2667ad", linewidth=0.9, label="Исходные значения")
    ax.axhline(average(data), color="#26946c", linestyle="--", label="Среднее")
    ax.plot(indices, [a + b * i for i in indices], color="#d65f32", label="Линейный тренд")
    ax.set(title="Исходная числовая последовательность · N = 300", xlabel="Номер элемента i", ylabel="Значение xᵢ")
    ax.legend()
    ax.grid(alpha=0.2)
    fig.savefig(OUTPUT_DIR / "sequence.png", dpi=160)

    fig, ax = plt.subplots(figsize=(12, 5), layout="constrained")
    ax.vlines(lags, 0, correlations, color="#2667ad", linewidth=1.5)
    ax.plot(lags, correlations, ".", color="#2667ad")
    ax.axhline(0, color="#555555", linewidth=0.8)
    ax.set(title="Автокорреляция исходной последовательности · N = 300", xlabel="Сдвиг k, элементов", ylabel="Коэффициент r(k)")
    ax.set_ylim(min(-0.3, min(correlations) - 0.1), max(0.4, max(correlations) + 0.2))
    ax.grid(alpha=0.2)
    fig.savefig(OUTPUT_DIR / "autocorrelation.png", dpi=160)

    fig, ax = plt.subplots(figsize=(10, 5), layout="constrained")
    ax.bar(edges[:-1], counts, width=[edges[i + 1] - edges[i] for i in range(len(counts))],
           align="edge", color="#2667ad", edgecolor="white")
    ax.set(title=f"Гистограмма частот · N = 300 · {len(counts)} интервалов", xlabel="Значение", ylabel="Число попаданий в интервал")
    ax.grid(axis="y", alpha=0.2)
    fig.savefig(OUTPUT_DIR / "histogram.png", dpi=160)
    plt.close("all")


def main():
    all_data = read_data(DATA_PATH)
    data = all_data[:SAMPLE_SIZE]
    lags, correlations = analyze(data)
    edges, counts = histogram_frequencies(data)
    OUTPUT_DIR.mkdir(exist_ok=True)
    draw_plots(data, lags, correlations, edges, counts)


if __name__ == "__main__":
    main()
