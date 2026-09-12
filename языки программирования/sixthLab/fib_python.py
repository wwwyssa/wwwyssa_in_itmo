import ctypes
import os
import time

def fib_py(n):
    if n <= 1:
        return n
    else:
        return fib_py(n-1) + fib_py(n-2)

if __name__ == "__main__":
    print("Start program")

    # --- Загрузка библиотеки ---
    # Определение имени файла библиотеки в зависимости от ОС
    lib_name = "my-lib.dll" if os.name == 'nt' else "my-lib.so"
    lib_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), lib_name)

    try:
        # Загрузка C-библиотеки
        c_lib = ctypes.CDLL(lib_path)
        print(f"Библиотека успешно загружена: {lib_path}")
    except Exception as e:
        print(f"ОШИБКА: Не удалось загрузить библиотеку '{lib_path}'")
        print(f"Детали ошибки: {e}")
        exit()

    number = 35

    start_time = time.time()
    result = fib_py(number)
    end_time = time.time()
    
    print(f"[Python] Fib({number}) = {result}")
    print(f"[Python] Время выполнения: {end_time - start_time:.4f} сек")

    start_time = time.time()
    c_lib.fib_c_iterative.argtypes = [ctypes.c_int]
    c_lib.fib_c_iterative.restype = ctypes.c_longlong
    result = c_lib.fib_c_iterative(number)

    end_time = time.time()
    
    print(f"[Python] C_Fib_iterative({number}) = {result}")
    print(f"[Python] Время выполнения: {end_time - start_time:.4f} сек")

    start_time = time.time()
    c_lib.fib_c.argtypes = [ctypes.c_int]
    c_lib.fib_c.restype = ctypes.c_longlong
    result = c_lib.fib_c(number)

    end_time = time.time()
    
    print(f"[Python] C_Fib_recursive({number}) = {result}")
    print(f"[Python] Время выполнения: {end_time - start_time:.4f} сек")

    print(result)
