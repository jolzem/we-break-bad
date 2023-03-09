import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class Obstacle here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Obstacle
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int speed;
    private ImageIcon img;
    private JLabel label;

    public Obstacle(int pSpeed) {
        speed = pSpeed;
        x = (int)(Math.random()*500);
        y = (int)(Math.random()*(-600));
        ImageIcon img = new ImageIcon((int)(Math.random()*3+1)+"-obst.png");
        label = new JLabel("", img, JLabel.CENTER);
    }
    
    public void stepY() {
        y += speed;
        if(y >= 600) {
            y = (int)(Math.random()*(-700)-100);
        }
    }
    
    public int getY() {
        return y;
    }
    
    public int getX() {
        return x;
    }
    
    public void setY(int pY) {
        y = pY;
    }
    
    public JLabel getObst() {
        return label;
    }
    
    public void setSpeed(int pSpeed) {
        speed = pSpeed;
    }
    
    public boolean touches(int pX, int pY) {
        if(
            x+100 < pX ||
            y+100 < pY ||
            pX+75 < x ||
            pY+155 < y
        ) return false;
        
        return true;
    }
}
