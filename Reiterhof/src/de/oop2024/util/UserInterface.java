package de.oop2024.util;

import java.util.Arrays;
import java.util.Scanner;


public class UserInterface {

    /**
     * Wrapper für Konsoleneingaben.
     */
    public static final UserInterface in;

    static {
        in = new UserInterface();
        Runtime.getRuntime().addShutdownHook(new Thread((new Runnable() {
        	
            public void run() {
        	try {
                in.close();
            } catch (UserInterfaceException ignored) {}
            }
        })));
    }

    private final Scanner scanner;

    private UserInterface() {
        scanner = new Scanner(System.in);
    }

    /**
     * Schließt den InputStream {@code System.in}. Nach dem Schließen sind keine Konsoleneingaben mehr möglich.
     *
     * @throws UserInterfaceException falls {@code System.in} bereits geschlossen wurde.
     */
    public void close() {
        try {
            scanner.close();
        } catch (IllegalStateException e) {
            throw new UserInterfaceException("InputStream ist bereits geschlossen.");
        }
    }

    // request functions

    /**
     * Fordert den Nutzer auf {@code Enter} oder {@code Return} zu drücken.
     */
    public void requestUserPressReturn() {
        System.out.println(" -- ENTER/RETURN drücken --");
        readln();
    }

    /**
     * Fordert den Nutzer auf eine Ganzzahl einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestInt("Bitte Ganzzahl eingeben:");
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @return eingegebene Ganzzahl
     */
    public int requestInt(String message) {
        Integer answer;
        do {
            answer = parseInt(askQuestion(message));
        } while (answer == null);
        return answer;
    }

    /**
     * Fordert den Nutzer auf eine Ganzzahl (&gt;= {@code minimum}) einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestInt("Bitte Ganzzahl &gt;= 5 eingeben:", 5);
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @param minimum Minimalwert (inklusive)
     * @return eingegebene Ganzzahl
     */
    public int requestInt(String message, int minimum) {
        Integer answer;
        do {
            answer = parseInt(askQuestion(message), minimum);
        } while (answer == null);
        return answer;
    }

    /**
     * Fordert den Nutzer auf eine Ganzzahl (&gt;= {@code minimum} &amp; &lt;= {@code maximum}) einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestInt("Bitte Ganzzahl &gt;= 5 und &lt;= 10 eingeben:", 5, 10);
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @param minimum Minimalwert (inklusive)
     * @param maximum maximalwert (inklusive)
     * @return eingegebene Ganzzahl
     */
    public int requestInt(String message, int minimum, int maximum) {
        Integer answer;
        do {
            answer = parseInt(askQuestion(message), minimum, maximum);
        } while (answer == null);
        return answer;
    }

    /**
     * Fordert den Nutzer auf eine Fließkommazahl einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestDouble("Bitte Fließkommazahl eingeben:");
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @return eingegebene Fließkommazahl
     */
    public double requestDouble(String message) {
        Double answer;
        do {
            answer = parseDouble(askQuestion(message));
        } while (answer == null);
        return answer;
    }

    /**
     * Fordert den Nutzer auf eine Fließkommazahl (&gt;= {@code minimum}) einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestDouble("Bitte Fließkommazahl &gt;= 5 eingeben:", 5);
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @param minimum Minimalwert (inklusive)
     * @return eingegebene Fließkommazahl
     */
    public double requestDouble(String message, double minimum) {
        Double answer;
        do {
            answer = parseDouble(askQuestion(message), minimum);
        } while (answer == null);
        return answer;
    }

    /**
     * Fordert den Nutzer auf eine Fließkommazahl (&gt;= {@code minimum} &amp; &lt;= {@code maximum}) einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestDouble("Bitte Fließkommazahl &gt;= 5 und &lt;= 10 eingeben:", 5, 10);
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @param minimum Minimalwert (inklusive)
     * @param maximum maximalwert (inklusive)
     * @return eingegebene Fließkommazahl
     */
    public double requestDouble(String message, double minimum, double maximum) {
        Double answer;
        do {
            answer = parseDouble(askQuestion(message), minimum, maximum);
        } while (answer == null);
        return answer;
    }

    /**
     * Fordert den Nutzer auf einen String einzugeben.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestString("Bitte Spielernamen eingeben:");
     * </code>
     *
     * @param message anzuzeigende Nachricht mit Aufforderung
     * @return eingegebener String
     */
    public String requestString(String message) {
        String answer = "";
        do {
            answer = askQuestion(message);
            if (answer.isEmpty()) {
                System.out.println(" Der eingegebene String darf nicht leer sein.\n");
            }
        } while (answer.isEmpty());
        return answer;
    }

