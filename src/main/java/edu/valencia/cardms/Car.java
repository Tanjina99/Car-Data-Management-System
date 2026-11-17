
package edu.valencia.cardms;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a car in the Car Data Management System.
 * Stores car information such as ID, make, model, year, price,
 * mileage, and color.
 *
 * This class also validates car information before it is stored
 * or updated in the system.
 */
public class Car {

    private int id;
    private String make;
    private String model;
    private int year;
    private double price;
    private int mileage;
    private String color;

    private static final Set<String> VALID_MAKES = new HashSet<>(
            Arrays.asList(
                    "Toyota", "Honda", "Ford", "Chevrolet", "Nissan",
                    "Hyundai", "Kia", "Mazda", "Subaru", "Volkswagen",
                    "BMW", "Mercedes-Benz", "Audi", "Lexus", "Acura",
                    "Infiniti", "Volvo", "Tesla", "Jeep", "Dodge",
                    "Chrysler", "Ram", "GMC", "Cadillac", "Buick",
                    "Lincoln", "Porsche", "Ferrari", "Lamborghini", "Maserati"
            )
    );

    private static final Set<String> VALID_COLORS = new HashSet<>(
            Arrays.asList(
                    "Black", "White", "Red", "Blue", "Gray", "Silver",
                    "Green", "Yellow", "Orange", "Brown", "Beige",
                    "Gold", "Purple"
            )
    );

    /**
     * Creates a car with the provided information.
     * All values are validated before being assigned.
     *
     * @param id the unique car ID
     * @param make the car manufacturer
     * @param model the car model
     * @param year the manufacturing year
     * @param price the car price
     * @param mileage the car mileage
     * @param color the car color
     * @throws IllegalArgumentException if any car information is invalid
     */
    public Car(int id, String make, String model, int year,
               double price, int mileage, String color) {

        this.id = validateId(id);
        this.make = validateMake(make);
        this.model = validateModel(model);
        this.year = validateYear(year);
        this.price = validatePrice(price);
        this.mileage = validateMileage(mileage);
        this.color = validateColor(color);
    }

    /**
     * Gets the car ID.
     *
     * @return the car ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the car manufacturer.
     *
     * @return the car make
     */
    public String getMake() {
        return make;
    }

    /**
     * Gets the car model.
     *
     * @return the car model
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the manufacturing year.
     *
     * @return the car year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the car price.
     *
     * @return the car price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the car mileage.
     *
     * @return the car mileage
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * Gets the car color.
     *
     * @return the car color
     */
    public String getColor() {
        return color;
    }

    /**
     * Updates the car manufacturer after validation.
     *
     * @param make the new car make
     * @throws IllegalArgumentException if the make is invalid
     */
    public void setMake(String make) {
        this.make = validateMake(make);
    }

    /**
     * Updates the car model after validation.
     *
     * @param model the new car model
     * @throws IllegalArgumentException if the model is empty or null
     */
    public void setModel(String model) {
        this.model = validateModel(model);
    }

    /**
     * Updates the manufacturing year after validation.
     *
     * @param year the new manufacturing year
     * @throws IllegalArgumentException if the year is outside the allowed range
     */
    public void setYear(int year) {
        this.year = validateYear(year);
    }

    /**
     * Updates the car price after validation.
     *
     * @param price the new car price
     * @throws IllegalArgumentException if the price is invalid
     */
    public void setPrice(double price) {
        this.price = validatePrice(price);
    }

    /**
     * Updates the car mileage after validation.
     *
     * @param mileage the new car mileage
     * @throws IllegalArgumentException if the mileage is outside the allowed range
     */
    public void setMileage(int mileage) {
        this.mileage = validateMileage(mileage);
    }

    /**
     * Updates the car color after validation.
     *
     * @param color the new car color
     * @throws IllegalArgumentException if the color is invalid
     */
    public void setColor(String color) {
        this.color = validateColor(color);
    }

