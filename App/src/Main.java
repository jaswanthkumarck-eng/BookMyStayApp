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

    public void displayDetails() {
        System.out.println("Room No: " + roomNumber);
        System.out.println("Type   : " + getType());
        System.out.println("Beds   : " + beds);
        System.out.println("Price  : " + price);
    }
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

    public void addRequest(Reservation r) { queue.add(r); }
    public Reservation getNext() { return queue.poll(); }
    public boolean isEmpty() { return queue.isEmpty(); }
}

// -------- Booking Service (returns reservationId) --------
class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public String processSingleBooking(Reservation r) {
        String type = r.getRoomType();

        if (inventory.getAvailability(type) > 0) {

            String roomId = generateRoomId(type);

            allocatedRooms.putIfAbsent(type, new HashSet<>());
            Set<String> set = allocatedRooms.get(type);

            if (!set.contains(roomId)) {
                set.add(roomId);
                inventory.reduceAvailability(type);

                System.out.println("Booking Confirmed for " + r.getGuestName());
                System.out.println("Room Type: " + type + " | Room ID: " + roomId);

                return roomId; // use as reservationId
            }
        }

        System.out.println("Booking Failed for " + r.getGuestName());
        return null;
    }

    private String generateRoomId(String type) {
        return type.substring(0, 1) + UUID.randomUUID().toString().substring(0, 4);
    }
}

// -------- UC7: Add-On Service --------
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() { return price; }

    public String getName() { return name; }
}

// -------- UC7: Add-On Manager --------
class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotal(String reservationId) {
        double total = 0;
        List<AddOnService> list = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : list) {
            total += s.getPrice();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        System.out.println("\nAdd-On Services for Reservation: " + reservationId);

        List<AddOnService> list = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService s : list) {
            System.out.println(s.getName() + " - " + s.getPrice());
        }

        System.out.println("Total Add-On Cost: " + calculateTotal(reservationId));
    }
}

// -------- Main --------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Booking
        Reservation r1 = new Reservation("Alice", "Single");
        String reservationId = bookingService.processSingleBooking(r1);

        // UC7: Add-On services
        AddOnServiceManager manager = new AddOnServiceManager();

        if (reservationId != null) {
            manager.addService(reservationId, new AddOnService("Breakfast", 200));
            manager.addService(reservationId, new AddOnService("WiFi", 100));
            manager.addService(reservationId, new AddOnService("Parking", 150));

            manager.displayServices(reservationId);
        }
    }
}