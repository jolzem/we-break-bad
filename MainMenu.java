import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame implements KeyListener {
    private JLabel background;
    private JLabel text;
    private Font font;
    private Timer t;
    private int startTextY = 600;
    private WavPlayer bgMusic;
    
    public MainMenu() {
        // Set the window properties
        setTitle("We Break Bad");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        
        
        // Create font from file
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Heart-Breaking-Bad.ttf")).deriveFont(Font.PLAIN, 48f);
        } catch(Exception e) { e.printStackTrace(); }
        
        // start music
        bgMusic = new WavPlayer("intro.wav");
        
        // Add interaction text
        text = new JLabel("Press space to Play");
        text.setFont(font);
        text.setForeground(new Color(255,255,255));
        text.setBounds(430, startTextY, text.getPreferredSize().width, text.getPreferredSize().height);
        add(text);
        
        // Add the background picture
        ImageIcon img = new ImageIcon("Titelcover1.png");
        background = new JLabel("", img, JLabel.CENTER);
        background.setBounds(0, 0, 800, 600);
        add(background);
        
        // Add key listener for space key
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        setVisible(true);
        
        t = new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doOnTick();
                }
            });
        t.start();
    }
    
    // Key listener implementation
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Close the current window
            dispose();
            bgMusic.stop();
            // Create a new GameWindow class
            new GameWindow();
        }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        new MainMenu();
    }
    
    public void doOnTick() {
        if(startTextY > 500) {
            startTextY -= 5;
            text.setBounds(430, startTextY, text.getPreferredSize().width, text.getPreferredSize().height);
            repaint();
        }
    }
}