import ru.ifmo.se.pokemon.*;
import pokemons.*;


public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Audino p1 = new Audino("Audino", 1);
        Eevee p2 = new Eevee("Eevee", 1);
        Vaporeon p3 = new Vaporeon("Vaporeon", 1);
        Chimchar p4 = new Chimchar("Chimchar", 1);
        Monferno p5 = new Monferno("Monferno", 1);
        Infernape p6 = new Infernape("Infernape", 1);

        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);

        b.go();
    }
}