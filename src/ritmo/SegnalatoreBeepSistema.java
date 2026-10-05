package ritmo;

import java.awt.Toolkit;

/**
 * Soluzione di riserva: usa il "beep" di sistema.
 * Viene usata solo se l'audio normale non è disponibile.
 */
public class SegnalatoreBeepSistema implements Segnalatore {

    @Override
    public void emetti() {
        Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void chiudi() {
        // niente da liberare
    }
}
