package ritmo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // se non riesce, resta l'aspetto standard di Java
        }
        SwingUtilities.invokeLater(() -> new FinestraPrincipale().setVisible(true));
    }
}
