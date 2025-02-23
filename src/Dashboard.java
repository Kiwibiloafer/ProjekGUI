import java.awt.*;
import java.awt.event.*;

public class Dashboard extends Frame {
    String userRole;

    Dashboard(String role) {
        this.userRole = role;
        setTitle("Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setVisible(true);

        Label welcomeLabel = new Label("Selamat datang di Dashboard " + userRole);
        add(welcomeLabel);

        // Fitur yang muncul untuk semua peran
        Button basicFeature = new Button("Fitur Basic");
        add(basicFeature);

        // Fitur tambahan hanya untuk Manager
        if (userRole.equals("manager")) {
            Button managerFeature = new Button("Fitur Manager");
            add(managerFeature);
        }

        // Event untuk menutup jendela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
