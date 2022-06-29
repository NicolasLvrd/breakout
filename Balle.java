import java.awt.image.BufferedImage;
import java.io.IOException;

public class Balle extends Objet{

protected double vX;
protected double vY;
protected static final int rayon = 10;

	public Balle(int x, int y) throws IOException{
		super(x, y, null);
	}
	
	public void calculAccel() {
		double ky = 0.004;
		double kx = 0.01;
		vY += 0.4 - ky * vY;
		vX -= kx * vX;
	}

	public void updatePos() {
		if (!Main.gravite) {
			if (Math.abs(vY) < 1) vY = Math.signum(vY); // pour éviter que la balle se coince
			if (Math.abs(vX) < 1) vX = Math.signum(vX);
		}
		x += vX;
		y += vY;
	}
}
