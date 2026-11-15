
package edu.valencia.cardms;

import java.io.IOException;
import java.util.List;

public class CarManager {

    private DatabaseManager databaseManager;

    // Empty constructor for the existing GUI
    public CarManager() {
    }

    // Constructor that connects to the database
    public CarManager(DatabaseManager databaseManager) {

        if (databaseManager == null) {
            throw new IllegalArgumentException(
                    "Database manager cannot be null."
            );
        }

        this.databaseManager = databaseManager;
    }

    // Set the database connection.
    public void setDatabaseManager(DatabaseManager databaseManager) {

        if (databaseManager == null) {
            throw new IllegalArgumentException(
                    "Database manager cannot be null."
            );
        }

        this.databaseManager = databaseManager;
    }

    // Add a car to the SQLite database.
    public boolean addCar(Car car) {

        if (databaseManager == null) {
            return false;
        }

        return databaseManager.addCar(car);
    }

    // Get all cars from the database
    public List<Car> getAllCars() {

        if (databaseManager == null) {
            return List.of();
        }

        return databaseManager.getAllCars();
    }

    // Find a car using its ID
    public Car findCarById(int id) {

        if (databaseManager == null) {
            return null;
        }

        return databaseManager.findCarById(id);
    }

    // Update a car in the database
    // Update a car without changing its ID.
    public boolean updateCar(
            int id,
            String make,
            String model,
            int year,
            double price,
            int mileage,
            String color) {

        return updateCar(
                id, id, make, model, year, price, mileage, color
        );
    }

    // Update a car, including changing its ID.
    public boolean updateCar(
            int oldId,
            int newId,
            String make,
            String model,
            int year,
            double price,
            int mileage,
            String color) {

        if (databaseManager == null) {
            return false;
        }

        return databaseManager.updateCar(
                oldId, newId, make, model,
                year, price, mileage, color
        );
    }

    // Delete a car from the database
    public boolean deleteCar(int id) {

        if (databaseManager == null) {
            return false;
        }

        return databaseManager.deleteCar(id);
    }

    // Calculate the average price of all cars
    public double calculateAveragePrice() {

        if (databaseManager == null) {
            return 0.0;
        }

        return databaseManager.calculateAveragePrice();
    }

    // Import cars from a file into the database
    public int loadFromFile(String filename) throws IOException {

        if (databaseManager == null) {
            return 0;
        }

        return databaseManager.loadFromFile(filename);
    }
}
