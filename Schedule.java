import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Schedule {
    List<Course> courses;
    int generation; // keeps track of which generation this schedule was created in
    public boolean testing;

    // create a new empty schedule
    public Schedule() {
        courses = Course.sortedCourseList();
    }

    // returns true when all courses have room with sufficient seating and
    // multimedia, as well as no professor has 2 classes at the same time, and no
    // more than one class occurs in a room at a certain time
    public boolean isValid() {
        Map<Integer, List<Integer>> usedRoomTimes = new HashMap<Integer, List<Integer>>(); // <roomID, List<usedTimes>>
        Map<Integer, List<Integer>> usedProfessorTimes = new HashMap<Integer, List<Integer>>(); // <roomID,
                                                                                                // List<usedTimes>>

        for (Course c : courses) {
            // check each course for sufficient seating & multimedia
            if (!c.isValid()) {
                return false;
            }

            // put room and time data into a structure for later
            List<Integer> roomTimes = usedRoomTimes.containsKey(c.rid) ? usedRoomTimes.get(c.rid)
                    : new ArrayList<Integer>();

            roomTimes.add(c.tpid);
            usedRoomTimes.put(c.rid, roomTimes);

            // put professor and time data into a structure for later
            List<Integer> professorTimes = usedProfessorTimes.containsKey(c.professorID)
                    ? usedProfessorTimes.get(c.professorID)
                    : new ArrayList<Integer>();

            professorTimes.add(c.tpid);
            usedProfessorTimes.put(c.professorID, professorTimes);
        }
        // ensure that only one course happens in a room at a time
        // (find duplicates in value list, done by adding to set(returns false if
        // already exists in set))
        for (List<Integer> values : usedRoomTimes.values()) {
            Set<Integer> set = new HashSet<Integer>();

            for (Integer value : values) {
                if (!set.add(value)) {
                    return false; // if set.add returned false, value already exists
                }
            }
        }

        // ensure that a professor does not have more than one class at a time
        // done the same way as directly above
        for (List<Integer> values : usedProfessorTimes.values()) {
            Set<Integer> set = new HashSet<Integer>();

            for (Integer value : values) {
                if (!set.add(value)) {
                    return false; // if set.add returned false, value already exists
                }
            }
        }

        // if passed all the checks, the schedule is valid, return true;
        return true;
    }

    public int getFitness() {
        int fitness = 0;

        int sum = 0;

        // subtract num_unused_seats from sum for each course
        for (Course c : courses) {
            sum -= Room.roomList().get(c.rid).size - c.size;
        }

        if (this.testing) {
            System.out.println("Sum from unused seats: " + sum);
        }
        fitness += sum;

        // for each professor, check the following and set fitness values:
        // - duplicate rooms
        // - consecutive courses
        // - delay between courses
        for (Professor p : Professor.professorList()) {
            Map<Integer, Integer> rooms = new HashMap<Integer, Integer>(); // <roomID, numTimesUsed>
            List<Integer> times = new ArrayList<Integer>();

            // for each course
            for (Course c : courses) {
                // if professor teaches that course,
                if (c.professorID == p.ID) {
                    // add course room to professors list of rooms used
                    if (rooms.containsKey(c.rid)) {
                        int val = rooms.get(c.rid) + 1;
                        rooms.put(c.rid, val);
                    } else {
                        rooms.put(c.rid, 1);
                    }

                    // note the time that the course starts
                    times.add(c.tpid);
                }
            }

            sum = 0;

            // for each time above 1 that a room is used by a professor, add 5 to fitness
            for (Integer v : rooms.values()) {
                while (v > 1) {
                    v--;
                    sum += 5;
                }
            }

            if (this.testing) {
                System.out.println("Professor " + p.name + " bonus from duplicate rooms: " + sum);
            }

            fitness += sum;

            // sort times in ascending order
            Collections.sort(times);
            // System.out.println(times);

            sum = 0;

            // check for gaps in professor's schedule and subtract 15 from fitness for each
            // gap of 3 hours (30pts for gap of 4hrs, etc...)
            for (int i = 0; i < times.size() - 1; i++) { // don't check the last element
                int t = times.get(i) + 1; // + 1 takes into account the length of the class (assumed 1hr)
                int nextT = times.get(i + 1);

                int gap = nextT - t;
                while (gap >= 3) {
                    sum -= 15;
                    gap--;
                }
            }

            if (this.testing) {
                System.out.println("Professor " + p.name + " penalty for large gaps " + sum);
            }
            fitness += sum;

            sum = 0;

            // check for multiple classes in a row in a professor's schedule, and subtract
            // 10 from fitness for each time more than the professor has 3 in a row (10pts
            // for 3 in a row, 20pts for 4 in a row)
            for (int i = 0; i < times.size() - 2; i++) { // don't check the last 2 elements
                int t = times.get(i); // + 1 takes into account the length of the class (assumed 1hr)

                int j = i + 1;
                int numInARow = 1;
                try {
                    while (times.get(j) != null) {
                        if (times.get(j) - 1 == t) {
                            numInARow++;
                            t = times.get(j);
                            j++;
                        } else {
                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {

                }

                while (numInARow >= 3) {
                    sum -= 10;
                    numInARow--;
                }

            }
            if (this.testing) {
                System.out.println("Professor " + p.name + " penalty for consecutive classes:" + sum);

            }
            fitness += sum;
        }

        return fitness;
    }

    public Schedule clone() {
        Schedule s = new Schedule();
        s.generation = this.generation;
        s.courses = new ArrayList<Course>(this.courses);

        return s;
    }

    private String centerString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    // print out the schedule to the console
    public void print() {
        int fitness = getFitness();
        System.out.println("--------------------------------------------------------------------");
        String title = String.format("Schedule with fitness: %d", fitness);
        String centeredTitle = centerString(64, title);
        System.out.println("| " + centeredTitle + " |");
        String sgeneration = String.format("from generation " + generation);
        String centeredGeneration = centerString(64, sgeneration);
        System.out.println("| " + centeredGeneration + " |");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("| CRN | Course  | Professor | Size | Multimedia | Room      | Time |");
        System.out.println("--------------------------------------------------------------------");

        for (Course c : courses) {
            String fid = String.format("%-3d", c.ID);
            String fname = String.format("%-7s", c.name);

            String profName = Professor.professorList().get(c.professorID).name;
            String fprofessor = String.format("%-9s", profName);

            String fsize = String.format("%-4d", c.size);
            String fmultimedia = String.format("%-10b", c.multimedia);

            String roomName = "";
            if (c.rid == -1)
                roomName = "unknown";
            else
                roomName = Room.roomList().get(c.rid).name;

            String froom = String.format("%-9s", roomName);

            int time = TimePeriod.timeList().get(c.tpid).startTime;
            String ftime = String.format("%-4d", time);

            System.out.println("| " + fid + " | " + fname + " | " + fprofessor + " | " + fsize + " | " + fmultimedia
                    + " | " + froom + " | " + ftime + " |");
        }

        System.out.println("--------------------------------------------------------------------");
    }
}