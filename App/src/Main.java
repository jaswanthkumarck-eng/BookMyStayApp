import java.util.*;

// Room hierarchy
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

// Inventory (UC3)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        int count = getAvailability(type);
        if (count > 0) {
            inventory.put(type, count - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// Reservation (UC5)
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
}

// Booking Queue (UC5)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNext() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// UC6: Booking Service
class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    public void processBookings(BookingQueue queue) {
        System.out.println("=== Booking Processing (v6.0) ===\n");

        while (!queue.isEmpty()) {
            Reservation r = queue.getNext();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {

                String roomId = generateRoomId(type);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                Set<String> assigned = allocatedRooms.get(type);

                if (!assigned.contains(roomId)) {
                    assigned.add(roomId);
                    inventory.reduceAvailability(type);

                    System.out.println("Booking Confirmed for " + r.getGuestName());
                    System.out.println("Room Type: " + type + " | Room ID: " + roomId);
                    System.out.println("--------------------------------");
                }

            } else {
                System.out.println("Booking Failed for " + r.getGuestName() + " (No availability)");
                System.out.println("--------------------------------");
            }
        }

        inventory.displayInventory();
    }

    private String generateRoomId(String type) {
        return type.substring(0, 1).toUpperCase() + UUID.randomUUID().toString().substring(0, 4);
    }
}

// Main
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Suite"));

        BookingService service = new BookingService(inventory);

        service.processBookings(queue);
    }
}