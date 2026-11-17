
package edu.valencia.cardms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for the Car Data Management System.
 *
 * This class connects to the SQLite database and manages car records.
 * It supports adding, retrieving, updating, deleting, and importing cars.
 */
public class DatabaseManager {

    private final String databaseURL;

    /**
     * Creates a database manager and initializes the cars table.
     *
     * @param databasePath the location of the SQLite database file
     * @throws IllegalArgumentException if the database path is empty or null
     * @throws SQLException if the database connection or table creation fails
     */
    public DatabaseManager(String databasePath) throws SQLException {

        if (databasePath == null || databasePath.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Database location cannot be empty."
            );
        }

        databaseURL = "jdbc:sqlite:" + databasePath;

        createTable();
    }

    /**
     * Opens a connection to the SQLite database.
     *
     * @return an active database connection
     * @throws SQLException if a database connection cannot be established
     */
    public Connection connect() throws SQLException {

        return DriverManager.getConnection(databaseURL);
    }

    /**
     * Creates the cars table if it does not already exist.
     *
     * @throws SQLException if the table cannot be created
     */
    private void createTable() throws SQLException {

        String sql =
                "CREATE TABLE IF NOT EXISTS cars (" +
                        "id INTEGER PRIMARY KEY, " +
                        "make TEXT NOT NULL, " +
                        "model TEXT NOT NULL, " +
                        "year INTEGER NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "mileage INTEGER NOT NULL, " +
                        "color TEXT NOT NULL" +
                        ")";

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        }
    }

    /**
     * Adds a car record to the database.
     *
     * @param car the car to add
     * @return true if the car was added successfully, otherwise false
     */
    public boolean addCar(Car car) {

        String sql = "INSERT INTO cars " +
                "(id, make, model, year, price, mileage, color) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, car.getId());
            statement.setString(2, car.getMake());
            statement.setString(3, car.getModel());
            statement.setInt(4, car.getYear());
            statement.setDouble(5, car.getPrice());
            statement.setInt(6, car.getMileage());
            statement.setString(7, car.getColor());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding car: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all car records from the database.
     *
     * @return a list containing all cars, or an empty list if none are found
     */
    public List<Car> getAllCars() {

        List<Car> cars = new ArrayList<>();

        String sql = "SELECT * FROM cars";

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                Car car = new Car(
                        result.getInt("id"),
                        result.getString("make"),
                        result.getString("model"),
                        result.getInt("year"),
                        result.getDouble("price"),
                        result.getInt("mileage"),
                        result.getString("color")
                );

                cars.add(car);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving cars: " + e.getMessage());
        }

        return cars;
    }

    /**
     * Searches the database for a car using its ID.
     *
     * @param id the ID of the car to find
     * @return the matching car, or null if the car is not found
     */
    public Car findCarById(int id) {

        String sql = "SELECT * FROM cars WHERE id = ?";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Car(
                            result.getInt("id"),
                            result.getString("make"),
                            result.getString("model"),
                            result.getInt("year"),
                            result.getDouble("price"),
                            result.getInt("mileage"),
                            result.getString("color")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding car: " + e.getMessage());
        }

        return null;
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

        String sql = "UPDATE cars SET " +
                "id = ?, make = ?, model = ?, year = ?, " +
                "price = ?, mileage = ?, color = ? " +
                "WHERE id = ?";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, newId);
            statement.setString(2, make);
            statement.setString(3, model);
            statement.setInt(4, year);
            statement.setDouble(5, price);
            statement.setInt(6, mileage);
            statement.setString(7, color);
            statement.setInt(8, oldId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating car: " + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Deletes a car from the database using its ID.
     *
     * @param id the ID of the car to delete
     * @return true if the car was deleted successfully, otherwise false
     */
    public boolean deleteCar(int id) {

        String sql = "DELETE FROM cars WHERE id = ?";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting car: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calculates the average price of all cars in the database.
     *
     * @return the average car price, or 0.0 if no result is available
     */
    public double calculateAveragePrice() {

        String sql = "SELECT AVG(price) FROM cars";

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            if (result.next()) {
                return result.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Error calculating average: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Imports car records from a CSV file into the database.
     *
     * Each record must contain seven comma-separated values:
     * ID, make, model, year, price, mileage, and color.
     *
     * Invalid records are skipped.
     *
     * @param filename the path to the CSV file
     * @return the number of cars successfully imported
     * @throws IOException if an error occurs while reading the file
     */
    public int loadFromFile(String filename) throws IOException {

        int count = 0;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length != 7) {
                    continue;
                }

                try {

                    int id = Integer.parseInt(data[0].trim());
                    String make = data[1].trim();
                    String model = data[2].trim();
                    int year = Integer.parseInt(data[3].trim());
                    double price = Double.parseDouble(data[4].trim());
                    int mileage = Integer.parseInt(data[5].trim());
                    String color = data[6].trim();

                    Car car = new Car(
                            id, make, model, year,
                            price, mileage, color
                    );

                    if (addCar(car)) {
                        count++;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid car record.");
                }
            }
        }

        return count;
    }
}
