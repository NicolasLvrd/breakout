import java.io.IOException;

public class Brique extends Obstacle{
    final protected int tailleX = 85;
    final protected int tailleY = 40;

    public Brique(int x, int y, String nomImage) throws IOException {
        super(x, y, nomImage);
        accel = 0;
    }

    @Override
    public boolean collision(Balle balle, double normeV, double x2, double y2) {
        double a = balle.vY/balle.vX;
        double b = balle.y - a*balle.x;
        double yH = y-tailleY/2.;
        double yB = y+tailleY/2.;
        double xG = x-tailleX/2.;
        double xD = x+tailleX/2.;
        if (xG <= x2 && x2 <= xD && yH <= y2 && y2 <= yB) { // collision avec la brique
            if ((balle.x <= x2 && xG <= (yH-b)/a && (yH-b)/a <= xD && balle.x <= (yH-b)/a && (yH-b)/a <= x2) || (balle.x >= x2 && xG <= (yH-b)/a && (yH-b)/a <= xD && balle.x >= (yH-b)/a && (yH-b)/a >= x2) || (balle.x == x2 && balle.y < y2)) {
                // collision avec le haut de la brique
                balle.vY *= -1;
            }
            if ((balle.x <= x2 && xG <= (yB-b)/a && (yB-b)/a <= xD && balle.x <= (yB-b)/a && (yB-b)/a <= x2) || (balle.x >= x2 && xG <= (yB-b)/a && (yB-b)/a <= xD && balle.x >= (yB-b)/a && (yB-b)/a >= x2) || (balle.x == x2 && balle.y > y2)) {
                // collision avec le bas de la brique
                balle.vY *= -1;
            }
            if ((balle.y <= y2 && yH <= a*xG+b && a*xG+b <= yB && balle.y <= a*xG+b && a*xG+b <= y2) || (balle.y >= y2 && yH <= a*xG+b && a*xG+b <= yB && balle.y >= a*xG+b && a*xG+b >= y2)) {
                // collision avec la gauche de la brique
                balle.vX *= -1;
            }
            if ((balle.y <= y2 && yH <= a*xD+b && a*xD+b <= yB && balle.y <= a*xD+b && a*xD+b <= y2) || (balle.y >= y2 && yH <= a*xD+b && a*xD+b <= yB && balle.y >= a*xD+b && a*xD+b >= y2)) {
                // collision avec la droite de la brique
                balle.vX *= -1;
            }

            double h = Math.hypot(balle.vX, balle.vY);
            balle.vX = balle.vX * (h + accel)/h;
            balle.vY = balle.vY * (h + accel)/h;
            this.isVivant = false;
            return true;
        }
        return false;
    }
}
