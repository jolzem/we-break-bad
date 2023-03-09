import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements KeyListener {
    private JLabel background1;
    private JLabel background2;
    private JLabel text;
    private JLabel player;
    private Font font;
    private Timer t;
    
    private int streetY1 = 0;
    private int streetY2 = -600;
    private int counter = 0;
    private int speed = 6;
    private int carX = 365;
    private boolean inputDisabled = false;
    
    private Obstacle[] obstacles = {new Obstacle(speed), new Obstacle(speed), new Obstacle(speed), new Obstacle(speed)};
    
    public GameWindow() {
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
        } catch(Exception e){ e.printStackTrace(); }
        
        // Add obstacles
        for(Obstacle obst : obstacles) {
            obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
            add(obst.getObst());
        }
        
        // add player
        ImageIcon playerImg = new ImageIcon("car-player.png");
        player = new JLabel("", playerImg, JLabel.CENTER);
        player.setBounds(carX, 385, 75, 155);
        add(player);
        
        // Add the background picture
        ImageIcon streetImg1 = new ImageIcon("street.jpg");
        background1 = new JLabel("", streetImg1, JLabel.CENTER);
        background1.setBounds(0, streetY1, 800, 600);
        add(background1);
        ImageIcon streetImg2 = new ImageIcon("street.jpg");
        background2 = new JLabel("", streetImg2, JLabel.CENTER);
        background2.setBounds(0, streetY2, 800, 600);
        add(background2);
        
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
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                dispose();
                // Close the current window
                break;
            case KeyEvent.VK_A:
                if(!inputDisabled) {
                    if(carX > 0) carX -= 10;
                    player.setBounds(carX, 385, 75, 155);
                    break;
                }
            case KeyEvent.VK_D:
                if(!inputDisabled) {
                    if(carX < 725) carX += 10;
                    player.setBounds(carX, 385, 75, 155);
                    break;
                }
        }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        new GameWindow();
    }
    
    public void doOnTick() {
        // increase counter
        counter++;
        
        // Street animation
        if(streetY1 == 600) streetY1 = -600;
        if(streetY2 == 600) streetY2 = -600;
        streetY1 += speed;
        streetY2 += speed;
        background1.setBounds(0, streetY1, 800, 600);
        background2.setBounds(0, streetY2, 800, 600);
        
        // handle obstacles
        for(Obstacle obst : obstacles) {
            obst.stepY();
            // renew obstacle
            if(obst.getY() >= 600 && counter < 1000) {
                renewObstacle(obst);
                break;
            }
            // handle collision
            if(obst.touches(carX, 385)) {
                System.out.println("Hit");
                speed -= 2;
                renewObstacle(obst);
                obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
            }
            obst.setSpeed(speed);
            obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
        }
        
        // when player has hit too many obstacles, he has lost
        if(speed <= 2) {
            speed = 0;
            inputDisabled = true;
            System.out.println("lost");
            t.stop();
        }
        
        if(counter > 1300) {
            speed = 0;
            inputDisabled = true;
            System.out.println("won");
            t.stop();
        }
        
        // repaint every tick
        repaint();
    }
    
    public void renewObstacle(Obstacle obst) {
        obst = new Obstacle(speed);
        obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
        add(obst.getObst());
    }
    
    public int getSpeed() {
        return speed;
    }
}