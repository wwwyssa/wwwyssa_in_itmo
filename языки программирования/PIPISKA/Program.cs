string docPath = Environment.CurrentDirectory;

Battle battle = new Battle();
Warrior warrior = new Warrior("Sven", 120, 25, 10);
Wizard wizard = new Wizard("grimsroke", 80, 30, 5);
battle.StartBattle(warrior, wizard);



public abstract class Hero : IAtackable
{
    public string Name { get; set; }
    public int Hp { get; set; }
    public int Damage { get; set; }
    public int Armor { get; set; }

    public Hero(String name, int hp, int damage, int armor)
    {
        Name = name;
        Hp = hp;
        Damage = damage;
        Armor = armor;
    }
    public void TakeDamage(int damage)
    {
        int damageTaken = damage - Armor;
        if (damageTaken < 0)
            damageTaken = 0;
        this.Hp -= damageTaken;
        if (this.Hp < 0)
            this.Hp = 0;
        if (this.Hp == 0)
        {
            Console.ForegroundColor = ConsoleColor.Magenta;
            File.AppendAllText(CurPath.GetPath(), $"{Name} has been defeated!\n");
            Console.WriteLine($"{Name} has been defeated!");
            Console.ForegroundColor = ConsoleColor.White;
        }
    }

    public virtual void Attack(Hero hero, int damage)
    {
        hero.TakeDamage(damage);
        Console.ForegroundColor = ConsoleColor.Red;
        File.AppendAllText(CurPath.GetPath(), $"{Name} attacked {hero.Name} for {damage} damage.\n");   
        Console.WriteLine($"{Name} attacked {hero.Name} for {damage} damage.");
        Console.ForegroundColor = ConsoleColor.White;

    }

    public virtual void Ability() { }

    public virtual void DropAbility() { }

    public override string ToString()
    {
        return $"Name: {Name}, Hp: {Hp}, Damage: {Damage}, Armor: {Armor}";
    }
}

public interface IAtackable
{
    public void TakeDamage(int damage);
}


public class Warrior : Hero, IAtackable
{
    public Warrior(string name, int hp, int damage, int armor) : base(name, hp, damage, armor)
    {
    }

    public override void Attack(Hero hero, int damage)
    {
        base.Attack(hero, damage);
    }

    public override void Ability()
    {
        this.Damage *= 2;
        Console.ForegroundColor = ConsoleColor.Yellow;
        File.AppendAllText(CurPath.GetPath(), $"{Name} got Double Damage\n");
        Console.WriteLine($"{Name} got Double Damage");
        Console.ForegroundColor = ConsoleColor.White;
    }

    public override void DropAbility()
    {
        this.Damage /= 2;
        Console.ForegroundColor = ConsoleColor.Yellow;
        File.AppendAllText(CurPath.GetPath(), $"{Name} lost Double Damage\n");
        Console.WriteLine($"{Name} lost Double Damage");
        Console.ForegroundColor = ConsoleColor.White;
    }

    public override string ToString()
    {
        return $"Warrior - {base.ToString()}";
    }

}

public class Wizard : Hero, IAtackable
{
    public Wizard(string name, int hp, int damage, int armor) : base(name, hp, damage, armor) { }
    public override void Attack(Hero hero, int damage)
    {
        base.Attack(hero, damage + hero.Armor);
    }

    public override void Ability()
    {
        this.Hp = (int)(this.Hp * 1.5);
        Console.ForegroundColor = ConsoleColor.Green;
        File.AppendAllText(CurPath.GetPath(), $"{Name} got +50% HP\n");
        Console.WriteLine($"{Name} got +50% HP");
        Console.ForegroundColor = ConsoleColor.White;
    }
    public override string ToString()
    {
        return $"Wizard - {base.ToString()}";
    }
}

public class Battle
{
    Random rnd = new Random();
    public void StartBattle(Hero hero1, Hero hero2)
    {
        int round = 0;
        
        while (hero1.Hp > 0 && hero2.Hp > 0)
        {
            round++;
            Thread.Sleep(1000);
            int WhoStep = rnd.Next(0, 2);
            Console.WriteLine($"\n--- Round {round} ---{WhoStep}---");
            if (WhoStep == 0)
            {
                if (round % 5 == 1)
                {
                    hero1.Ability();
                    continue;
                }
                hero1.Attack(hero2, hero1.Damage);
                if (round % 5 == 3)
                {
                    hero1.DropAbility();
                }
            }
            else
            {
                if (round % 5 == 1)
                {
                    hero2.Ability();
                    continue;
                }
                hero2.Attack(hero1, hero2.Damage);
                if (round % 5 == 3)
                {
                    hero2.DropAbility();
                }
            }

        }
    }
}



public static class CurPath
{
    public static string GetPath()
    {
        string docPath = Environment.CurrentDirectory;
        docPath = Path.Combine(docPath, "log.txt");
        return docPath;
    }
}
