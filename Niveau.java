import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Niveau implements Serializable {
    protected String nom;
    protected LinkedList<Obstacle> obstacles;
    protected Date dateCreation;
    protected int nbNiveau;
    protected int record; 
    protected String cheminAcces;
    private static final long serialVersionUID = -7870855644887116271L; // généré aléatoirement

    public Niveau(String nom, LinkedList<Obstacle> obstacles, int nbNiveau, String cheminAcces) {
        this.nom = nom;
        this.obstacles = obstacles;
        this.nbNiveau = nbNiveau;
        this.record = 0;
        this.cheminAcces = cheminAcces;
        dateCreation = new Date();
    }
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String tempNom;
        if (nom.length() > 20) {
            tempNom = nom.substring(0, 17);
            tempNom += "...";
        } else tempNom = nom;

        return tempNom + " - Créé le " + simpleDateFormat.format(dateCreation);
    }
}
