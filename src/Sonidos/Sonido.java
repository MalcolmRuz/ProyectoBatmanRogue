
package Sonidos;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
/**
 *
 * @author Panky
 */
public class Sonido {
    public static void reproducir (String Transicion){
        try{
            URL url = Sonido.class.getResource("/Sonidos/" + Transicion);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
        }
    }
    
   //public static Clip reproducir
}
