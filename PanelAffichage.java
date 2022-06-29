import java.awt.*;
import javax.swing.*;
import java.util.LinkedList;

public class PanelAffichage extends JPanel {
    private Plateforme plateforme;
    private Balle balle;
    private Obstacle selectedObstacle;
    private LinkedList<Obstacle> obstacles;
    private LinkedList<Bonus> bonus;
    private final Color bgColor = new Color(173,216,230);
    private final Color plateformColor = Color.BLACK;
    private final Color ballColor = Color.RED;

    public PanelAffichage(Dimension dimension, Plateforme plateforme, Balle balle, LinkedList<Obstacle> obstacles, LinkedList<Bonus> bonus, Obstacle selectedObstacle) {
        this.plateforme = plateforme;
        this.balle = balle;
        this.obstacles = obstacles;
        this.bonus = bonus;
        this.selectedObstacle = selectedObstacle; // sert uniquement pour l'éditeur de niveaux

        setPreferredSize(dimension);
        setBackground(bgColor);
	}

	@Override
	public void paint(Graphics g) {
        // on efface l'écran pour redessiner par dessus
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // dessin de la plateforme
        if (plateforme != null) {
            g.setColor(plateformColor);
            g.fillRect(plateforme.x - plateforme.tailleX / 2, plateforme.y, plateforme.tailleX, plateforme.tailleY);
        }

        // dessin de la balle
        if (balle != null) {
            g.setColor(ballColor);
            g.fillOval(balle.x - Balle.rayon, balle.y - Balle.rayon, Balle.rayon *2, Balle.rayon *2);
        }

        // dessin des obstacles
        for(Obstacle obstacle:obstacles) {
            if (obstacle instanceof Brique) {
                Brique brique = (Brique) obstacle;
                g.drawImage(brique.image, brique.x - brique.tailleX/2, brique.y - brique.tailleY/2, null);
            } else if (obstacle instanceof Bumper) {
                Bumper bumper = (Bumper) obstacle;
                g.drawImage(bumper.image, bumper.x - bumper.rayon, bumper.y - bumper.rayon, null);
            }
        }

        if (selectedObstacle != null) {
            if (selectedObstacle instanceof Brique) {
                Brique brique = (Brique) selectedObstacle;
                g.drawImage(brique.image, brique.x - brique.tailleX/2, brique.y - brique.tailleY/2, null);
            } else if (selectedObstacle instanceof Bumper) {
                Bumper bumper = (Bumper) selectedObstacle;
                g.drawImage(bumper.image, bumper.x - bumper.rayon, bumper.y - bumper.rayon, null);
            }
        }

        // dessin des bonus
        for (Bonus b:bonus) {
            g.drawImage(b.image, b.x - b.rayon, b.y - b.rayon, null);
        }
    }

    public void repaint(Plateforme plateforme, Balle balle, LinkedList<Obstacle> obstacles, LinkedList<Bonus> bonus, Obstacle selectedObstacle) {
        this.plateforme = plateforme;
        this.balle = balle;
        this.obstacles = obstacles;
        this.bonus = bonus;
        this.selectedObstacle = selectedObstacle; // sert uniquement pour l'éditeur de niveaux
        repaint();
    }
 }