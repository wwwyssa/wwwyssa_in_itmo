package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Infernape extends Monferno {
    public Infernape(String name, int lvl) {
        super(name, lvl);
        addMove(new CalmMind());
    }
}
