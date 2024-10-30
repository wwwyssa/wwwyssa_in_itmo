package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Monferno extends Chimchar {
    public Monferno(String name, int lvl) {
        super(name, lvl);
        setStats(64, 78, 52, 78, 52, 81);
        addMove(new SlackOff());
    }
}
