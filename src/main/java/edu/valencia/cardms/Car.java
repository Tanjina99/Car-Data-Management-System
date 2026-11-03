package edu.valencia.cardms;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
            )
    );

    private static final Set<String> VALID_COLORS = new HashSet<>(
            Arrays.asList(
                    "Black",
                    "White",
                    "Red",
                    "Blue",
                    "Gray",
                    "Silver",
                    "Green",
                    "Yellow",
                    "Orange",
                    "Brown",
                    "Beige",
                    "Gold",
                    "Purple"
            )
    );

    public Car(
            int id,
            String make,
            String model,
            int year,
            double price,
            int mileage,
            String color) {

        this.id = validateId(id);
        this.make = validateMake(make);
        this.model = validateModel(model);
        this.year = validateYear(year);
        this.price = validatePrice(price);
        this.mileage = validateMileage(mileage);
        this.color = validateColor(color);
    }

    public int getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public int getMileage() {
        return mileage;
    }

    public String getColor() {
        return color;
    }

    public void setMake(String make) {
        this.make = validateMake(make);
    }

    public void setModel(String model) {
        this.model = validateModel(model);
    }

    public void setYear(int year) {
        this.year = validateYear(year);
    }

    public void setPrice(double price) {
        this.price = validatePrice(price);
    }

    public void setMileage(int mileage) {
        this.mileage = validateMileage(mileage);
    }

    public void setColor(String color) {
        this.color = validateColor(color);
    }

    public static void validateData(
            int id,
            String make,
            String model,
            int year,
            double price,
            int mileage,
            String color) {

        validateId(id);
        validateMake(make);
        validateModel(model);
        validateYear(year);
        validatePrice(price);
        validateMileage(mileage);
        validateColor(color);
    }

    public static int validateId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than 0."
            );
        }

        return id;
    }

    public static String validateMake(String make) {

        if (make == null || make.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Make cannot be empty."
            );
        }

        make = make.trim();

        for (String validMake : VALID_MAKES) {

            if (validMake.equalsIgnoreCase(make)) {
                return validMake;
            }
        }

        throw new IllegalArgumentException(
                "Invalid car make."
        );
    }

    public static String validateModel(String model) {

        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Model cannot be empty."
            );
        }

        return model.trim();
    }

    public static int validateYear(int year) {

        int currentYear = LocalDate.now().getYear();

        if (year < 2000) {
            throw new IllegalArgumentException(
                    "Year must be 2000 or later."
            );
        }

        if (year > currentYear) {
            throw new IllegalArgumentException(
                    "Year cannot be later than "
                            + currentYear
                            + "."
            );
        }

        return year;
    }

    public static double validatePrice(double price) {

        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException(
                    "Price must be a real number."
            );
        }

        if (price < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative."
            );
        }

        if (price > 250000) {
            throw new IllegalArgumentException(
                    "Price cannot be greater than 250000."
            );
        }

        return price;
    }

    public static int validateMileage(int mileage) {

        if (mileage < 0) {
            throw new IllegalArgumentException(
                    "Mileage cannot be negative."
            );
        }

        if (mileage > 150000) {
            throw new IllegalArgumentException(
                    "Mileage cannot be greater than 150000."
            );
        }

        return mileage;
    }

    public static String validateColor(String color) {

        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Color cannot be empty."
            );
        }

        color = color.trim();

        for (String validColor : VALID_COLORS) {

            if (validColor.equalsIgnoreCase(color)) {
                return validColor;
            }
        }

        throw new IllegalArgumentException(
                "Invalid car color."
        );
    }

    @Override
    public String toString() {

        return String.format(
                "ID: %d | %s %s | Year: %d | Price: $%.2f | Mileage: %d | Color: %s",
                id,
                make,
                model,
                year,
                price,
                mileage,
                color
        );
    }
}