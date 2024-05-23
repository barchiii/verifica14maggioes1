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
                "[4] Modifica contatto",
                "[5] cancellazione",
                "[6] Telefona",
                "[7] Salva file",
                "[8] Carica file",
                "[9] Fine"
        };

        boolean Sitel = true;  // Variabile di controllo per la lettura dei dati
        final int nMax = 3;    // Numero massimo di contatti gestiti
        int contrattiVenduti = 0; // Contatore dei contatti venduti
        int posizione; // Variabile per tenere traccia della posizione dell'array
        Contatto[] gestore = new Contatto[nMax]; // Array di contatti
        boolean hidden = false;
        Scanner keyboard = new Scanner(System.in); // Scanner per la lettura degli input
        boolean fine = true; // Variabile di controllo del ciclo principale
        int ricerca = 0;
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
                    if (!hidden) {
                        visualizza(gestore, contrattiVenduti, hidden);
                        Wait(2);
                    }
                    else if (hidden) {
                        visualizzaN(gestore, contrattiVenduti, hidden);
                        Wait(2);
                    }
                    else {
                        System.out.println("Non ci sono contratti\n");
                        Wait(2);
                    }
                    break;
                }

                case 3: {
                    // Ricerca di un contatto
                    Contatto inserito=new Contatto();
                    inserito=creaContatto(Sitel, keyboard, hidden);
                    if (contrattiVenduti != 0) {
                        if(ricerca(gestore, inserito, contrattiVenduti) == 1) {
                            System.out.println("Il contatto è nascosto");
                            Wait(2);
                        } else if (ricerca(gestore, inserito, contrattiVenduti) == 2){
                            System.out.println("Il contatto è pubblico");
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
                                if(numero.nascosto) {
                                    System.out.println("Modifica numero telefonico nascosto: ");
                                    numero.telefono = keyboard.nextLine();
                                    gestore[posizione].telefono = numero.telefono;
                                }
                                else {
                                    System.out.println("Modifica numero telefonico pubblico: ");
                                    numero.telefono = keyboard.nextLine();
                                    gestore[posizione].telefono = numero.telefono;
                                }
                            } else {
                                System.out.println("Numero telefonico non modificato");
                                Wait(2);
                            }
                        }
                        else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;
                case 5:
                    // Cancellazione di un contatto
                    ricerca = 0;
                    if (contrattiVenduti != 0) {
                        posizione = RicercaIndex(gestore, ricerca, contrattiVenduti, creaContatto(Sitel, keyboard, hidden));
                        if (posizione != -1 && !hidden) {
                            contrattiVenduti = cancellazione(gestore, posizione, contrattiVenduti);
                        }
                        else if (posizione != -1) {
                            contrattiVenduti = cancellaNascosto(gestore, posizione, contrattiVenduti);
                        }
                        else {
                            System.out.println("Contatto inesistente");
                            Wait(2);
                        }
                    } else {
                        System.out.println("Non sono ancora presenti contratti venduti");
                        Wait(2);
                    }
                    break;

                case 6:
                    // Simulazione di una telefonata
                    ricerca = 0;
                    Telefona(gestore, keyboard, contrattiVenduti, hidden, ricerca, Sitel);
                    break;
                case 7:
                    // Salvataggio dei contatti su file
                    try {
                        //ScriviFile("archivio.csv", gestore, contrattiVenduti);
                        ScriviNcontatti("archivio.csv", gestore, contrattiVenduti);
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                        break;
                    }
                    break;
                case 8:
                    // Caricamento dei contatti da file
                    try {
                        //contrattiVenduti = LeggiFile("archivio.csv", gestore, contrattiVenduti);
                        gestore=LeggiNcontatti("archivio.csv");
                        contrattiVenduti= gestore.length;
                    } catch (IOException ex) {
                        System.out.println(ex);
                        break;
                    }
                    break;

                case 1234:
                    hidden = true;
                    System.out.println("MENU' NASCOSTO");
                    break;
                default:
                    // Uscita dal programma
                    if (hidden) {
                        //ritorno a pubblico
                        hidden = false;
                    }
                    else {
                        fine = false;
                    }
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
        return persona;
    }

    // Funzione per cercare un contatto nell'array dei contatti
    private static int ricerca(Contatto[] gestore, Contatto contatto, int contrattiVenduti) {
        for (int i = 0; i < contrattiVenduti; i++) {
            if (contatto.nome.equals(gestore[i].nome) && contatto.cognome.equals(gestore[i].cognome) && contatto.telefono.equalsIgnoreCase(gestore[i].telefono) && gestore[i].nascosto) {
                return 1; //nascosta
            }
            else if (contatto.nome.equals(gestore[i].nome) && contatto.cognome.equals(gestore[i].cognome) &&contatto.telefono.equalsIgnoreCase(gestore[i].telefono) && !gestore[i].nascosto) {
                return 2; //pubblica
            }
        }
        return -1;
    }

    // Funzione per ottenere l'indice di un contatto nell'array
    private static int RicercaIndex(Contatto[] gestore, int ricerca, int contrattiVenduti, Contatto cercaPersona) {
            for (int i = 0; i < contrattiVenduti; i++) {
                if (cercaPersona.nome.equals(gestore[i].nome) && cercaPersona.cognome.equals(gestore[i].cognome) && gestore[i].nascosto) {
                    return i;
                } else {
                    if (cercaPersona.nome.equals(gestore[i].nome) && cercaPersona.cognome.equals(gestore[i].cognome) && !gestore[i].nascosto) {
                        return i;
                    }
                }
            }
        return -1;
    }

    // Funzione per visualizzare tutti i contatti
    private static void visualizza(Contatto[] gestore, int contrattiVenduti, boolean hidden) {
        for (int i = 0; i < contrattiVenduti; i++) {
            //stampa pubblico
            if (!gestore[i].nascosto) {
                System.out.println(gestore[i].stampa());
            }
        }
    }
    private static void visualizzaN(Contatto[] gestore, int contrattiVenduti, boolean hidden) {
        for (int i = 0; i < contrattiVenduti; i++) {
            //stampa privato
            System.out.println(gestore[i].stampaN());
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
    // Funzione per simulare una telefonata
    public static void Telefona(Contatto[] gestore, Scanner keyboard, int contrattiVenduti, boolean hidden, int ricerca, boolean Sitel) {
        int posizione;
        if (!hidden) {
            if (contrattiVenduti != 0) {
                posizione = RicercaIndex(gestore, ricerca, contrattiVenduti, creaContatto(Sitel, keyboard, hidden));
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
                } else {
                    System.out.println("Contatto non trovato");
                    Wait(2);
                }
            } else {
                System.out.println("Non sono ancora presenti contratti venduti");
                Wait(2);
            }
        }
        else {
            if (contrattiVenduti != 0) {
                posizione = RicercaIndex(gestore, ricerca, contrattiVenduti, creaContatto(Sitel, keyboard, hidden));
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
                } else {
                    System.out.println("Contatto non trovato");
                    Wait(2);
                }
            } else {
                System.out.println("Non sono ancora presenti contratti venduti");
                Wait(2);
            }
        }
    }
    // Funzione per scrivere i contatti su file
    public static void ScriviFile(String fileName, Contatto[] gestore, int contrattiVenduti) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        //ScriviNcontatti(fileName, gestore, contrattiVenduti);
        for (int i = 0; i < contrattiVenduti; i++) {
            writer.write(gestore[i].toString() + "\r\n");
        }
        writer.flush();
        writer.close();
    }

    //Funzione per importare dati da file esterni
    private static int LeggiFile(String fileName, Contatto[] gestore, int contrattiVenduti) throws IOException {
        FileReader reader = new FileReader(fileName);
        Scanner input = new Scanner(reader);
        String lineIn;
        String[] vetAttributi;
        int contaElementi = 0;
        while (input.hasNextLine() && (contaElementi < gestore.length)) {
            lineIn = input.nextLine();
            vetAttributi = lineIn.split(",");
            Contatto persona = new Contatto();
            persona.nome = vetAttributi[0];
            persona.cognome = vetAttributi[1];
            persona.telefono = vetAttributi[2];
            switch (vetAttributi[3]) {
                case "abitazione":
                    persona.tipo = tipoContratto.abitazione;
                    break;
                case "cellulare":
                    persona.tipo = tipoContratto.cellulare;
                    break;
                case "aziendale":
                    persona.tipo = tipoContratto.aziendale;
                    break;
            }
            gestore[contaElementi++] = persona;
        }
        reader.close();
        return contaElementi;
    }
    private static void ScriviNcontatti(String fileName, Contatto[] gestore, int contrattiVenduti) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(contrattiVenduti+"\r\n");
        for (int i = 0; i < contrattiVenduti; i++) {
            writer.write(gestore[i].nome + "," + gestore[i].cognome + "," + gestore[i].telefono + "," + gestore[i].tipo +"\n");
        }
        writer.flush();
        writer.close();
    }
    private static Contatto[] LeggiNcontatti(String fileName) throws IOException {
        FileReader reader = new FileReader(fileName);
        Scanner input = new Scanner(reader);
        String lineIn;
        String[] vetAttributi;
        int contaElementi = 0;
        Contatto gestore2[]=new Contatto[Integer.parseInt(input.nextLine())];
        while (input.hasNextLine()) {
            lineIn = input.nextLine();
            vetAttributi = lineIn.split(",");
            Contatto persona = new Contatto();
            persona.nome = vetAttributi[0];
            persona.cognome = vetAttributi[1];
            persona.telefono = vetAttributi[2];
            switch (vetAttributi[3]) {
                case "abitazione":
                    persona.tipo = tipoContratto.abitazione;
                    break;
                case "cellulare":
                    persona.tipo = tipoContratto.cellulare;
                    break;
                case "aziendale":
                    persona.tipo = tipoContratto.aziendale;
                    break;
            }
            gestore2[contaElementi] = persona;
            contaElementi++;
        }
        return gestore2;
    }
}
