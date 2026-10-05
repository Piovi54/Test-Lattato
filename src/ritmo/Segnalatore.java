package ritmo;

/**
 * Astrazione di "qualcosa che emette un segnale sonoro".
 * Le classi concrete decidono COME viene prodotto il suono.
 */
public interface Segnalatore {

    /** Emette un singolo segnale sonoro. */
    void emetti();

    /** Libera le risorse audio. */
    void chiudi();
}
