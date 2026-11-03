package edu.valencia.cardms;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarManagerTest {

    @Test
    void testAddCar() {

        CarManager manager = new CarManager();

        Car car = new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        );

        boolean result = manager.addCar(car);

        assertTrue(result);
        assertEquals(1, manager.getAllCars().size());
        assertEquals("Toyota", manager.findCarById(1).getMake());
    }

    @Test
    void testAddNullCar() {

        CarManager manager = new CarManager();

        assertFalse(manager.addCar(null));
        assertEquals(0, manager.getAllCars().size());
    }

    @Test
    void testDuplicateIdIsRejected() {

        CarManager manager = new CarManager();

        Car firstCar = new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        );

        Car secondCar = new Car(
                1,
                "Honda",
                "Civic",
                2021,
                22000,
                28000,
                "Blue"
        );

        assertTrue(manager.addCar(firstCar));
        assertFalse(manager.addCar(secondCar));

        assertEquals(1, manager.getAllCars().size());
    }

    @Test
    void testFindCarById() {

        CarManager manager = new CarManager();

        Car car = new Car(
                1,
                "Honda",
                "Civic",
                2021,
                22000,
                28000,
                "Blue"
        );

        manager.addCar(car);

        Car foundCar = manager.findCarById(1);

        assertNotNull(foundCar);
        assertEquals("Honda", foundCar.getMake());
        assertEquals("Civic", foundCar.getModel());
    }

    @Test
    void testFindCarThatDoesNotExist() {

        CarManager manager = new CarManager();

        assertNull(manager.findCarById(999));
    }

    @Test
    void testGetAllCars() {

        CarManager manager = new CarManager();

        manager.addCar(new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        ));

        manager.addCar(new Car(
                2,
                "Honda",
                "Civic",
                2021,
                22000,
                28000,
                "Blue"
        ));

        List<Car> cars = manager.getAllCars();

        assertEquals(2, cars.size());
    }

    @Test
    void testUpdateCar() {

        CarManager manager = new CarManager();

        manager.addCar(new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        ));

        boolean result = manager.updateCar(
                1,
                "Honda",
                "Civic",
                2021,
                22000,
                28000,
                "Blue"
        );

        assertTrue(result);

        Car car = manager.findCarById(1);

        assertEquals("Honda", car.getMake());
        assertEquals("Civic", car.getModel());
        assertEquals(2021, car.getYear());
        assertEquals(22000, car.getPrice());
        assertEquals(28000, car.getMileage());
        assertEquals("Blue", car.getColor());
    }

    @Test
    void testUpdateCarThatDoesNotExist() {

        CarManager manager = new CarManager();

        boolean result = manager.updateCar(
                999,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        );

        assertFalse(result);
    }

    @Test
    void testInvalidUpdateDoesNotChangeCar() {

        CarManager manager = new CarManager();

        manager.addCar(new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        ));

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.updateCar(
                        1,
                        "Honda",
                        "Civic",
                        2022,
                        -5000,
                        30000,
                        "Blue"
                )
        );

        Car car = manager.findCarById(1);

        assertEquals("Toyota", car.getMake());
        assertEquals("Camry", car.getModel());
        assertEquals(2022, car.getYear());
        assertEquals(25000, car.getPrice());
        assertEquals(35000, car.getMileage());
        assertEquals("Red", car.getColor());
    }

    @Test
    void testDeleteCar() {

        CarManager manager = new CarManager();

        manager.addCar(new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        ));

        assertTrue(manager.deleteCar(1));
        assertNull(manager.findCarById(1));
        assertEquals(0, manager.getAllCars().size());
    }

    @Test
    void testDeleteCarThatDoesNotExist() {

        CarManager manager = new CarManager();

        assertFalse(manager.deleteCar(999));
    }

    @Test
    void testAveragePrice() {

        CarManager manager = new CarManager();

        manager.addCar(new Car(
                1,
                "Toyota",
                "Camry",
                2022,
                25000,
                35000,
                "Red"
        ));

        manager.addCar(new Car(
                2,
                "Honda",
                "Civic",
                2021,
                22000,
                28000,
                "Blue"
        ));

        double average = manager.calculateAveragePrice();

        assertEquals(23500.0, average, 0.001);
    }

    @Test
    void testAveragePriceWithNoCars() {

        CarManager manager = new CarManager();

        assertEquals(0.0, manager.calculateAveragePrice());
    }

    @Test
    void testInvalidMakeIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Rose",
                        "Camry",
                        2022,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testEmptyMakeIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "",
                        "Camry",
                        2022,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testEmptyModelIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "",
                        2022,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testInvalidColorIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2022,
                        25000,
                        35000,
                        "34"
                )
        );
    }

    @Test
    void testEmptyColorIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2022,
                        25000,
                        35000,
                        ""
                )
        );
    }

    @Test
    void testYearWithTooManyDigitsIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        202020,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testYearBefore1886IsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        1777,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testFutureYearIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2099,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testNegativePriceIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2022,
                        -5000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testNaNPriceIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2022,
                        Double.NaN,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testInfinitePriceIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2022,
                        Double.POSITIVE_INFINITY,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testNegativeMileageIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        1,
                        "Toyota",
                        "Camry",
                        2022,
                        25000,
                        -35000,
                        "Red"
                )
        );
    }

    @Test
    void testNegativeIdIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Car(
                        -1,
                        "Toyota",
                        "Camry",
                        2022,
                        25000,
                        35000,
                        "Red"
                )
        );
    }

    @Test
    void testFileImport() throws IOException {

        CarManager manager = new CarManager();

        Path file = Files.createTempFile("cars", ".txt");

        try {

            Files.writeString(
                    file,
                    "1,Toyota,Camry,2022,25000,35000,Red\n"
                            + "2,Honda,Civic,2021,22000,28000,Blue\n"
                            + "3,Ford,Mustang,2020,30000,20000,Black\n"
            );

            int imported = manager.loadFromFile(file.toString());

            assertEquals(3, imported);
            assertEquals(3, manager.getAllCars().size());

        } finally {

            Files.deleteIfExists(file);
        }
    }

    @Test
    void testInvalidFileDataIsSkipped() throws IOException {

        CarManager manager = new CarManager();

        Path file = Files.createTempFile("bad-cars", ".txt");

        try {

            Files.writeString(
                    file,
                    "1,Toyota,Camry,2022,25000,35000,Red\n"
                            + "2,Rose,Civic,2021,22000,28000,Blue\n"
                            + "3,Toyota,Camry,202020,25000,35000,Black\n"
                            + "4,Honda,Civic,2021,-5000,20000,White\n"
                            + "5,Toyota,Camry,2022,25000,35000,34\n"
                            + "6,Ford,Mustang,2020,+30000,20000,Black\n"
                            + "7,Ford,Mustang,2020,30000,+20000,Black\n"
            );

            int imported = manager.loadFromFile(file.toString());

            assertEquals(1, imported);
            assertEquals(1, manager.getAllCars().size());

        } finally {

            Files.deleteIfExists(file);
        }
    }

    @Test
    void testDuplicateIdInFileIsSkipped() throws IOException {

        CarManager manager = new CarManager();

        Path file = Files.createTempFile("duplicate-cars", ".txt");

        try {

            Files.writeString(
                    file,
                    "1,Toyota,Camry,2022,25000,35000,Red\n"
                            + "1,Honda,Civic,2021,22000,28000,Blue\n"
            );

            int imported = manager.loadFromFile(file.toString());

            assertEquals(1, imported);
            assertEquals(1, manager.getAllCars().size());

        } finally {

            Files.deleteIfExists(file);
        }
    }

    @Test
    void testMissingFileThrowsException() {

        CarManager manager = new CarManager();

        assertThrows(
                IOException.class,
                () -> manager.loadFromFile(
                        "file-that-does-not-exist.txt"
                )
        );
    }
}