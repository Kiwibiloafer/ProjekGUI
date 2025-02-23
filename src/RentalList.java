import java.awt.*;
import java.awt.event.*;

public class RentalList extends Frame {
    String userPosition;
    String userName;

    RentalList(String UserPosition, String UserName) {
        this.userPosition = UserPosition;
        this.userName = UserName;
        setTitle("ListStock");
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
            new Dashboard(userPosition, userName); // Kembali ke Dashboard (pastikan kelas Dashboard sudah ada)
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
        //tulis code disini
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}