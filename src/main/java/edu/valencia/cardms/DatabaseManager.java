
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

public class DatabaseManager {

    private final String databaseURL;

    // Constructor
    public DatabaseManager(String databasePath) throws SQLException {

        if (databasePath == null || databasePath.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Database location cannot be empty."
            );
        }

        databaseURL = "jdbc:sqlite:" + databasePath;

        createTable();
    }

    // Connect to SQLite
    public Connection connect() throws SQLException {

    return DriverManager.getConnection(databaseURL);
    }
    // Create cars table
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

    // Add a car
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

    // Get all cars
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

    // Find a car by ID
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

    // Update a car

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


    // Delete a car
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

    // Calculate average car price
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

    // Import cars from a CSV file
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
