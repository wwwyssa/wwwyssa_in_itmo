import numpy as np

def rectangle_method(f, a, b, n):
    h = (b - a) / n
    integral = 0.0
    for i in range(n):
        x = a + (i + 0.5) * h
        integral += f(x)
    integral *= h
    return integral

def trapezoidal_method(f, a, b, n):
    h = (b - a) / n
    integral = 0.5 * (f(a) + f(b))
    for i in range(1, n):
        x = a + i * h
        integral += f(x)
    integral *= h
    return integral

def simpson_method(f, a, b, n):
    if n % 2 != 0:
        n += 1  # Убедимся, что n четное
    h = (b - a) / n
    integral = f(a) + f(b)
    for i in range(1, n):
        x = a + i * h
        if i % 2 == 0:
            integral += 2 * f(x)
        else:
            integral += 4 * f(x)
    integral *= h / 3
    return integral

def adaptive_integration(f, a, b, method, epsilon=1e-5, max_iter=1000):
    n = 4
    prev_integral = method(f, a, b, n)
    for _ in range(max_iter):
        n *= 2
        current_integral = method(f, a, b, n)
        if abs(current_integral - prev_integral) < epsilon:
            return current_integral, n
        prev_integral = current_integral
    return current_integral, n

# Примеры функций из задания
def f1(x): return np.cos(x + x**2)

# Выбор функции для интегрирования (здесь f1 как пример)
f = f1
a, b = 0, 1
epsilon = 1e-5

# Вычисление интеграла разными методами
print("Метод прямоугольников:")
integral_rect, n_rect = adaptive_integration(f, a, b, rectangle_method, epsilon)
print(f"Значение интеграла: {integral_rect}, количество разбиений: {n_rect}")

print("\nМетод трапеций:")
integral_trap, n_trap = adaptive_integration(f, a, b, trapezoidal_method, epsilon)
print(f"Значение интеграла: {integral_trap}, количество разбиений: {n_trap}")

print("\nМетод Симпсона:")
integral_simp, n_simp = adaptive_integration(f, a, b, simpson_method, epsilon)
print(f"Значение интеграла: {integral_simp}, количество разбиений: {n_simp}")