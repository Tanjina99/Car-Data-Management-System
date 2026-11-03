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

    private static final String DATABASE_URL = "jdbc:sqlite:cars.db";

    public DatabaseManager() {
        createTable();
    }

    public Connection connect() throws SQLException {

        return DriverManager.getConnection(DATABASE_URL);
    }

    private void createTable() {

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

        } catch (SQLException e) {

            System.out.println(
                    "Could not create the cars table."
            );

            System.out.println(
                    "Database error: " + e.getMessage()
            );
        }
    }

    public boolean addCar(Car car) {

        if (car == null) {
            return false;
        }

        String sql =
                "INSERT INTO cars " +
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

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            return false;
        }
    }

    public List<Car> getAllCars() {

        List<Car> cars = new ArrayList<>();

        String sql =
                "SELECT id, make, model, year, price, mileage, color " +
                        "FROM cars ORDER BY id";

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {

            while (resultSet.next()) {

                Car car = new Car(
                        resultSet.getInt("id"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("mileage"),
                        resultSet.getString("color")
                );

                cars.add(car);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not read cars from the database."
            );
        }

        return cars;
    }

    public Car findCarById(int id) {

        String sql =
                "SELECT id, make, model, year, price, mileage, color " +
                        "FROM cars WHERE id = ?";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Car(
                            resultSet.getInt("id"),
                            resultSet.getString("make"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("mileage"),
                            resultSet.getString("color")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not find the car."
            );
        }

        return null;
    }

    public boolean updateCar(
            int id,
            String make,
            String model,
            int year,
            double price,
            int mileage,
            String color) {

        Car.validateData(
                id,
                make,
                model,
                year,
                price,
                mileage,
                color
        );

        String sql =
                "UPDATE cars SET " +
                        "make = ?, " +
                        "model = ?, " +
                        "year = ?, " +
                        "price = ?, " +
                        "mileage = ?, " +
                        "color = ? " +
                        "WHERE id = ?";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, make.trim());
            statement.setString(2, model.trim());
            statement.setInt(3, year);
            statement.setDouble(4, price);
            statement.setInt(5, mileage);
            statement.setString(6, color.trim());
            statement.setInt(7, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            return false;
        }
    }

    public boolean deleteCar(int id) {

        String sql =
                "DELETE FROM cars WHERE id = ?";

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            return false;
        }
    }

    public double calculateAveragePrice() {

        String sql =
                "SELECT AVG(price) AS average_price FROM cars";

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {

            if (resultSet.next()) {
                return resultSet.getDouble("average_price");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not calculate the average price."
            );
        }

        return 0.0;
    }

    public int loadFromFile(String filename)
            throws IOException {

        int recordsLoaded = 0;
        int lineNumber = 0;

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(filename)
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data =
                        line.split(",", -1);

                if (data.length != 7) {

                    System.out.println(
                            "Line " + lineNumber +
                                    " was skipped because it does not have 7 fields."
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

                if (!idText.matches("[0-9]+")) {

                    System.out.println(
                            "Line " + lineNumber +
                                    " was skipped: ID must contain numbers only."
                    );

                    continue;
                }

                if (!yearText.matches("[0-9]{4}")) {

                    System.out.println(
                            "Line " + lineNumber +
                                    " was skipped: Year must contain exactly 4 digits."
                    );

                    continue;
                }

                if (!priceText.matches("[0-9]+(\\.[0-9]+)?")) {

                    System.out.println(
                            "Line " + lineNumber +
                                    " was skipped: Price must be a valid number."
                    );

                    continue;
                }

                if (!mileageText.matches("[0-9]+")) {

                    System.out.println(
                            "Line " + lineNumber +
                                    " was skipped: Mileage must contain numbers only."
                    );

                    continue;
                }

                try {

                    int id = Integer.parseInt(idText);
                    int year = Integer.parseInt(yearText);
                    double price =
                            Double.parseDouble(priceText);
                    int mileage =
                            Integer.parseInt(mileageText);

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
                                "Line " + lineNumber +
                                        " was skipped: A car with ID " +
                                        id + " already exists."
                        );
                    }

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Line " + lineNumber +
                                    " was skipped: " +
                                    e.getMessage()
                    );
                }
            }
        }

        return recordsLoaded;
    }

    public static void main(String[] args) {

        DatabaseManager database =
                new DatabaseManager();

        try (
                Connection connection =
                        database.connect()
        ) {

            System.out.println(
                    "SQLite connection test passed."
            );

        } catch (SQLException e) {

            System.out.println(
                    "SQLite connection test failed."
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }
}