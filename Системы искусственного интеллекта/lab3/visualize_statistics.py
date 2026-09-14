import argparse
from pathlib import Path

import matplotlib
import pandas as pd


BASE_DIR = Path(__file__).resolve().parent
QUANTILES = [0.05, 0.25, 0.50, 0.75, 0.95]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--show", action="store_true", help="Показать окна графиков")
    args = parser.parse_args()
    if not args.show:
        matplotlib.use("Agg")
    import matplotlib.pyplot as plt
    from matplotlib.lines import Line2D

    data = pd.read_csv(BASE_DIR / "california_housing_train.csv")
    numeric = data.select_dtypes(include="number")
    if numeric.empty:
        raise ValueError("Датасет не содержит числовых данных.")

    # Квантили pandas вычисляет с линейной интерполяцией.
    stats = numeric.describe(percentiles=QUANTILES).T
    stats["missing"] = numeric.isna().sum()
    output_dir = BASE_DIR / "statistics_results"
    output_dir.mkdir(exist_ok=True)
    stats.to_csv(output_dir / "statistics.csv", encoding="utf-8-sig", index_label="feature")
    print(f"Строк: {len(data)}, столбцов: {data.shape[1]}")
    print(f"Пропущенных значений: {int(data.isna().sum().sum())}")
    print(stats.to_string(float_format=lambda value: f"{value:.3f}"))

    plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 10})
    figures = []


    fig, ax = plt.subplots(figsize=(19, 5.5))
    ax.axis("off")
    labels = ["Количество", "Среднее", "Станд. откл.", "Минимум",
              "5%", "25%", "50%", "75%", "95%", "Максимум", "Пропуски"]
    columns = ["count", "mean", "std", "min", "5%", "25%", "50%",
               "75%", "95%", "max", "missing"]
    cell_text = []
    for name, row in stats.iterrows():
        values = [f"{row[col]:,.0f}" if col in ("count", "missing")
                  else f"{row[col]:,.3f}" for col in columns]
        cell_text.append([name] + values)
    table = ax.table(cellText=cell_text, colLabels=["Признак"] + labels,
                     colWidths=[0.19] + [0.073] * len(columns),
                     cellLoc="right", loc="center")
    table.auto_set_font_size(False)
    table.set_fontsize(9)
    table.scale(1, 2)
    for (row, col), cell in table.get_celld().items():
        cell.set_edgecolor("white")
        if row == 0:
            cell.set_facecolor("#244967")
            cell.set_text_props(color="white", weight="bold")
        else:
            cell.set_facecolor("#edf3f8" if row % 2 else "#f8fafc")
        if col == 0:
            cell.set_text_props(ha="left")
    ax.set_title(f"California Housing: описательная статистика ({len(data):,} строк)",
                 fontsize=17, pad=18)
    fig.tight_layout(rect=(0, 0.09, 1, 1))
    figures.append((fig, "statistics_table.png"))

    ncols = 3
    nrows = (len(numeric.columns) + ncols - 1) // ncols


    fig, axes = plt.subplots(nrows, ncols, figsize=(16, 3.6 * nrows), squeeze=False)
    for ax, name in zip(axes.flat, numeric.columns):
        values = numeric[name].dropna()
        ax.hist(values, bins=40, color="#4c8fba", edgecolor="white", linewidth=0.4)
        ax.axvline(stats.loc[name, "mean"], color="#cf652d", lw=2, label="Среднее")
        ax.axvline(stats.loc[name, "50%"], color="#287650", lw=2,
                   linestyle="--", label="Медиана")
        ax.set(title=name, xlabel="Значение", ylabel="Количество")
        ax.grid(axis="y", alpha=0.2)
    for ax in list(axes.flat)[len(numeric.columns):]:
        ax.set_visible(False)
    axes.flat[0].legend(fontsize=9)
    fig.suptitle("Распределения признаков", fontsize=18)
    fig.tight_layout(rect=(0, 0, 1, 0.96))
    figures.append((fig, "histograms.png"))

   
    for fig, filename in figures:
        fig.savefig(output_dir / filename, dpi=160, bbox_inches="tight")
    if args.show:
        plt.show()
    plt.close("all")


if __name__ == "__main__":
    main()
