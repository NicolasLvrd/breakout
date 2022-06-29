import java.io.IOException;

public class Bonus extends Objet{
    private final String[] listeBonus = {"plat+", "plat-", "vie+", "vie-", "score+"};
    private final int vitesse = 2;
    protected final int rayon = 25;
    protected String type;

    public Bonus(int x, int y) throws IOException {
        super(x, y, null);
        type = listeBonus[Main.random.nextInt(listeBonus.length)];
        setImage(type+".png");
    }

    public void updatePos() {
        y += vitesse;
    }

    public boolean collision(Plateforme plateforme) {
        return y + rayon >= plateforme.y - plateforme.tailleY/2 && y - rayon <= plateforme.y + plateforme.tailleY/2
                && x - rayon <= plateforme.x + plateforme.tailleX/2 && x + rayon >= plateforme.x - plateforme.tailleX/2;
    }
}
