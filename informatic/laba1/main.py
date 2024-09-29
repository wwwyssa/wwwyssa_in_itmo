a = input("Введите число в 10сс: ")
for digit in a:
    if digit.isalpha():
        print('Введено некорректное число')
        quit()
result = ""
base = -10
a = int(a)
while a:
    ost = a % base
    a //= base
    if ost < 0:
        ost += abs(base)
        a += 1
    result = str(ost) + result
print("Число в -10сс: ", result)