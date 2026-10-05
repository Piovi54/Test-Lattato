package ritmo;

/** Chi vuole essere avvisato a ogni segnale (ad esempio la GUI) implementa questa interfaccia. */
public interface AllenamentoListener {

    /**
     * @param numero numero progressivo del segnale (1, 2, 3, ...)
     * @param metri  metri percorsi fino a questo segnale
     */
    void segnaleEmesso(int numero, int metri);
}
