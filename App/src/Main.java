import java.io.*;
import java.util.*;

// -------- Reservation --------
class Reservation implements Serializable {
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
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// -------- Inventory --------
class RoomInventory implements Serializable {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public void reduce(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// -------- System State (for persistence) --------
class SystemState implements Serializable {
    List<Reservation> bookings;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> bookings, Map<String, Integer> inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// -------- Persistence Service --------
class PersistenceService {

    private static final String FILE_NAME = "data.ser";

    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("\nData saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("Data loaded successfully!");
            return (SystemState) ois.readObject();
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh...");
            return null;
        }
    }
}

// -------- Main --------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Try to restore previous state
        SystemState state = PersistenceService.load();

        List<Reservation> bookings;
        RoomInventory inventory = new RoomInventory();

        if (state != null) {
            bookings = state.bookings;
            inventory.getInventory().putAll(state.inventory);
        } else {
            bookings = new ArrayList<>();
        }

        // Simulate booking
        String id = "S" + UUID.randomUUID().toString().substring(0, 4);
        Reservation r = new Reservation(id, "Alice", "Single");

        bookings.add(r);
        inventory.reduce("Single");

        System.out.println("\nNew Booking:");
        r.display();

        // Display state
        System.out.println("\nBooking History:");
        for (Reservation res : bookings) {
            res.display();
        }

        inventory.display();

        // Save state before exit
        PersistenceService.save(new SystemState(bookings, inventory.getInventory()));
    }
}