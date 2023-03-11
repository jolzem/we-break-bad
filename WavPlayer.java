import javax.media.*;
import java.net.*;

public class WavPlayer {
    private Player player;
    
    public WavPlayer(String filename) {
        try {
            // create a URL for the wav file
            URL url = new URL("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/sounds/" + filename);

            // create a player for the wav file
            player = Manager.createPlayer(url);

            // start playing the wav file
            player.start();
        } catch (Exception e) {}
    }
    
    public void stop() {
        player.close();
    }
}