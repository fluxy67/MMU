package mmu.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilePageStorage implements PageStorage {
	private final String directory;

	private File getPageFile(int pageNumber) {
		return new File(directory, pageNumber + ".page");
	}
	
	public FilePageStorage(String directory) {
		this.directory = directory;
	}

	public String readPage(int pageNumber) {        
        try (FileInputStream input = new FileInputStream(getPageFile(pageNumber))) {
            return new String(input.readAllBytes());
        } catch (IOException e) {
            System.out.println("Erreur de lecture : " + e.getMessage());
            return null;
        }
	}

	public void writePage(int pageNumber,String data) {
        try (FileOutputStream output =
                new FileOutputStream(getPageFile(pageNumber))) {

            output.write(data.getBytes());

        } catch (IOException e) {
            System.out.println("Erreur d'écriture : " + e.getMessage());
        }
	}

}
