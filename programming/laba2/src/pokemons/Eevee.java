package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Eevee extends Pokemon {
    public Eevee(String name, int lvl) {
        super(name, lvl);
        setStats(55, 55, 50, 45, 65, 55);
        setType(Type.NORMAL);
        setMove(new QuickAttack(), new WorkUp(), new Tackle());
    }
}
