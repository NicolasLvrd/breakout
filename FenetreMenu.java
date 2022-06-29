import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;

public class FenetreMenu extends JFrame implements ActionListener, ListSelectionListener {
    private JButton jeu;
    private JButton editeur;
    private JButton charger;
    private JButton gravite;
    private JList<Niveau> choixNiveau;

    public FenetreMenu() {
        File f = new File("niveaux_personnalises");
        Niveau[] niveaux = new Niveau[f.list().length];
        for (int i = 0; i < f.list().length; i++) {
            niveaux[i] = Main.lireNiveau("niveaux_personnalises/" + f.list()[i]);
        }

        int longueur = 850;
        int hauteur;
        int hauteurChoixNiveau;
        if (f.list().length == 0) {
            hauteurChoixNiveau = 0;
            hauteur = 500;
        } else if (f.list().length <= 7) {
            hauteurChoixNiveau = 2 + f.list().length * 19;
            hauteur = 595 + hauteurChoixNiveau;
        } else {
            hauteurChoixNiveau = 7 * 19;
            hauteur = 595 + hauteurChoixNiveau;
        }

        // Paramètres de la fenêtre
        setResizable(false);
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(longueur, hauteur);

        // Création des éléments
        JLabel fond = new JLabel(new ImageIcon("Ressources/Images/fond_menu.jpg"));
        fond.setBounds(0, 0, longueur, hauteur);

        JLabel nom = new JLabel(" Casse-brique - Flipper ", SwingConstants.CENTER);
        nom.setFont(new java.awt.Font("Algerian", Font.BOLD, 40));
        nom.setSize(530, 50);
        nom.setLocation((longueur - nom.getWidth()) / 2, 75);
        nom.setOpaque(true);
        nom.setBackground(Color.white);

        jeu = new JButton("Jouer");
        jeu.setFont(new java.awt.Font("Algerian", Font.BOLD, 50));
        jeu.setSize(300, 50);
        jeu.setLocation((longueur - jeu.getWidth()) / 2, 175);
        jeu.setBackground(new Color(240, 178, 122));
        jeu.addActionListener(this);

        gravite = new JButton();
        gravite.setFont(new java.awt.Font("Algerian", Font.BOLD, 20));
        gravite.setSize(300, 50);
        gravite.setLocation((longueur - gravite.getWidth()) / 2, 250);
        gravite.setBackground(new Color(240, 178, 122));
        gravite.addActionListener(this);
        if (Main.gravite) {
            gravite.setText("Désactiver la gravité");
        } else {
            gravite.setText("Activer la gravité");
        }

        editeur = new JButton("Créer un niveau");
        editeur.setFont(new java.awt.Font("Algerian", Font.BOLD, 30));
        editeur.setSize(325, 50);
        editeur.setLocation((longueur - editeur.getWidth()) / 2, 385);
        editeur.setBackground(new Color(240, 178, 122));
        editeur.addActionListener(this);

        choixNiveau = new JList<Niveau>(niveaux);
        choixNiveau.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        choixNiveau.addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(choixNiveau);
        scrollPane.setSize(275, hauteurChoixNiveau);
        scrollPane.setLocation((longueur - scrollPane.getWidth()) / 2, 460);

        charger = new JButton("Charger le niveau");
        charger.setFont(new java.awt.Font("Algerian", Font.BOLD, 30));
        charger.setSize(350, 50);
        charger.setLocation((longueur - charger.getWidth()) / 2, 460 + scrollPane.getHeight() + 25);
        charger.setBackground(new Color(240, 178, 122));
        charger.addActionListener(this);
        charger.setEnabled(false);

        // Ajout des éléments à la fenêtre Menu
        add(nom);
        add(jeu);
        add(gravite);
        add(editeur);
        if (f.list().length > 0) {
            add(scrollPane);
            add(charger);
        }
        add(fond);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        this.setLocation(x, y);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jeu) {
            try {
                Audio.jouerSon("lancement.wav");
                Main.lancerJeu(Main.niveauActuel);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            setVisible(false);
            dispose();
        } else if (e.getSource() == editeur) {
            try {
                Audio.jouerSon("lancement.wav");
                new FenetreEditeur();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            setVisible(false);
            dispose();
        } else if (e.getSource() == charger) {
            Main.niveauActuel = choixNiveau.getSelectedValue();
            Main.niveauPersonnalise = true;
            try {
                Main.lancerJeu(Main.niveauActuel);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            setVisible(false);
            dispose();
        } else if (e.getSource() == gravite) {
            Main.gravite = !Main.gravite;
            if (Main.gravite) gravite.setText("Désactiver la gravité");
            else gravite.setText("Activer la gravité");
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            charger.setEnabled(choixNiveau.getSelectedIndex() != -1); // on active le bouton charger uniquement si un niveau est sélectionné
        }
    }
}

