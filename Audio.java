import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {
    public static void jouerSon(final String nomFichier){           
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Ressources/Sons/" + nomFichier)); // Il faut utiliser des .wav
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.out.println("Erreur dans JouerSon: " + e.getMessage() + " pour " + nomFichier);
                }
            }
        }).start();
    }
}

//	Pour lancer un son : Audio.jouerSon("Ressources/Sons/nom.wav");
