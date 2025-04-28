import numpy as np
from scipy.integrate import quad

# --- Входные данные ---
# Опорные точки начала и конца
P0 = np.array([0.0, 0.0])
P2 = np.array([10.0, 0.0])
# Точка, через которую должна пройти кривая
K = np.array([2.0, 7.0])

# --- Функции ---

def calculate_P1(p0, p2, k, tk):
  """
  Вычисляет контрольную точку P1 для квадратичной кривой Безье,
  проходящей через p0, k, p2, где k соответствует параметру tk.
  """
  # Проверка tk на допустимость
  if tk <= 0 or tk >= 1:
      raise ValueError("tk должен быть в интервале (0, 1)")

  numerator = k - (1 - tk)**2 * p0 - tk**2 * p2
  denominator = 2 * tk * (1 - tk)

  if abs(denominator) < 1e-9:
    # Эта ситуация не должна возникать при tk в (0, 1), но проверка не помешает
    raise ValueError("Знаменатель близок к нулю, невозможно вычислить P1.")

  return numerator / denominator

def bezier_derivative(t, p0, p1, p2):
  """
  Вычисляет вектор производной B'(t) для квадратичной кривой Безье.
  B'(t) = 2(1-t)(P1-P0) + 2t(P2-P1)
  """
  term1 = 2 * (1 - t) * (p1 - p0)
  term2 = 2 * t * (p2 - p1)
  return term1 + term2

def integrand_for_length(t, p0, p1, p2):
  """
  Подынтегральная функция для вычисления длины кривой: ||B'(t)||.
  """
  derivative_vector = bezier_derivative(t, p0, p1, p2)
  # Вычисляем норму (длину) вектора производной
  magnitude = np.linalg.norm(derivative_vector)
  return magnitude

def calculate_curve_length(p0, p1, p2):
  """
  Вычисляет длину кривой Безье от t=0 до t=1 с помощью численного интегрирования.
  """
  length, error = quad(integrand_for_length, 0, 1, args=(p0, p1, p2))
  return length, error

# --- Основной расчет ---

# Задаем диапазон значений tk, которые хотим проверить
# Исключаем 0 и 1, так как знаменатель в calculate_P1 обратится в ноль
tk_values = np.linspace(0.4, 0.5, 9000) # Например, 9 точек от 0.1 до 0.9

print("Расчет длины кривой Безье, проходящей через:")
print(f"P0 = {P0}")
print(f"K  = {K}")
print(f"P2 = {P2}")
print("-" * 40)
print("tk (параметр для K) | Координаты P1        | Длина кривой")
print("-" * 40)

min_length = float('inf')
best_tk = None

for tk in tk_values:
  try:
    # 1. Находим P1 для текущего tk
    P1 = calculate_P1(P0, P2, K, tk)

    # 2. Вычисляем длину кривой для найденного P1
    length, integration_error = calculate_curve_length(P0, P1, P2)

    print(f"{tk:^21.3f} | ({P1[0]:<8.3f}, {P1[1]:<8.3f}) | {length:<.5f}")

    # Обновляем минимальную длину
    if length < min_length:
        min_length = length
        best_tk = tk

  except ValueError as e:
    print(f"{tk:^21.3f} | Ошибка: {e}")

print("-" * 40)
print(f"Минимальная длина ({min_length:.5f}) в исследованном диапазоне достигается примерно при tk = {best_tk:.7f}")
print("\nПримечание: Найденный tk дает минимум только среди проверенных значений.")
print("Для точного поиска минимума требуется более сложный анализ или оптимизация.")

# Пример вычисления для tk=0.5 отдельно
print("\nСравнение с tk = 0.5:")
tk_05 = 0.5
P1_05 = calculate_P1(P0, P2, K, tk_05)
length_05, _ = calculate_curve_length(P0, P1_05, P2)
print(f"{tk_05:^21.3f} | ({P1_05[0]:<8.3f}, {P1_05[1]:<8.3f}) | {length_05:<.5f}")
