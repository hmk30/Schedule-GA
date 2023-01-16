import java.util.ArrayList;
import java.util.List;

public class Course {
    int ID; // unique id for identifying a course offering
    String name; // description of course
    int professorID; // from Professors Structure
    int size; // max number of people enrolled
    boolean multimedia; // multimedia needed

    //// place holders for room-time-professor matchings /////
    int tpid; // suggested time course is offered
    int rid; // suggested room

    static int count = 26;

    public Course(int ID, String name, int professorID, int size, boolean multimedia) {
        this.ID = ID;
        this.name = name;
        this.professorID = professorID;
        this.size = size;
        this.multimedia = multimedia;
        this.rid = -1;
    }

    // returns a list of courses, sorted by requiring multimedia first, and size
    // second
    public static List<Course> sortedCourseList() {
        List<Course> courses = new ArrayList<Course>();

        courses.add(new Course(16, "Soc100", 7, 45, true));
        courses.add(new Course(17, "Soc100", 7, 40, true));
        courses.add(new Course(6, "cs015", 2, 35, true));
        courses.add(new Course(18, "Soc100", 7, 35, true));
        courses.add(new Course(1, "cs456", 0, 20, true));
        courses.add(new Course(2, "cs456", 0, 20, true));
        courses.add(new Course(4, "cs455", 1, 20, true));
        courses.add(new Course(5, "cs455", 1, 20, true));
        courses.add(new Course(24, "cs045", 1, 20, true));
        courses.add(new Course(25, "cs045", 1, 20, true));
        courses.add(new Course(26, "cs015", 2, 20, true));
        courses.add(new Course(3, "cs1783", 0, 15, true));
        courses.add(new Course(19, "CS047", 8, 15, true));
        courses.add(new Course(20, "CS047", 8, 15, true));
        courses.add(new Course(15, "math002", 6, 60, false));
        courses.add(new Course(12, "math001", 5, 60, false));
        courses.add(new Course(14, "math002", 6, 50, false));
        courses.add(new Course(11, "math001", 5, 50, false));
        courses.add(new Course(10, "math001", 5, 40, false));
        courses.add(new Course(13, "math002", 6, 40, false));
        courses.add(new Course(8, "cs015", 3, 35, false));
        courses.add(new Course(7, "cs015", 3, 35, false));
        courses.add(new Course(22, "PSY200", 9, 35, false));
        courses.add(new Course(9, "cs015", 4, 35, false));
        courses.add(new Course(21, "PSY200", 9, 30, false));
        courses.add(new Course(23, "PSY200", 9, 30, false));

        return courses;
    }

    // returns a list of the courses
    public static List<Course> courseList() {
        List<Course> courses = new ArrayList<Course>();

        courses.add(new Course(1, "cs456", 0, 20, true));
        courses.add(new Course(2, "cs456", 0, 20, true));
        courses.add(new Course(3, "cs1783", 0, 15, true));
        courses.add(new Course(4, "cs455", 1, 20, true));
        courses.add(new Course(5, "cs455", 1, 20, true));
        courses.add(new Course(6, "cs015", 2, 35, true));
        courses.add(new Course(7, "cs015", 3, 35, false));
        courses.add(new Course(8, "cs015", 3, 35, false));
        courses.add(new Course(9, "cs015", 4, 35, false));
        courses.add(new Course(10, "math001", 5, 40, false));
        courses.add(new Course(11, "math001", 5, 50, false));
        courses.add(new Course(12, "math001", 5, 60, false));
        courses.add(new Course(13, "math002", 6, 40, false));
        courses.add(new Course(14, "math002", 6, 50, false));
        courses.add(new Course(15, "math002", 6, 60, false));
        courses.add(new Course(16, "Soc100", 7, 45, true));
        courses.add(new Course(17, "Soc100", 7, 40, true));
        courses.add(new Course(18, "Soc100", 7, 35, true));
        courses.add(new Course(19, "CS047", 8, 15, true));
        courses.add(new Course(20, "CS047", 8, 15, true));
        courses.add(new Course(21, "PSY200", 9, 30, false));
        courses.add(new Course(22, "PSY200", 9, 35, false));
        courses.add(new Course(23, "PSY200", 9, 30, false));
        courses.add(new Course(24, "cs045", 1, 20, true));
        courses.add(new Course(25, "cs045", 1, 20, true));
        courses.add(new Course(26, "cs015", 2, 20, true));

        return courses;
    }

    // returns true if the suggested room has a large enough size and multimedia is
    // fulfilled
    public boolean isValid() {
        if (rid == -1)
            return false;

        Room r = Room.roomList().get(rid);

        if (size > r.size || multimedia == true && r.multimedia == false)
            return false;

        return true;
    }
}
