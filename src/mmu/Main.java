
package mmu;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;

import mmu.storage.PageStorage;
import mmu.storage.FilePageStorage;
import mmu.storage.MySqlPageStorage;

public class Main {

	// Pour les accès Mysql
	private static final String host = System.getenv("MYSQL_HOST");
	private static final int port = 3307;
	private static final String database = "testdb";
	private static final String user = System.getenv("MYSQL_USER");
	private static final String password = System.getenv("MYSQL_PASSWORD");
	

	
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		// ici on choisit de stocker les pages sur disque
		// PageStorage filestorage = new FilePageStorage("pages");
		// ici on choisit de stocker les pages dans Mysql
		PageStorage storage = new MySqlPageStorage(host,port,database,user,password);
				


		while (true) {

			System.out.print("\nLire (r), Écrire (w), Quitter (q) : ");
			String action = scanner.nextLine();

			if (action.equals("q")) {
				break;
			}

			if (!action.equals("r") && !action.equals("w")) {
				System.out.println("Commande inconnue");
				continue;
			}

			System.out.print("Numéro de page : ");
			int pageNumber = Integer.parseInt(scanner.nextLine());

			if (action.equals("w")) {

				System.out.print("Contenu de la page : ");
				String content = scanner.nextLine();

				storage.writePage(pageNumber, content);

				System.out.println("Page écrite");

			} else {

				String content = storage.readPage(pageNumber);

				if (content == null) {
					System.out.println("La page n'existe pas");
				} else {
					System.out.println("Contenu : " + content);
				}
			}
		}

		scanner.close();
		System.out.println("Fin du programme");
	}
}
