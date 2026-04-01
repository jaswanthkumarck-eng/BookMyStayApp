import java.util.*;

// Room hierarchy (same as previous UCs)
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
    public String getType() { return "Single"; }
}

class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber) {
        super(roomNumber, 2, 2000);
    }
    public String getType() { return "Double"; }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, 3, 5000);
    }
    public String getType() { return "Suite"; }
}

// UC3: Inventory (unchanged)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// UC5: Reservation (booking request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// UC5: Booking Queue (FIFO)
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request added for " + r.getGuestName());
    }

    public void displayQueue() {
        System.out.println("\n=== Booking Request Queue (FIFO) ===");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingQueue bookingQueue = new BookingQueue();

        // Simulating booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();
    }
}