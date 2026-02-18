using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace part02
{
    public static class Examples
    {
        public static double NativeCalc(double a, double b, double c)
        {
            return (a + b) * c;
        }

        public static Func<double, double, double, double> CreateDynamicMethod()
        {
            // 1. Создаем "Метод-призрак" (DynamicMethod)
            // Имя: "FastCalc"
            // Возвращает: double
            // Аргументы: double, double, double
            var dynamicMethod = new DynamicMethod("FastCalc", typeof(double), [typeof(double), typeof(double), typeof(double)]);

            // 2. Получаем ILGenerator — это наш "ассемблер" для .NET
            ILGenerator il = dynamicMethod.GetILGenerator();

            // Пишем программу на стековой машине:

            // Загружаем аргумент 0 (a) в стек
            il.Emit(OpCodes.Ldarg_0);

            // Загружаем аргумент 1 (b) в стек
            il.Emit(OpCodes.Ldarg_1);

            // Складываем два верхних числа в стеке (a + b) -> результат падает в стек
            il.Emit(OpCodes.Add);

            // Загружаем аргумент 2 (c) в стек
            il.Emit(OpCodes.Ldarg_2);

            // Умножаем (result * c) -> результат в стеке
            il.Emit(OpCodes.Mul);

            // Возвращаем то, что лежит на вершине стека
            il.Emit(OpCodes.Ret);

            // 3. "Компилируем" это в делегат C#
            return (Func<double, double, double, double>)dynamicMethod.CreateDelegate(typeof(Func<double, double, double, double>));
        }
    }
}
