package ritmo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/** Interfaccia grafica: l'utente inserisce il passo e avvia/ferma i segnali. */
public class FinestraPrincipale extends JFrame implements AllenamentoListener {

    private static final long serialVersionUID = 1L;

    private final JTextField campoPasso = new JTextField("4:20", 6);
    private final JButton pulsante = new JButton("Avvia");
    private final JLabel etichettaInfo = new JLabel("Inserisci il passo e premi Avvia", SwingConstants.CENTER);
    private final JLabel etichettaContatore = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel etichettaErrore = new JLabel(" ", SwingConstants.CENTER);

    private final Segnalatore segnalatore;
    private Allenamento allenamento;

    public FinestraPrincipale() {
        super("Segnale ogni 25 m");
        this.segnalatore = creaSegnalatore();
        costruisciInterfaccia();
    }

    /** Prova il suono "vero"; se l'audio non è disponibile usa il beep di sistema. */
    private static Segnalatore creaSegnalatore() {
        try {
            return new SegnalatoreSinusoidale(1000, 150);
        } catch (Exception e) {
            return new SegnalatoreBeepSistema();
        }
    }

    private void costruisciInterfaccia() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chiudiApplicazione();
            }
        });

        JLabel titolo = new JLabel("Passo di corsa (min:sec al km)", SwingConstants.CENTER);
        titolo.setFont(titolo.getFont().deriveFont(Font.BOLD, 14f));

        campoPasso.setFont(campoPasso.getFont().deriveFont(Font.BOLD, 28f));
        campoPasso.setHorizontalAlignment(SwingConstants.CENTER);

        pulsante.setFont(pulsante.getFont().deriveFont(Font.BOLD, 18f));
        pulsante.addActionListener(e -> premutoPulsante());
        campoPasso.addActionListener(e -> premutoPulsante()); // invio da tastiera

        etichettaContatore.setFont(etichettaContatore.getFont().deriveFont(Font.BOLD, 16f));
        etichettaErrore.setForeground(Color.RED);

        JPanel centro = new JPanel(new GridLayout(0, 1, 5, 8));
        centro.add(titolo);
        centro.add(campoPasso);
        centro.add(pulsante);
        centro.add(etichettaInfo);
        centro.add(etichettaContatore);
        centro.add(etichettaErrore);
        centro.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        add(centro, BorderLayout.CENTER);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void premutoPulsante() {
        if (allenamento != null && allenamento.isInEsecuzione()) {
            fermaAllenamento();
        } else {
            avviaAllenamento();
        }
    }

    private void avviaAllenamento() {
        try {
            Passo passo = Passo.daTesto(campoPasso.getText());
            allenamento = new Allenamento(passo, segnalatore, this);
            allenamento.avvia();

            etichettaErrore.setText(" ");
            etichettaInfo.setText(String.format(Locale.ITALY,
                    "Passo %s: un suono ogni %.2f s", passo, passo.getIntervalloSecondi()));
            etichettaContatore.setText("Segnali: 0 - Distanza: 0 m");
            campoPasso.setEnabled(false);
            pulsante.setText("Ferma");
        } catch (IllegalArgumentException e) {
            etichettaErrore.setText(e.getMessage());
        }
    }

    private void fermaAllenamento() {
        allenamento.ferma();
        campoPasso.setEnabled(true);
        pulsante.setText("Avvia");
        etichettaInfo.setText("Fermato. Premi Avvia per ripartire");
    }

    /** Chiamato dal thread del timer: la GUI va aggiornata nel thread di Swing. */
    @Override
    public void segnaleEmesso(int numero, int metri) {
        SwingUtilities.invokeLater(() ->
                etichettaContatore.setText("Segnali: " + numero + " - Distanza: " + metri + " m"));
    }

    private void chiudiApplicazione() {
        if (allenamento != null) {
            allenamento.ferma();
        }
        segnalatore.chiudi();
        dispose();
        System.exit(0);
    }
}
