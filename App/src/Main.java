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
        System.out.println("Type: " + getType());
        System.out.println("Beds: " + beds);
        System.out.println("Price: " + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom(int roomNumber) {
        super(roomNumber, 1, 1000);
    }

    public String getType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber) {
        super(roomNumber, 2, 2000);
    }

    public String getType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, 3, 5000);
    }

    public String getType() {
        return "Suite Room";
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {
        Room r1 = new SingleRoom(101);
        Room r2 = new DoubleRoom(102);
        Room r3 = new SuiteRoom(103);

        boolean isSingleAvailable = true;
        boolean isDoubleAvailable = false;
        boolean isSuiteAvailable = true;

        System.out.println("=== BookMyStayApp v2.0 ===");

        r1.displayDetails();
        System.out.println("Available: " + isSingleAvailable);
        System.out.println();

        r2.displayDetails();
        System.out.println("Available: " + isDoubleAvailable);
        System.out.println();

        r3.displayDetails();
        System.out.println("Available: " + isSuiteAvailable);
    }
}