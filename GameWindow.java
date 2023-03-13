import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.AbstractSet;

public class GameWindow extends JFrame implements KeyListener {
    private JLabel background1;
    private JLabel background2;
    private JLabel text;
    private RotatedLabel player;
    private Font font;
    private Timer t;
    private WavPlayer bgMusic;
    private AbstractSet<Integer> activeKeys;

    private int streetY1 = 0;
    private int streetY2 = -600;
    private int counter = 0;
    private int speed = 6;
    private int oldSpeed = speed;
    private int obsSpeed = speed;
    private int carX = 365;
    private int carRot = 0;
    private int boostTimer;
    private int timesHit = 0;
    private boolean inputDisabled = false;
    private boolean boostActive = false;

    private Obstacle[] obstacles = {new Obstacle(speed)};

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

        // start music
        bgMusic = new WavPlayer("car.wav");

        // Add obstacles
        for(Obstacle obst : obstacles) {
            obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
            add(obst.getObst());
        }

        // add player
        ImageIcon playerImg = new ImageIcon("img/car-player.png");
        player = new RotatedLabel("", playerImg, JLabel.CENTER, 0);
        player.setBounds(carX, 385, 75, 155);
        add(player);

        // Add the background picture
        ImageIcon streetImg1 = new ImageIcon("img/street.png");
        background1 = new JLabel("", streetImg1, JLabel.CENTER);
        background1.setBounds(0, streetY1, 800, 600);
        add(background1);
        ImageIcon streetImg2 = new ImageIcon("img/street2.png");
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
        activeKeys.add(e.getKeyCode());
        handleInput();
    }

    public void keyReleased(KeyEvent e) {
        activeKeys.remove(e.getKeyCode());
        handleInput();
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new GameWindow();
    }

    public void handleInput() {
        if(activeKeys.contains(KeyEvent.VK_W) && activeKeys.contains(KeyEvent.VK_A)) {
            player.setAngle(0.825);
        } else if(activeKeys.contains(KeyEvent.VK_W) && activeKeys.contains(KeyEvent.VK_D)) {
            carRot = 45;
        } else if(activeKeys.contains(KeyEvent.VK_W)) {
            speed = oldSpeed;
        } else if(activeKeys.contains(KeyEvent.VK_A)) {
            if(!inputDisabled) {
                if(carX > 0) carX -= oldSpeed;
                player.setBounds(carX, 385, 75, 155);
            }
        } else if(activeKeys.contains(KeyEvent.VK_D)) {
            if(!inputDisabled) {
                if(carX < 725) carX += oldSpeed;
                player.setBounds(carX, 385, 75, 155);
            }
        }
    }

    public void doOnTick() {
        // increase counter
        counter++;

        // Street animation
        if(streetY1 >= 600) streetY1 = -600;
        if(streetY2 >= 600) streetY2 = -600;
        streetY1 += speed;
        streetY2 += speed;
        background1.setBounds(0, streetY1, 800, 600);
        background2.setBounds(0, streetY2, 800, 600);

        // handle boost
        if(counter == boostTimer) speed = oldSpeed;

        // handle obstacles
        // source: https://www.w3schools.com/java/java_foreach_loop.asp
        for(Obstacle obst : obstacles) {
            obst.stepY();
            // renew obstacle
            if(obst.getY() >= 600 && counter < 1000) {
                renewObstacle(obst);
                break;
            }
            // handle collision
            if(obst.touches(carX, 385)) {
                // speed -= 2;
                oldSpeed = speed;
                timesHit++;
                renewObstacle(obst);
            }
            obst.setSpeed(obsSpeed);
            obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
        }

        // when player has hit too many obstacles, he has lost
        if(timesHit >= 399999) {
            speed = 0;
            inputDisabled = true;
            //System.out.println("lost");
            t.stop();
            bgMusic.stop();
            new WavPlayer("lost.wav");
        }

        if(counter > 1500) {
            speed = 0;
            inputDisabled = true;
            //System.out.println("won");
            t.stop();
            bgMusic.stop();
            new WavPlayer("intro.wav");
        }

        // repaint every tick
        repaint();
    }

    public void renewObstacle(Obstacle obst) {
        obst.setY((int)(Math.random()*(-700)-100));
        obst.setX((int)(Math.random()*500)+100);
        obst.getObst().setBounds(obst.getX(), obst.getY(), 100, 100);
    }

    public int getSpeed() {
        return speed;
    }

    private class RotatedLabel extends JLabel {
        double angle;
        public RotatedLabel(String text, Icon image, int horizontalAlignment, double pAngle) {
            super(text, image, horizontalAlignment);
            angle = pAngle;
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D gx = (Graphics2D) g;
            gx.rotate(angle, getX() + getWidth()/2, getY() + getHeight()/2);
            super.paintComponent(g);
        }
        
        public void setAngle(double pAngle) {
            angle = pAngle;
        }
    }
}
