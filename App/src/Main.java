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

// UC3: Centralized Inventory using HashMap
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void updateAvailability(String type, int count) {
        inventory.put(type, count);
    }

    public void displayInventory() {
        System.out.println("=== Room Inventory (v3.0) ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Room r1 = new SingleRoom(101);
        Room r2 = new DoubleRoom(102);
        Room r3 = new SuiteRoom(103);

        RoomInventory inventory = new RoomInventory();

        System.out.println("=== BookMyStayApp v3.0 ===\n");

        r1.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Single"));
        System.out.println();

        r2.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Double"));
        System.out.println();

        r3.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Suite"));
        System.out.println();

        inventory.displayInventory();
    }
}