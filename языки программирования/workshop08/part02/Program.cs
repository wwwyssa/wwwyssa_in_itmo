// Цель: Показать разницу в производительности между Native, Reflection и Emit.

using part02;
using System.Diagnostics;
using System.Reflection;

Console.WriteLine("--- High-Performance Runtime Code Generation ---");

// 1. Задача: Вычислять формулу: result = (a + b) * c
// Допустим, эта логика пришла из конфига/скрипта и её не было в коде.

double a = 10.5;
double b = 20.1;
double c = 5.0;
long iterations = 50_000_000; // 50 миллионов вызовов

// --- ВАРИАНТ 1: Native C# (Эталон скорости) ---
var sw = Stopwatch.StartNew();
double dummy = 0;
for (int i = 0; i < iterations; i++)
{
    dummy = Examples.NativeCalc(a, b, c);
}
sw.Stop();
Console.WriteLine($"1. Native C#       : {sw.ElapsedMilliseconds} ms (Базовая линия)");


// --- ВАРИАНТ 2: Классическая Рефлексия (Медленно) ---
MethodInfo methodInfo = typeof(Examples).GetMethod(nameof(Examples.NativeCalc))!;
object[] arguments = new object[] { a, b, c }; // Аллокация массива на каждый вызов!

sw.Restart();
for (int i = 0; i < iterations; i++)
{
    // Внимание: Это ОЧЕНЬ медленно из-за боксинга и проверок безопасности
    dummy = (double)methodInfo.Invoke(null, arguments)!;

    // P.S. Даже если оптимизировать args, Invoke всё равно медленный.
}
sw.Stop();
Console.WriteLine($"2. Reflection      : {sw.ElapsedMilliseconds} ms (Тормоз)");


// --- ВАРИАНТ 3: Reflection.Emit (Black Magic) ---
// Мы создаем метод прямо в оперативной памяти.
// Func<double, double, double, double> -> принимает 3 double, возвращает double
var fastDelegate = Examples.CreateDynamicMethod();

sw.Restart();
for (int i = 0; i < iterations; i++)
{
    // Прямой вызов делегата. Zero Overhead.
    dummy = fastDelegate(a, b, c);
}
sw.Stop();
Console.WriteLine($"3. Reflection.Emit : {sw.ElapsedMilliseconds} ms");