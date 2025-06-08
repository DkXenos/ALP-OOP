import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class AudioManager {

    private static AudioManager instance;
    private Clip currentClip;
    private float volume = 0.7f; // Default volume (0.0 to 1.0)

    private AudioManager() {
        // Private constructor for Singleton pattern
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playMusic(String resourcePath, boolean loop) {
        stopMusic(); // Stop any currently playing music

        try {
            InputStream audioSrc = AudioManager.class.getResourceAsStream(resourcePath);
            if (audioSrc == null) {
                System.err.println("Audio resource not found: " + resourcePath);
                return;
            }
            
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            

            if (loop) {
                currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                currentClip.start();
            }
            // System.out.println("Playing music: " + resourcePath);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file: " + resourcePath + " - " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (Exception e) { // Catch generic IOException and others
            System.err.println("Error playing audio: " + resourcePath + " - " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (currentClip != null) {
            if (currentClip.isRunning()) {
                currentClip.stop();
            }
            currentClip.close();
            currentClip = null;
            // System.out.println("Music stopped.");
        }
    }

    

    public float getVolume() {
        return this.volume;
    }
}
