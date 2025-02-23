import java.awt.*;
import java.awt.event.*;

public class Dashboard extends Frame {
    String userPosition;
    String userName;

    Dashboard(String position, String userName) {
        this.userPosition = position;
        setTitle("Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setVisible(true);


        // Fitur Basic
        Button basicFeature1 = new Button("Lihat Laporan");
        basicFeature1.setBackground(Color.CYAN);
        Button basicFeature2 = new Button("Edit Profil");
        basicFeature2.setBackground(Color.GREEN);
        Button basicFeature3 = new Button("Hubungi Dukungan");
        basicFeature3.setBackground(Color.ORANGE);
        add(basicFeature1);
        add(basicFeature2);
        add(basicFeature3);

        // Fitur tambahan hanya untuk Manager dengan warna berbeda
        if (userPosition.equals("manager")) {
            Button managerFeature1 = new Button("Kelola Karyawan");
            managerFeature1.setBackground(Color.RED);
            Button managerFeature2 = new Button("Analisis Keuangan");
            managerFeature2.setBackground(Color.MAGENTA);
            Button managerFeature3 = new Button("Setel Kebijakan Perusahaan");
            managerFeature3.setBackground(Color.BLUE);
            add(managerFeature1);
            add(managerFeature2);
            add(managerFeature3);
        }

        // Event untuk menutup jendela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
