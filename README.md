# Segnale ogni 25 m

Programma con interfaccia grafica che, dato un passo di corsa (es. `4:20` al km), emette un suono ogni 25 metri.

## Come si usa
1. Scarica `ritmo.jar` dalla sezione **Releases** del repository.
2. Serve Java 11 o superiore (https://adoptium.net).
3. Doppio clic su `ritmo.jar` oppure da terminale: `java -jar ritmo.jar`
4. Scrivi il passo nel formato `minuti:secondi` (es. `4:20`) e premi **Avvia**.

Intervallo tra i suoni = (secondi al km) / 40. Con 4:20/km: un suono ogni 6,5 s.

## Struttura (OOP)
- `Passo` – dato immutabile con validazione e calcolo dell'intervallo (incapsulamento)
- `Segnalatore` – interfaccia (astrazione), con `SegnalatoreSinusoidale` e `SegnalatoreBeepSistema` (polimorfismo)
- `Allenamento` – gestisce il timer senza accumulare ritardo
- `AllenamentoListener` – la GUI viene avvisata a ogni segnale
- `FinestraPrincipale` – interfaccia Swing
- `Main` – punto di ingresso
