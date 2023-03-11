import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.lang.Thread;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.util.TimerTask;
import java.util.Date;
import java.util.Timer;

public class WebPageViewer {
    
    public WebPageViewer(String filename, String soundFile, int milliLength, String gameWindow) {
        SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("We Break Bad");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(800, 600);
                    frame.setLocationRelativeTo(null);

                    JEditorPane editorPane = new JEditorPane();
                    editorPane.setEditable(false);
                    editorPane.addHyperlinkListener(new HyperlinkListener() {
                            @Override
                            public void hyperlinkUpdate(HyperlinkEvent e) {
                                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                                    try {
                                        editorPane.setPage(e.getURL());
                                    } catch (IOException ex) {
                                        System.err.println("Error loading webpage: " + ex);
                                    }
                                }
                            }
                        });

                    try {
                        editorPane.setPage("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/vids/" + filename); // set the initial URL to load
                    } catch (IOException e) {
                        System.err.println("Error loading webpage: " + e);
                    }

                    WavPlayer sound = new WavPlayer(soundFile);

                    JScrollPane scrollPane = new JScrollPane(editorPane);
                    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
                    frame.setVisible(true);

                    // wait until video is finished
                    // source: https://stackoverflow.com/questions/11361332/how-to-call-a-method-on-specific-time-in-java
                    Date finishedDate = new Date(System.currentTimeMillis() + milliLength);
                    
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            sound.stop();
                            // create instance from string
                            // source: https://stackoverflow.com/questions/4767088/creating-an-instance-from-string-in-java
                            try {
                                Class c = Class.forName(gameWindow);
                                c.getDeclaredConstructor().newInstance();
                            } catch(Exception e) { e.printStackTrace(); }
                            frame.dispose();
                        }
                    }, finishedDate);
            });
    }
}