    /**
     * Fordert den Nutzer auf zwischen verschiedenen Optionen zu wählen.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestChoice("Wähle eine Variante", "Var-1", "Var-2");
     * </code>
     * <p>
     * Konsoleneingabe: Var-2 resultiert in Rückgabe: 1
     *
     * <p>
     * Sonderfall:
     * <p>
     * Wenn nur eine einzige Option übergeben wurde,
     * dann wird diese automatisch gewählt.
     *
     * @param message       anzuzeigende Nachricht mit Aufforderung
     * @param choiceOptions mögliche Optionen ({@code Array} oder {@code Sequenz von Argumenten})
     * @return int - Index der gewählten Option
     * @throws UserInterfaceException falls keine {@code choiceOptions} übergeben wurden.
     */
    public int requestChoice(String message, String... choiceOptions) {
        if (choiceOptions == null || choiceOptions.length == 0) {
            throw new UserInterfaceException("Keine Optionen zur Auswahl übergeben.");
        }
        if (choiceOptions.length == 1) {
            System.out.printf(" Nur eine Option wählbar.\n Wähle %s.\n", choiceOptions[0]);
            return 0;
        }
        String choiceString = Arrays.toString(choiceOptions).replaceAll("[\\[\\]]", "");
        int givenChoice = -1;
        do {
            String choice = askQuestion(String.format("%s: %s", message, choiceString));
            for (int i = 0; i < choiceOptions.length; i++) {
                if (choice.equalsIgnoreCase(choiceOptions[i])) {
                    givenChoice = i;
                    break;
                }
            }
            if (givenChoice == -1) {
                System.out.println(" Die Auswahl muss eine der Optionen sein.\n");
            }
        } while (givenChoice == -1);
        return givenChoice;
    }

    /**
     * Fordert den Nutzer auf zwischen verschiedenen Optionen zu wählen.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * requestChoice("Wähle eine Variante", "Var-1", "Var-2");
     * </code>
     * <p>
     * Konsoleneingabe: Var-2 resultiert in Rückgabe: "Var-2"
     *
     * <p>
     * Sonderfall:
     * <p>
     * Wenn nur eine einzige Option übergeben wurde,
     * dann wird diese automatisch gewählt.
     *
     * @param message       anzuzeigende Nachricht mit Aufforderung
     * @param choiceOptions mögliche Optionen ({@code Array} oder {@code Sequenz von Argumenten})
     * @return String - Name der gewählten Option
     * @throws UserInterfaceException falls keine {@code choiceOptions} übergeben wurden.
     */
    public String requestChoiceName(String message, String... choiceOptions) {
        if (choiceOptions == null || choiceOptions.length == 0) {
            throw new UserInterfaceException("Keine Optionen zur Auswahl übergeben.");
        }
        return choiceOptions[requestChoice(message, choiceOptions)];
    }

    // utility function

    /**
     * Fordert den Nutzer auf eine Konsoleneingabe als Antwort auf eine Ausgabe zu machen.
     * <p>
     * Beispielnutzung:
     * <p>
     * <code>
     * askQuestion("Wie ist dein Name?");
     * </code>
     *
     * @param question Nachricht für den Nutzer
     * @return String - Antwort vom Nutzer
     */
    public String askQuestion(String question) {
        System.out.printf(" %s%n ", question);
        return readln();
    }

    /**
     * Liest eine Eingabe von der Konsole.
     *
     * @return String - Eingabe vom Nutzer
     * @throws UserInterfaceException falls {@code System.in} bereits geschlossen wurde.
     */
    public String readln() {
        String content = null;
        try {
            if (scanner.hasNextLine()) {
                content = scanner.nextLine().trim();
            }
        } catch (IllegalStateException e) {
            throw new UserInterfaceException("InputStream ist bereits geschlossen.");
        }
        return content;
    }

    // Parsing functions

    private Integer parseInt(String string) {
        if (string == null) {
            return null;
        }
        int number;
        try {
            number = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            System.out.println(" Es muss eine Ganzzahl eingegeben werden!");
            return null;
        }
        return number;
    }

    private Integer parseInt(String string, int minimum) {
        Integer number = parseInt(string);
        if (number != null && number < minimum) {
            System.out.printf(" Die Zahl muss größer gleich \"%d\" sein!\n", minimum);
            return null;
        }
        return number;
    }

    private Integer parseInt(String string, int minimum, int maximum) {
        Integer number = parseInt(string, minimum);
        if (number != null && number > maximum) {
            System.out.printf(" Die Zahl muss kleiner gleich \"%d\" sein!\n", maximum);
            return null;
        }
        return number;
    }

    private Double parseDouble(String string) {
        if (string == null) {
            return null;
        }
        double number;
        try {
            number = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            System.out.println(" Es muss eine Fließkommazahl eingegeben werden (Englische Schreibweise)!");
            return null;
        }
        return number;
    }

    private Double parseDouble(String string, double minimum) {
        Double number = parseDouble(string);
        if (number != null && number < minimum) {
            System.out.printf(" Die Zahl muss größer gleich \"%.2f\" sein!\n", minimum);
            return null;
        }
        return number;
    }

    private Double parseDouble(String string, double minimum, double maximum) {
        Double number = parseDouble(string, minimum);
        if (number != null && number > maximum) {
            System.out.printf(" Die Zahl muss kleiner gleich \"%.2f\" sein!\n", maximum);
            return null;
        }
        return number;
    }

    /**
     * Spezialisierte Exception während der Verwendung vom {@link UserInterface}.
     * Erbt von {@link RuntimeException} und gehört somit zu den <i>unchecked</i> Exceptions.
     *
     * @author Nicolas Handke
     * @version 1.3
     * @since JDK1.8
     * @see UserInterface
     * @see RuntimeException
     */
    public static class UserInterfaceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * Erstellt eine neue UserInterfaceException mit der spezifizierten Nachricht.
         *
         * @param message die Fehlernachricht (welche für das spätere Auslesen mit der Methode {@link java.lang.Throwable#getMessage()} gespeichert wird).
         */
        public UserInterfaceException(String message) {
            super(message);
        }

    }
}

