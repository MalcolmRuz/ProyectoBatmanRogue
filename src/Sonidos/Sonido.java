package Sonidos;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Panky
 */
public class Sonido {

    private static Clip musicaLoop;


//    public static void reproducir(String Transicion) {
//        try {
//            URL url = Sonido.class.getResource("/Sonidos/" + Transicion);
//            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
//            Clip clip = AudioSystem.getClip();
//            clip.open(audio);
//            clip.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void reproducirLoop(String Fondo) {
        try {

            // Si ya hay una canción sonando, no volver a iniciarla
            if (musicaLoop != null && musicaLoop.isActive()) {
                return;
            }

            URL url = Sonido.class.getResource("/Sonidos/" + Fondo);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            musicaLoop = AudioSystem.getClip();
            musicaLoop.open(audio);
            musicaLoop.loop(Clip.LOOP_CONTINUOUSLY);
            musicaLoop.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detenerLoop() {
        if (musicaLoop != null) {
            musicaLoop.stop();
            musicaLoop.close();
            musicaLoop = null;
        }
    }
}
