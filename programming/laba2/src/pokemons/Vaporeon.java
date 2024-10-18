package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Vaporeon extends Pokemon {
    public Vaporeon(String name, int lvl) {
        super(name, lvl);
        setStats(130, 65, 60, 110, 95, 65);
        setType(Type.WATER);
        setMove(new QuickAttack(), new WorkUp(), new Tackle(), new AuroraBeam());
    }
}
