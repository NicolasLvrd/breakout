import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import javax.swing.ImageIcon;

    public class FenetreResultat extends JFrame implements ActionListener {
    JButton rejouer ;
    JButton continuer;
    JButton menu;

	public FenetreResultat (int score, String texte, int record) {
		// Paramètres de la fenêtre
		setResizable(false);
		setTitle("Résultat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Définition des composants
        JLabel message = new JLabel (texte, SwingConstants.CENTER);
        message.setFont(new java.awt.Font("Algerian",Font.BOLD,50));

        JLabel scoreFinal = new JLabel("Score : " + score, SwingConstants.CENTER);
        scoreFinal.setFont(new java.awt.Font("Algerian",Font.BOLD,30));
        
        JLabel recordNiveau = new JLabel("Record : " + record, SwingConstants.CENTER);
        recordNiveau.setFont(new java.awt.Font("Algerian",Font.BOLD,30));

        Dimension tailleBouton = new Dimension(300,50);

        continuer = new JButton("Niveau suivant");
        continuer.setFont(new java.awt.Font("Algerian",Font.BOLD,30));
        continuer.setBackground(new Color(240, 178, 122));
        continuer.addActionListener(this);
        continuer.setMaximumSize(tailleBouton);

        rejouer = new JButton ("Rejouer");
        rejouer.setFont(new java.awt.Font("Algerian",Font.BOLD,30));
        rejouer.setBackground(new Color(240, 178, 122));
        rejouer.addActionListener(this);
        rejouer.setMaximumSize(tailleBouton);

        menu = new JButton("Menu");
        menu.setFont(new java.awt.Font("Algerian",Font.BOLD,30));
        menu.setBackground(new Color(240, 178, 122));
        menu.addActionListener(this);
        menu.setMaximumSize(tailleBouton);

        // Panel conteneur
        JPanel conteneur = new JPanel();
        conteneur.setBorder(BorderFactory.createEmptyBorder(30, 60, 50, 60)); // on crée une marge autour du panel
        conteneur.setLayout(new BoxLayout(conteneur, BoxLayout.Y_AXIS));
        conteneur.setBackground(new Color(128,208,208));
        conteneur.add(message);
        conteneur.add(Box.createRigidArea(new Dimension(300, 20))); // on sépare les éléments par des composants invisibles
        conteneur.add(scoreFinal);
        conteneur.add(Box.createRigidArea(new Dimension(300, 20)));
        conteneur.add(recordNiveau);
        conteneur.add(Box.createRigidArea(new Dimension(300, 30)));
        // s'il reste un niveau à jouer, que le joueur a gagné et que le niveau n'est pas un niveau personnalisé, on ajoute le bouton continuer
        if (Main.niveauSuivant() != null && "Gagné".equals(texte) && !Main.niveauPersonnalise) {
            conteneur.add(continuer);
            conteneur.add(Box.createRigidArea(new Dimension(300, 10)));
        }
        conteneur.add(rejouer);
        conteneur.add(Box.createRigidArea(new Dimension(300, 10)));
        conteneur.add(menu);

        // on centre les éléments dans le conteneur
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreFinal.setAlignmentX(Component.CENTER_ALIGNMENT);
        recordNiveau.setAlignmentX(Component.CENTER_ALIGNMENT);
        rejouer.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        continuer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajout des composants à FenetreResultat
        add(conteneur);
        pack();

        // on récupère les dimensions de l'écran et on centre la fenêtre
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x,y);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == rejouer) {
            try {
                Main.lancerJeu(Main.niveauActuel);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            setVisible(false);
            dispose();
        } else if (e.getSource() == menu) {
            Main.niveauActuel = Main.lireNiveau("Ressources/Niveaux/1.level");
            setVisible(false);
            new FenetreMenu();
            dispose();
        } else if (e.getSource() == continuer) {
            Main.niveauActuel = Main.niveauSuivant();
            Main.numNiveau ++;
            setVisible(false);
            try {
                Main.lancerJeu(Main.niveauActuel);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            dispose();
        }
    }
}



