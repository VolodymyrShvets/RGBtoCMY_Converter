import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowImageWindow extends JFrame {
    public ShowImageWindow(BufferedImage image, String title) {
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(image.getWidth() + 25, image.getHeight() + 50);

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        add(panel);
        setVisible(true);
    }

    public void close() {
        setVisible(false);
        dispose();
    }
}