    /**
     * Validates all car information without creating a Car object.
     *
     * @param id the car ID
     * @param make the car manufacturer
     * @param model the car model
     * @param year the manufacturing year
     * @param price the car price
     * @param mileage the car mileage
     * @param color the car color
     * @throws IllegalArgumentException if any value is invalid
     */
    public static void validateData(int id, String make, String model,
                                    int year, double price,
                                    int mileage, String color) {

        validateId(id);
        validateMake(make);
        validateModel(model);
        validateYear(year);
        validatePrice(price);
        validateMileage(mileage);
        validateColor(color);
    }

    /**
     * Checks whether the car ID is valid.
     *
     * @param id the car ID to check
     * @return the validated ID
     * @throws IllegalArgumentException if the ID is not greater than zero
     */
    public static int validateId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0.");
        }

        return id;
    }

    /**
     * Checks whether the car manufacturer is supported.
     *
     * @param make the car manufacturer to check
     * @return the valid manufacturer name
     * @throws IllegalArgumentException if the make is empty or unsupported
     */
    public static String validateMake(String make) {

        if (make == null || make.trim().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be empty.");
        }

        make = make.trim();

        for (String validMake : VALID_MAKES) {
            if (validMake.equalsIgnoreCase(make)) {
                return validMake;
            }
        }

        throw new IllegalArgumentException("Invalid car make.");
    }

    /**
     * Checks whether the car model is valid.
     *
     * @param model the car model to check
     * @return the trimmed car model
     * @throws IllegalArgumentException if the model is empty or null
     */
    public static String validateModel(String model) {

        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be empty.");
        }

        return model.trim();
    }

    /**
     * Checks whether the manufacturing year is valid.
     *
     * @param year the manufacturing year to check
     * @return the validated year
     * @throws IllegalArgumentException if the year is before 2000
     * or later than the current year
     */
    public static int validateYear(int year) {

        int currentYear = LocalDate.now().getYear();

        if (year < 2000) {
            throw new IllegalArgumentException("Year must be 2000 or later.");
        }

        if (year > currentYear) {
            throw new IllegalArgumentException(
                    "Year cannot be later than " + currentYear + "."
            );
        }

        return year;
    }

    /**
     * Checks whether the car price is within the allowed range.
     *
     * @param price the car price to check
     * @return the validated price
     * @throws IllegalArgumentException if the price is not finite,
     * negative, or greater than 250000
     */
    public static double validatePrice(double price) {

        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException("Price must be a real number.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (price > 250000) {
            throw new IllegalArgumentException("Price cannot be greater than 250000.");
        }

        return price;
    }

    /**
     * Checks whether the car mileage is within the allowed range.
     *
     * @param mileage the mileage to check
     * @return the validated mileage
     * @throws IllegalArgumentException if mileage is negative
     * or greater than 150000
     */
    public static int validateMileage(int mileage) {

        if (mileage < 0) {
            throw new IllegalArgumentException("Mileage cannot be negative.");
        }

        if (mileage > 150000) {
            throw new IllegalArgumentException("Mileage cannot be greater than 150000.");
        }

        return mileage;
    }

    /**
     * Checks whether the car color is supported.
     *
     * @param color the car color to check
     * @return the valid color name
     * @throws IllegalArgumentException if the color is empty or unsupported
     */
    public static String validateColor(String color) {

        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be empty.");
        }

        color = color.trim();

        for (String validColor : VALID_COLORS) {
            if (validColor.equalsIgnoreCase(color)) {
                return validColor;
            }
        }

        throw new IllegalArgumentException("Invalid car color.");
    }

    /**
     * Returns the car information as a formatted string.
     *
     * @return a formatted string containing all car details
     */
    @Override
    public String toString() {

        return String.format(
                "ID: %d | %s %s | Year: %d | Price: $%.2f | Mileage: %d | Color: %s",
                id, make, model, year, price, mileage, color
        );
    }
}
