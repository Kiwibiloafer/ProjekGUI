import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class ListStock extends Frame {
    String userPosition;
    String userName;
    String idEmployees;
    JTable table;
    DefaultTableModel model;

    ListStock(String UserPosition, String UserName, String idEmployees) {
        this.userPosition = UserPosition;
        this.userName = UserName;
        this.idEmployees = idEmployees;
        setTitle("ListStock");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setVisible(true);

        // Header panel
        Panel headerPanel = new Panel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        Panel leftPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> {
            dispose();
            new Dashboard(userPosition, userName, idEmployees);
        });
        
        Label titleLabel = new Label("Bali Rent Car", Label.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        leftPanel.add(backButton);
        leftPanel.add(titleLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Profile Panel
        Panel profilePanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Label profileLabel = new Label(userPosition + " | " + userName);
        profileLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button logoutButton = new Button("Log Out");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        
        profilePanel.add(profileLabel);
        profilePanel.add(logoutButton);
        headerPanel.add(profilePanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Panel bawah untuk tombol
        Panel bottomPanel = new Panel(new BorderLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 50));
        Panel bottomPanelRight = new Panel(new FlowLayout(FlowLayout.RIGHT));

        Button addCarButton = new Button("Add Car");
        addCarButton.setBackground(Color.GREEN);
        addCarButton.setForeground(Color.WHITE);
        addCarButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        addCarButton.setSize(30, 20);
        addCarButton.addActionListener(e -> openCarForm(null));
        
        Button editCarButton = new Button("Edit Car");
        editCarButton.setBackground(Color.GRAY);
        editCarButton.setForeground(Color.WHITE);
        editCarButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        editCarButton.setSize(30, 20);
        editCarButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int carId = (int) model.getValueAt(selectedRow, 0);
                openCarForm(carId);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Car", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        Button viewCarButton = new Button("view detail Car");
        viewCarButton.setBackground(Color.GRAY);
        viewCarButton.setForeground(Color.WHITE);
        viewCarButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        viewCarButton.setSize(30, 20);
        viewCarButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int carId = (int) model.getValueAt(selectedRow, 0);
                new viewCarForm(carId);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Car", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        Button regisRentButton = new Button("Regis Rent");
        regisRentButton.setBackground(Color.GREEN);
        regisRentButton.setForeground(Color.WHITE);
        regisRentButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        regisRentButton.setSize(30, 20);
        regisRentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int carId = (int) model.getValueAt(selectedRow, 0);
                regisRentForm(carId, idEmployees);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Car", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomPanelRight.add(viewCarButton);
        bottomPanelRight.add(regisRentButton);
        if (userPosition.equals("manager")) {
            bottomPanelRight.add(editCarButton);
            bottomPanelRight.add(addCarButton);
        }
        
        bottomPanel.add(bottomPanelRight, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Model tabel (read-only)
        String[] columnNames = {"ID", "Merk", "Type", "Colour", "Frame No", "Engine No", "Reg No", "Status", "Price"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        add(scrollPane, BorderLayout.CENTER);
        
        loadTableData();
        
        // Event untuk menutup jendela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void loadTableData() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM car ORDER BY FIELD(status, 'ready', 'on going', 'not available')")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_car"),
                    rs.getString("merk"),
                    rs.getString("type"),
                    rs.getString("colour"),
                    rs.getString("frame_number"),
                    rs.getString("engine_number"),
                    rs.getString("reg_number"),
                    rs.getString("status"),
                    rs.getInt("price")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openCarForm(Integer carId) {
        JDialog carForm = new JDialog((Frame) null, carId == null ? "Add Car" : "Edit Car", true);
        carForm.setSize(400, 400);
        carForm.setLayout(new GridLayout(10, 2));
    
        JTextField merkField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField colourField = new JTextField();
        JTextField frameNumberField = new JTextField();
        JTextField engineNumberField = new JTextField();
        JTextField regNumberField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField priceField = new JTextField();
        JButton saveButton = new JButton("Save");
    
        // Jika mode edit, isi field dengan data dari database
        if (carId != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM car WHERE id_car = ?")) {
                pstmt.setInt(1, carId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    merkField.setText(rs.getString("merk"));
                    typeField.setText(rs.getString("type"));
                    colourField.setText(rs.getString("colour"));
                    frameNumberField.setText(rs.getString("frame_number"));
                    engineNumberField.setText(rs.getString("engine_number"));
                    regNumberField.setText(rs.getString("reg_number"));
                    statusField.setText(rs.getString("status"));
                    priceField.setText(String.valueOf(rs.getInt("price")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        carForm.add(new JLabel("Merk:")); carForm.add(merkField);
        carForm.add(new JLabel("Type:")); carForm.add(typeField);
        carForm.add(new JLabel("Colour:")); carForm.add(colourField);
        carForm.add(new JLabel("Frame No:")); carForm.add(frameNumberField);
        carForm.add(new JLabel("Engine No:")); carForm.add(engineNumberField);
        carForm.add(new JLabel("Reg No:")); carForm.add(regNumberField);
        carForm.add(new JLabel("Status:")); carForm.add(statusField);
        carForm.add(new JLabel("Price:")); carForm.add(priceField);
        carForm.add(saveButton);
    
        saveButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "")) {
                String query;
                if (carId == null) {
                    query = "INSERT INTO car (merk, type, colour, frame_number, engine_number, reg_number, status, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                } else {
                    query = "UPDATE car SET merk=?, type=?, colour=?, frame_number=?, engine_number=?, reg_number=?, status=?, price=? WHERE id_car=?";
                }
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, merkField.getText());
                pstmt.setString(2, typeField.getText());
                pstmt.setString(3, colourField.getText());
                pstmt.setString(4, frameNumberField.getText());
                pstmt.setString(5, engineNumberField.getText());
                pstmt.setString(6, regNumberField.getText());
                pstmt.setString(7, statusField.getText());
                pstmt.setInt(8, Integer.parseInt(priceField.getText()));
                if (carId != null) pstmt.setInt(9, carId);
                pstmt.executeUpdate();
                loadTableData();
                carForm.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    
        carForm.setVisible(true);
    }

    private class viewCarForm extends JDialog {
        viewCarForm(int carId) {
            setTitle("View Car");
            setSize(400, 400);
            setLayout(new GridLayout(9, 2));
            setModal(true);
            
            JTextField merkField = new JTextField();
            JTextField typeField = new JTextField();
            JTextField colourField = new JTextField();
            JTextField frameNumberField = new JTextField();
            JTextField engineNumberField = new JTextField();
            JTextField regNumberField = new JTextField();
            JTextField statusField = new JTextField();
            JTextField priceField = new JTextField();
            
            merkField.setEditable(false);
            typeField.setEditable(false);
            colourField.setEditable(false);
            frameNumberField.setEditable(false);
            engineNumberField.setEditable(false);
            regNumberField.setEditable(false);
            statusField.setEditable(false);
            priceField.setEditable(false);
            
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM car WHERE id_car = ?")) {
                pstmt.setInt(1, carId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    merkField.setText(rs.getString("merk"));
                    typeField.setText(rs.getString("type"));
                    colourField.setText(rs.getString("colour"));
                    frameNumberField.setText(rs.getString("frame_number"));
                    engineNumberField.setText(rs.getString("engine_number"));
                    regNumberField.setText(rs.getString("reg_number"));
                    statusField.setText(rs.getString("status"));
                    priceField.setText(String.valueOf(rs.getInt("price")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            add(new JLabel("Merk:")); add(merkField);
            add(new JLabel("Type:")); add(typeField);
            add(new JLabel("Colour:")); add(colourField);
            add(new JLabel("Frame No:")); add(frameNumberField);
            add(new JLabel("Engine No:")); add(engineNumberField);
            add(new JLabel("Reg No:")); add(regNumberField);
            add(new JLabel("Status:")); add(statusField);
            add(new JLabel("Price:")); add(priceField);
            
            setVisible(true);
        }
    }
    
    private void regisRentForm(Integer idCar, String employeeid) {
        JDialog rentForm = new JDialog((Frame) null, "Register Rent", true);
        rentForm.setSize(400, 400);
        rentForm.setLayout(new GridLayout(6, 2));
    
        Integer idEmployee = Integer.parseInt(employeeid);
    
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField nationalIdField = new JTextField();
        JTextField durationField = new JTextField();
        JButton submitButton = new JButton("Submit");
    
        rentForm.add(new JLabel("Name:")); rentForm.add(nameField);
        rentForm.add(new JLabel("Address:")); rentForm.add(addressField);
        rentForm.add(new JLabel("Phone Number:")); rentForm.add(phoneField);
        rentForm.add(new JLabel("National ID:")); rentForm.add(nationalIdField);
        rentForm.add(new JLabel("Duration (days):")); rentForm.add(durationField);
        rentForm.add(submitButton);
    
        submitButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "")) {
                conn.setAutoCommit(false); // Mulai transaksi
                
                String nationalId = nationalIdField.getText();
                Integer idCustomer = null;
    
                // Cek apakah customer sudah ada berdasarkan national_id
                String checkCustomerQuery = "SELECT id_customer FROM customers WHERE national_id = ?";
                try (PreparedStatement pstmtCheck = conn.prepareStatement(checkCustomerQuery)) {
                    pstmtCheck.setString(1, nationalId);
                    ResultSet rs = pstmtCheck.executeQuery();
                    if (rs.next()) {
                        idCustomer = rs.getInt("id_customer"); // Jika sudah ada, gunakan id_customer yang lama
                    }
                }
    
                // Jika customer belum ada, insert data baru
                if (idCustomer == null) {
                    String insertCustomerQuery = "INSERT INTO customers (name, address, phone_number, national_id) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmtCustomer = conn.prepareStatement(insertCustomerQuery, Statement.RETURN_GENERATED_KEYS)) {
                        pstmtCustomer.setString(1, nameField.getText());
                        pstmtCustomer.setString(2, addressField.getText());
                        pstmtCustomer.setString(3, phoneField.getText());
                        pstmtCustomer.setString(4, nationalId);
                        pstmtCustomer.executeUpdate();
    
                        // Ambil id_customer yang baru dimasukkan
                        ResultSet generatedKeys = pstmtCustomer.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            idCustomer = generatedKeys.getInt(1);
                        }
                    }
                }
    
                // Insert rent data dengan id_customer yang valid
                String rentQuery = "INSERT INTO rents (id_employees, id_customer, id_car, rent_date, duration, status) VALUES (?, ?, ?, NOW(), ?, 'on going')";
                try (PreparedStatement pstmtRent = conn.prepareStatement(rentQuery)) {
                    pstmtRent.setInt(1, idEmployee);
                    pstmtRent.setInt(2, idCustomer);
                    pstmtRent.setInt(3, idCar);
                    pstmtRent.setInt(4, Integer.parseInt(durationField.getText()));
                    pstmtRent.executeUpdate();
                }
    
                // Update status mobil menjadi 'on going'
                String carQuery = "UPDATE car SET status = ? WHERE id_car = ?";
                try (PreparedStatement pstmtCar = conn.prepareStatement(carQuery)) {
                    pstmtCar.setString(1, "on going");
                    pstmtCar.setInt(2, idCar);
                    pstmtCar.executeUpdate();
                }
    
                conn.commit(); // Commit transaksi jika semuanya berhasil
    
                loadTableData();
                rentForm.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    
        rentForm.setVisible(true);
    }
    
    
}
