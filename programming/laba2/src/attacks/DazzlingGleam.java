package attacks;
import ru.ifmo.se.pokemon.*;


public class DazzlingGleam extends SpecialMove {
    public DazzlingGleam() {
        super(Type.FAIRY, 80, 1);
    }


    @Override
    protected String describe() {
        return "uses Dazzling Gleam";
    }
}
