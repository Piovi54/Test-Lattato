package ritmo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 * Genera un breve tono sinusoidale e lo riproduce con le librerie audio standard di Java.
 * Il suono è identico su Windows, macOS e Linux.
 */
public class SegnalatoreSinusoidale implements Segnalatore {

    private static final float FREQUENZA_CAMPIONAMENTO = 44100f;

    private final Clip clip;

    public SegnalatoreSinusoidale(double frequenzaHz, int durataMs) throws LineUnavailableException {
        byte[] dati = generaTono(frequenzaHz, durataMs);
        AudioFormat formato = new AudioFormat(FREQUENZA_CAMPIONAMENTO, 16, 1, true, false);
        clip = AudioSystem.getClip();
        clip.open(formato, dati, 0, dati.length);
    }

    private static byte[] generaTono(double frequenzaHz, int durataMs) {
        int campioni = (int) (FREQUENZA_CAMPIONAMENTO * durataMs / 1000);
        int dissolvenza = (int) (FREQUENZA_CAMPIONAMENTO * 0.01); // 10 ms per evitare "click"
        byte[] buffer = new byte[campioni * 2];
        for (int i = 0; i < campioni; i++) {
            double ampiezza = 0.6;
            if (i < dissolvenza) {
                ampiezza *= (double) i / dissolvenza;
            } else if (i > campioni - dissolvenza) {
                ampiezza *= (double) (campioni - i) / dissolvenza;
            }
            double angolo = 2.0 * Math.PI * frequenzaHz * i / FREQUENZA_CAMPIONAMENTO;
            short valore = (short) (Math.sin(angolo) * Short.MAX_VALUE * ampiezza);
            buffer[2 * i] = (byte) (valore & 0xFF);          // byte meno significativo
            buffer[2 * i + 1] = (byte) ((valore >> 8) & 0xFF); // byte più significativo
        }
        return buffer;
    }

    @Override
    public void emetti() {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void chiudi() {
        clip.close();
    }
}
