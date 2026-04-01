import java.util.*;

// -------- Room Hierarchy --------
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
}

class SingleRoom extends Room {
    public SingleRoom(int roomNumber) { super(roomNumber, 1, 1000); }
    public String getType() { return "Single"; }
}

class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber) { super(roomNumber, 2, 2000); }
    public String getType() { return "Double"; }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) { super(roomNumber, 3, 5000); }
    public String getType() { return "Suite"; }
}

// -------- Inventory --------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }
}

// -------- Reservation --------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// -------- Booking Queue --------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) { queue.add(r); }
    public Reservation getNext() { return queue.poll(); }
    public boolean isEmpty() { return queue.isEmpty(); }
}

// -------- Booking History (UC8) --------
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }
}

// -------- Booking Service --------
class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private BookingHistory history;

    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void processBookings(BookingQueue queue) {

        System.out.println("=== Booking Processing (v8.0) ===\n");

        while (!queue.isEmpty()) {
            Reservation req = queue.getNext();
            String type = req.getRoomType();

            if (inventory.getAvailability(type) > 0) {

                String roomId = generateRoomId(type);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                Set<String> set = allocatedRooms.get(type);

                if (!set.contains(roomId)) {
                    set.add(roomId);
                    inventory.reduceAvailability(type);

                    // Create confirmed reservation
                    Reservation confirmed = new Reservation(roomId, req.getGuestName(), type);

                    // Store in history
                    history.add(confirmed);

                    System.out.println("Confirmed: " + req.getGuestName() + " -> " + roomId);
                }

            } else {
                System.out.println("Failed: " + req.getGuestName() + " (No availability)");
            }
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0,1) + UUID.randomUUID().toString().substring(0,4);
    }
}

// -------- Reporting Service --------
class BookingReportService {

    public void generateReport(BookingHistory history) {

        System.out.println("\n=== Booking History Report ===");

        List<Reservation> list = history.getAll();

        for (Reservation r : list) {
            r.display();
        }

        System.out.println("\nTotal Bookings: " + list.size());
    }
}

// -------- Main --------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        // Add requests
        queue.addRequest(new Reservation("REQ1", "Alice", "Single"));
        queue.addRequest(new Reservation("REQ2", "Bob", "Double"));
        queue.addRequest(new Reservation("REQ3", "Charlie", "Suite"));

        BookingService service = new BookingService(inventory, history);
        service.processBookings(queue);

        // Generate report
        BookingReportService report = new BookingReportService();
        report.generateReport(history);
    }
}