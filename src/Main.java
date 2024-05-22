import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static tools.utility.*;

public class Main {
    public static void main(String[] args) {
        // Definizione del menu delle operazioni disponibili
        String[] operazioni = {"VODAFONE",
                "[1] Inserimento",
                "[2] Visualizzazione",
                "[3] Ricerca",
                "[4] Ricerca Numero telefonico",
                "[5] Modifica contatto",
                "[6] cancellazione",
                "[7] Carica saldo telefonico",
                "[8] Telefona",
                "[9] Ordina (Selection sort, insertion sort, bubble sort)",
                "[10] Salva file",
                "[11] Carica file",
                "[12] Fine"
        };

        boolean Sitel = true;  // Variabile di controllo per la lettura dei dati
        final int nMax = 3;    // Numero massimo di contatti gestiti
        int contrattiVenduti = 0; // Contatore dei contatti venduti
        int posizione = 0; // Variabile per tenere traccia della posizione dell'array
        Contatto[] gestore = new Contatto[nMax]; // Array di contatti
        boolean hidden = false;

        Scanner keyboard = new Scanner(System.in); // Scanner per la lettura degli input

        boolean fine = true; // Variabile di controllo del ciclo principale
        do {
            // Switch case per la gestione delle diverse opzioni del menu
            switch (menu(operazioni, keyboard)) {
                case 1:
                    // Inserimento di un nuovo contatto
                    if (contrattiVenduti < nMax) {
                        gestore[contrattiVenduti] = creaContatto(Sitel, keyboard, hidden);
                        contrattiVenduti++;
                    } else {
                        System.out.println("Non ci sono più contratti da vendere");
                        Wait(2);
                    }
                    break;
                case 2: {
                    // Visualizzazione dei contatti
                    if (contrattiVenduti != 0) {
                        visualizza(gestore, contrattiVenduti, hidden);
                        Wait(2);
                    } else {
                        System.out.println("Non ci sono contratti\n");
                        Wait(2);
                    }
                    break;
                }

                case 3: {
                    // Ricerca di un contatto
                    if (contrattiVenduti != 0) {
                        if(ricerca(gestore, creaContatto(Sitel, keyboard, hidden), contrattiVenduti, hidden) == 1) {
                            System.out.println("Il contatto è nascosto");
                            Wait(2);
                        } else if (ricerca(gestore, creaContatto(Sitel, keyboard, hidden), contrattiVenduti, hidden) == 2){
                            System.out.println("Il contatto non è nascosto");
                            Wait(2);
                        }
                        else{
                            System.out.println("Il contatto non esiste");
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;
                }

                case 4:
                    // Ricerca e visualizzazione del numero telefonico di un contatto
                    int ricerca = 0;
                    if (contrattiVenduti != 0) {
                        posizione = RicercaIndex(gestore, ricerca, contrattiVenduti, creaContatto(Sitel, keyboard, hidden));
                        if (posizione != -1) {
                            System.out.println(gestore[posizione].cognome + " " + gestore[posizione].nome + ": " + gestore[posizione].telefono);
                            Wait(2);
                        } else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;

                case 5:
                    // Modifica del numero telefonico di un contatto
                    Contatto numero = new Contatto();
                    int scelta;
                    if (contrattiVenduti != 0) {
                        posizione = RicercaIndex(gestore, ricerca, contrattiVenduti, creaContatto(Sitel, keyboard, hidden));
                        if (posizione != -1) {
                            System.out.println("Vuoi modificare il numero telefonico (si = 1 | no = 0): ");
                            scelta = keyboard.nextInt();
                            keyboard.nextLine();
                            if (scelta == 1) {
                                System.out.println("Modifica numero telefonico: ");
                                numero.telefono = keyboard.nextLine();
                                gestore[posizione].telefono = numero.telefono;
                            } else {
                                System.out.println("Numero telefonico non modificato");
                                Wait(2);
                            }
                        } else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;
                case 6:
                    // Cancellazione di un contatto
                    if (contrattiVenduti != 0) {
                        posizione = RicercaIndex(gestore, ricerca, contrattiVenduti, creaContatto(Sitel, keyboard, hidden));
                        if (posizione != -1) {
                            contrattiVenduti = cancellazione(gestore, posizione, contrattiVenduti);
                        } else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;

                case 7:
                    // Aggiunta di saldo telefonico a un contatto
                    AggiuntaSaldo(gestore, keyboard, contrattiVenduti, hidden);
                    break;
                case 8:
                    // Simulazione di una telefonata
                    Telefona(gestore, keyboard, contrattiVenduti, hidden);
                    break;
                case 9:
                    // Ordinamento dei contatti (da implementare)
                    break;
                case 10:
                    // Salvataggio dei contatti su file
                    try {
                        ScriviFile("archivio.csv", gestore, contrattiVenduti);
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                        break;
                    }
                    break;
                case 11:
                    try {
                        int contElementi=LeggiFile("archivio.csv", gestore);
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                        break;
                    }
                    contrattiVenduti++;
                    // Caricamento dei contatti da file (da implementare)
                    break;
                case 1234:
                    hidden = true;
                    menu(operazioni, keyboard);
                    break;

                default:
                    // Uscita dal programma
                    fine = false;
                    break;
            }
        } while (fine);
    }

    // Funzione per leggere i dati di un contatto
    private static Contatto creaContatto(boolean Sitel, Scanner keyboard, boolean hidden) {
        String[] tipoC = {"Telefono", "1]abitazione", "2]cellulare", "3]aziendale"};
        Contatto persona = new Contatto();
        System.out.println("\nInserisci il nome: ");
        persona.nome = keyboard.nextLine();
        System.out.println("\nInserisci il cognome: ");
        persona.cognome = keyboard.nextLine();

        if(hidden){
            persona.nascosto = true;
        }
        else {
            persona.nascosto = false;
        }

        if (Sitel) {
            System.out.println("\nInserisci il numero di telefono: ");
            persona.telefono = keyboard.nextLine();
            switch (menu(tipoC, keyboard)) {
                case 1 -> persona.tipo = tipoContratto.abitazione;
                case 2 -> persona.tipo = tipoContratto.cellulare;
                default -> persona.tipo = tipoContratto.aziendale;
            }
        }
        System.out.println("Inserire il saldo attuale");
        persona.saldo = keyboard.nextDouble();
        keyboard.nextLine();

        return persona;
    }

    // Funzione per cercare un contatto nell'array dei contatti
    private static int ricerca(Contatto[] gestore, Contatto contatto, int contrattiVenduti, boolean hidden) {
        int ricerca = 0;

        if(hidden){
            for (int i = 0; i < contrattiVenduti; i++) {
                if (contatto.nome.equals(gestore[i].nome) && contatto.cognome.equals(gestore[i].cognome) && contatto.nascosto) {
                    ricerca = 1; //nascosta
                }
            }
        }

        else{
            for (int i = 0; i < contrattiVenduti; i++) {
                if (contatto.nome.equals(gestore[i].nome) && contatto.cognome.equals(gestore[i].cognome) && !contatto.nascosto) {
                    ricerca = 2; //pubblica

                }
            }
        }
        return ricerca;
    }

    // Funzione per ottenere l'indice di un contatto nell'array
    private static int RicercaIndex(Contatto[] gestore, int ricerca, int contrattiVenduti, Contatto cercaPersona) {
        int indice = -1;

        if(ricerca == 1){
            for (int i = 0; i < contrattiVenduti; i++) {
                if (cercaPersona.nome.equals(gestore[i].nome) && cercaPersona.cognome.equals(gestore[i].cognome) && cercaPersona.equals(gestore[i].nascosto)) {
                    indice = i;
                    break;
                }
            }
        }
        else if (ricerca == 2){
            for (int i = 0; i < contrattiVenduti; i++) {
                if (cercaPersona.nome.equals(gestore[i].nome) && cercaPersona.cognome.equals(gestore[i].cognome) && cercaPersona.equals(!gestore[i].nascosto)) {
                    indice = i;
                    break;
                }
            }
        }
        else{
            return indice;
        }
        return indice;
    }

    // Funzione per visualizzare tutti i contatti
    private static void visualizza(Contatto[] gestore, int contrattiVenduti, boolean hidden) {
        if(hidden){
            for (int i = 0; i < contrattiVenduti; i++) {
                System.out.println(gestore[i].nascosto);
            }
        }
        else{
            for (int i = 0; i < contrattiVenduti; i++) {
                System.out.println(!gestore[i].nascosto);
            }
        }
    }


    // Funzione per cancellare un contatto dall'array
    public static int cancellazione(Contatto[] gestore, int posizione, int contrattiVenduti) {
        if (posizione != gestore.length - 1) {
            for (int i = posizione; i < contrattiVenduti - 1; i++) {
                gestore[i] = gestore[i + 1];
            }
        }
        contrattiVenduti--;
        return contrattiVenduti;
    }

    public static int cancellaNascosto(Contatto[] gestore, int posizione, int contrattiVenduti) {
        if (posizione != gestore.length - 1) {
            for (int i = posizione; i < contrattiVenduti - 1; i++) {
                gestore[i] = gestore[i + 1];
            }
        }
        contrattiVenduti--;
        return contrattiVenduti;
    }


    // Funzione per aggiungere saldo a un contatto
    public static void AggiuntaSaldo(Contatto[] gestore, Scanner keyboard, int contrattiVenduti, boolean hidden) {
        Contatto ricarica = new Contatto();
        int posizione;
        if (contrattiVenduti != 0) {
            posizione = RicercaIndex(gestore, creaContatto(false, keyboard, hidden), contrattiVenduti);
            if (posizione != -1) {
                System.out.println("Inserire la ricarica");
                ricarica.saldo = Integer.parseInt(keyboard.nextLine());
                gestore[posizione].saldo += ricarica.saldo;
            } else {
                System.out.println("Contatto non trovato");
                Wait(2);
            }
        } else {
            System.out.println("Non sono ancora presenti contratti venduti");
            Wait(2);
        }
    }

    // Funzione per simulare una telefonata
    public static void Telefona(Contatto[] gestore, Scanner keyboard, int contrattiVenduti, boolean hidden) {
        int posizione;
        if (contrattiVenduti != 0) {
            posizione = RicercaIndex(gestore, creaContatto(false, keyboard, hidden), contrattiVenduti);
            if (posizione != -1) {
                System.out.println("Telefonata in corso");
                Wait(1);
                System.out.println(".");
                Wait(1);
                System.out.println(".");
                Wait(1);
                System.out.println(".");
                Wait(1);
                System.out.println("Telefonata terminata");
                Wait(2);
                gestore[posizione].saldo--;
            } else {
                System.out.println("Contatto non trovato");
                Wait(2);
            }
        } else {
            System.out.println("Non sono ancora presenti contratti venduti");
            Wait(2);
        }
    }

    // Funzione per scrivere i contatti su file
    public static void ScriviFile(String fileName, Contatto[] gestore, int contrattiVenduti) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (int i = 0; i < contrattiVenduti; i++) {
            writer.write(gestore[i].nome + ", " + gestore[i].cognome + ", " + gestore[i].telefono + ", " + gestore[i].tipo + ", " + gestore[i].saldo + "\n");
        }
        writer.flush();
        writer.close();

    }
    private static int LeggiFile(String fileName, Contatto[] gestore)throws IOException
    {
        FileReader reader = new FileReader(fileName);
        Scanner input= new Scanner(reader);
        int nContatti=0;
        String lineIn;
        String []vetAttributi;
        int contaElementi=0;
        while(input.hasNextLine() && (contaElementi< gestore.length)){
            lineIn=input.nextLine();
            vetAttributi=lineIn.split(",");
            Contatto persona = new Contatto();
            persona.nome=vetAttributi[0];
            persona.cognome=vetAttributi[1];
            persona.telefono=vetAttributi[2];
            switch (vetAttributi[3]){
                case "abitazione":
                    persona.tipo=tipoContratto.abitazione;
                    break;
                case "cellulare":
                    persona.tipo=tipoContratto.cellulare;
                    break;
                case "aziendale":
                    persona.tipo=tipoContratto.aziendale;
                    break;
            }
            //persona.tipo= tipoContratto.valueOf(vetAttributi[3]);
            persona.saldo=Double.parseDouble(vetAttributi[4]);
            gestore[contaElementi++]=persona;

        }
        reader.close();
        return nContatti;

    }
}