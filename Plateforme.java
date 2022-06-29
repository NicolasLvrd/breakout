import java.io.IOException;

public class Plateforme extends Obstacle{
    protected int tailleX;
    protected int tailleY;

    public Plateforme(int x, int y) throws IOException {
        super(x, y, null);
        tailleX = 200;
        tailleY = 14;
    }

    @Override
    public boolean collision(Balle balle, double normeV, double x2, double y2) {
        double a = balle.vY/balle.vX;
        double b = balle.y - a*balle.x;
        double xG = x-tailleX/2.;
        double xD = x+tailleX/2.;
        double yH = y-tailleY/2.;

        if ((balle.x <= x2 && xG <= (yH-b)/a && (yH-b)/a <= xD && balle.x <= (yH-b)/a && (yH-b)/a <= x2) || (balle.x >= x2 && xG <= (yH-b)/a && (yH-b)/a <= xD && balle.x >= (yH-b)/a && (yH-b)/a >= x2) || (balle.x == x2 && balle.y < y2)) {
            if (Main.gravite) accel = 3.5f;
            else accel = 0;

            Audio.jouerSon("rebond.wav");
            // l'angle de renvoi dépend de la position sur la plateforme
            double theta_min = 0.393; // pi/8
            double theta_max = 2.749; // 7pi/8
            double theta = theta_min + ((theta_max-theta_min)/tailleX)*((yH-b)/a-x+tailleX/2.);
            balle.vX = -Math.cos(theta)*(normeV+accel);
            balle.vY = -Math.sin(theta)*(normeV+accel);
        }
        return false;
    }
}
