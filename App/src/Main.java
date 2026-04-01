import java.util.HashMap;
import java.util.Map;

abstract class Room {
    private int roomNumber;
    private int beds;
    private double price;

    public Room(int roomNumber, int beds, double price) {
        this.roomNumber = roomNumber;
        this.beds = beds;
        this.price = price;
    }

    public abstract String getType();

    public void displayDetails() {
        System.out.println("Room No: " + roomNumber);
        System.out.println("Type   : " + getType());
        System.out.println("Beds   : " + beds);
        System.out.println("Price  : " + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom(int roomNumber) {
        super(roomNumber, 1, 1000);
    }

    public String getType() {
        return "Single";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber) {
        super(roomNumber, 2, 2000);
    }

    public String getType() {
        return "Double";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, 3, 5000);
    }

    public String getType() {
        return "Suite";
    }
}

// Inventory (from UC3)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 0); // unavailable
        inventory.put("Suite", 2);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

// UC4: Search Service (Read-only)
class RoomSearchService {

    public void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {

        System.out.println("=== Available Rooms (v4.0) ===\n");

        for (Room room : rooms) {
            String type = room.getType();
            int available = inventory.getAvailability(type);

            if (available > 0) { // filter unavailable rooms
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("--------------------------------");
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Room[] rooms = {
                new SingleRoom(101),
                new DoubleRoom(102),
                new SuiteRoom(103)
        };

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        System.out.println("=== BookMyStayApp v4.0 ===\n");

        // Perform search (read-only)
        searchService.searchAvailableRooms(rooms, inventory);
    }
}