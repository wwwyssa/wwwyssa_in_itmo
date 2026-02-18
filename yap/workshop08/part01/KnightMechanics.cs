using System;

namespace part01
{
    [GameAttribute]
    public class KnightMechanics
    {
        [CombatSkill("HalfDamageOnDefense", TriggerType.OnDefense, 50)]
        public void ExecuteDefense(BattleContext ctx)
        {
            ctx.DamageDealt = IlMath.Razdelitb(ctx.DamageDealt, ctx.DamageDivisor);
            Console.WriteLine($"[System] Defense: damage divided by {ctx.DamageDivisor} to {ctx.DamageDealt}.");
        }

        [CombatSkill("Poison", TriggerType.PostBattle, 10)]
        public void ExecutePostBattle(BattleContext ctx)
        {
            ctx.Attacker.Hp = IlMath.Vichestb(ctx.Attacker.Hp, ctx.HpSubtractAmount);
            ctx.Defender.Hp = IlMath.Vichestb(ctx.Defender.Hp, ctx.HpSubtractAmount);
            Console.WriteLine($"[System] Poison -{ctx.HpSubtractAmount} HP.");
        }
    }
}
