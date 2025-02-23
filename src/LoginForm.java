import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class LoginForm extends Frame {
    TextField usernameField, passwordField;
    Label messageLabel;

    LoginForm() {
        // Set judul frame dan ukuran windowed
        setTitle("Login Form");
        setSize(400, 300);
        setLocationRelativeTo(null); // MEMBUAT FRAME BERADA DI TENGAH LAYAR
        setLayout(null);
        setVisible(true);

        // Label Username
        Label usernameLabel = new Label("Username:");
        usernameLabel.setBounds(50, 80, 80, 30);
        add(usernameLabel);

        // Input Username
        usernameField = new TextField();
        usernameField.setBounds(150, 80, 180, 30);
        add(usernameField);

        // Label Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setBounds(50, 120, 80, 30);
        add(passwordLabel);

        // Input Password
        passwordField = new TextField();
        passwordField.setBounds(150, 120, 180, 30);
        passwordField.setEchoChar('*'); // Menyembunyikan input password
        add(passwordField);

        // Tombol Login
        Button loginButton = new Button("Login");
        loginButton.setBounds(150, 170, 100, 40);
        add(loginButton);

        // Label Pesan
        messageLabel = new Label("");
        messageLabel.setBounds(50, 220, 300, 30);
        add(messageLabel);

        // Event Listener untuk tombol login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String[] userInfo = authenticate(username, password);
                if (userInfo != null) {
                    JOptionPane.showMessageDialog(null, "Wellcome " + userInfo[0] + " " + userInfo[1], "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Menutup jendela login
                    new Dashboard(userInfo[0]); // Membuka dashboard sesuai dengan posisi pengguna
                } else {
                    messageLabel.setText("Failed to Login! Check username/password.");
                }
            }
        });

        // Event untuk menutup jendela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    // Fungsi untuk memverifikasi login dari database dan mengambil posisi serta nama
    private String[] authenticate(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/projekgui", "root", "");
            String sql = "SELECT position, name FROM employes WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String position = rs.getString("position");
                String name = rs.getString("name");
                rs.close();
                stmt.close();
                conn.close();
                return new String[]{position, name};
            }

            rs.close();
            stmt.close();
            conn.close();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}