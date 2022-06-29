import java.io.*;

public class Bumper extends Obstacle {
	protected final int rayon = 50;

	public Bumper(int x, int y) throws IOException {
		super(x, y, "bumper.png");
	}

	@Override
	public boolean collision(Balle balle, double normeV, double x2, double y2) {
		double d = Math.sqrt(Math.pow(x2-x, 2)+Math.pow(y2-y, 2));

		if (d <= rayon) { // collision avec le bumper
			if (Main.gravite) accel = 4;
			else accel = 0;

			// on renvoie la balle selon la normale au bumper et on l'accélère
			balle.vX = (x2-x)*(normeV+accel)/d;
			balle.vY = (y2-y)*(normeV+accel)/d;
			return true;
		}
		return false;
	}
}
