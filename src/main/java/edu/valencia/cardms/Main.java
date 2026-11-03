package edu.valencia.cardms;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CarManager manager = new CarManager();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addCar();
                    break;

                case 2:
                    displayCars();
                    break;

                case 3:
                    updateCar();
                    break;

                case 4:
                    deleteCar();
                    break;

                case 5:
                    importCars();
                    break;

                case 6:
                    calculateAveragePrice();
                    break;

                case 7:
                    running = false;
                    System.out.println("Program ended.");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please enter 1-7."
                    );
            }
        }

        scanner.close();
    }

    private static void displayMenu() {

        System.out.println();
        System.out.println("Car Data Management System");
        System.out.println("1. Add Car");
        System.out.println("2. Display All Cars");
        System.out.println("3. Update Car");
        System.out.println("4. Delete Car");
        System.out.println("5. Import Cars From File");
        System.out.println("6. Calculate Average Price");
        System.out.println("7. Exit");
    }
    // Collects car information and adds a new car to the system.
    private static void addCar() {

        System.out.println();
        System.out.println("Add Car");

        int id = readPositiveInt("ID: ");

        if (manager.findCarById(id) != null) {

            System.out.println(
                    "A car with that ID already exists."
            );

            return;
        }

        String make = readMake("Make: ");
        String model = readString("Model: ");
        int year = readYear("Year: ");
        double price = readPrice("Price: ");
        int mileage = readMileage("Mileage: ");
        String color = readColor("Color: ");

        try {

            Car car = new Car(
                    id,
                    make,
                    model,
                    year,
                    price,
                    mileage,
                    color
            );

            if (manager.addCar(car)) {

                System.out.println(
                        "Car added successfully."
                );

            } else {

                System.out.println(
                        "Car could not be added."
                );
            }

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Car was not added: "
                            + e.getMessage()
            );
        }
    }
    // Displays all car records currently stored in the system.
    private static void displayCars() {

        System.out.println();
        System.out.println("All Cars");

        List<Car> cars =
                manager.getAllCars();

        if (cars.isEmpty()) {

            System.out.println(
                    "No cars found."
            );

            return;
        }

        for (Car car : cars) {

            System.out.println(car);
        }
    }
    // Updates an existing car using its ID and the new information.
    private static void updateCar() {

        System.out.println();
        System.out.println("Update Car");

        int id =
                readPositiveInt(
                        "Enter the ID of the car to update: "
                );

        if (manager.findCarById(id) == null) {

            System.out.println(
                    "Car not found."
            );

            return;
        }

        String make =
                readMake("New Make: ");

        String model =
                readString("New Model: ");

        int year =
                readYear("New Year: ");

        double price =
                readPrice("New Price: ");

        int mileage =
                readMileage("New Mileage: ");

        String color =
                readColor("New Color: ");

        try {

            boolean updated =
                    manager.updateCar(
                            id,
                            make,
                            model,
                            year,
                            price,
                            mileage,
                            color
                    );

            if (updated) {

                System.out.println(
                        "Car updated successfully."
                );

            } else {

                System.out.println(
                        "Car could not be updated."
                );
            }

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Car was not updated: "
                            + e.getMessage()
            );
        }
    }

    private static void deleteCar() {

        System.out.println();
        System.out.println("Delete Car");

        int id =
                readPositiveInt(
                        "Enter the ID of the car to delete: "
                );

        if (manager.deleteCar(id)) {

            System.out.println(
                    "Car deleted successfully."
            );

        } else {

            System.out.println(
                    "Car not found."
            );
        }
    }
    // Imports car records from a text file.
    private static void importCars() {

        System.out.println();
        System.out.println("Import Cars");

        String filename =
                readString(
                        "Enter the text file location: "
                );

        try {

            int count =
                    manager.loadFromFile(filename);

            System.out.println(
                    count
                            + " car(s) imported successfully."
            );

        } catch (IOException e) {

            System.out.println(
                    "Could not open the file. "
                            + "Please check the file location."
            );
        }
    }

    private static void calculateAveragePrice() {

        if (manager.getAllCars().isEmpty()) {

            System.out.println(
                    "There are no cars to calculate."
            );

            return;
        }

        double average =
                manager.calculateAveragePrice();

        System.out.printf(
                "Average car price: $%.2f%n",
                average
        );
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.matches("\\d+")) {

                System.out.println(
                        "Invalid input. Please enter numbers only."
                );

                continue;
            }

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. The number is too large."
                );
            }
        }
    }

    private static int readPositiveInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (input.startsWith("+")) {

                System.out.println(
                        "ID cannot have + sign."
                );

                continue;
            }

            if (input.startsWith("-")) {

                System.out.println(
                        "ID cannot be negative."
                );

                continue;
            }

            if (!input.matches("\\d+")) {

                System.out.println(
                        "Invalid ID. Please enter a whole number."
                );

                continue;
            }

            try {

                int id =
                        Integer.parseInt(input);

                if (id <= 0) {

                    System.out.println(
                            "ID must be greater than 0."
                    );

                    continue;
                }

                return id;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid ID. The number is too large."
                );
            }
        }
    }

    private static int readMileage(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println(
                        "Mileage cannot be empty."
                );

                continue;
            }

            if (input.startsWith("+")) {

                System.out.println(
                        "Mileage cannot have + sign."
                );

                continue;
            }

            if (input.startsWith("-")) {

                System.out.println(
                        "Mileage cannot be negative."
                );

                continue;
            }

            if (!input.matches("\\d+")) {

                System.out.println(
                        "Invalid mileage. Please enter a whole number."
                );

                continue;
            }

            try {

                int mileage =
                        Integer.parseInt(input);

                if (mileage > 150000) {

                    System.out.println(
                            "Mileage must be 150000 or less."
                    );

                    continue;
                }

                return mileage;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Mileage must be 150000 or less."
                );
            }
        }
    }

    private static int readYear(String message) {

        int currentYear =
                LocalDate.now().getYear();

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.matches("\\d{4}")) {

                System.out.println(
                        "Invalid year. Please enter exactly 4 digits."
                );

                continue;
            }

            int year =
                    Integer.parseInt(input);

            if (year < 2000) {

                System.out.println(
                        "Invalid year. The year must be 2000 or later."
                );

                continue;
            }

            if (year > currentYear) {

                System.out.println(
                        "Invalid year. The year cannot be later than "
                                + currentYear
                                + "."
                );

                continue;
            }

            return year;
        }
    }

    private static double readPrice(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println(
                        "Price cannot be empty."
                );

                continue;
            }

            if (input.startsWith("+")) {

                System.out.println(
                        "Price cannot have + sign."
                );

                continue;
            }

            if (input.startsWith("-")) {

                System.out.println(
                        "Price cannot be negative."
                );

                continue;
            }

            if (!input.matches(
                    "[0-9]+(\\.[0-9]+)?")) {

                System.out.println(
                        "Price must be a number."
                );

                continue;
            }

            try {

                double price =
                        Double.parseDouble(input);

                if (!Double.isFinite(price)) {

                    System.out.println(
                            "Price must be a real number."
                    );

                    continue;
                }

                if (price > 250000) {

                    System.out.println(
                            "Price must be 250000 or less."
                    );

                    continue;
                }

                return price;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Price must be 250000 or less."
                );
            }
        }
    }

    private static String readMake(String message) {

        String[] validMakes = {

                "Toyota",
                "Honda",
                "Ford",
                "Chevrolet",
                "Nissan",
                "Hyundai",
                "Kia",
                "Mazda",
                "Subaru",
                "Volkswagen",
                "BMW",
                "Mercedes-Benz",
                "Audi",
                "Lexus",
                "Acura",
                "Infiniti",
                "Volvo",
                "Tesla",
                "Jeep",
                "Dodge",
                "Chrysler",
                "Ram",
                "GMC",
                "Cadillac",
                "Buick",
                "Lincoln",
                "Porsche",
                "Ferrari",
                "Lamborghini",
                "Maserati"
        };

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println(
                        "Make cannot be empty."
                );

                continue;
            }

            boolean valid = false;

            for (String make : validMakes) {

                if (make.equalsIgnoreCase(input)) {

                    valid = true;
                    break;
                }
            }

            if (valid) {

                return input;
            }

            System.out.println(
                    "Invalid car make. "
                            + "Please enter a valid car manufacturer."
            );
        }
    }

    private static String readColor(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return Car.validateColor(input);

            } catch (IllegalArgumentException e) {

                System.out.println(
                        e.getMessage()
                );
            }
        }
    }

    private static String readString(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.isEmpty()) {

                return input;
            }

            System.out.println(
                    "Input cannot be empty. Please try again."
            );
        }
    }
}