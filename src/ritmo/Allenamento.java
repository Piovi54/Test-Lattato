package ritmo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Gestisce la sessione: ogni 25 m (calcolati dal passo) chiede al Segnalatore di suonare.
 * Usa scheduleAtFixedRate, quindi il ritmo NON accumula ritardo nel tempo.
 */
public class Allenamento {

    private final Passo passo;
    private final Segnalatore segnalatore;
    private final AllenamentoListener listener;

    private ScheduledExecutorService esecutore;
    private int numeroSegnali;

    public Allenamento(Passo passo, Segnalatore segnalatore, AllenamentoListener listener) {
        if (passo == null || segnalatore == null) {
            throw new IllegalArgumentException("Passo e segnalatore sono obbligatori.");
        }
        this.passo = passo;
        this.segnalatore = segnalatore;
        this.listener = listener;
    }

    public Passo getPasso() {
        return passo;
    }

    public synchronized boolean isInEsecuzione() {
        return esecutore != null;
    }

    public synchronized void avvia() {
        if (esecutore != null) {
            return; // già avviato
        }
        numeroSegnali = 0;
        esecutore = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "timer-allenamento");
            t.setDaemon(true);
            return t;
        });
        long intervallo = passo.getIntervalloNanosecondi();
        // Il primo segnale arriva dopo i primi 25 m.
        esecutore.scheduleAtFixedRate(this::segnala, intervallo, intervallo, TimeUnit.NANOSECONDS);
    }

    public synchronized void ferma() {
        if (esecutore != null) {
            esecutore.shutdownNow();
            esecutore = null;
        }
    }

    private void segnala() {
        try {
            segnalatore.emetti();
            numeroSegnali++;
            if (listener != null) {
                listener.segnaleEmesso(numeroSegnali, numeroSegnali * Passo.METRI_PER_SEGNALE);
            }
        } catch (RuntimeException e) {
            // un errore non deve fermare i segnali successivi
            e.printStackTrace();
        }
    }
}
