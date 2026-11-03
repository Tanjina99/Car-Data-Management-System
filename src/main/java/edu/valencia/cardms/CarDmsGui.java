package edu.valencia.cardms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CarDmsGui extends JFrame {

    private final CarManager manager =
            new CarManager();
    // Form input components
    private JTextField idField;
    private JComboBox<String> makeBox;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField priceField;
    private JTextField mileageField;
    //First entry is the "Select Color" placeholder, which is rejected on submit
    private JComboBox<String> colorBox;

    private JTable carTable;
    //Backing model for carTable; cells are read-only.
    private DefaultTableModel tableModel;

    //creates the main window, builds the interface, and loads the initial table data.
    public CarDmsGui() {

        setTitle("Car Data Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
        refreshTable();
    }

    private void createInterface() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel = new JLabel(
                "Car Data Management System",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel formPanel =
                new JPanel(
                        new GridLayout(7, 2, 10, 10)
                );

        formPanel.setBorder(
                new TitledBorder("Car Information")
        );

        idField = new JTextField();
        // Make list is fixed; the first entry is selected by default.
        String[] makes = {
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

        makeBox =
                new JComboBox<>(makes);

        modelField = new JTextField();
        yearField = new JTextField();
        priceField = new JTextField();
        mileageField = new JTextField();
        // Index 0 is a placeholder and must not be accepted as a real color.
        String[] colors = {
                "Select Color",
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
        };

        colorBox =
                new JComboBox<>(colors);

        formPanel.add(
                new JLabel("ID:")
        );

        formPanel.add(
                idField
        );

        formPanel.add(
                new JLabel("Make:")
        );

        formPanel.add(
                makeBox
        );

        formPanel.add(
                new JLabel("Model:")
        );

        formPanel.add(
                modelField
        );

        formPanel.add(
                new JLabel("Year:")
        );

        formPanel.add(
                yearField
        );

        formPanel.add(
                new JLabel("Price:")
        );

        formPanel.add(
                priceField
        );

        formPanel.add(
                new JLabel("Mileage:")
        );

        formPanel.add(
                mileageField
        );

        formPanel.add(
                new JLabel("Color:")
        );

        formPanel.add(
                colorBox
        );

        JButton addButton =
                new JButton("Add Car");

        JButton updateButton =
                new JButton("Update Car");

        JButton deleteButton =
                new JButton("Delete Car");

        JButton clearButton =
                new JButton("Clear");

        JButton importButton =
                new JButton("Import Cars");

        JButton averageButton =
                new JButton("Average Price");
        JButton exitButton =
                new JButton("Exit");

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                8
                        )
                );

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        JPanel extraButtonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                8
                        )
                );

        extraButtonPanel.add(
                importButton
        );

        extraButtonPanel.add(
                averageButton
        );
        extraButtonPanel.add(
                exitButton
        );

        JPanel leftPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        leftPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        JPanel allButtonsPanel =
                new JPanel(
                        new BorderLayout()
                );

        allButtonsPanel.add(
                buttonPanel,
                BorderLayout.NORTH
        );

        allButtonsPanel.add(
                extraButtonPanel,
                BorderLayout.SOUTH
        );

        leftPanel.add(
                allButtonsPanel,
                BorderLayout.SOUTH
        );
        // Table cells are not editable; edits happen through the form.
        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Make",
                                "Model",
                                "Year",
                                "Price",
                                "Mileage",
                                "Color"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        carTable =
                new JTable(tableModel);

        carTable.setRowHeight(25);

        carTable.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        JScrollPane tableScrollPane =
                new JScrollPane(carTable);

        tableScrollPane.setBorder(
                new TitledBorder("Cars")
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(15, 15)
                );

        centerPanel.add(
                leftPanel,
                BorderLayout.WEST
        );

        centerPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        addButton.addActionListener(
                e -> addCar()
        );

        updateButton.addActionListener(
                e -> updateCar()
        );

        deleteButton.addActionListener(
                e -> deleteCar()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        importButton.addActionListener(
                e -> importCars()
        );

        averageButton.addActionListener(
                e -> showAveragePrice()
        );
        exitButton.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        //Populate the form when a row is selected. getValueIsAdjusting()
        // filters out intermediate events so the form loads only once.
        carTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedCar();
                    }
                });
    }

    private void addCar() {

        try {

            int id =
                    readPositiveInt(
                            idField.getText(),
                            "ID"
                    );

            if (manager.findCarById(id) != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "A car with that ID already exists.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String make =
                    (String) makeBox.getSelectedItem();

            String model =
                    readText(
                            modelField.getText(),
                            "Model"
                    );

            int year =
                    readYear(
                            yearField.getText()
                    );

            double price =
                    readPrice(
                            priceField.getText()
                    );

            int mileage =
                    readMileage(
                            mileageField.getText()
                    );

            String color =
                    getSelectedColor();

            Car car =
                    new Car(
                            id,
                            make,
                            model,
                            year,
                            price,
                            mileage,
                            color
                    );

            if (manager.addCar(car)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Car added successfully.",
                        "Add Car",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshTable();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Car could not be added.",
                        "Add Car",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void updateCar() {

        try {

            int id =
                    readPositiveInt(
                            idField.getText(),
                            "ID"
                    );

            if (manager.findCarById(id) == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Car not found.",
                        "Update Car",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String make =
                    (String) makeBox.getSelectedItem();

            String model =
                    readText(
                            modelField.getText(),
                            "Model"
                    );

            int year =
                    readYear(
                            yearField.getText()
                    );

            double price =
                    readPrice(
                            priceField.getText()
                    );

            int mileage =
                    readMileage(
                            mileageField.getText()
                    );

            String color =
                    getSelectedColor();

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

                JOptionPane.showMessageDialog(
                        this,
                        "Car updated successfully.",
                        "Update Car",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshTable();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Car could not be updated.",
                        "Update Car",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void deleteCar() {

        try {

            int id =
                    readPositiveInt(
                            idField.getText(),
                            "ID"
                    );

            Car car =
                    manager.findCarById(id);

            if (car == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Car not found.",
                        "Delete Car",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int answer =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete car ID "
                                    + id
                                    + "?",
                            "Delete Car",
                            JOptionPane.YES_NO_OPTION
                    );

            if (answer ==
                    JOptionPane.YES_OPTION) {

                manager.deleteCar(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Car deleted successfully.",
                        "Delete Car",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshTable();
                clearFields();
            }

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void importCars() {

        JFileChooser fileChooser =
                new JFileChooser();

        int result =
                fileChooser.showOpenDialog(this);

        if (result !=
                JFileChooser.APPROVE_OPTION) {

            return;
        }

        try {

            int count =
                    manager.loadFromFile(
                            fileChooser
                                    .getSelectedFile()
                                    .getAbsolutePath()
                    );

            refreshTable();

            JOptionPane.showMessageDialog(
                    this,
                    count
                            + " car(s) imported successfully.",
                    "Import Cars",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not open the file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showAveragePrice() {

        List<Car> cars =
                manager.getAllCars();

        if (cars.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "There are no cars to calculate.",
                    "Average Price",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        double average =
                manager.calculateAveragePrice();

        JOptionPane.showMessageDialog(
                this,
                String.format(
                        "Average car price: $%.2f",
                        average
                ),
                "Average Price",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void loadSelectedCar() {

        int selectedRow =
                carTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        idField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString()
        );

        makeBox.setSelectedItem(
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString()
        );

        modelField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                2
                        )
                        .toString()
        );

        yearField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                3
                        )
                        .toString()
        );

        priceField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                4
                        )
                        .toString()
        );

        mileageField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                5
                        )
                        .toString()
        );

        colorBox.setSelectedItem(
                tableModel
                        .getValueAt(
                                selectedRow,
                                6
                        )
                        .toString()
        );
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (Car car :
                manager.getAllCars()) {

            tableModel.addRow(
                    new Object[]{
                            car.getId(),
                            car.getMake(),
                            car.getModel(),
                            car.getYear(),
                            String.format(
                                    "%.2f",
                                    car.getPrice()
                            ),
                            car.getMileage(),
                            car.getColor()
                    }
            );
        }
    }

    private void clearFields() {

        idField.setText("");

        makeBox.setSelectedIndex(0);

        modelField.setText("");

        yearField.setText("");

        priceField.setText("");

        mileageField.setText("");

        colorBox.setSelectedIndex(0);

        carTable.clearSelection();
    }

    private String getSelectedColor() {

        String color =
                (String) colorBox.getSelectedItem();

        if (color == null ||
                color.equals("Select Color")) {

            throw new IllegalArgumentException(
                    "Color cannot be empty."
            );
        }

        return color;
    }

    private int readPositiveInt(
            String input,
            String fieldName) {

        if (input == null ||
                input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot be empty."
            );
        }

        input = input.trim();

        if (input.startsWith("+")) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot have + sign."
            );
        }

        if (input.startsWith("-")) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot be negative."
            );
        }

        if (!input.matches("[0-9]+")) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must contain numbers only."
            );
        }

        try {

            int value =
                    Integer.parseInt(input);

            if (value <= 0) {

                throw new IllegalArgumentException(
                        fieldName
                                + " must be greater than 0."
                );
            }

            return value;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    fieldName
                            + " is too large."
            );
        }
    }

    private int readYear(String input) {

        if (input == null ||
                input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Year cannot be empty."
            );
        }

        input = input.trim();

        if (!input.matches("[0-9]{4}")) {

            throw new IllegalArgumentException(
                    "Year must contain exactly 4 digits."
            );
        }

        int year;

        try {

            year =
                    Integer.parseInt(input);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid year."
            );
        }

        int currentYear =
                LocalDate.now().getYear();

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

    private double readPrice(String input) {

        if (input == null ||
                input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Price cannot be empty."
            );
        }

        input = input.trim();

        if (input.startsWith("+")) {

            throw new IllegalArgumentException(
                    "Price cannot have + sign."
            );
        }

        if (input.startsWith("-")) {

            throw new IllegalArgumentException(
                    "Price cannot be negative."
            );
        }

        if (!input.matches(
                "[0-9]+(\\.[0-9]+)?")) {

            throw new IllegalArgumentException(
                    "Price must be a number."
            );
        }

        try {

            double price =
                    Double.parseDouble(input);

            if (!Double.isFinite(price)) {

                throw new IllegalArgumentException(
                        "Price must be a real number."
                );
            }

            return price;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Price is too large."
            );
        }
    }

    private int readMileage(String input) {

        if (input == null ||
                input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Mileage cannot be empty."
            );
        }

        input = input.trim();

        if (input.startsWith("+")) {

            throw new IllegalArgumentException(
                    "Mileage cannot have + sign."
            );
        }

        if (input.startsWith("-")) {

            throw new IllegalArgumentException(
                    "Mileage cannot be negative."
            );
        }

        if (!input.matches("[0-9]+")) {

            throw new IllegalArgumentException(
                    "Mileage must contain numbers only."
            );
        }

        try {

            return Integer.parseInt(input);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Mileage is too large."
            );
        }
    }

    private String readText(
            String input,
            String fieldName) {

        if (input == null ||
                input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot be empty."
            );
        }

        return input.trim();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CarDmsGui gui =
                    new CarDmsGui();

            gui.setVisible(true);
        });
    }
}