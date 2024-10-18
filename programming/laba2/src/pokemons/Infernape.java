package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Infernape extends Pokemon {
    public Infernape(String name, int lvl) {
        super(name, lvl);
        setStats(76, 104, 71, 104, 71, 108);
        setType(Type.FIRE, Type.FIGHTING);
        setMove(new Rest(), new LowSweep(), new SlackOff(), new CalmMind());
    }
}
