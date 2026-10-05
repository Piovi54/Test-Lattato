package ritmo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rappresenta un passo di corsa espresso in minuti e secondi al km (es. 4:20/km).
 * La classe è immutabile: una volta creata non può essere modificata.
 */
public class Passo {

    /** Ogni quanti metri deve suonare il segnale. */
    public static final int METRI_PER_SEGNALE = 25;

    private static final Pattern FORMATO = Pattern.compile("^\\s*(\\d{1,2})\\s*:\\s*(\\d{2})\\s*$");

    private final int minuti;
    private final int secondi;

    public Passo(int minuti, int secondi) {
        if (minuti < 1 || minuti > 59) {
            throw new IllegalArgumentException("I minuti devono essere tra 1 e 59.");
        }
        if (secondi < 0 || secondi > 59) {
            throw new IllegalArgumentException("I secondi devono essere tra 0 e 59.");
        }
        this.minuti = minuti;
        this.secondi = secondi;
    }

    /** Crea un Passo da un testo nel formato "m:ss", ad esempio "4:20". */
    public static Passo daTesto(String testo) {
        Matcher m = FORMATO.matcher(testo == null ? "" : testo);
        if (!m.matches()) {
            throw new IllegalArgumentException("Formato non valido. Scrivi il passo come minuti:secondi, ad esempio 4:20");
        }
        return new Passo(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
    }

    public int getMinuti() {
        return minuti;
    }

    public int getSecondi() {
        return secondi;
    }

    /** Secondi necessari per percorrere 1 km. */
    public int getSecondiAlKm() {
        return minuti * 60 + secondi;
    }

    /** Tempo tra un segnale e il successivo, in nanosecondi (valore esatto, senza arrotondamenti). */
    public long getIntervalloNanosecondi() {
        // secondiAlKm / 1000 m * 25 m = secondi per 25 m -> in nanosecondi: * 1_000_000_000
        return getSecondiAlKm() * METRI_PER_SEGNALE * 1_000_000L;
    }

    /** Tempo tra un segnale e il successivo, in secondi. */
    public double getIntervalloSecondi() {
        return getIntervalloNanosecondi() / 1_000_000_000.0;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d/km", minuti, secondi);
    }
}
