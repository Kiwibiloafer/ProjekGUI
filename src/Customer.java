import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class Customer extends Frame {
    String userPosition;
    String userName;
    String idEmployees;
    JTable table;
    DefaultTableModel model;

    Customer(String UserPosition, String UserName, String idEmployees) {
        this.userPosition = UserPosition;
        this.userName = UserName;
        this.idEmployees = idEmployees;

        setTitle("Customer");
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

        Button editCustomerButton = new Button("Edit Customer");
        editCustomerButton.setBackground(Color.GREEN);
        editCustomerButton.setForeground(Color.WHITE);
        editCustomerButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        editCustomerButton.setSize(30, 20);
        editCustomerButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int cusId = (int) model.getValueAt(selectedRow, 0);
                editCustomerForm(cusId);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Cus", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        if (userPosition.equals("manager")) {
            bottomPanelRight.add(editCustomerButton);
        }
        
        bottomPanel.add(bottomPanelRight, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        //tulis code disini

        // Model tabel (read-only)
        String[] columnNames = {"ID", "Name", "Address", "Phone Number", "National ID"};
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_customer"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getString("national_id")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void editCustomerForm(Integer cusId) {
        JDialog cusForm = new JDialog((Frame) null, true);
        cusForm.setSize(400, 400);
        cusForm.setLayout(new GridLayout(10, 2));
    
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField nationaIdField = new JTextField();
        JButton saveButton = new JButton("Save");
    
        // Jika mode edit, isi field dengan data dari database
        if (cusId != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customers WHERE id_customer = ?")) {
                pstmt.setInt(1, cusId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    addressField.setText(rs.getString("address"));
                    phoneNumberField.setText(rs.getString("phone_number"));
                    nationaIdField.setText(rs.getString("national_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        cusForm.add(new JLabel("Name:")); cusForm.add(nameField);
        cusForm.add(new JLabel("Address:")); cusForm.add(addressField);
        cusForm.add(new JLabel("Phone Number:")); cusForm.add(phoneNumberField);
        cusForm.add(new JLabel("National Id:")); cusForm.add(nationaIdField);
        cusForm.add(saveButton);
    
        saveButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "")) {
                String query;
                query = "UPDATE customers SET name=?, address=?, phone_number=?, national_id=? WHERE id_customer=?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, addressField.getText());
                pstmt.setString(3, phoneNumberField.getText());
                pstmt.setString(4, nationaIdField.getText());
                if (cusId != null) pstmt.setInt(5, cusId);
                pstmt.executeUpdate();
                loadTableData();
                cusForm.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    
        cusForm.setVisible(true);
    }
}