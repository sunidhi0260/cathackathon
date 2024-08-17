import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

// Enum to define user roles
enum UserRole {
    ADMIN, USER
}

// Class representing a user in the system
class User {
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}

// Class representing an electronic item
class EWasteItem {
    private String name;
    private String type;
    private int yearsInUse;
    private boolean recycled;
    private String collectionDate;

    public EWasteItem(String name, String type, int yearsInUse) {
        this.name = name;
        this.type = type;
        this.yearsInUse = yearsInUse;
        this.recycled = false;
        this.collectionDate = "Not Scheduled"; // Default to not scheduled
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getYearsInUse() {
        return yearsInUse;
    }

    public boolean isRecycled() {
        return recycled;
    }

    public void recycle() {
        this.recycled = true;
    }

    public void scheduleCollection(String date) {
        this.collectionDate = date;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void displayInfo() {
        System.out.println("Item: " + name);
        System.out.println("Type: " + type);
        System.out.println("Years in Use: " + yearsInUse);
        System.out.println("Recycled: " + (recycled ? "Yes" : "No"));
        System.out.println("Collection Date: " + collectionDate);
        System.out.println("---------------------------");
    }
}

// Main class for E-Waste Monitoring System
public class EWasteMonitoringSystem {

    private ArrayList<EWasteItem> ewasteList;
    private ArrayList<User> users;
    private Scanner scanner;
    private User loggedInUser;

    public EWasteMonitoringSystem() {
        ewasteList = new ArrayList<>();
        users = new ArrayList<>();
        scanner = new Scanner(System.in);
        initializeUsers();
    }

    // Initialize some default users
    private void initializeUsers() {
        users.add(new User("admin", "admin123", UserRole.ADMIN));
        users.add(new User("user", "user123", UserRole.USER));
    }

    public void start() {
        if (!authenticate()) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        while (true) {
            System.out.println("\n--- E-Waste Monitoring System ---");
            if (loggedInUser.getRole() == UserRole.ADMIN) {
                System.out.println("1. Add E-Waste Item");
                System.out.println("2. View E-Waste Items");
                System.out.println("3. Recycle an Item");
                System.out.println("4. Generate Report");
                System.out.println("5. Schedule Collection");
                System.out.println("6. Exit");
            } else {
                System.out.println("1. View E-Waste Items");
                System.out.println("2. Generate Report");
                System.out.println("3. Exit");
            }

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    if (loggedInUser.getRole() == UserRole.ADMIN) {
                        addEWasteItem();
                    } else {
                        viewEWasteItems();
                    }
                    break;
                case 2:
                    if (loggedInUser.getRole() == UserRole.ADMIN) {
                        viewEWasteItems();
                    } else {
                        generateReport();
                    }
                    break;
                case 3:
                    if (loggedInUser.getRole() == UserRole.ADMIN) {
                        recycleItem();
                    } else {
                        System.out.println("Exiting the system...");
                        return;
                    }
                    break;
                case 4:
                    if (loggedInUser.getRole() == UserRole.ADMIN) {
                        generateReport();
                    }
                    break;
                case 5:
                    if (loggedInUser.getRole() == UserRole.ADMIN) {
                        scheduleCollection();
                    }
                    break;
                case 6:
                    if (loggedInUser.getRole() == UserRole.ADMIN) {
                        System.out.println("Exiting the system...");
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // Method to authenticate user
    private boolean authenticate() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                System.out.println("Login successful! Welcome, " + loggedInUser.getUsername());
                return true;
            }
        }

        System.out.println("Invalid credentials.");
        return false;
    }

    private void addEWasteItem() {
        System.out.print("Enter Item Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Item Type (e.g., Laptop, Phone): ");
        String type = scanner.nextLine();
        System.out.print("Enter Years in Use: ");
        int yearsInUse = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        ewasteList.add(new EWasteItem(name, type, yearsInUse));
        System.out.println("E-Waste item added successfully.");
    }

    private void viewEWasteItems() {
        if (ewasteList.isEmpty()) {
            System.out.println("No E-Waste items to display.");
            return;
        }

        System.out.println("\n--- E-Waste Items ---");
        System.out.println("1. View All Items");
        System.out.println("2. Sort by Years in Use");
        System.out.println("3. View by Type");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                for (EWasteItem item : ewasteList) {
                    item.displayInfo();
                }
                break;
            case 2:
                Collections.sort(ewasteList, Comparator.comparingInt(EWasteItem::getYearsInUse));
                for (EWasteItem item : ewasteList) {
                    item.displayInfo();
                }
                break;
            case 3:
                System.out.print("Enter Type to Filter (e.g., Laptop, Phone): ");
                String type = scanner.nextLine();
                for (EWasteItem item : ewasteList) {
                    if (item.getType().equalsIgnoreCase(type)) {
                        item.displayInfo();
                    }
                }
                break;
            default:
                System.out.println("Invalid choice! Returning to main menu.");
        }
    }

    private void recycleItem() {
        if (ewasteList.isEmpty()) {
            System.out.println("No E-Waste items to recycle.");
            return;
        }

        System.out.print("Enter the name of the item to recycle: ");
        String itemName = scanner.nextLine();
        boolean found = false;

        for (EWasteItem item : ewasteList) {
            if (item.getName().equalsIgnoreCase(itemName) && !item.isRecycled()) {
                item.recycle();
                System.out.println("Item recycled successfully.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Item not found or already recycled.");
        }
    }

    private void generateReport() {
        if (ewasteList.isEmpty()) {
            System.out.println("No E-Waste items to report.");
            return;
        }

        int totalItems = ewasteList.size();
        int recycledItems = 0;

        for (EWasteItem item : ewasteList) {
            if (item.isRecycled()) {
                recycledItems++;
            }
        }

        System.out.println("\n--- E-Waste Report ---");
        System.out.println("Total E-Waste Items: " + totalItems);
        System.out.println("Recycled Items: " + recycledItems);
        System.out.println("Pending for Recycling: " + (totalItems - recycledItems));
        System.out.println("---------------------------");
    }

    private void scheduleCollection() {
        if (ewasteList.isEmpty()) {
            System.out.println("No E-Waste items to schedule.");
            return;
        }

        System.out.print("Enter the name of the item to schedule collection: ");
        String itemName = scanner.nextLine();
        boolean found = false;

        for (EWasteItem item : ewasteList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                System.out.print("Enter Collection Date (e.g., 2024-08-20): ");
                String date = scanner.nextLine();
                item.scheduleCollection(date);
                System.out.println("Collection scheduled successfully for " + date);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Item not found.");
        }
    }

    public static void main(String[] args) {
        EWasteMonitoringSystem system = new EWasteMonitoringSystem();
        system.start();
    }
}
                