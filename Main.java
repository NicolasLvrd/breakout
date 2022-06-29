import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Random;
import java.io.*;

public class Main {
    public static Random random = new Random();
    public static Niveau niveauActuel;
    public static boolean niveauPersonnalise;
    public static int numNiveau;
    public static boolean gravite;

    public static void main(String[] args) throws IOException {
        // on crée le répertoire niveaux_personnalises s'il n'existe pas encore
        File f = new File("niveaux_personnalises");
        f.mkdir();

        niveauPersonnalise = false;
        gravite = true;
        numNiveau = 1;
        niveauActuel = lireNiveau("Ressources/Niveaux/1.level");
        new FenetreMenu();
    }

    public static void lancerJeu(Niveau niveau) throws IOException {
        new FenetreJeu(niveau);
    }

    public static Niveau lireNiveau(String adresse) {
        try (FileInputStream fis = new FileInputStream(adresse);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Niveau) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Niveau niveauSuivant() {
        File f = new File("Ressources/Niveaux");
        if (f.list().length > numNiveau) {
            return lireNiveau("Ressources/Niveaux/" + (numNiveau+1) + ".level");
        }
        return null;
    }
    
    public static void editerNiveau(Niveau niveau) { // permet de faire la mise à jour du record
		try (FileOutputStream fos = new FileOutputStream(niveau.cheminAcces + niveau.nbNiveau + ".level"); //	le fichier avec le précédent record est automatiquement écrasé
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(niveau);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
