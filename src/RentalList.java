import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class RentalList extends Frame {
    String userPosition;
    String userName;
    String idEmployees;
    JTable table;
    DefaultTableModel model;

    RentalList(String UserPosition, String UserName, String idEmployees) {
        this.userPosition = UserPosition;
        this.userName = UserName;
        this.idEmployees = idEmployees;
        setTitle("RentList");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // Panel utama dengan GridBagLayout agar lebih terpusat
        Panel mainPanel = new Panel(new GridBagLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Header panel
        Panel headerPanel = new Panel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        Panel leftPanel = new Panel(new FlowLayout(FlowLayout.LEFT));

        //Tombol back
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.BLACK);

        backButton.addActionListener(e -> {
            dispose(); // Tutup frame saat ini
            new Dashboard(userPosition, userName, idEmployees); // Kembali ke Dashboard (pastikan kelas Dashboard sudah ada)
        });

        // Label kiri atas
        Label titleLabel = new Label("Bali Rent Car", Label.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        leftPanel.add(backButton);
        leftPanel.add(titleLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Panel profil kanan atas
        Panel profilePanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Label profileLabel = new Label(userPosition + " | " + userName);
        profileLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button logoutButton = new Button("Log Out");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        logoutButton.addActionListener(e -> {
            dispose(); // Menutup dashboard
            new LoginForm(); // Kembali ke login form (pastikan LoginForm didefinisikan)
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

        
        Button PayRentButton = new Button("Pay Rent");
        PayRentButton.setBackground(Color.RED);
        PayRentButton.setForeground(Color.WHITE);
        PayRentButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        PayRentButton.setSize(30, 20);
        PayRentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int carId = (int) model.getValueAt(selectedRow, 0);
                new payRentForm(carId);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Rent", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        Button RetRentButton = new Button("Return Rent");
        RetRentButton.setBackground(Color.RED);
        RetRentButton.setForeground(Color.WHITE);
        RetRentButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        RetRentButton.setSize(30, 20);
        RetRentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int carId = (int) model.getValueAt(selectedRow, 0);
                new retRentForm(carId);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Rent", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        Button viewRentButton = new Button("view detail Rent");
        viewRentButton.setBackground(Color.GRAY);
        viewRentButton.setForeground(Color.WHITE);
        viewRentButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        viewRentButton.setSize(30, 20);
        viewRentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id_rent = (int) model.getValueAt(selectedRow, 0);
                new viewRentForm(id_rent);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Rent", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomPanelRight.add(viewRentButton);
        bottomPanelRight.add(RetRentButton);
        bottomPanelRight.add(PayRentButton);
        
        bottomPanel.add(bottomPanelRight, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Model tabel (read-only)
        String[] columnNames = {"ID Rent", "Customer Name", "Merk", "Type", "Colour", "Rent Date", "Duration", "Return Date", "Payment Status"};
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
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    private void loadTableData() {
        model.setRowCount(0);
        String query = "SELECT r.id_rent, c.name AS customer_name, car.merk, car.type, car.colour, " +
                       "r.rent_date, r.duration, r.return_date, p.status AS payment_status " +
                       "FROM rents r " +
                       "JOIN customers c ON r.id_customer = c.id_customer " +
                       "JOIN car car ON r.id_car = car.id_car " +
                       "LEFT JOIN payments p ON r.id_payment = p.id_payment " +
                       "WHERE r.status = 'on going'";
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_rent"),
                    rs.getString("customer_name"),
                    rs.getString("merk"),
                    rs.getString("type"),
                    rs.getString("colour"),
                    rs.getDate("rent_date"),
                    rs.getInt("duration"),
                    rs.getDate("return_date"),
                    rs.getString("payment_status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    
    private class viewRentForm extends JDialog {
        public viewRentForm(int id_rent) {
            setTitle("View Rent");
            setSize(500, 500);
            setLayout(new GridLayout(0, 2));
            setModal(true);
            
            // Membuat label dan field
            JTextField idField = new JTextField();
            JTextField customerField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField nationalField = new JTextField();
            JTextField merkField = new JTextField();
            JTextField typeField = new JTextField();
            JTextField colourField = new JTextField();
            JTextField regNumberField = new JTextField();
            JTextField frameNumberField = new JTextField();
            JTextField engineNumberField = new JTextField();
            JTextField statusField = new JTextField();
            JTextField rentDateField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField returnDateField = new JTextField();
            JTextField explanationField = new JTextField();
            JTextField paymentStatusField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField staffField = new JTextField();

            // Membuat semua field menjadi non-editable
            JTextField[] fields = {idField, customerField, phoneField, addressField, nationalField, merkField,
                    typeField, colourField, regNumberField, frameNumberField, engineNumberField, statusField,
                    rentDateField, durationField, returnDateField, explanationField, paymentStatusField, priceField, staffField};
            for (JTextField field : fields) {
                field.setEditable(false);
            }

            // Query untuk mengambil data dari database
            String query = "SELECT r.id_rent, c.name AS customer_name, c.address, c.phone_number, c.national_id, " +
                    "car.merk, car.type, car.colour, car.reg_number, car.frame_number, car.engine_number, " +
                    "r.status, r.rent_date, r.duration, r.return_date, r.explanation, " +
                    "p.status AS payment_status, p.total_price, e.name AS staff " +
                    "FROM rents r " +
                    "JOIN customers c ON r.id_customer = c.id_customer " +
                    "JOIN car car ON r.id_car = car.id_car " +
                    "LEFT JOIN payments p ON r.id_payment = p.id_payment " +
                    "JOIN employees e ON r.id_employees = e.id_employees " +
                    "WHERE r.id_rent = ?";

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id_rent);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idField.setText(rs.getString("id_rent"));
                    customerField.setText(rs.getString("customer_name"));
                    addressField.setText(rs.getString("address"));
                    phoneField.setText(rs.getString("phone_number"));
                    nationalField.setText(rs.getString("national_id"));
                    merkField.setText(rs.getString("merk"));
                    typeField.setText(rs.getString("type"));
                    colourField.setText(rs.getString("colour"));
                    regNumberField.setText(rs.getString("reg_number"));
                    frameNumberField.setText(rs.getString("frame_number"));
                    engineNumberField.setText(rs.getString("engine_number"));
                    statusField.setText(rs.getString("status"));
                    rentDateField.setText(rs.getString("rent_date"));
                    durationField.setText(rs.getString("duration"));
                    returnDateField.setText(rs.getString("return_date"));
                    explanationField.setText(rs.getString("explanation"));
                    paymentStatusField.setText(rs.getString("payment_status"));
                    priceField.setText(rs.getString("total_price"));
                    staffField.setText(rs.getString("staff"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Menambahkan komponen ke dialog
            add(new JLabel("ID Rent:")); add(idField);
            add(new JLabel("Customer Name:")); add(customerField);
            add(new JLabel("Phone Number:")); add(phoneField);
            add(new JLabel("Address:")); add(addressField);
            add(new JLabel("National ID:")); add(nationalField);
            add(new JLabel("Merk:")); add(merkField);
            add(new JLabel("Type:")); add(typeField);
            add(new JLabel("Colour:")); add(colourField);
            add(new JLabel("Reg Number:")); add(regNumberField);
            add(new JLabel("Frame Number:")); add(frameNumberField);
            add(new JLabel("Engine Number:")); add(engineNumberField);
            add(new JLabel("Status:")); add(statusField);
            add(new JLabel("Rent Date:")); add(rentDateField);
            add(new JLabel("Duration:")); add(durationField);
            add(new JLabel("Return Date:")); add(returnDateField);
            add(new JLabel("Explanation:")); add(explanationField);
            add(new JLabel("Payment Status:")); add(paymentStatusField);
            add(new JLabel("Total Price:")); add(priceField);
            add(new JLabel("Staff:")); add(staffField);
            
            // Menambahkan tombol close
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            add(new JLabel()); // Spacer
            add(closeButton);
            
            setVisible(true);
        }
    }
    
    private class payRentForm extends JDialog {
        public payRentForm(int id_rent) {
            setTitle("Pay Rent");
            setSize(500, 500);
            setLayout(new GridLayout(0, 2));
            setModal(true);
            
            // Membuat label dan field
            JTextField idField = new JTextField();
            JTextField customerField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField nationalField = new JTextField();
            JTextField merkField = new JTextField();
            JTextField typeField = new JTextField();
            JTextField colourField = new JTextField();
            JTextField regNumberField = new JTextField();
            JTextField frameNumberField = new JTextField();
            JTextField engineNumberField = new JTextField();
            JTextField statusField = new JTextField();
            JTextField rentDateField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField returnDateField = new JTextField();
            JTextField explanationField = new JTextField();
            JTextField paymentStatusField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField staffField = new JTextField();
            
            // ComboBox untuk metode pembayaran
            String[] paymentMethods = {"Cash", "Debit", "Qris"};
            JComboBox<String> methodComboBox = new JComboBox<>(paymentMethods);
            
            // Membuat semua field menjadi non-editable kecuali method
            JTextField[] nonEditableFields = {idField, customerField, phoneField, addressField, nationalField, merkField,
                    typeField, colourField, regNumberField, frameNumberField, engineNumberField,
                    rentDateField, durationField, returnDateField, statusField, explanationField,
                    paymentStatusField, priceField, staffField};
            for (JTextField field : nonEditableFields) {
                field.setEditable(false);
            }
            
            // Query untuk mengambil data dari database
            String query = "SELECT r.id_rent, c.name AS customer_name, c.address, c.phone_number, c.national_id, " +
                    "car.merk, car.type, car.colour, car.reg_number, car.frame_number, car.engine_number, " +
                    "r.status, r.rent_date, r.duration, r.return_date, r.explanation, " +
                    "p.status AS payment_status, p.total_price, p.method, e.name AS staff " +
                    "FROM rents r " +
                    "JOIN customers c ON r.id_customer = c.id_customer " +
                    "JOIN car car ON r.id_car = car.id_car " +
                    "LEFT JOIN payments p ON r.id_payment = p.id_payment " +
                    "JOIN employees e ON r.id_employees = e.id_employees " +
                    "WHERE r.id_rent = ?";
            
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id_rent);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idField.setText(rs.getString("id_rent"));
                    customerField.setText(rs.getString("customer_name"));
                    addressField.setText(rs.getString("address"));
                    phoneField.setText(rs.getString("phone_number"));
                    nationalField.setText(rs.getString("national_id"));
                    merkField.setText(rs.getString("merk"));
                    typeField.setText(rs.getString("type"));
                    colourField.setText(rs.getString("colour"));
                    regNumberField.setText(rs.getString("reg_number"));
                    frameNumberField.setText(rs.getString("frame_number"));
                    engineNumberField.setText(rs.getString("engine_number"));
                    statusField.setText(rs.getString("status"));
                    rentDateField.setText(rs.getString("rent_date"));
                    durationField.setText(rs.getString("duration"));
                    returnDateField.setText(rs.getString("return_date"));
                    explanationField.setText(rs.getString("explanation"));
                    paymentStatusField.setText(rs.getString("payment_status"));
                    priceField.setText(rs.getString("total_price"));
                    staffField.setText(rs.getString("staff"));
                    methodComboBox.setSelectedItem(rs.getString("method"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            // Menambahkan komponen ke dialog
            add(new JLabel("ID Rent:")); add(idField);
            add(new JLabel("Customer Name:")); add(customerField);
            add(new JLabel("Phone Number:")); add(phoneField);
            add(new JLabel("Address:")); add(addressField);
            add(new JLabel("National ID:")); add(nationalField);
            add(new JLabel("Merk:")); add(merkField);
            add(new JLabel("Type:")); add(typeField);
            add(new JLabel("Colour:")); add(colourField);
            add(new JLabel("Reg Number:")); add(regNumberField);
            add(new JLabel("Frame Number:")); add(frameNumberField);
            add(new JLabel("Engine Number:")); add(engineNumberField);
            add(new JLabel("Status:")); add(statusField);
            add(new JLabel("Rent Date:")); add(rentDateField);
            add(new JLabel("Duration:")); add(durationField);
            add(new JLabel("Return Date:")); add(returnDateField);
            add(new JLabel("Explanation:")); add(explanationField);
            add(new JLabel("Payment Status:")); add(paymentStatusField);
            add(new JLabel("Total Price:")); add(priceField);
            add(new JLabel("Staff:")); add(staffField);
            add(new JLabel("Payment Method:")); add(methodComboBox);
            
            // Tombol untuk melakukan pembayaran
            JButton payButton = new JButton("Pay");
            payButton.addActionListener(e -> {
                String updatePaymentQuery = "UPDATE payments SET status = ?, method = ? WHERE id_payment = " +
                                            "(SELECT id_payment FROM rents WHERE id_rent = ? )";
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                     PreparedStatement pstmt = conn.prepareStatement(updatePaymentQuery)) {
                    pstmt.setString(1, "Paid");
                    pstmt.setString(2, (String) methodComboBox.getSelectedItem());
                    pstmt.setInt(3, id_rent);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Payment successful!");
                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error processing payment!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                loadTableData();
            });
            
            // Tombol untuk menutup dialog
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            
            add(payButton);
            add(closeButton);
            
            setVisible(true);
        }
    }
    
    private class retRentForm extends JDialog {
        public retRentForm(int id_rent) {
            setTitle("Return Car");
            setSize(500, 500);
            setLayout(new GridLayout(0, 2));
            setModal(true);
            Integer idCar = -1;
            
            // Membuat label dan field
            JTextField idField = new JTextField();
            JTextField customerField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField nationalField = new JTextField();
            JTextField merkField = new JTextField();
            JTextField typeField = new JTextField();
            JTextField colourField = new JTextField();
            JTextField regNumberField = new JTextField();
            JTextField frameNumberField = new JTextField();
            JTextField engineNumberField = new JTextField();
            JTextField statusField = new JTextField();
            JTextField rentDateField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField returnDateField = new JTextField();
            JTextField explanationField = new JTextField();
            JTextField paymentStatusField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField staffField = new JTextField();
            
            JTextField[] nonEditableFields = {idField, customerField, phoneField, addressField, nationalField, merkField,
                    typeField, colourField, regNumberField, frameNumberField, engineNumberField,
                    rentDateField, durationField, returnDateField, statusField, explanationField,
                    paymentStatusField, priceField, staffField};
            for (JTextField field : nonEditableFields) {
                field.setEditable(false);
            }
            
            // Query untuk mengambil data dari database
            String query = "SELECT r.id_rent, c.name AS customer_name, c.address, c.phone_number, c.national_id, " +
                    "car.id_car, car.merk, car.type, car.colour, car.reg_number, car.frame_number, car.engine_number, " +
                    "r.status, r.rent_date, r.duration, r.return_date, r.explanation, " +
                    "p.status AS payment_status, p.total_price, e.name AS staff " +
                    "FROM rents r " +
                    "JOIN customers c ON r.id_customer = c.id_customer " +
                    "JOIN car car ON r.id_car = car.id_car " +
                    "LEFT JOIN payments p ON r.id_payment = p.id_payment " +
                    "JOIN employees e ON r.id_employees = e.id_employees " +
                    "WHERE r.id_rent = ?";
            
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id_rent);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idField.setText(rs.getString("id_rent"));
                    customerField.setText(rs.getString("customer_name"));
                    addressField.setText(rs.getString("address"));
                    phoneField.setText(rs.getString("phone_number"));
                    nationalField.setText(rs.getString("national_id"));
                    merkField.setText(rs.getString("merk"));
                    typeField.setText(rs.getString("type"));
                    colourField.setText(rs.getString("colour"));
                    regNumberField.setText(rs.getString("reg_number"));
                    frameNumberField.setText(rs.getString("frame_number"));
                    engineNumberField.setText(rs.getString("engine_number"));
                    statusField.setText(rs.getString("status"));
                    rentDateField.setText(rs.getString("rent_date"));
                    durationField.setText(rs.getString("duration"));
                    returnDateField.setText(rs.getString("return_date"));
                    explanationField.setText(rs.getString("explanation"));
                    paymentStatusField.setText(rs.getString("payment_status"));
                    priceField.setText(rs.getString("total_price"));
                    staffField.setText(rs.getString("staff"));
                    idCar = rs.getInt("id_car");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            // Menambahkan komponen ke dialog
            add(new JLabel("ID Rent:")); add(idField);
            add(new JLabel("Customer Name:")); add(customerField);
            add(new JLabel("Phone Number:")); add(phoneField);
            add(new JLabel("Address:")); add(addressField);
            add(new JLabel("National ID:")); add(nationalField);
            add(new JLabel("Merk:")); add(merkField);
            add(new JLabel("Type:")); add(typeField);
            add(new JLabel("Colour:")); add(colourField);
            add(new JLabel("Reg Number:")); add(regNumberField);
            add(new JLabel("Frame Number:")); add(frameNumberField);
            add(new JLabel("Engine Number:")); add(engineNumberField);
            add(new JLabel("Status:")); add(statusField);
            add(new JLabel("Rent Date:")); add(rentDateField);
            add(new JLabel("Duration:")); add(durationField);
            add(new JLabel("Return Date:")); add(returnDateField);
            add(new JLabel("Explanation:")); add(explanationField);
            add(new JLabel("Payment Status:")); add(paymentStatusField);
            add(new JLabel("Total Price:")); add(priceField);
            add(new JLabel("Staff:")); add(staffField);
            
            // Tombol untuk mengembalikan mobil rental
            JButton returnButton = new JButton("Return Car");
            int finalIdCar = idCar;
            returnButton.addActionListener(e -> {
                String updateRentQuery = "UPDATE rents SET status = ?, return_date = ? WHERE id_rent = ?";
                String updateCarQuery = "UPDATE car SET status = ? WHERE id_car = ?";
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "")) {
                    conn.setAutoCommit(false);
                    try (PreparedStatement pstmtRent = conn.prepareStatement(updateRentQuery);
                         PreparedStatement pstmtCar = conn.prepareStatement(updateCarQuery)) {
                        
                        pstmtRent.setString(1, "Done");
                        pstmtRent.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                        pstmtRent.setInt(3, id_rent);
                        pstmtRent.executeUpdate();
                        
                        pstmtCar.setString(1, "ready");
                        pstmtCar.setInt(2, finalIdCar);
                        pstmtCar.executeUpdate();
                        
                        conn.commit();
                        JOptionPane.showMessageDialog(this, "Car returned successfully and status updated to 'ready'!");
                        dispose();
                    } catch (SQLException ex) {
                        conn.rollback();
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error processing return!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database connection error!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Tombol untuk menutup dialog
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            
            add(returnButton);
            add(closeButton);
            
            setVisible(true);
        }
    }    
}