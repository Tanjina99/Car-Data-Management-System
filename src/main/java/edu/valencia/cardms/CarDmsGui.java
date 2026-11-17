
package edu.valencia.cardms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides the graphical user interface for the Car Data Management System.
 * <p>
 * This class allows users to add, display, update, delete, and import car
 * records. It also connects the interface to the database through
 * CarManager and displays car information in a table.
 *
 * @author Tanjina Sultana
 * @version 1.0
 */

public class CarDmsGui extends JFrame {

    // Application color theme
    private static final Color APP_BACKGROUND =
            new Color(197, 217, 236);

    private static final Color PANEL_BACKGROUND =
            new Color(220, 234, 246);

    private static final Color HEADER_COLOR =
            new Color(20, 45, 73);

    private static final Color GLOW_COLOR =
            new Color(0, 200, 255);

    private static final Color TEXT_COLOR =
            new Color(25, 45, 65);

    private static final Color HOVER_COLOR =
            new Color(188, 212, 230);

    // Database manager
    private final CarManager manager = new CarManager();

    // Form fields
    private JTextField idField;
    private JComboBox<String> makeBox;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField priceField;
    private JTextField mileageField;
    private JComboBox<String> colorBox;

    // Table
    private JTable carTable;
    private DefaultTableModel tableModel;

    // Tracks the hovered table row
    private int hoveredRow = -1;

    // Remembers the original ID of a selected car
    private Integer selectedCarId = null;

    /**
     * Creates a custom button with a blue gradient background and glowing border.
     * The button changes its appearance when the mouse hovers over it or when
     * it is pressed.
     */
    // Custom blue glowing buttons
    private static class OrbButton extends JButton {

        private final Color baseColor;


