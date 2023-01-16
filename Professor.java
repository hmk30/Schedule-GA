import java.util.ArrayList;
import java.util.List;

public class Professor {
    int ID; // unique id for identifying professors
    String name; // name of professor
    static final int size = 10;
    static final List<Professor> list = professorList();

    private Professor(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    // returns a list of the professors
    public static List<Professor> professorList() {
        List<Professor> professors = new ArrayList<Professor>();

        professors.add(new Professor(0, "Bilitski"));
        professors.add(new Professor(1, "Ohl"));
        professors.add(new Professor(2, "Sandro"));
        professors.add(new Professor(3, "Mr xxx"));
        professors.add(new Professor(4, "Frederick"));
        professors.add(new Professor(5, "Peter"));
        professors.add(new Professor(6, "Brian"));
        professors.add(new Professor(7, "Meg"));
        professors.add(new Professor(8, "Stewie"));
        professors.add(new Professor(9, "Glen"));

        return professors;
    }
}
