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
            
            setVolumeInternal(this.volume); // Apply current volume setting

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

    private void setVolumeInternal(float vol) {
        if (currentClip != null && currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            if (vol < 0.0f) vol = 0.0f;
            if (vol > 1.0f) vol = 1.0f;
            
            // Convert linear volume (0-1) to dB. 0 means silence (-80dB or min), 1 means 0dB (original).
            float min = gainControl.getMinimum(); // e.g., -80.0f
            float max = gainControl.getMaximum(); // e.g., 6.0206f
            
            float dB;
            if (vol == 0.0f) {
                dB = min; // Mute
            } else {
                // A common formula: dB = 20 * log10(volume)
                // This maps 1.0 to 0dB. Values < 1.0 become negative dB.
                dB = (float) (Math.log10(vol) * 20.0);
            }
            
            // Clamp to the control's actual min/max range
            if (dB < min) dB = min;
            if (dB > max) dB = max;
            
            gainControl.setValue(dB);
        }
    }

    public void setVolume(float volume) { 
        this.volume = volume;
        if (currentClip != null) {
            setVolumeInternal(this.volume);
        }
    }
    
    public float getVolume() {
        return this.volume;
    }
}
