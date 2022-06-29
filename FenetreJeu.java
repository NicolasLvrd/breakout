import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class FenetreJeu extends JFrame implements ActionListener, MouseInputListener {
    // Attributs du temps
    private final int DELTA_T = 10;
    private final Dimension dimJeu = new Dimension(1000, 700);
    private Timer Chrono;
    private int temps;
    private int nbVie;
    private int score;
    private JLabel affichageVie;
    private JLabel affichageScore;
    private LinkedList<Obstacle> obstacles;
    private LinkedList<Bonus> bonus;
    private Plateforme plateforme;
    private Balle balle;
    private PanelAffichage panelAffichage;
    private boolean lancer;
    private int nbBrique;
    private Niveau niveau;

    public FenetreJeu(Niveau niveau) throws IOException {
		this.niveau = niveau;
        this.obstacles = (LinkedList<Obstacle>) niveau.obstacles.clone();
        bonus = new LinkedList<Bonus>();
        plateforme = new Plateforme(dimJeu.width/2, 600);
        balle = new Balle(plateforme.x, plateforme.y - Balle.rayon);
        lancer = true;
        nbVie = 3;
        temps = 0;
        nbBrique = 0;
        for (Obstacle a : obstacles) {
            if (a instanceof Brique) {
                nbBrique++;
            }
        }

        // Paramètres de la fenêtre
        setResizable(false);
        setTitle("Flipper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout((new BoxLayout(getContentPane(), BoxLayout.Y_AXIS))); // les différents éléments de la fenêtre sont empilés

        // Création du chronomètre
        Chrono = new Timer(DELTA_T,this);
        Chrono.start();

        // Interception des mouvements et clicks souris
        addMouseMotionListener(this);
        addMouseListener(this);

        // Panel contenant le score et le nombre de vies
        JPanel panelStatique = new PanelStatique(dimJeu.width,50);
        affichageScore = new JLabel("Score : " + score);
        affichageScore.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        affichageVie = new JLabel("Vies : " + nbVie);
        affichageVie.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        JLabel titre = new JLabel(niveau.nom, SwingConstants.CENTER);
        panelStatique.add(affichageScore, BorderLayout.LINE_START);
        panelStatique.add(affichageVie, BorderLayout.LINE_END);
        panelStatique.add(titre, BorderLayout.CENTER);

        // Panel affichant les éléments du jeu
        panelAffichage = new PanelAffichage(dimJeu, plateforme, balle, obstacles, bonus, null);

		add(panelStatique);
        add(panelAffichage);
        pack();

        // on récupère les dimensions de l'écran et on centre la fenêtre
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        this.setLocation(x,y);

		this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        temps += DELTA_T;

        // calcul de la nouvelle accélération et direction de la balle
        if (!lancer && Main.gravite) balle.calculAccel();

        // gestion des collisions entre la plateforme et les bords de l'écran
        if ((balle.x + balle.vX) >= dimJeu.width || (balle.x + balle.vX) <= 0){
			Audio.jouerSon("rebond.wav");
			balle.vX *= -1;
		}
        if ((balle.y + balle.vY) <= 0){
			Audio.jouerSon("rebond.wav");
			balle.vY *= -1;
		}

        // comme ces paramètres sont nécessaires à tous les calculs de collision, on les déclare ici pour gagner du temps de calcul
        double normeV = Math.hypot(balle.vX, balle.vY);
        double x2 = balle.x+balle.vX+balle.vX*Balle.rayon/normeV;
        double y2 = balle.y+balle.vY+balle.vY*Balle.rayon/normeV;

        // gestion des collisions entre la balle, les briques et les bumpers
        // on utilise un iterator pour éviter l'erreur ConcurrentModificationException
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            if (obstacle.collision(balle, normeV, x2, y2)) {
                if (obstacle instanceof Brique) {
					Audio.jouerSon("brique.wav");
                    Brique brique = (Brique) obstacle;
                    if (Main.random.nextInt(101) <= 20) {
                        try {
                            bonus.add(new Bonus(brique.x, brique.y));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    iterator.remove();
                    nbBrique--;
                    score += 50;
                } else if (obstacle instanceof Bumper) {
					Audio.jouerSon("bumper.wav");
                    score += 10;
                }
            }
        }

        // gestion des collisions entre la balle et la plateforme
        plateforme.collision(balle, normeV, x2, y2);

        // si la balle atteint le bord inférieur de l'écran, on perd une vie
        if ((balle.y) > dimJeu.height) {
			Audio.jouerSon("tombé.wav");
            balle.vY = 0;
            balle.vX = 0;
            balle.x = plateforme.x;
            balle.y = plateforme.y - Balle.rayon;
            lancer = true;
            nbVie -= 1;
        }

        // on gère les bonus
        // on utilise un iterator pour éviter l'erreur ConcurrentModificationException
        Iterator<Bonus> iterator2 = bonus.iterator();
        while (iterator2.hasNext()) {
            Bonus b = iterator2.next();
            b.updatePos();
            if (b.collision(plateforme)) {
                switch (b.type) {
                    case "vie+" :
						Audio.jouerSon("bonus.wav");
                        nbVie++;
                        break;
                    case "vie-" :
						Audio.jouerSon("malus.wav");
                        nbVie--;
                        break;
                    case "score+" :
						Audio.jouerSon("bonus.wav");
                        score += 200;
                        break;
                    case "plat+" :
						Audio.jouerSon("bonus.wav");
                        plateforme.tailleX += 50;
                        break;
                    case "plat-" :
						Audio.jouerSon("malus.wav");
                        plateforme.tailleX -= 50;
                        break;
                }
                b.isVivant = false;
                iterator2.remove();
            } else if (b.y - b.rayon >= dimJeu.height) {
                b.isVivant = false;
                iterator2.remove();
            }
        }

        // on met à jour la position de la balle
        balle.updatePos();

        // on met à jour les compteurs de vie et de score
        affichageVie.setText("Vies : " + nbVie);
        affichageScore.setText("Score : " + score);

        panelAffichage.repaint(plateforme, balle, obstacles, bonus, null);
        
        // si le joueur perd, on arrête le jeu
        if (nbVie <= 0){
			Audio.jouerSon("perdu.wav");
			if (score > niveau.record) { //	mise à jour du score
				niveau.record = score;
				Main.editerNiveau(niveau);
			}
            new FenetreResultat(score, "Perdu", niveau.record);
            setVisible(false);
            Chrono.stop();
            dispose();
        }
        
        // si le joueur gagne (il n'y a plus de brique), on arrête le jeu
        if (nbVie > 0 && nbBrique == 0) {
			Audio.jouerSon("gagné.wav");
			if (score > niveau.record) { //	mise à jour du score
				niveau.record = score;
				Main.editerNiveau(niveau);
			}
            new FenetreResultat(score, "Gagné", niveau.record);
            setVisible(false);
            Chrono.stop();
            dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (lancer) {
            final int vDepart;
            if (Main.gravite) vDepart = 20;
            else vDepart = 10;
            double thetaMin = 0.785; // pi/4
            double thetaMax = 2.356; // 3pi/4
            double theta = thetaMin + Main.random.nextDouble() * (thetaMax - thetaMin); // l'angle de lancer est aléatoire
            balle.vX = Math.cos(theta)*vDepart;
            balle.vY = -Math.sin(theta)*vDepart; // la balle doit partir vers le haut d'où le signe -
            lancer = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // on récupère la position de la souris par rapport au panel affichage
        Point point = MouseInfo.getPointerInfo().getLocation();
        Point point2 = panelAffichage.getLocationOnScreen();
        plateforme.x = (int) point.getX() - (int) point2.getX();

        // on vérifie que la plateforme ne sort pas de l'écran
        if (plateforme.x - plateforme.tailleX/2 < 0) plateforme.x = plateforme.tailleX/2;
        if (plateforme.x + plateforme.tailleX/2 > dimJeu.width) plateforme.x = dimJeu.width - plateforme.tailleX/2;

        // on change les coordonnées de la balle si elle n'a pas encore été lancée pour qu'elle suive la plateforme
        if (lancer) balle.x = plateforme.x;

        panelAffichage.repaint(plateforme, balle, obstacles, bonus, null);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}
