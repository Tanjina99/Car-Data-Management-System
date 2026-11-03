package edu.valencia.cardms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarManager {

    private final List<Car> cars;

    public CarManager() {
        cars = new ArrayList<>();
    }

    // CREATE
    public boolean addCar(Car car) {

        if (car == null) {
            return false;
        }

        if (findCarById(car.getId()) != null) {
            return false;
        }

        cars.add(car);
        return true;
    }

    // READ
    public List<Car> getAllCars() {

        return new ArrayList<>(cars);
    }

    // FIND
    public Car findCarById(int id) {

        for (Car car : cars) {

            if (car.getId() == id) {
                return car;
            }
        }

        return null;
    }

    // UPDATE
    public boolean updateCar(
            int id,
            String make,
            String model,
            int year,
            double price,
            int mileage,
            String color) {

        Car car = findCarById(id);

        if (car == null) {
            return false;
        }

        // Validate EVERYTHING before changing anything.
        Car.validateData(
                id,
                make,
                model,
                year,
                price,
                mileage,
                color
        );

        // Only after validation succeeds do we change the object.
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        car.setMileage(mileage);
        car.setColor(color);

        return true;
    }

    // DELETE
    public boolean deleteCar(int id) {

        Car car = findCarById(id);

        if (car == null) {
            return false;
        }

        cars.remove(car);
        return true;
    }

    // CUSTOM ACTION
    // Calculates average price of all cars.
    public double calculateAveragePrice() {

        if (cars.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (Car car : cars) {
            total += car.getPrice();
        }

        return total / cars.size();
    }

    // IMPORT FROM TEXT FILE
    public int loadFromFile(String filename) throws IOException {

        int recordsLoaded = 0;
        int lineNumber = 0;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length != 7) {
                    System.out.println(
                            "Line " + lineNumber
                                    + " was skipped because it does not have 7 fields."
                    );
                    continue;
                }

                String idText = data[0].trim();
                String make = data[1].trim();
                String model = data[2].trim();
                String yearText = data[3].trim();
                String priceText = data[4].trim();
                String mileageText = data[5].trim();
                String color = data[6].trim();

                // ID must contain digits only.
                if (!idText.matches("\\d+")) {
                    System.out.println(
                            "Line " + lineNumber
                                    + " was skipped: ID must contain numbers only."
                    );
                    continue;
                }

                // Year must contain exactly 4 digits.
                if (!yearText.matches("\\d{4}")) {
                    System.out.println(
                            "Line " + lineNumber
                                    + " was skipped: Year must contain exactly 4 digits."
                    );
                    continue;
                }

                // Price must contain digits with an optional decimal part.
                // No + or - signs are allowed.
                if (!priceText.matches("\\d+(\\.\\d+)?")) {
                    System.out.println(
                            "Line " + lineNumber
                                    + " was skipped: Price must be a valid number without + or - signs."
                    );
                    continue;
                }

                // Mileage must contain digits only.
                if (!mileageText.matches("\\d+")) {
                    System.out.println(
                            "Line " + lineNumber
                                    + " was skipped: Mileage must contain numbers only."
                    );
                    continue;
                }

                try {

                    int id = Integer.parseInt(idText);
                    int year = Integer.parseInt(yearText);
                    double price = Double.parseDouble(priceText);
                    int mileage = Integer.parseInt(mileageText);

                    Car car = new Car(
                            id,
                            make,
                            model,
                            year,
                            price,
                            mileage,
                            color
                    );

                    if (addCar(car)) {
                        recordsLoaded++;
                    } else {
                        System.out.println(
                                "Line " + lineNumber
                                        + " was skipped: A car with ID "
                                        + id + " already exists."
                        );
                    }

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Line " + lineNumber
                                    + " was skipped: "
                                    + e.getMessage()
                    );
                }
            }
        }

        return recordsLoaded;
    }
}