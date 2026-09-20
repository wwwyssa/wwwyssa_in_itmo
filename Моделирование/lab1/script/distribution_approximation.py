import math
from pathlib import Path

from main import DATA_PATH, read_data
from sequence_analysis import histogram_frequencies


SAMPLE_SIZE = 300
OUTPUT_PATH = Path(__file__).resolve().parent / "sequence_results" / "approximation.png"


def initial_moments(data):
    if len(data) < 2:
        raise ValueError("Нужно минимум два значения")
    total = 0
    total_squares = 0
    for value in data:
        if not math.isfinite(value) or value < 0:
            raise ValueError("Для выбранного закона нужны конечные неотрицательные значения")
        total += value
        total_squares += value ** 2
    return total / len(data), total_squares / len(data)


def fit_hyperexponential(data):
    m1, m2 = initial_moments(data)
    if m1 <= 0:
        raise ValueError("Среднее должно быть положительным")

    variance = m2 - m1 ** 2
    cv_squared = variance / m1 ** 2
    if cv_squared <= 1:
        raise ValueError(
            "Гиперэкспоненциальная аппроксимация H2 применяется при CV > 1. "
            "Для этой выборки нужно выбрать другое распределение из задания."
        )

    cv = cv_squared ** 0.5
    p = (1 + ((cv_squared - 1) / (cv_squared + 1)) ** 0.5) / 2
    lambda1 = 2 * p / m1
    lambda2 = 2 * (1 - p) / m1
    return {
        "m1": m1,
        "m2": m2,
        "variance": variance,
        "cv": cv,
        "p": p,
        "lambda1": lambda1,
        "lambda2": lambda2,
    }


def density(x, parameters):
    """f(x) = p*lambda1*exp(-lambda1*x) + (1-p)*lambda2*exp(-lambda2*x)."""
    if x < 0:
        return 0.0
    p = parameters["p"]
    lambda1 = parameters["lambda1"]
    lambda2 = parameters["lambda2"]
    return (
        p * lambda1 * math.exp(-lambda1 * x)
        + (1 - p) * lambda2 * math.exp(-lambda2 * x)
    )


def theoretical_moments(parameters):
    """Моменты полученного закона для проверки совпадения с исходными."""
    p = parameters["p"]
    lambda1 = parameters["lambda1"]
    lambda2 = parameters["lambda2"]
    m1 = p / lambda1 + (1 - p) / lambda2
    m2 = 2 * p / lambda1 ** 2 + 2 * (1 - p) / lambda2 ** 2
    return m1, m2


def plot_approximation(data, parameters):
    import matplotlib
    matplotlib.use("Agg")
    import matplotlib.pyplot as plt

    edges, counts = histogram_frequencies(data)
    widths = [edges[i + 1] - edges[i] for i in range(len(counts))]
    # Плотность сравнивается с counts / (N * ширина), а не с числом попаданий.
    heights = [counts[i] / (len(data) * widths[i]) for i in range(len(counts))]
    grid = [max(data) * i / 1000 for i in range(1001)]
    values = [density(x, parameters) for x in grid]

    plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 11})
    fig, ax = plt.subplots(figsize=(12, 6), layout="constrained")
    ax.bar(edges[:-1], heights, width=widths, align="edge", color="#2667ad",
           alpha=0.6, edgecolor="white", label="Нормированная гистограмма исходных данных")
    ax.plot(grid, values, color="#b83b40", linewidth=2, label="Плотность гиперэкспоненциального закона H₂")
    ax.set(
        title=f"Аппроксимация по двум начальным моментам · N = {len(data)}",
        xlabel="Значение x",
        ylabel="Плотность вероятности",
    )
    ax.text(
        0.98, 0.72,
        f"m₁ = {parameters['m1']:.6f}\nm₂ = {parameters['m2']:.6f}\n"
        f"CV = {parameters['cv']:.6f}\n\n"
        f"p = {parameters['p']:.6f}\n"
        f"λ₁ = {parameters['lambda1']:.8f}\nλ₂ = {parameters['lambda2']:.8f}",
        transform=ax.transAxes, ha="right", va="top",
        bbox={"facecolor": "white", "edgecolor": "#dddddd", "pad": 10},
    )
    ax.legend(loc="upper right")
    ax.set_xlim(left=0)
    ax.set_ylim(bottom=0)
    ax.grid(axis="y", alpha=0.2)
    OUTPUT_PATH.parent.mkdir(exist_ok=True)
    fig.savefig(OUTPUT_PATH, dpi=160)
    plt.close(fig)


def main():
    data = read_data(DATA_PATH)[:SAMPLE_SIZE]
    parameters = fit_hyperexponential(data)
    plot_approximation(data, parameters)
    return parameters


if __name__ == "__main__":
    main()
