using part01;
using System.Reflection;

var engine = new SkillEngine();

// Регистрация текущей сборки (или загрузка внешней DLL)
engine.RegisterAssembly(Assembly.GetExecutingAssembly());

// 1. Запустили примеры
// 2. Написали класс, на которой повесили атрибут GameAttribute 
// 3. создали два метода в этом классе с атрибутами CombatSkillAttribute 
// С тригерами (OnDefense и на PostBattle)
// 4. OnDefense снижение урона в два раза
// 5. PostBattle у Attacker и Defender снижается HP на 10
// 6. Вызвать попорядку pipeline OnDefense => OnAttack => PostBattle
// 7. 4 и 5 пункт математику сделать с помощью ILGenerator

// Симуляция контекста боя
var context = new BattleContext
{
    DamageDealt = 100,
    Attacker = new UnitStats { Hp = 50 },
    Defender = new UnitStats { Hp = 100 }
};

Console.WriteLine("--- Starting Defense Phase ---");
engine.ExecutePipeline(TriggerType.OnDefense, context);

Console.WriteLine("--- Starting Attack Phase ---");
// Ядро само находит нужные методы и вызывает их в правильном порядке (сначала Crit, потом Vampirism)
engine.ExecutePipeline(TriggerType.OnAttack, context);

Console.WriteLine($"Damage Dealt After Defense: {context.DamageDealt}");


Console.WriteLine("--- Post Battle Phase ---");
engine.ExecutePipeline(TriggerType.PostBattle, context);

Console.WriteLine($"Attacker Final HP: {context.Attacker.Hp}");
Console.WriteLine($"Defender Final HP: {context.Defender.Hp}");

