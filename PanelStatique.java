import javax.swing.*;
import java.awt.*;

public class PanelStatique extends JPanel {
    public PanelStatique(int w, int h) {
        setPreferredSize(new Dimension(w, h));
        setLayout(new BorderLayout());

        setBackground(new Color(221,238,255));
        setBorder(BorderFactory.createMatteBorder(0, 1, 2, 0, Color.black));
    }
}
