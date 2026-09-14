import sys

class Gauss:
    def __init__(self, matrix, b, epsilon):
        self.n = len(matrix)
        self.A = matrix
        self.b = b
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
            ne_diagonal = sum(abs(self.A[i][j]) for j in range(self.n) if i != j)
            if diagonal < ne_diagonal:
                return True
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
        A = [row[:] for row in self.A]
        b = self.b[:]
        n = self.n

        for i in range(n):
            pivot_row = max(range(i, n), key=lambda row: abs(A[row][i]))
            pivot_value = A[pivot_row][i]

            if abs(pivot_value) <= self.epsilon:
                print("Нулевой ведущий элемент. Метод Гаусса неприменим.")
                return None, 0, None

            if pivot_row != i:
                A[i], A[pivot_row] = A[pivot_row], A[i]
                b[i], b[pivot_row] = b[pivot_row], b[i]

            for j in range(i + 1, n):
                factor = A[j][i] / A[i][i]
                if factor == 0:
                    continue

                for k in range(i, n):
                    A[j][k] -= factor * A[i][k]
                b[j] -= factor * b[i]

        x = [0.0] * n
        for i in range(n - 1, -1, -1):
            diagonal = A[i][i]
            if abs(diagonal) < self.epsilon:
                print("Нулевой элемент на главной диагонали. Метод Гаусса неприменим.")
                return None, 0, None

            s = sum(A[i][j] * x[j] for j in range(i + 1, n))
            x[i] = (b[i] - s) / diagonal

        return x, 1, [0.0] * n
        

