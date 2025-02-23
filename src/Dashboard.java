import java.awt.*;
import java.awt.event.*;

public class Dashboard extends Frame {
    String userPosition;
    String userName;

    Dashboard(String position, String userName) {
        this.userPosition = position;
        this.userName = userName;
        setTitle("Dashboard");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel utama dengan GridBagLayout agar lebih terpusat
        Panel mainPanel = new Panel(new GridBagLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Header panel
        Panel headerPanel = new Panel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        // Label kiri atas
        Label titleLabel = new Label("Bali Rent Car", Label.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        headerPanel.add(titleLabel, BorderLayout.WEST);

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

        // Label welcome
        Label welcomeLabel = new Label(" " + userPosition, Label.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        mainPanel.add(welcomeLabel, gbc);

        // Panel untuk tombol fitur dasar
        Panel buttonPanel = new Panel(new GridLayout(1, 3, 20, 20));
        
        Button RentalList = createStyledButton("Rental List", new Color(0, 120, 215));
        RentalList.addActionListener(e -> {
            dispose(); // Menutup dashboard
            new RentalList(); 
        });

        Button RegisRent = createStyledButton("Regis Rent", new Color(0, 153, 51));
        RegisRent.addActionListener(e -> {
            dispose(); // Menutup dashboard
            new RegisRent(); 
        });

        Button ListStock = createStyledButton("List Stock", new Color(255, 140, 0));
        ListStock.addActionListener(e -> {
            dispose(); // Menutup dashboard
            new ListStock(); 
        });
        
        buttonPanel.add(RentalList);
        buttonPanel.add(RegisRent);
        buttonPanel.add(ListStock);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        mainPanel.add(buttonPanel, gbc);
        
        // Jika pengguna adalah manajer, tambahkan fitur tambahan
        if (userPosition.equals("manager")) {
            Panel managerPanel = new Panel(new GridLayout(1, 3, 20, 20));
            Button managerFeature1 = createStyledButton("Manage Employees", new Color(192, 0, 0));
            Button managerFeature2 = createStyledButton("Rent Log", new Color(142, 36, 170));
            Button managerFeature3 = createStyledButton("Payment List", new Color(0, 102, 204));
            
            managerPanel.add(managerFeature1);
            managerPanel.add(managerFeature2);
            managerPanel.add(managerFeature3);
            
            gbc.gridy = 2;
            mainPanel.add(managerPanel, gbc);
        }
        
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        // Event untuk menutup jendela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    // Metode untuk membuat tombol dengan desain modern
    private Button createStyledButton(String text, Color color) {
        Button button = new Button(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(300, 150));
        button.setFocusable(false);
        return button;
    }
}


