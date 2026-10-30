package mmu;

import java.util.Scanner;

import mmu.storage.PageStorage;
import mmu.storage.FilePageStorage;
import mmu.replacement.PageReplacementPolicy;
import mmu.replacement.FifoPageReplacement;

public class Main {

    public static void main(String[] args) {

        // Configuration
        final int MEMORY_SIZE = 5;

        PageStorage storage = new FilePageStorage("pages");
        PageReplacementPolicy policy = new FifoPageReplacement();

        MMU mmu = new MMU(MEMORY_SIZE, storage, policy);

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("\n===== SIMULATEUR MMU =====");
            System.out.println("R - Lire une page");
            System.out.println("W - Écrire une page");
            System.out.println("P - Afficher la mémoire");
            System.out.println("S - Sauvegarder (flush)");
            System.out.println("Q - Quitter");
            System.out.print("Choix : ");

            String choice = scanner.nextLine();

            try {

                switch (choice) {

                    case "R": {
                        System.out.print("Numéro de page : ");
                        int pageNumber = Integer.parseInt(scanner.nextLine());

                        String content = mmu.readPage(pageNumber);

                        System.out.println("Page " + pageNumber
                                + " : " + content);
                        break;
                    }

                    case "W": {
                        System.out.print("Numéro de page : ");
                        int pageNumber = Integer.parseInt(scanner.nextLine());

                        System.out.print("Contenu : ");
                        String content = scanner.nextLine();

                        mmu.writePage(pageNumber, content);

                        System.out.println("Page " + pageNumber
                                + " modifiée.");
                        break;
                    }

                    case "P":
                        mmu.printMemory();
                        break;

                    case "S":
                        mmu.flush();
                        System.out.println("Mémoire sauvegardée.");
                        break;

                    case "Q":
                        mmu.flush();
                        running = false;
                        System.out.println("Fin du programme.");
                        break;

                    default:
                        System.out.println("Choix invalide.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Numéro de page invalide.");

            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        scanner.close();
    }
}