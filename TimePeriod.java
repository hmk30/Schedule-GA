import java.util.ArrayList;
import java.util.List;

public class TimePeriod {
    int ID; // unique id for identifying a period
    String days; // an identifier that specifies the days
    int startTime; // start time of course

    static int count = 7;

    private TimePeriod(int ID, String days, int startTime) {
        this.ID = ID;
        this.days = days;
        this.startTime = startTime;
    }

    // returns a list of the times
    public static List<TimePeriod> timeList() {
        List<TimePeriod> times = new ArrayList<TimePeriod>();

        times.add(new TimePeriod(0, "MWF", 9));
        times.add(new TimePeriod(1, "MWF", 10));
        times.add(new TimePeriod(2, "MWF", 11));
        times.add(new TimePeriod(3, "MWF", 12));
        times.add(new TimePeriod(4, "MWF", 1));
        times.add(new TimePeriod(5, "MWF", 2));
        times.add(new TimePeriod(6, "MWF", 3));

        return times;
    }
}
