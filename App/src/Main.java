import java.util.*;

// -------- Custom Exception --------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------- Inventory --------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 0); // no availability
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) throws InvalidBookingException {
        int count = getAvailability(type);
        if (count <= 0) {
            throw new InvalidBookingException("No availability for room type: " + type);
        }
        inventory.put(type, count - 1);
    }
}

// -------- Reservation --------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// -------- Booking Queue --------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNext() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------- Validator --------
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory) throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + r.getRoomType());
        }
    }
}

// -------- Booking Service --------
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBookings(BookingQueue queue) {

        System.out.println("=== Booking Processing (v9.0) ===\n");

        while (!queue.isEmpty()) {
            Reservation r = queue.getNext();

            try {
                // Validation (Fail-Fast)
                BookingValidator.validate(r, inventory);

                // Allocation
                String roomId = generateRoomId(r.getRoomType());
                inventory.reduceAvailability(r.getRoomType());

                System.out.println("Booking Confirmed for " + r.getGuestName());
                System.out.println("Room Type: " + r.getRoomType() + " | Room ID: " + roomId);
                System.out.println("--------------------------------");

            } catch (InvalidBookingException e) {
                // Graceful failure
                System.out.println("Booking Failed for " + r.getGuestName());
                System.out.println("Reason: " + e.getMessage());
                System.out.println("--------------------------------");
            }
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0,1) + UUID.randomUUID().toString().substring(0,4);
    }
}

// -------- Main --------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Valid request
        queue.addRequest(new Reservation("Alice", "Single"));

        // Invalid room type
        queue.addRequest(new Reservation("Bob", "Luxury"));

        // No availability
        queue.addRequest(new Reservation("Charlie", "Suite"));

        // Empty name
        queue.addRequest(new Reservation("", "Double"));

        BookingService service = new BookingService(inventory);
        service.processBookings(queue);
    }
}