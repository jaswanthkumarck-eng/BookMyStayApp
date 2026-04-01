import java.util.*;

// -------- Reservation --------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.active = true;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public boolean isActive() { return active; }

    public void cancel() {
        this.active = false;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | Status: " + (active ? "ACTIVE" : "CANCELLED"));
    }
}

// -------- Inventory --------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    public void increaseAvailability(String type) {
        inventory.put(type, getAvailability(type) + 1);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// -------- Booking History --------
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void add(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation get(String id) {
        return history.get(id);
    }

    public void displayAll() {
        System.out.println("\n=== Booking History ===");
        for (Reservation r : history.values()) {
            r.display();
        }
    }
}

// -------- Booking Service --------
class BookingService {
    private RoomInventory inventory;
    private BookingHistory history;

    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public String book(String guestName, String type) {
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking Failed for " + guestName);
            return null;
        }

        String id = type.substring(0,1) + UUID.randomUUID().toString().substring(0,4);
        inventory.reduceAvailability(type);

        Reservation r = new Reservation(id, guestName, type);
        history.add(r);

        System.out.println("Booking Confirmed: " + id + " for " + guestName);
        return id;
    }
}

// -------- UC10: Cancellation Service --------
class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        Reservation r = history.get(reservationId);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        if (!r.isActive()) {
            System.out.println("Cancellation Failed: Already cancelled");
            return;
        }

        // LIFO rollback tracking
        rollbackStack.push(reservationId);

        // Reverse state
        r.cancel();
        inventory.increaseAvailability(r.getRoomType());

        System.out.println("Cancellation Successful for ID: " + reservationId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack: " + rollbackStack);
    }
}

// -------- Main --------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        BookingService bookingService = new BookingService(inventory, history);
        CancellationService cancelService = new CancellationService(inventory, history);

        // Bookings
        String id1 = bookingService.book("Alice", "Single");
        String id2 = bookingService.book("Bob", "Double");

        // Cancel booking
        cancelService.cancelBooking(id1);

        // Invalid cancellation
        cancelService.cancelBooking("XYZ123");

        // Already cancelled
        cancelService.cancelBooking(id1);

        // Display state
        history.displayAll();
        inventory.displayInventory();
        cancelService.showRollbackStack();
    }
}