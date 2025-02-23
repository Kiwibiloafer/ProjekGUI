import java.awt.*;
import java.awt.event.*;

public class Dashboard extends Frame {

    Dashboard() {
        // Mengatur judul dan ukuran frame
        setTitle("Dashboard AWT");
        setSize(500, 400);
        setLayout(null);
        setVisible(true);

        Label usernameLabel = new Label("selamat datang");
        usernameLabel.setBounds(50, 80, 80, 30);
        add(usernameLabel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}