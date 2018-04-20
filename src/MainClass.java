import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainClass extends JComponent implements MouseListener {
    static MainClass main; // obiekt klasy, który wrzucamy do JFrama
    private BufferedImage bufferedImage = null; // obraz tła
    private List<String> sTextList = new ArrayList<String>(); // lista ze stringami, które będą wyświetlane
    private List<java.awt.Point> pObjectsCoordinates = new java.util.ArrayList<java.awt.Point>(); // lista z współrzędnymi stringów
    private List<Image> imagesList = new java.util.ArrayList<java.awt.Image>(); // lista z fragmentami obrazków
    private List<java.awt.Point> pImagesCoordinates = new java.util.ArrayList<java.awt.Point>(); // lista ze współrzędnymi obrazków
    private int width = 200; // początkowa szerokość
    private int height = 200; // początkowa wysokość

    MainClass() {
        this.setLayout(new BorderLayout()); // borderLayout - tutaj niepotrzebny
        this.setSize(width, height);
        pObjectsCoordinates.add(new Point(100, 100)); // dodajemy 3 stringi w konstruktorze
        pObjectsCoordinates.add(new Point(20, 50));
        pObjectsCoordinates.add(new Point(30, 200));
        sTextList.add("First");
        sTextList.add("Second");
        sTextList.add("Third");
        addMouseListener(this); // musimy dodać do obiektu nasłuchiwanie myszy, inaczej zaimplementowane metody z interfejsu nie będą działać - this odnosi się do obiektu, który właśnie tworzymy
        try {
            bufferedImage = javax.imageio.ImageIO.read(
                    MainClass.class.getResource("resources/cats.jpg")); // odczytujemy obrazek z zasobów
        } catch (Exception ex) {
            bufferedImage = new java.awt.image.BufferedImage(
                    width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        }
    }


    public static void main(String[] args) {
        main = new MainClass(); // tworzymy obiekt
        JFrame frame = new JFrame(); // główne okno programu
        frame.setLayout(new BorderLayout()); // bez sensu, borderLayout: patrz google
        frame.setSize(300, 300); // początkowy rozmiar
        frame.add(main); // dodajemy komponent
        frame.setVisible(true); // widoczny dla użytkownika
        main.addText("abc", 70, 80); // metoda własna, dodajemy tekst do listy wraz ze współrzędnymi
        frame.setTitle("abc");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static String generateString(Random rng, String characters, int length) { // losowe generowanie stringów
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public void paintComponent(Graphics graph) {
        bufferedImage = scaleImage(bufferedImage); // dostoswuje rozmiar obrazu do rozmiaru komponentu
        Graphics2D graph2 = (Graphics2D) graph; // This is the fundamental class for rendering 2-dimensional shapes, text and images on the Java(tm) platform.
        graph2.setColor(Color.black);
        graph2.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(null),
                bufferedImage.getHeight(null), null);
        graph2.setColor(Color.green);
        java.util.Iterator iterator = sTextList.iterator(); // tworzymy iterator, który przechodzi przez elementy listy, od pierwszego
        int counter = 0; // licznik dla współrzędnych, aby zapamiętać numer elementu z listy

        while (iterator.hasNext()) { // po wszystkich elementach
            String sText = (String) iterator.next();
            Point xy = pObjectsCoordinates.get(counter); //obiekt klasy xy, zawierający x oraz y
            counter++;
            graph2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
            graph2.drawString(sText, xy.x, xy.y - 10); // rysujemy stringa
        }

        counter = 0;
        iterator = imagesList.iterator();
        while (iterator.hasNext()) { // po wszystkich elementach
            Image image = (Image) iterator.next();
            Point xy = pImagesCoordinates.get(counter);
            counter++;
            graph2.drawImage(image, xy.x, xy.y - 10, null); // rysujemy fragment obrazka
        }
        // w ten sposób narysowaliśmy wszystkie elementy graficznena grafice
    }

    private void addText(String sText, int x, int y) {
        sTextList.add(sText);
        pObjectsCoordinates.add(new Point((int) (x), (int) (y)));
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Random rand = new Random();
        addText(generateString(rand, "abcdefghijklmmoprstuvwxyz", 8), rand.nextInt(main.getWidth()), rand.nextInt(main.getHeight()));
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        setBufferedImage("resources/cats.jpg");
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        addFragmentOfImage();
    }

    private void setBufferedImage(String path) {
        try {
            bufferedImage = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage scaleImage(BufferedImage image) {
        return convertToBufferedImage(image.getScaledInstance(main.getWidth(),
                main.getHeight(), Image.SCALE_SMOOTH));
    }

    private void addFragmentOfImage() {
        try {
            Random rand = new Random();
            Graphics2D graphics = (Graphics2D) this.getGraphics();
            BufferedImage image = (ImageIO.read(getClass().getResource("resources/th.jpeg")));
            image = convertToBufferedImage(scaleImage(image));
            int x = rand.nextInt(image.getWidth(null) - 20);
            int y = rand.nextInt(image.getHeight(null) - 20);
            image = image.getSubimage(x, y, 20, 20);
            pImagesCoordinates.add(new Point(x, y));
            imagesList.add(image);
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}

