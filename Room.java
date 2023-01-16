import java.util.ArrayList;
import java.util.List;

public class Room {
    int ID; // unique id for identifying a room
    String name; // room Name Number
    int size;
    boolean multimedia; // multimedia available in room

    static int count = 7;

    private Room(int ID, String name, int size, boolean multimedia) {
        this.ID = ID;
        this.name = name;
        this.size = size;
        this.multimedia = multimedia;
    }

    // returns a list of the rooms
    public static List<Room> roomList() {
        List<Room> rooms = new ArrayList<Room>();

        rooms.add(new Room(0, "BL134", 30, true));
        rooms.add(new Room(1, "BL138", 50, true));
        rooms.add(new Room(2, "KR224", 40, false));
        rooms.add(new Room(3, "KR206", 30, true));
        rooms.add(new Room(4, "Biddle123", 35, false));
        rooms.add(new Room(5, "Biddle205", 45, false));
        rooms.add(new Room(6, "ES100", 100, true));

        return rooms;
    }
}
