package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Audino extends Pokemon {
    public Audino(String name, int lvl) {
        super(name, lvl);
        setStats(103, 60, 86, 60, 86, 50);
        setType(Type.NORMAL);
        setMove(new Pound(), new DazzlingGleam(), new CalmMind(), new DisarmingVoice());
    }
}