        /**
         * Creates a custom button with the specified text, color, and width.
         *
         * @param text the text displayed on the button
         * @param color the base color used for the button
         * @param width the preferred width of the button
         */
        public OrbButton(String text, Color color, int width) {

            super(text);

            baseColor = color;

            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 13));
            setPreferredSize(new Dimension(width, 38));

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);

            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }


        /**
         * Draws the button's gradient background, glowing border, and text.
         *
         * @param g the graphics context used to draw the button
         */
        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            boolean hover = getModel().isRollover();
            boolean pressed = getModel().isPressed();

            // Glowing border
            for (int i = 8; i >= 2; i -= 3) {

                g2.setColor(new Color(
                        0, 190, 255, hover ? 45 : 20
                ));

                g2.setStroke(new BasicStroke(i));

                g2.drawRoundRect(
                        5, 5, width - 11, height - 11, 25, 25
                );
            }

            Color top = baseColor.brighter();
            Color bottom = baseColor.darker();

            if (hover) {
                top = top.brighter();
                bottom = baseColor;
            }

            if (pressed) {
                top = baseColor.darker();
                bottom = baseColor.darker().darker();
            }

            // Gradient background
            g2.setPaint(new GradientPaint(
                    0, 0, top,
                    0, height, bottom
            ));

            g2.fillRoundRect(
                    5, 5, width - 11, height - 11, 25, 25
            );

            // Cyan outline
            g2.setColor(GLOW_COLOR);
            g2.setStroke(new BasicStroke(1.5f));

            g2.drawRoundRect(
                    5, 5, width - 11, height - 11, 25, 25
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }


    // Constructor
    /**
     * Creates the Car Data Management System window.
     * Initializes the database connection, GUI components, menu bar,
     * and car records table.
     */
    public CarDmsGui() {

        setTitle("Car Data Management System");
        setSize(1000, 650);

        // Ask before closing the application
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        setLocationRelativeTo(null);

        // Connect to the selected database
        connectToDatabase();

        // Create the GUI
        createInterface();

        // Create the menu
        createMenuBar();

        // Display database records
        refreshTable();
    }


    // Let the user select the SQLite database
    private void connectToDatabase() {

        while (true) {

            JFileChooser chooser = new JFileChooser();

            chooser.setDialogTitle("Select SQLite Database");
            chooser.setFileSelectionMode(
                    JFileChooser.FILES_ONLY
            );

            int result = chooser.showOpenDialog(this);

            // Handle cancellation
            if (result != JFileChooser.APPROVE_OPTION) {

                int answer = JOptionPane.showConfirmDialog(
                        this,
                        "No database was selected. Do you want to exit?",
                        "Database Required",
                        JOptionPane.YES_NO_OPTION
                );

                if (answer == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }

                continue;
            }

            File selectedFile = chooser.getSelectedFile();

            // Check the selected file
            if (selectedFile == null ||
                    !selectedFile.isFile() ||
                    !selectedFile.canRead()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a valid, readable database file.",
                        "Invalid Database",
                        JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            try {

                // Connect to the selected database
                DatabaseManager database =
                        new DatabaseManager(
                                selectedFile.getAbsolutePath()
                        );

                // Give the database to CarManager
                manager.setDatabaseManager(database);

                JOptionPane.showMessageDialog(
                        this,
                        "Database connected successfully!",
                        "Database Connection",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;

            } catch (SQLException | IllegalArgumentException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Could not connect to the database.\n"
                                + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    // Create the application menu
    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu carMenu = new JMenu("Car Operations");

        JMenuItem addItem = new JMenuItem("Add Car");
        JMenuItem displayItem = new JMenuItem("Display Cars");
        JMenuItem updateItem = new JMenuItem("Update Car");
        JMenuItem deleteItem = new JMenuItem("Delete Car");
        JMenuItem importItem = new JMenuItem("Import Cars");
        JMenuItem averageItem = new JMenuItem("Average Price");
        JMenuItem clearItem = new JMenuItem("Clear Form");
        JMenuItem exitItem = new JMenuItem("Exit");

        addItem.addActionListener(e -> addCar());
        displayItem.addActionListener(e -> displayCars());
        updateItem.addActionListener(e -> updateCar());
        deleteItem.addActionListener(e -> deleteCar());
        importItem.addActionListener(e -> importCars());
        averageItem.addActionListener(e -> showAveragePrice());
        clearItem.addActionListener(e -> clearFields());
        exitItem.addActionListener(e -> exitApplication());

        carMenu.add(addItem);
        carMenu.add(displayItem);
        carMenu.add(updateItem);
        carMenu.add(deleteItem);
        carMenu.addSeparator();
        carMenu.add(importItem);
        carMenu.add(averageItem);
        carMenu.add(clearItem);

        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(carMenu);

        setJMenuBar(menuBar);
    }


    // Create the existing blue GUI
    private void createInterface() {

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        mainPanel.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        mainPanel.setBackground(APP_BACKGROUND);
        getContentPane().setBackground(APP_BACKGROUND);

        // Header
        JLabel titleLabel = new JLabel(
                "Car Data Management System",
                SwingConstants.CENTER
        );

        titleLabel.setOpaque(true);
        titleLabel.setBackground(HEADER_COLOR);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(
                new EmptyBorder(12, 8, 12, 8)
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Car information form
        JPanel formPanel = new JPanel(
                new GridLayout(7, 2, 10, 10)
        );

        formPanel.setBorder(
                new TitledBorder("Car Information")
        );

        formPanel.setBackground(PANEL_BACKGROUND);

        idField = new JTextField();

        String[] makes = {
                "Toyota", "Honda", "Ford", "Chevrolet", "Nissan",
                "Hyundai", "Kia", "Mazda", "Subaru", "Volkswagen",
                "BMW", "Mercedes-Benz", "Audi", "Lexus", "Acura",
                "Infiniti", "Volvo", "Tesla", "Jeep", "Dodge",
                "Chrysler", "Ram", "GMC", "Cadillac", "Buick",
                "Lincoln", "Porsche", "Ferrari", "Lamborghini", "Maserati"
        };

        makeBox = new JComboBox<>(makes);

        modelField = new JTextField();
        yearField = new JTextField();
        priceField = new JTextField();
        mileageField = new JTextField();

        String[] colors = {
                "Select Color", "Black", "White", "Red", "Blue",
                "Gray", "Silver", "Green", "Yellow", "Orange",
                "Brown", "Beige", "Gold", "Purple"
        };

        colorBox = new JComboBox<>(colors);

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Make:"));
        formPanel.add(makeBox);

        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);

        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Mileage:"));
        formPanel.add(mileageField);

        formPanel.add(new JLabel("Color:"));
        formPanel.add(colorBox);

        for (Component component : formPanel.getComponents()) {

            if (component instanceof JLabel) {
                component.setForeground(TEXT_COLOR);
            }
        }


        // Buttons
        JButton addButton =
                new OrbButton("Add Car", HEADER_COLOR, 120);

        JButton displayButton =
                new OrbButton("Display Cars", HEADER_COLOR, 125);

        JButton updateButton =
                new OrbButton("Update Car", HEADER_COLOR, 130);

        JButton deleteButton =
                new OrbButton("Delete Car", HEADER_COLOR, 120);

        JButton clearButton =
                new OrbButton("Clear", HEADER_COLOR, 100);

        JButton importButton =
                new OrbButton("Import Cars", HEADER_COLOR, 125);

        JButton averageButton =
                new OrbButton("Average Price", HEADER_COLOR, 135);

        JButton exitButton =
                new OrbButton("Exit", HEADER_COLOR, 90);


        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 8)
        );

        buttonPanel.setBackground(APP_BACKGROUND);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);


        JPanel extraButtonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 8)
        );

        extraButtonPanel.setBackground(APP_BACKGROUND);

        extraButtonPanel.add(displayButton);
        extraButtonPanel.add(importButton);
        extraButtonPanel.add(averageButton);
        extraButtonPanel.add(exitButton);


        JPanel allButtonsPanel = new JPanel(
                new BorderLayout()
        );

        allButtonsPanel.setBackground(APP_BACKGROUND);
        allButtonsPanel.add(buttonPanel, BorderLayout.NORTH);
        allButtonsPanel.add(extraButtonPanel, BorderLayout.SOUTH);


        JPanel leftPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        leftPanel.setBackground(APP_BACKGROUND);
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(allButtonsPanel, BorderLayout.SOUTH);


        // Table model
        tableModel = new DefaultTableModel(
                new String[]{
                        "ID", "Make", "Model", "Year",
                        "Price", "Mileage", "Color"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        carTable = new JTable(tableModel);

        carTable.setRowHeight(25);
        carTable.setBackground(Color.WHITE);
        carTable.setForeground(TEXT_COLOR);
        carTable.setSelectionBackground(HOVER_COLOR);
        carTable.setSelectionForeground(Color.BLACK);
        carTable.setGridColor(PANEL_BACKGROUND);
        carTable.setFillsViewportHeight(true);

        carTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        carTable.getTableHeader().setBackground(HEADER_COLOR);
        carTable.getTableHeader().setForeground(Color.WHITE);
        carTable.getTableHeader().setOpaque(true);

        carTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );


        // Light blue hover renderer
        DefaultTableCellRenderer hoverRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column) {

                        Component component =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        if (isSelected) {

                            component.setBackground(HOVER_COLOR);
                            component.setForeground(Color.BLACK);

                        } else if (row == hoveredRow) {

                            component.setBackground(HOVER_COLOR);
                            component.setForeground(TEXT_COLOR);

                        } else {

                            component.setBackground(Color.WHITE);
                            component.setForeground(TEXT_COLOR);
                        }

                        return component;
                    }
                };


        // Apply renderer to all columns
        for (int i = 0; i < carTable.getColumnCount(); i++) {

            carTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(hoverRenderer);
        }


        // Mouse hover listener
        carTable.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                int row = carTable.rowAtPoint(e.getPoint());

                if (row != hoveredRow) {

                    hoveredRow = row;
                    carTable.repaint();
                }
            }
        });


        // Remove hover when the mouse leaves the table
        carTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {

                hoveredRow = -1;
                carTable.repaint();
            }
        });


        JScrollPane tableScrollPane = new JScrollPane(carTable);

        tableScrollPane.setBorder(
                new TitledBorder("Cars")
        );


        // Center layout
        JPanel centerPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        centerPanel.setBackground(APP_BACKGROUND);
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);


        // Button actions
        addButton.addActionListener(e -> addCar());
        displayButton.addActionListener(e -> displayCars());
        updateButton.addActionListener(e -> updateCar());
        deleteButton.addActionListener(e -> deleteCar());
        clearButton.addActionListener(e -> clearFields());
        importButton.addActionListener(e -> importCars());
        averageButton.addActionListener(e -> showAveragePrice());
        exitButton.addActionListener(e -> exitApplication());


        // Load selected car into the form
        carTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                loadSelectedCar();
            }
        });
    }

    
    // Display all cars and show the total number of records.
    private void displayCars() {

        List<Car> cars = manager.getAllCars();

        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Total cars in database: " + cars.size(),
                "Database Records",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // ADD CAR
    private void addCar() {

        try {

            int id = readPositiveInt(idField.getText(), "ID");

            if (manager.findCarById(id) != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "A car with that ID already exists.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String make = (String) makeBox.getSelectedItem();

            String model = readText(
                    modelField.getText(), "Model"
            );

            int year = readYear(yearField.getText());

            double price = readPrice(priceField.getText());

            int mileage = readMileage(mileageField.getText());

            String color = getSelectedColor();

            Car car = new Car(
                    id, make, model, year, price, mileage, color
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
                        "Car could not be added. Check the database.",
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


    // UPDATE CAR
    private void updateCar() {

        try {

            // Get the original ID of the selected car.
            int oldId;

            if (selectedCarId != null) {
                oldId = selectedCarId;
            } else {
                oldId = readPositiveInt(
                        idField.getText(), "ID"
                );
            }

            // Get the new ID from the form.
            int newId = readPositiveInt(
                    idField.getText(), "ID"
            );

            if (manager.findCarById(oldId) == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Car not found.",
                        "Update Car",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Check whether another car already has the new ID.
            if (newId != oldId &&
                    manager.findCarById(newId) != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "That ID already belongs to another car.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String make = (String) makeBox.getSelectedItem();

            String model = readText(
                    modelField.getText(), "Model"
            );

            int year = readYear(yearField.getText());

            double price = readPrice(priceField.getText());

            int mileage = readMileage(mileageField.getText());

            String color = getSelectedColor();

            boolean updated = manager.updateCar(
                    oldId,
                    newId,
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


    // DELETE CAR
    private void deleteCar() {

        try {

            int id = readPositiveInt(
                    idField.getText(), "ID"
            );

            Car car = manager.findCarById(id);

            if (car == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Car not found.",
                        "Delete Car",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete car ID " + id + "?",
                    "Delete Car",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {

                if (manager.deleteCar(id)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Car deleted successfully.",
                            "Delete Car",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    refreshTable();
                    clearFields();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Car could not be deleted.",
                            "Delete Car",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
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


    // IMPORT CARS
// Import cars from a text file into SQLite.
    private void importCars() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Select Car Data File");

        int result = fileChooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();

        // Check that the file exists and can be read.
        // Check the selected file.
        if (selectedFile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No file was selected.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String details =
                "Selected path: " + selectedFile.getAbsolutePath() +
                        "\nFile exists: " + selectedFile.exists() +
                        "\nIs a file: " + selectedFile.isFile() +
                        "\nCan read: " + selectedFile.canRead();

        if (!selectedFile.exists() ||
                !selectedFile.isFile() ||
                !selectedFile.canRead()) {

            JOptionPane.showMessageDialog(
                    this,
                    details,
                    "File Information",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            int count = manager.loadFromFile(
                    selectedFile.getAbsolutePath()
            );

            refreshTable();

            JOptionPane.showMessageDialog(
                    this,
                    count + " car(s) imported successfully.",
                    "Import Cars",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Import failed:\n" + e.getMessage()
                            + "\n\nFile: "
                            + selectedFile.getAbsolutePath(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CUSTOM FEATURE: AVERAGE PRICE
    private void showAveragePrice() {

        List<Car> cars = manager.getAllCars();

        if (cars.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "There are no cars to calculate.",
                    "Average Price",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        double average = manager.calculateAveragePrice();

        JOptionPane.showMessageDialog(
                this,
                String.format("Average car price: $%.2f", average),
                "Average Price",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // LOAD SELECTED CAR
    private void loadSelectedCar() {

        int selectedRow = carTable.getSelectedRow();

        if (selectedRow == -1) {

            selectedCarId = null;
            return;
        }

        // Convert the view row to the model row.
        int modelRow =
                carTable.convertRowIndexToModel(selectedRow);

        // Remember the original ID for updates.
        selectedCarId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        idField.setText(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        makeBox.setSelectedItem(
                tableModel.getValueAt(modelRow, 1).toString()
        );

        modelField.setText(
                tableModel.getValueAt(modelRow, 2).toString()
        );

        yearField.setText(
                tableModel.getValueAt(modelRow, 3).toString()
        );

        priceField.setText(
                tableModel.getValueAt(modelRow, 4).toString()
        );

        mileageField.setText(
                tableModel.getValueAt(modelRow, 5).toString()
        );

        colorBox.setSelectedItem(
                tableModel.getValueAt(modelRow, 6).toString()
        );
    }


    // REFRESH TABLE
    private void refreshTable() {

        tableModel.setRowCount(0);

        hoveredRow = -1;

        for (Car car : manager.getAllCars()) {

            tableModel.addRow(new Object[]{
                    car.getId(),
                    car.getMake(),
                    car.getModel(),
                    car.getYear(),
                    String.format("%.2f", car.getPrice()),
                    car.getMileage(),
                    car.getColor()
            });
        }

        carTable.clearSelection();
        carTable.repaint();
    }


    // CLEAR FORM
    private void clearFields() {

        selectedCarId = null;

        idField.setText("");

        makeBox.setSelectedIndex(0);

        modelField.setText("");

        yearField.setText("");

        priceField.setText("");

        mileageField.setText("");

        colorBox.setSelectedIndex(0);

        carTable.clearSelection();
    }


    // Confirm before closing the application
    private void exitApplication() {

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Application",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }


    // VALIDATE COLOR
    private String getSelectedColor() {

        String color = (String) colorBox.getSelectedItem();

        if (color == null || color.equals("Select Color")) {

            throw new IllegalArgumentException(
                    "Color cannot be empty."
            );
        }

        return color;
    }


    // VALIDATE POSITIVE INTEGER
    private int readPositiveInt(String input, String fieldName) {

        if (input == null || input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        input = input.trim();

        if (input.startsWith("+")) {

            throw new IllegalArgumentException(
                    fieldName + " cannot have + sign."
            );
        }

        if (input.startsWith("-")) {

            throw new IllegalArgumentException(
                    fieldName + " cannot be negative."
            );
        }

        if (!input.matches("[0-9]+")) {

            throw new IllegalArgumentException(
                    fieldName + " must contain numbers only."
            );
        }

        try {

            int value = Integer.parseInt(input);

            if (value <= 0) {

                throw new IllegalArgumentException(
                        fieldName + " must be greater than 0."
                );
            }

            return value;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    fieldName + " is too large."
            );
        }
    }


    // VALIDATE YEAR
    private int readYear(String input) {

        if (input == null || input.trim().isEmpty()) {

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

            year = Integer.parseInt(input);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid year."
            );
        }

        int currentYear = LocalDate.now().getYear();

        if (year < 2000) {

            throw new IllegalArgumentException(
                    "Year must be 2000 or later."
            );
        }

        if (year > currentYear) {

            throw new IllegalArgumentException(
                    "Year cannot be later than " + currentYear + "."
            );
        }

        return year;
    }


    // VALIDATE PRICE
    private double readPrice(String input) {

        if (input == null || input.trim().isEmpty()) {

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

        if (!input.matches("[0-9]+(\\.[0-9]+)?")) {

            throw new IllegalArgumentException(
                    "Price must be a number."
            );
        }

        try {

            double price = Double.parseDouble(input);

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


    // VALIDATE MILEAGE
    private int readMileage(String input) {

        if (input == null || input.trim().isEmpty()) {

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


    // VALIDATE TEXT
    private String readText(String input, String fieldName) {

        if (input == null || input.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return input.trim();
    }


    // MAIN METHOD
    /**
     * Starts the Car Data Management System application.
     * Creates and displays the GUI on the Swing event dispatch thread.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CarDmsGui gui = new CarDmsGui();

            gui.setVisible(true);
        });
    }
}
