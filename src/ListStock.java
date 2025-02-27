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
        
        bottomPanelRight.add(editCarButton);
        bottomPanelRight.add(addCarButton);
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
    
}
