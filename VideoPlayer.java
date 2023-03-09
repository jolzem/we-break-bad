import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VideoPlayer extends JFrame {

    private Player player;

    public VideoPlayer(String filename) {
        super("Video Player");

        try {
            MediaLocator mediaLocator = new MediaLocator(new File(filename).toURI().toURL());
            player = Manager.createPlayer(mediaLocator);
            player.addControllerListener(new PlayerControllerListener());
            player.realize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel videoPanel = new JPanel(new BorderLayout());
        Component videoComponent = player.getVisualComponent();
        if (videoComponent != null) {
            videoPanel.add(videoComponent, BorderLayout.CENTER);
        }
        add(videoPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setVisible(true);

        player.start();
    }

    public static void main(String[] args) {
        String filename = "video.mp4";
        new VideoPlayer(filename);
    }

    private class PlayerControllerListener extends javax.media.ControllerAdapter {
        @Override
        public void realizeComplete(javax.media.RealizeCompleteEvent event) {
            Component controlComponent = player.getControlPanelComponent();
            if (controlComponent != null) {
                JPanel controlPanel = new JPanel(new BorderLayout());
                controlPanel.add(controlComponent, BorderLayout.CENTER);
                add(controlPanel, BorderLayout.SOUTH);
            }
        }
    }
}