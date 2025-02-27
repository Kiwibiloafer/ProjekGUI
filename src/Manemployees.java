import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class Manemployees extends Frame {
    String userPosition;
    String userName;
    String idEmployees;
    JTable table;
    DefaultTableModel model;

    Manemployees(String UserPosition, String UserName, String idEmployees) {
        this.userPosition = UserPosition;
        this.userName = UserName;
        this.idEmployees = idEmployees;
        setTitle("Manage Employees");
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
        
        Label titleLabel = new Label("Bali Rent Employees", Label.LEFT);
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

        Button addEmployeesButton = new Button("Add Employees");
        addEmployeesButton.setBackground(Color.GREEN);
        addEmployeesButton.setForeground(Color.WHITE);
        addEmployeesButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        addEmployeesButton.setSize(30, 20);
        addEmployeesButton.addActionListener(e -> openEmployeesForm(null));
        
        Button editEmployeesButton = new Button("Edit Employees");
        editEmployeesButton.setBackground(Color.GRAY);
        editEmployeesButton.setForeground(Color.WHITE);
        editEmployeesButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        editEmployeesButton.setSize(30, 20);
        editEmployeesButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int employeesId = (int) model.getValueAt(selectedRow, 0);
                openEmployeesForm(employeesId);
            } else {
                JOptionPane.showMessageDialog(this, "Choose the Employees", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        bottomPanelRight.add(editEmployeesButton);
        bottomPanelRight.add(addEmployeesButton);
        bottomPanel.add(bottomPanelRight, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Model tabel (read-only)
        String[] columnNames = {"ID", "name", "username", "position", "password"};
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
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_employees"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("position"),
                    rs.getString("password")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openEmployeesForm(Integer employeesId) {
        JDialog employeesForm = new JDialog((Frame) null, employeesId == null ? "Add Employees" : "Edit Employees", true);
        employeesForm.setSize(400, 400);
        employeesForm.setLayout(new GridLayout(10, 2));
    
        JTextField nameField = new JTextField();
        JTextField usenameField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField passwordField = new JTextField();
        JButton saveButton = new JButton("Save");
    
        // Jika mode edit, isi field dengan data dari database
        if (employeesId != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees WHERE id_employees = ?")) {
                pstmt.setInt(1, employeesId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    usenameField.setText(rs.getString("username"));
                    positionField.setText(rs.getString("position"));
                    passwordField.setText(rs.getString("password"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        employeesForm.add(new JLabel("name:")); employeesForm.add(nameField);
        employeesForm.add(new JLabel("username:")); employeesForm.add(usenameField);
        employeesForm.add(new JLabel("position:")); employeesForm.add(positionField);
        employeesForm.add(new JLabel("password:")); employeesForm.add(passwordField);
        employeesForm.add(saveButton);
    
        saveButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "")) {
                String query;
                if (employeesId == null) {
                    query = "INSERT INTO employees (name, username, password, position) VALUES (?, ?, ?, ?)";
                } else {
                    query = "UPDATE employees SET name=?, username=?, position=?, password=? WHERE id_employees=?";
                }
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, usenameField.getText());
                pstmt.setString(3, positionField.getText());
                pstmt.setString(4, passwordField.getText());
                if (employeesId != null) pstmt.setInt(9, employeesId);
                pstmt.executeUpdate();
                loadTableData();
                employeesForm.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    
        employeesForm.setVisible(true);
    }
    
}
