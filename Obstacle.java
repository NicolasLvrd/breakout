import java.io.IOException;

public abstract class Obstacle extends Objet{
    float accel;

    public Obstacle(int x, int y, String nomImage) throws IOException {
        super(x, y, nomImage);
    }

    public abstract boolean collision(Balle balle, double normeV, double x2, double y2);
}
