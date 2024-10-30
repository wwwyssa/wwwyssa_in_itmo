package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Vaporeon extends Eevee {
    public Vaporeon(String name, int lvl) {
        super(name, lvl);
        setStats(130, 65, 60, 110, 95, 65);
        addMove(new AuroraBeam());
    }
}
