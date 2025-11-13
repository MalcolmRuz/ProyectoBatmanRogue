package Sonidos;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Panky
 */
public class Sonido {

    public static void reproducir(String Transicion) {
        try {
            URL url = Sonido.class.getResource("/Sonidos/" + Transicion);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static Clip reproducirLoop(String Fondo) {
        try {
            URL url = Sonido.class.getResource("/Sonidos/" + Fondo);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }
}
