"""Линейная регрессия методом наименьших квадратов, только NumPy и Pandas.

Запуск: python lab3/linear_regression.py
Результаты: lab3/regression_results.
"""

from pathlib import Path

import numpy as np
import pandas as pd

from Gauss import Gauss


BASE_DIR = Path(__file__).resolve().parent
TARGET = "median_house_value"
TEST_SIZE = 0.2
SEED = 67
MODEL_FEATURES = {
    "income": ["median_income"],
    "income_location_age": ["median_income", "longitude", "latitude", "housing_median_age"],
    "all_features": ["longitude", "latitude", "housing_median_age", "total_rooms",
                     "total_bedrooms", "population", "households", "median_income"],
}


def train_test_split(data, test_size=TEST_SIZE, seed=SEED):
    test_count = int(np.ceil(len(data) * test_size))
    indices = np.random.default_rng(seed).permutation(len(data))
    test = data.iloc[indices[:test_count]].copy()
    train = data.iloc[indices[test_count:]].copy()
    return train, test


def solve_gauss(matrix, rhs):
    a = np.array(matrix, dtype=float, copy=True)
    b = np.array(rhs, dtype=float, copy=True)
    n = len(b)
    if a.shape != (n, n) or b.shape != (n,):
        raise ValueError("Ожидаются квадратная матрица и одномерный вектор.")
    if not np.isfinite(a).all() or not np.isfinite(b).all():
        raise ValueError("Система содержит NaN или бесконечность.")
    tolerance = np.finfo(float).eps * n * max(float(np.max(np.abs(a))), 1.0)

    solution, status, _ = Gauss(a.tolist(), b.tolist(), tolerance).solve()
    if solution is None or status != 1:
        raise ValueError("Матрица вырождена или плохо обусловлена: "
                         "проверьте линейную зависимость признаков.")
    return np.asarray(solution, dtype=float)


def add_intercept(features):
    """Первый столбец единиц соответствует свободному члену b0."""
    return np.column_stack((np.ones(len(features)), np.asarray(features, dtype=float)))


def fit_ols(features, target):
    x = add_intercept(features)
    y = np.asarray(target, dtype=float)
    return solve_gauss(x.T @ x, x.T @ y)


def predict(features, coefficients):
    return add_intercept(features) @ coefficients


def regression_metrics(actual, predicted):
    """Метрики рассчитаны непосредственно по формулам."""
    actual = np.asarray(actual, dtype=float)
    residuals = actual - predicted
    sse = float(np.sum(residuals ** 2))
    mse = sse / len(actual)
    total = float(np.sum((actual - actual.mean()) ** 2))
    return {"count": len(actual), "SSE": sse, "MSE": mse,
            "RMSE": float(np.sqrt(mse)), "R2": 1 - sse / total if total > 0 else np.nan}


def permutation_importance(features, target, coefficients, repeats=5, seed=SEED):
    """Падение R² при перемешивании одного признака test, без переобучения.

    Каждый столбец перемешивается отдельно. Большое падение показывает,
    что прогнозы этой модели сильно зависят от соответствующего признака.
    """
    rng = np.random.default_rng(seed)
    original = features.to_numpy(dtype=float)
    baseline = regression_metrics(target, predict(original, coefficients))["R2"]
    rows = []
    for column, feature in enumerate(features.columns):
        drops = []
        for _ in range(repeats):
            shuffled = original.copy()
            shuffled[:, column] = rng.permutation(original[:, column])
            shuffled_r2 = regression_metrics(target, predict(shuffled, coefficients))["R2"]
            drops.append(baseline - shuffled_r2)
        rows.append({"feature": feature, "r2_drop_mean": float(np.mean(drops)),
                     "r2_drop_std": float(np.std(drops, ddof=0)), "repeats": repeats})
    return pd.DataFrame(rows).sort_values("r2_drop_mean", ascending=False)



