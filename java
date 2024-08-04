import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// User class to manage devices
class User {
    private List<Device> devices;

    // Constructor
    public User() {
        devices = new ArrayList<>();
    }

    // Method to add a device
    public void addDevice(Device device) {
        devices.add(device);
    }

    // Method to toggle a device on/off
    public void toggleDevice(String deviceName) {
        for (Device device : devices) {
            if (device.getName().equals(deviceName)) {
                device.toggle();
                System.out.println(device.getName() + " is now " + (device.isOn() ? "on" : "off"));
                return;
            }
        }
        System.out.println("Device not found.");
    }

    // Method to calculate daily electricity consumption for all devices
    public void calculateDailyConsumption() {
        for (Device device : devices) {
            device.calculateDailyExpenses();
            device.calculateDailyUnits();
            device.checkDailyLimit();
        }
    }

    // Method to calculate total expenses for all devices
    public double getTotalExpenses() {
        double totalExpenses = 0.0;
        for (Device device : devices) {
            totalExpenses += device.getDailyExpenses();
        }
        return totalExpenses;
    }

    // Method to calculate total units consumed for all devices
    public double getTotalUnitsConsumed() {
        double totalUnits = 0.0;
        for (Device device : devices) {
            totalUnits += device.getDailyUnits();
        }
        return totalUnits;
    }

    // Getter for devices
    public List<Device> getDevices() {
        return devices;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create a user and add devices
        User user = new User();
        
        // Add devices with name, wattage range, initial budget, and daily limit
        user.addDevice(new Device("Light", 50, 70, 100.0, 200));   // 200W daily limit
        user.addDevice(new Device("Fan", 60, 90, 150.0, 300));     // 300W daily limit
        user.addDevice(new Device("AC", 1100, 1300, 500.0, 1000)); // 1000W daily limit
        user.addDevice(new Device("TV", 30, 50, 80.0, 150));   // 150W daily limit for Light2
        
        // Determine current time of day
        LocalTime currentTime = LocalTime.now();
        boolean isDayTime = currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(18, 0));

        // Toggle Light2 based on time of day
        if (isDayTime) {
            user.toggleDevice("TV"); // Turn Light2 off during daytime
        } else {
            user.toggleDevice("TV"); // Turn Light2 on during evening
        }

        // Simulate turning on/off other devices
        user.toggleDevice("Light");
        user.toggleDevice("Fan");

        // Calculate daily consumption for all devices
        user.calculateDailyConsumption();

        // Display devices and their configurations after calculations
        System.out.println("Devices:");
        for (Device device : user.getDevices()) {
            System.out.println(device);
        }

        // Print total expenses and units consumed
        System.out.println("\nTotal Expenses: " + user.getTotalExpenses() + " rupees");
        System.out.println("Total Units Consumed: " + user.getTotalUnitsConsumed() + " watts");
    }
}

// Device class to represent each appliance
class Device {
    private String name;
    private int wattage;
    private double assignedBudget; // in rupees
    private double dailyExpenses; // today's expenses
    private double dailyUnits; // today's units consumed
    private int dailyLimit; // today's wattage limit
    private boolean isOn; // device state
    private LocalTime lastToggleTime; // last time the device was toggled

    // Constructor
    public Device(String name, int minWattage, int maxWattage, double assignedBudget, int dailyLimit) {
        this.name = name;
        this.wattage = new Random().nextInt(maxWattage - minWattage + 1) + minWattage;
        this.assignedBudget = assignedBudget;
        this.dailyExpenses = 0.0;
        this.dailyUnits = 0.0;
        this.dailyLimit = dailyLimit;
        this.isOn = new Random().nextBoolean(); // Randomly assign initial on/off state
        this.lastToggleTime = LocalTime.now();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getWattage() {
        return wattage;
    }

    public double getAssignedBudget() {
        return assignedBudget;
    }

    public double getDailyExpenses() {
        return dailyExpenses;
    }

    public double getDailyUnits() {
        return dailyUnits;
    }

    public boolean isOn() {
        return isOn;
    }

    // Method to toggle device on/off
    public void toggle() {
        isOn = !isOn;
        lastToggleTime = LocalTime.now();
    }

    // Method to calculate today's expenses
    public void calculateDailyExpenses() {
        if (isOn) {
            dailyExpenses += (wattage / 1000.0) * assignedBudget;
        }
    }

    // Method to calculate today's units consumed
    public void calculateDailyUnits() {
        if (isOn) {
            dailyUnits += wattage;
        }
    }

    // Method to check if daily limit exceeded and turn off if exceeded
    public void checkDailyLimit() {
        if (dailyUnits >= dailyLimit) {
            isOn = false;
            System.out.println(name + " has reached its daily wattage limit of " + dailyLimit + "W and has been turned off.");
        }
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", wattage=" + wattage +
                ", assignedBudget=" + assignedBudget +
                ", dailyExpenses=" + dailyExpenses +
                ", dailyUnits=" + dailyUnits +
                ", dailyLimit=" + dailyLimit +
                ", isOn=" + isOn +
                ", lastToggleTime=" + lastToggleTime +
                '}';
    }
}
