import math
from pathlib import Path
SAMPLE_SIZES = (10, 20, 50, 100, 200, 300)
CONFIDENCE_LEVELS = (0.9, 0.95, 0.99)
DATA_PATH = Path(__file__).resolve().with_name("data.txt")
CONFIDENCE_COEFFICIENTS = {0.9: 1.643, 0.95: 1.960, 0.99: 2.576}


def read_data(path):
    values = []
    for line_number, line in enumerate(
        path.read_text(encoding="utf-8-sig").splitlines(), start=1
    ):
        line = line.strip()
        if not line:
            continue
        try:
            value = float(line.replace(",", "."))
        except ValueError as error:
            raise ValueError(f"Строка {line_number}: неверное число {line!r}") from error
        if not math.isfinite(value):
            raise ValueError(f"Строка {line_number}: число должно быть конечным")
        values.append(value)
    if len(values) < max(SAMPLE_SIZES):
        raise ValueError(f"Нужно минимум 300 чисел, прочитано {len(values)}")
    return values


def average(data):
    n = len(data)
    total = 0
    for i in range(n):
        total += data[i]
    return total / n


def dispersion(data):
    n = len(data)
    mean_value = average(data)
    disp = 0
    for i in range(n):
        disp += (data[i] - mean_value) ** 2
    return disp / (n - 1)


def std(data):
    return dispersion(data) ** 0.5


def variation_coefficient(data):
    mean_value = average(data)
    return std(data) / abs(mean_value)


def epsilon(data, confidence):
    coefficient = CONFIDENCE_COEFFICIENTS[confidence]
    standard_error = (dispersion(data) / len(data)) ** 0.5
    return coefficient * standard_error


def confidence_interval(data, confidence):
    mean_value = average(data)
    eps = epsilon(data, confidence)
    return mean_value - eps, mean_value + eps


def relative_deviation(value, reference):
    return abs(value - reference) / abs(reference) * 100


def main():
    data = read_data(DATA_PATH)
    print(f"Прочитано чисел: {len(data)}. Используются первые 300.")
    print("Дисперсия с делением на N-1. Коэффициент вариации — в долях.")
    print("Интервалы приближённые, по таблице 1 методички.\n")

    characteristics = (
        ("Среднее", average),
        ("Дисперсия", dispersion),
        ("СКО", std),
        ("Коэф. вариации", variation_coefficient),
    )
    reference = data[:300]
    print(f"{'N':>3} | {'Характеристика':<15} | {'Значение':>14} | {'Откл. от N=300, %':>18}")
    for n in SAMPLE_SIZES:
        sample = data[:n]
        for name, calculate in characteristics:
            value = calculate(sample)
            deviation = relative_deviation(value, calculate(reference))
            print(f"{n:3} | {name:<15} | {value:14.6f} | {deviation:18.6f}")

    print("\nДоверительные интервалы математического ожидания:")
    print(f"{'N':>3} | {'p':>4} | {'Нижняя':>12} | {'Верхняя':>12} | {'delta, %':>12} | {'delta':>12}")
    for n in SAMPLE_SIZES:
        sample = data[:n]
        for confidence in CONFIDENCE_LEVELS:
            eps = epsilon(sample, confidence)
            lower, upper = confidence_interval(sample, confidence)
            # Относительная погрешность по формуле (7) методички.
            delta = eps / abs(average(sample)) * 100
            print(f"{n:3} | {confidence:.2f} | {lower:12.6f} | {upper:12.6f} | {delta:12.6f} | {eps:12.6f}")


if __name__ == "__main__":
    main()