def main():
    data = pd.read_csv(BASE_DIR / "california_housing_train.csv")
    missing_target = int(data[TARGET].isna().sum())
    data = data.dropna(subset=[TARGET])
    train, test = train_test_split(data)
    x_train = train.drop(columns=TARGET)
    x_test = test.drop(columns=TARGET)
    if not all(pd.api.types.is_numeric_dtype(dtype) for dtype in x_train.dtypes):
        raise ValueError("Этот скрипт рассчитан на числовые признаки California Housing.")

    # В исходном датасете все признаки числовые, кодирование не требуется.
    # Все параметры предобработки вычисляются только на train.
    medians = x_train.median()
    if medians.isna().any():
        raise ValueError("В train есть полностью пустой признак.")
    x_train = x_train.fillna(medians)
    x_test = x_test.fillna(medians)
    means = x_train.mean()
    scales = x_train.std(ddof=0)

    train_scaled = (x_train - means) / scales
    test_scaled = (x_test - means) / scales
    y_train = train[TARGET].to_numpy(dtype=float)
    y_test = test[TARGET].to_numpy(dtype=float)

    metric_rows = []
    comparison_rows = []
    coefficient_tables = []
    importance_tables = []
    prediction_tables = {"train": [], "test": []}

    # Обучаем ровно три модели на одинаковых строках, меняя только признаки.
    for model_name, feature_names in MODEL_FEATURES.items():
        model_train = train_scaled[feature_names]
        model_test = test_scaled[feature_names]
        coefficients = fit_ols(model_train, y_train)
        train_prediction = predict(model_train, coefficients)
        test_prediction = predict(model_test, coefficients)
        train_metrics = regression_metrics(y_train, train_prediction)
        test_metrics = regression_metrics(y_test, test_prediction)
        metric_rows.extend([
            {"model": model_name, "split": "train", **train_metrics},
            {"model": model_name, "split": "test", **test_metrics},
        ])
        comparison_rows.append({
            "model": model_name, "features": ", ".join(feature_names),
            "feature_count": len(feature_names), "r2_train": train_metrics["R2"],
            "r2_test": test_metrics["R2"], "r2_gap": train_metrics["R2"] - test_metrics["R2"],
            "rmse_test": test_metrics["RMSE"],
        })

        # Коэффициенты пересчитываются с параметрами выбранных столбцов.
        original_weights = coefficients[1:] / scales[feature_names].to_numpy()
        original_intercept = coefficients[0] - means[feature_names].to_numpy() @ original_weights
        coefficient_tables.append(pd.DataFrame({
            "model": model_name, "term": ["intercept"] + feature_names,
            "standardized": coefficients,
            "original_units": np.concatenate(([original_intercept], original_weights)),
        }))
        importance = permutation_importance(model_test, y_test, coefficients)
        importance.insert(0, "model", model_name)
        importance_tables.append(importance)

        for name, subset, predictions in [("train", train, train_prediction),
                                           ("test", test, test_prediction)]:
            prediction_tables[name].append(pd.DataFrame({
                "model": model_name, "source_index": subset.index,
                "actual": subset[TARGET].to_numpy(), "predicted": predictions,
            }))

    metrics = pd.DataFrame(metric_rows)
    comparison = pd.DataFrame(comparison_rows)
    coefficient_table = pd.concat(coefficient_tables, ignore_index=True)
    importance = pd.concat(importance_tables, ignore_index=True)

    output = BASE_DIR / "regression_results"
    output.mkdir(exist_ok=True)
    coefficient_table.to_csv(output / "coefficients.csv", index=False)
    metrics.to_csv(output / "metrics.csv", index=False)
    comparison.to_csv(output / "model_comparison.csv", index=False)
    importance.to_csv(output / "feature_importance.csv", index=False)
    pd.DataFrame({"median": medians, "mean": means, "scale": scales}).to_csv(
        output / "preprocessing.csv", index_label="feature")
    for name, tables in prediction_tables.items():
        pd.concat(tables, ignore_index=True).to_csv(output / f"{name}_predictions.csv", index=False)

    print(f"Train: {len(train)}, test: {len(test)}, seed: {SEED}")
    print(f"Dropped rows with missing target: {missing_target}")
    print("\nModel comparison:\n", comparison.to_string(index=False))
    print(f"\nResults: {output}")


if __name__ == "__main__":
    main()
