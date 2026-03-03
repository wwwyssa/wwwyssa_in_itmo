import sys

class GaussSeidelSolver:
    def __init__(self, matrix, rhs, epsilon):
        self.n = len(matrix)
        self.A = matrix
        self.b = rhs
        self.epsilon = epsilon

    def calculate_matrix_norm(self):
        max_sum = 0.0
        for i in range(self.n):
            row_sum = sum(abs(x) for x in self.A[i])
            if row_sum > max_sum:
                max_sum = row_sum
        return max_sum

    def check_diagolnal_preobl(self):
        for i in range(self.n):
            diagonal = abs(self.A[i][i])
            off_diagonal = sum(abs(self.A[i][j]) for j in range(self.n) if i != j)
            if diagonal < off_diagonal:
                return False
        return True

    def make_diagonally_dominant(self):
        p = list(range(self.n))

        for i in range(self.n):
            max_val = 0
            max_row_index = -1
            
            for k in range(i, self.n):
                current_row = p[k]
                val = abs(self.A[current_row][i])
                if val > max_val:
                    max_val = val
                    max_row_index = k
            
            if max_row_index != -1:
                p[i], p[max_row_index] = p[max_row_index], p[i]


        new_A = [[0]*self.n for _ in range(self.n)]
        new_b = [0]*self.n
        
        for i in range(self.n):
            new_A[i] = self.A[p[i]]
            new_b[i] = self.b[p[i]]
            
        self.A = new_A
        self.b = new_b

        return self.check_diagolnal_preobl()


    def solve(self):
        if not self.check_diagolnal_preobl():
            print("Диагональное преобладание отсутствует.")
            if self.make_diagonally_dominant():
                print("Диагональное преобладание достигнуто после перестановки.")
                print("Полученная матрица:")
                for row in self.A:
                    print(row)
            else:
                print("Невозможно достичь диагонального преобладания перестановкой строк.")

 
        norm = self.calculate_matrix_norm()
        print(f"Норма матрицы: {norm:.4f}")

  
        x = [0.0] * self.n
        iterations = 0
        errors = [0.0] * self.n
        
        while True:
            x_new = x[:]
            current_errors = [0.0] * self.n
            
            for i in range(self.n):
                s1 = sum(self.A[i][j] * x_new[j] for j in range(i))
                s2 = sum(self.A[i][j] * x[j] for j in range(i + 1, self.n))
                
                if self.A[i][i] == 0:
                    print("\nНулевой элемент на главной диагонали! Метод неприменим.")
                    return None, iterations, None

                val = (self.b[i] - s1 - s2) / self.A[i][i]
                
                current_errors[i] = abs(val - x[i])
                x_new[i] = val


            x = x_new
            iterations += 1
            errors = current_errors
            # print(f"Итерация {iterations}: x = {[f'{xi:.6f}' for xi in x]}, погрешности = {[f'{ei:.10f}' for ei in errors]}")
            if max(errors) < self.epsilon:
                return x, iterations, errors
        
        


def get_input_mode():
    print("\nВыберите режим ввода данных:")
    print("1. С клавиатуры")
    print("2. Из файла")
    while True:
        choice = input("Ваш выбор (1 или 2): ").strip()
        if choice in ['1', '2']:
            return choice
        print("Неверный ввод. Попробуйте еще раз.")

def read_from_keyboard():
    try:
        n = int(input("Введите размерность матрицы n (<= 20): "))
        if n > 20 or n < 1:
            print("Размерность должна быть от 1 до 20.")
            sys.exit()

        print("Введите коэффициенты матрицы (построчно, через пробел):")
        matrix = []
        for i in range(n):
            row = list(map(float, input(f"Строка {i+1}: ").split()))
            if len(row) != n:
                print(f"Ошибка! Ожидалось {n} элементов.")
                sys.exit()
            matrix.append(row)

        print("Введите вектор свободных членов (через пробел):")
        rhs = list(map(float, input().split()))
        if len(rhs) != n:
            print(f"Ошибка! Ожидалось {n} элементов.")
            sys.exit()

        epsilon = float(input("Введите точность вычислений (например, 0.001): "))
        return matrix, rhs, epsilon
    except ValueError:
        print("Ошибка ввода чисел.")
        sys.exit()

def read_from_file():
    filename = input("Введите имя файла: ").strip()
    try:
        with open(filename, 'r') as f:
            lines = [line.strip() for line in f if line.strip()]
            
            n = int(lines[0])
            if n > 20 or n < 1:
                print("Размерность в файле некорректна.")
                sys.exit()
                
            matrix = []
            for i in range(n):
                row = list(map(float, lines[i+1].split()))
                matrix.append(row)
                
            rhs = list(map(float, lines[n+1].split()))
            epsilon = float(lines[n+2])
            
            return matrix, rhs, epsilon
    except FileNotFoundError:
        print("Файл не найден.")
        sys.exit()
    except (ValueError, IndexError):
        print("Ошибка структуры файла.")
        sys.exit()



def main():
    
    mode = get_input_mode()
    
    if mode == '1':
        matrix, rhs, epsilon = read_from_keyboard()
    else:
        matrix, rhs, epsilon = read_from_file()

    solver = GaussSeidelSolver(matrix, rhs, epsilon)
    result_x, iters, final_errors = solver.solve()

    if result_x is not None:
        print("\n=== Ответ: ===")
        print(f"Количество итераций: {iters}")
        
        print("\nВектор неизвестных x:")
        for i, val in enumerate(result_x):
            print(f"x{i+1} = {val:.6f}")
            
        print("\nВектор погрешностей |x(k) - x(k-1)|:")
        for i, val in enumerate(final_errors):
            print(f"e{i+1} = {val:.10f}")
    else:
        print("Решение не найдено.")

if __name__ == "__main__":
    main()