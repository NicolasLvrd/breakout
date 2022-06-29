import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;


public class FenetreEditeur extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    private LinkedList<Obstacle> obstacles;
    private LinkedList<Bonus> bonus;
    private Timer Chrono;
    private PanelAffichage panelAffichage;
    private final Dimension dimJeu = new Dimension(1000, 700);
    private JLabel message;
    private JLabel erreur;
    private JButton valider;
    private JButton quitter;
    private JButton briqueJaune;
    private JButton briqueRouge;
    private JButton briqueBleue;
    private JButton briqueVerte;
    private JButton bumper;
    private Obstacle sObstacle; // obstacle sélectionné
    private boolean effacer;
    private JButton bEffacer;
    private Plateforme plateforme;

    public FenetreEditeur() throws IOException {
        obstacles = new LinkedList<Obstacle>();
        bonus = new LinkedList<Bonus>();
        effacer = false;
        plateforme = new Plateforme(dimJeu.width/2, 600);
        sObstacle = null;

        // Paramètres de la fenêtre
        setResizable(false);
        setTitle("Editeur de niveau");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // on intercepte les clicks et les mouvements souris
        addMouseListener(this);
        addMouseMotionListener(this);

        // Création du chronomètre
        Chrono = new Timer(10, this);
        Chrono.start();

        // Panel affichant les éléments du jeu
        panelAffichage = new PanelAffichage(dimJeu, plateforme, null, obstacles, bonus, sObstacle);

        // Panel permettant de sélectionner l'obstacle à placer
        JPanel panelObstacles = new JPanel();
        panelObstacles.setPreferredSize(new Dimension(300, 350));
        panelObstacles.setBackground(new Color(221,238,255));
        panelObstacles.setLayout(null);
        briqueBleue = new JButton();
        briqueBleue.setIcon(new ImageIcon("Ressources/Images/brique_bleue.png"));
        briqueBleue.setSize(new Dimension(85,40));
        briqueBleue.setLocation(40, 180);
        briqueJaune = new JButton();
        briqueJaune.setIcon(new ImageIcon("Ressources/Images/brique_jaune.png"));
        briqueJaune.setSize(new Dimension(85,40));
        briqueJaune.setLocation(175, 180);
        briqueRouge = new JButton();
        briqueRouge.setIcon(new ImageIcon("Ressources/Images/brique_rouge.png"));
        briqueRouge.setSize(new Dimension(85,40));
        briqueRouge.setLocation(40, 270);
        briqueVerte = new JButton();
        briqueVerte.setIcon(new ImageIcon("Ressources/Images/brique_verte.png"));
        briqueVerte.setSize(new Dimension(85,40));
        briqueVerte.setLocation(175, 270);
        bumper = new JButton();
        bumper.setIcon(new ImageIcon("Ressources/Images/bumper.png"));
        bumper.setSize(new Dimension(100, 100));
        bumper.setLocation(100, 40);
        bumper.setContentAreaFilled(false);
        briqueBleue.addActionListener(this);
        briqueVerte.addActionListener(this);
        briqueJaune.addActionListener(this);
        briqueRouge.addActionListener(this);
        bumper.addActionListener(this);
        panelObstacles.add(briqueBleue);
        panelObstacles.add(briqueJaune);
        panelObstacles.add(briqueRouge);
        panelObstacles.add(briqueVerte);
        panelObstacles.add(bumper);

        // Panel permettant de sauvegarder et quitter l'éditeur
        JPanel panelBoutons = new JPanel();
        panelBoutons.setPreferredSize(new Dimension(300, 350));
        panelBoutons.setBackground(new Color(221,238,255));
        valider = new JButton("Sauvegarder et quitter");
        valider.addActionListener(this);
        quitter = new JButton("Quitter");
        quitter.addActionListener(this);
        bEffacer = new JButton("Supprimer un obstacle");
        bEffacer.addActionListener(this);
        panelBoutons.add(bEffacer);
        panelBoutons.add(valider);
        panelBoutons.add(quitter);

        // Panel conteneur
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.add(panelObstacles);
        panelMenu.add(panelBoutons);

        // Panel servant à l'affichage de messages
        JPanel panelMessage = new JPanel();
        panelMessage.setLayout(new BorderLayout());
        panelMessage.setPreferredSize(new Dimension(1300, 30));
        panelMessage.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
        panelMessage.setBackground(new Color(221,238,255));
        message = new JLabel("Bienvenue dans l'éditeur de niveau !", SwingConstants.CENTER);
        message.setPreferredSize(new Dimension(1000, 18));
        erreur = new JLabel("", SwingConstants.CENTER);
        erreur.setPreferredSize(new Dimension(300, 18));
        erreur.setForeground(Color.red);
        panelMessage.add(message, BorderLayout.LINE_START);
        panelMessage.add(erreur, BorderLayout.LINE_END);


        add(panelAffichage, BorderLayout.LINE_START);
        add(panelMenu, BorderLayout.LINE_END);
        add(panelMessage, BorderLayout.PAGE_END);
        pack();

        // on récupère les dimensions de l'écran et on centre la fenêtre
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        this.setLocation(x,y);

        this.setVisible(true);
    }

    private void quitter() {
        Chrono.stop();
        setVisible(false);
        new FenetreMenu();
        dispose();
    }

    private void enregistrerNiveau() throws IOException {
        if (obstacles.size() == 0) {
            print("Il n'y a rien à enregistrer", true);
        } else {
            // on définit le nom du niveau
            String nom = JOptionPane.showInputDialog(this, "Veuillez nommer le niveau", "Nom du niveau", JOptionPane.PLAIN_MESSAGE);

            if (nom == null) {
                print("Le niveau n'a pas été enregistré", true);
            } else {
                File f = new File("niveaux_personnalises");

                // on crée un objet Niveau
                int nbNiveau = f.list().length;
                Niveau niveau = new Niveau(nom, obstacles, (nbNiveau + 1), "niveaux_personnalises/");

                // on l'enregistre dans un fichier dont le nom n'est pas le nom du niveau pour pouvoir avoir plusieurs niveaux avec le même nom
                try (FileOutputStream fos = new FileOutputStream("niveaux_personnalises/"+ (nbNiveau + 1) + ".level");
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(niveau);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                quitter();
            }
        }
    }

    private void print(String s, boolean isErreur) {
        if (isErreur) {
            erreur.setText(s);
        } else message.setText(s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Chrono) {
            panelAffichage.repaint(plateforme, null, obstacles, bonus, sObstacle);
        } else if (e.getSource() == valider) {
            try {
                enregistrerNiveau();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == quitter) {
            quitter();
        } else if (e.getSource() == bEffacer) {
            print("Sélectionné : Supprimer un obstacle", false);
            effacer = true;
            sObstacle = null;
        } else if (e.getSource() == briqueBleue) {
            effacer = false;
            try {
                sObstacle = new Brique(-1000, -1000, "brique_bleue.png");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            print("Sélectionné : Brique bleue", false);
        } else if (e.getSource() == briqueJaune) {
            effacer = false;
            try {
                sObstacle = new Brique(-1000, -1000, "brique_jaune.png");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            print("Sélectionné : Brique Jaune", false);
        } else if (e.getSource() == briqueRouge) {
            effacer = false;
            try {
                sObstacle = new Brique(-1000, -1000, "brique_rouge.png");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            print("Sélectionné : Brique rouge", false);
        } else if (e.getSource() == briqueVerte) {
            effacer = false;
            try {
                sObstacle = new Brique(-1000, -1000, "brique_verte.png");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            print("Sélectionné : Brique verte", false);
        } else if (e.getSource() == bumper) {
            effacer = false;
            try {
                sObstacle = new Bumper(-1000, -1000);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            print("Sélectionné : Bumper", false);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // on récupère la position de la souris par rapport au panel affichage
        Point point = MouseInfo.getPointerInfo().getLocation();
        Point point2 = panelAffichage.getLocationOnScreen();
        int x = (int) point.getX() - (int) point2.getX();
        int y = (int) point.getY() - (int) point2.getY();

        if (sObstacle != null) {
            if (sObstacle instanceof Brique) {
                Brique brique = (Brique) sObstacle;
                if (x >= brique.tailleX/2 && x <= dimJeu.width - brique.tailleX/2 && y >= brique.tailleY/2 && y <= dimJeu.height - brique.tailleY/2) {
                    sObstacle.x = x;
                    sObstacle.y = y;
                } else {
                    sObstacle.x = -1000;
                    sObstacle.y = -1000;
                }
            } else if (sObstacle instanceof Bumper) {
                Bumper bumper = (Bumper) sObstacle;
                if (x >= bumper.rayon && x <= dimJeu.width-bumper.rayon && y >= bumper.rayon && y <= dimJeu.height-bumper.rayon) {
                    sObstacle.x = x;
                    sObstacle.y = y;
                } else {
                    sObstacle.x = -1000;
                    sObstacle.y = -1000;
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // on récupère la position de la souris par rapport au panel affichage
        Point point = MouseInfo.getPointerInfo().getLocation();
        Point point2 = panelAffichage.getLocationOnScreen();
        int x = (int) point.getX() - (int) point2.getX();
        int y = (int) point.getY() - (int) point2.getY();

        if (effacer) {
            if (x >= 0 && x <= dimJeu.width && y >= 0 && y <= dimJeu.height) {
                Iterator<Obstacle> iterator = obstacles.iterator();
                while (iterator.hasNext()) {
                    Obstacle obstacle = iterator.next();
                    if (obstacle instanceof Brique) {
                        Brique b = (Brique) obstacle;
                        if (x >= b.x-b.tailleX/2 && x <= b.x+b.tailleX/2 && y >= b.y-b.tailleY/2 && y <= b.y + b.tailleY/2) {
                            iterator.remove();
                            print("", true);
                            break;
                        }
                    }
                    else if (obstacle instanceof Bumper) {
                        Bumper bumper = (Bumper) obstacle;
                        int hypothenuse = (int) Math.hypot(bumper.x-x, bumper.y-y);
                        if (hypothenuse <= bumper.rayon) {
                            iterator.remove();
                            print("", true);
                            break;
                        }
                    }
                }
            }
        } else if (sObstacle != null) {
            if (sObstacle.x >= 0 && sObstacle.x <= dimJeu.width && sObstacle.y >= 0 && sObstacle.y <= dimJeu.height) {
                boolean placementPossible = true;
                if (sObstacle instanceof Brique) {
                    Brique b = (Brique) sObstacle;

                    // on vérifie qu'aucun des quatre coins de la brique entre dans un autre obstacle
                    for(Obstacle o:obstacles) {
                        if (o instanceof Brique) {
                            Brique b2 = (Brique) o;

                            if (collisionBriqueBrique(b, b2)) {
                                print("Placement de l'obstacle impossible", true);
                                placementPossible = false;
                                break;
                            }
                        } else if (o instanceof Bumper) {
                            Bumper bumper = (Bumper) o;

                            if (collisionBriqueBumper(b, bumper)) {
                                print("Placement de l'obstacle impossible", true);
                                placementPossible = false;
                                break;
                            }
                        }
                    }
                    if (placementPossible) {
                        try {
                            obstacles.add(new Brique(b.x, b.y, b.nomImage));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        print("", true);
                    }
                } else if (sObstacle instanceof Bumper) {
                    Bumper bumper = (Bumper) sObstacle;
                    for (Obstacle o:obstacles) {
                        if (o instanceof Brique) {
                            Brique b = (Brique) o;

                            if (collisionBriqueBumper(b, bumper)) {
                                print("Placement de l'obstacle impossible", true);
                                placementPossible = false;
                                break;
                            }
                        } else if (o instanceof Bumper) {
                            Bumper b = (Bumper) o;
                            if (collisionBumperBumper(bumper, b)) {
                                print("Placement de l'obstacle impossible", true);
                                placementPossible = false;
                                break;
                            }
                        }
                    }
                    if (placementPossible) {
                        try {
                            obstacles.add(new Bumper(bumper.x, bumper.y));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        print("", true);
                    }
                }
            }
        }
    }

    private boolean collisionBriqueBrique(Brique b1, Brique b2) {
        int xD = b1.x+b1.tailleX/2;
        int xG = b1.x - b1.tailleX/2;
        int yH = b1.y - b1.tailleY/2;
        int yB = b1.y + b1.tailleY/2;
        int xD2 = b2.x + b2.tailleX / 2;
        int xG2 = b2.x - b2.tailleX / 2;
        int yH2 = b2.y - b2.tailleY / 2;
        int yB2 = b2.y + b2.tailleY / 2;

        return (xG >= xG2 && xG <= xD2 && yH >= yH2 && yH <= yB2) // coin bas-droit
                || (xD >= xG2 && xD <= xD2 && yH >= yH2 && yH <= yB2) // coin haut-gauche
                || (xG >= xG2 && xG <= xD2 && yB >= yH2 && yB <= yB2) // coin haut-droit
                || (xD >= xG2 && xD <= xD2 && yB >= yH2 && yB <= yB2); // coin bas-gauche
    }

    private boolean collisionBriqueBumper(Brique b, Bumper bumper) {
        int xD = b.x+b.tailleX/2;
        int xG = b.x - b.tailleX/2;
        int yH = b.y - b.tailleY/2;
        int yB = b.y + b.tailleY/2;
        int hypot1 = (int) Math.hypot(bumper.x-xG, bumper.y-yH);
        int hypot2 = (int) Math.hypot(bumper.x-xD, bumper.y-yH);
        int hypot3 = (int) Math.hypot(bumper.x-xG, bumper.y-yB);
        int hypot4 = (int) Math.hypot(bumper.x-xD, bumper.y-yB);

        return hypot1 <= bumper.rayon || hypot2 <= bumper.rayon || hypot3 <= bumper.rayon || hypot4 <= bumper.rayon;
    }

    private boolean collisionBumperBumper(Bumper b1, Bumper b2) {
        int hypot = (int) Math.hypot(b1.x-b2.x, b1.y-b2.y);
        return hypot <= b1.rayon+b2.rayon;
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
