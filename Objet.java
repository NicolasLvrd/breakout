import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public abstract class Objet implements Serializable {
    protected int x;
    protected int y;
    protected boolean isVivant;
    protected String nomImage;
    protected transient BufferedImage image; // une BufferedImage n'est pas serializable, d'où le mot-clé transient

    public Objet(int x, int y, String nomImage) throws IOException {
        this.x = x;
        this.y = y;
        this.nomImage = nomImage;
        isVivant = true;
        setImage(nomImage);
    }

    protected void setImage(String nomImage) throws IOException {
        if (nomImage != null) {
            image = ImageIO.read(new File("Ressources/Images/" + nomImage));
            this.nomImage = nomImage;
        }
        else image = null;
    }

    // on redéfinit la méthode readObject pour pouvoir redéfinir l'image de l'Objet
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setImage(nomImage);
    }
}
