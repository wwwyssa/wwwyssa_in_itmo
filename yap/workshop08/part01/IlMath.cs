using System;
using System.Reflection.Emit;

namespace part01
{
    // Вспомогательные методы для генерации простой арифметики через IL
    public static class IlMath
    {
        private static readonly Func<int, int, int> _Razdelitb;
        private static readonly Func<int, int, int> _Vichestb;

        static IlMath()
        {

            var Razdelitb = new DynamicMethod(
                "Razdelitb",
                typeof(int),
                new Type[] { typeof(int), typeof(int) },
                typeof(IlMath).Module,
                skipVisibility: true
            );
            var ilD = Razdelitb.GetILGenerator();
            ilD.Emit(OpCodes.Ldarg_0); // x
            ilD.Emit(OpCodes.Ldarg_1); // divisor
            ilD.Emit(OpCodes.Div);
            ilD.Emit(OpCodes.Ret);
            _Razdelitb = (Func<int, int, int>)Razdelitb.CreateDelegate(typeof(Func<int, int, int>));

            var Vichestb = new DynamicMethod(
                "Vichestb",
                typeof(int),
                new Type[] { typeof(int), typeof(int) },
                typeof(IlMath).Module,
                skipVisibility: true
            );
            var ilS = Vichestb.GetILGenerator();
            ilS.Emit(OpCodes.Ldarg_0); // x
            ilS.Emit(OpCodes.Ldarg_1); // amount
            ilS.Emit(OpCodes.Sub);
            ilS.Emit(OpCodes.Ret);
            _Vichestb = (Func<int, int, int>)Vichestb.CreateDelegate(typeof(Func<int, int, int>));
        }
        public static int Razdelitb(int x, int divisor) => _Razdelitb(x, divisor);
        public static int Vichestb(int x, int amount) => _Vichestb(x, amount);
    }
}
