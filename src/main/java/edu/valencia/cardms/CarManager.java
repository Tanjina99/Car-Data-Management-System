
package edu.valencia.cardms;

import java.io.IOException;
import java.util.List;

/**
 * Manages car operations for the Car Data Management System.
 *
 * This class connects the graphical user interface to the
 * DatabaseManager. It handles adding, retrieving, updating,
 * deleting, and importing car records.
 */
public class CarManager {

    private DatabaseManager databaseManager;

    /**
     * Creates a CarManager without a database connection.
     * A database manager can be assigned later using setDatabaseManager().
     */
    public CarManager() {
    }

    /**
     * Creates a CarManager with a database connection.
     *
     * @param databaseManager the database manager used for car operations
     * @throws IllegalArgumentException if the database manager is null
     */
    public CarManager(DatabaseManager databaseManager) {

        if (databaseManager == null) {
            throw new IllegalArgumentException(
                    "Database manager cannot be null."
            );
        }

        this.databaseManager = databaseManager;
    }

    /**
     * Sets the database manager used for car operations.
     *
     * @param databaseManager the database manager to connect
     * @throws IllegalArgumentException if the database manager is null
     */
    public void setDatabaseManager(DatabaseManager databaseManager) {

        if (databaseManager == null) {
            throw new IllegalArgumentException(
                    "Database manager cannot be null."
            );
        }

        this.databaseManager = databaseManager;
    }

    /**
     * Adds a car to the SQLite database.
     *
     * @param car the car to add
     * @return true if the car was added successfully, otherwise false
     */
    public boolean addCar(Car car) {

        if (databaseManager == null) {
            return false;
        }

        return databaseManager.addCar(car);
    }

    /**
     * Retrieves all cars stored in the database.
     *
     * @return a list of all cars, or an empty list if no database manager is set
     */
    public List<Car> getAllCars() {

        if (databaseManager == null) {
            return List.of();
        }

        return databaseManager.getAllCars();
    }

    /**
     * Finds a car using its ID.
     *
     * @param id the ID of the car to find
     * @return the matching car, or null if the car is not found
     */
    public Car findCarById(int id) {

        if (databaseManager == null) {
            return null;
        }

        return databaseManager.findCarById(id);
    }

    /**
     * Updates a car without changing its ID.
     *
     * @param id the ID of the car to update
     * @param make the new car manufacturer
     * @param model the new car model
     * @param year the new manufacturing year
     * @param price the new car price
     * @param mileage the new car mileage
     * @param color the new car color
     * @return true if the car was updated successfully, otherwise false
     */
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

    /**
     * Updates a car and allows its ID to be changed.
     *
     * @param oldId the current ID of the car
     * @param newId the new ID for the car
     * @param make the new car manufacturer
     * @param model the new car model
     * @param year the new manufacturing year
     * @param price the new car price
     * @param mileage the new car mileage
     * @param color the new car color
     * @return true if the car was updated successfully, otherwise false
     */
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

    /**
     * Deletes a car from the database using its ID.
     *
     * @param id the ID of the car to delete
     * @return true if the car was deleted successfully, otherwise false
     */
    public boolean deleteCar(int id) {

        if (databaseManager == null) {
            return false;
        }

        return databaseManager.deleteCar(id);
    }

    /**
     * Calculates the average price of all cars in the database.
     *
     * @return the average car price, or 0.0 if no database manager is set
     */
    public double calculateAveragePrice() {

        if (databaseManager == null) {
            return 0.0;
        }

        return databaseManager.calculateAveragePrice();
    }

    /**
     * Imports car records from a file into the database.
     *
     * @param filename the path or name of the file containing car records
     * @return the number of cars loaded
     * @throws IOException if an error occurs while reading the file
     */
    public int loadFromFile(String filename) throws IOException {

        if (databaseManager == null) {
            return 0;
        }

        return databaseManager.loadFromFile(filename);
    }
}
