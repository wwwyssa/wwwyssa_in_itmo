import ctypes
import os
import time

# TODO
# 1. Создать на питоне файл, который содержит 1000 пар чисел
# Пример
# 0,1 1,20
# 1,20 1,20 
# 3,10 3,10
# 1,0 1,0
# 2. Читаем файл и записываем в массив 
# 3. Массив содержит класс Point 
# 4. Передаём массив в фунцию написанную на C
# 5. Функция на C для каждой пары считаем расстояние и возвращаем массив с результатами
# 6. Результаты выводим из кода питона


class Point(ctypes.Structure):
    _fields_ = [("x", ctypes.c_int), ("y", ctypes.c_int)] 

if __name__ == "__main__":
    lib_name = "libpoint.dll"
    lib_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), lib_name)

    try:
        c_lib = ctypes.CDLL(lib_path)
        print(f"Библиотека успешно загружена: {lib_path}")
    except Exception as e:
        print(f"ОШИБКА: Не удалось загрузить библиотеку '{lib_path}'")
        print(f"Детали ошибки: {e}")
        exit()


    c_lib.process_point.argtypes = [ctypes.POINTER(Point)]
    c_lib.process_point.restype = None

    my_point = Point(x = 5, y = 10)
    print(my_point.x, my_point.y)
    c_lib.process_point(ctypes.byref(my_point))
    print("My point:", my_point.x, my_point.y)

    data = []
    ArrayType = ctypes.c_longlong * len(data)   # 1. Создаём тип "массив из N long long"
    c_array = ArrayType(*data)                  # 2. Создаём экземпляр этого массива




