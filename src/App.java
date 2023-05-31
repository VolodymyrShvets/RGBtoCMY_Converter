import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App extends JFrame {
    private final int APP_WIDTH = 380;
    private final int APP_HEIGHT = 200;
    private String sourcePath = "source files";
    private JFileChooser jfc;
    private JButton openFileButton;
    private JButton convertImageButton;
    private JPanel contentPane;
    private File selectedFile;
    private BufferedImage rgbImage;
    private ShowImageWindow windowRGB;
    private ShowImageWindow windowCMY;

    public App() {
        init();
    }

    public void init() {
        setTitle("RGB to CMY");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(APP_WIDTH, APP_HEIGHT);

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        initComponents();

        setVisible(true);
    }

    public void initComponents() {
        jfc = new JFileChooser(sourcePath);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        openFileButton = new JButton("Відкрити зображення");
        openFileButton.addActionListener(e -> {
            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = jfc.getSelectedFile();
                //System.out.println(selectedFile.getAbsolutePath());
            }

            if (selectedFile != null) {
                try {
                    rgbImage = ImageIO.read(selectedFile);
                    if (windowRGB != null) {
                        windowRGB.close();
                        windowCMY.close();
                    }
                    windowRGB = new ShowImageWindow(rgbImage, "Вихідне зображення");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        openFileButton.setBounds(30, 30, 300, 40);
        openFileButton.setFont(new Font("Arial", Font.BOLD, 15));
        contentPane.add(openFileButton);

        convertImageButton = new JButton("Конвертувати");
        convertImageButton.addActionListener(e -> convertImage(rgbImage));
        convertImageButton.setBounds(30, 85, 300, 40);
        convertImageButton.setFont(new Font("Arial", Font.BOLD, 15));
        contentPane.add(convertImageButton);
    }

    public void convertImage(BufferedImage image) {
        try {
            BufferedImage cmyImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int column = 0; column < image.getHeight(); column++) {
                for (int row = 0; row < image.getWidth(); row++) {
                    //System.out.println("x,y: " + row + ", " + column);

                    cmyImage.setRGB(row, column, getPixelCMYValues(image.getRGB(row, column)).getRGB());
                }
            }
            ImageIO.write(cmyImage, "bmp", new File(selectedFile.getParent() + "/result_img.bmp"));
            if (windowCMY != null)
                windowCMY.close();
            windowCMY = new ShowImageWindow(cmyImage, "Результуюче зображення");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color getPixelCMYValues(int pixel) {
        //int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return convertRGBToCMY(red, green, blue);
    }

    public Color convertRGBToCMY(int red, int green, int blue) {
        int cyan = 255 - red;
        int magenta = 255 - green;
        int yellow = 255 - blue;

        return new Color(cyan, magenta, yellow);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(App::new);
    }
}
