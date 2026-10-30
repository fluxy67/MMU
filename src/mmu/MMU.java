package mmu;

import mmu.storage.PageStorage;
import mmu.replacement.PageReplacementPolicy;

public class MMU {

    private final MemoryPage[] memory;
    private final PageStorage storage;
    private final PageReplacementPolicy replacementPolicy;

    public MMU(int memorySize,
               PageStorage storage,
               PageReplacementPolicy replacementPolicy) {

        if (memorySize <= 0) {
            throw new IllegalArgumentException("Taille mémoire invalide");
        }

        this.storage = storage;
        this.replacementPolicy = replacementPolicy;

        // Création du tableau de pages mémoire
        memory = new MemoryPage[memorySize];

        // Initialisation des emplacements
        for (int i = 0; i < memory.length; i++) {
            memory[i] = new MemoryPage();
        }
    }

    // Lecture d'une page logique
    public String readPage(int pageNumber) {
        MemoryPage memoryPage = findMemoryPage(pageNumber);
    
    	// La page n'est pas encore en mémoire
    	if(memoryPage == null) {
            // on la stocke en mémoire
            memoryPage = getFreeMemoryPage();
    		// on lit la page sur le "storage"
            String content = storage.readPage(pageNumber);
            memoryPage.init(pageNumber, content, false);
        	this.replacementPolicy.pageLoaded(pageNumber);            
    	}
    	this.replacementPolicy.pageAccessed(pageNumber, false);
        return memoryPage.getContent();
    }


    // écriture d'une page
    public void writePage(int pageNumber, String content) {
        MemoryPage memoryPage = findMemoryPage(pageNumber);
    	if(memoryPage != null) {
            memoryPage.setContent(content);
    	}
    	else {
        	// La page n'est pas encore en mémoire
            memoryPage = getFreeMemoryPage();
            this.replacementPolicy.pageLoaded(pageNumber);
            memoryPage.init(pageNumber, content, true);
    	}
    	this.replacementPolicy.pageAccessed(pageNumber, true);
    }
    
    // retourne une page mémore vide - s'il n'y en a pas encore, en libère une
    private MemoryPage getFreeMemoryPage() {
        // Recherche d'un emplacement libre
        for (MemoryPage page : memory) {
            if (page.isFree()) {
                return page;
            }
        }
        
        // on n'a pas trouvé de page libre : on en libère une
        int victimPageNum = replacementPolicy.getPageToRemove();
        MemoryPage victimPage = findMemoryPage(victimPageNum);
       
        // Sauvegarde si la page a été modifiée
        if (victimPage.isModified()) {
            storage.writePage(
                victimPage.getPageNumber(),
                victimPage.getContent()
            );
        }
        victimPage.free();
        return victimPage;
    }

    // Sauvegarde des pages modifiées encore présentes en mémoire
    public void flush() {

        for (int i = 0; i < memory.length; i++) {
            MemoryPage page = memory[i];
            if (!page.isFree() && page.isModified()) {
                storage.writePage(
                    page.getPageNumber(),
                    page.getContent()
                );
                page.setModified(false);
            }
        }
    }


    // Affichage de l'état de la mémoire physique
    public void printMemory() {
        System.out.println("\nÉtat de la mémoire :");

        for (int i = 0; i < memory.length; i++) {

            MemoryPage page = memory[i];

            if (page.isFree()) {

                System.out.println(
                    "Emplacement " + i + " : libre"
                );

            } else {

                System.out.println(
                    "Emplacement " + i
                    + " : page " + page.getPageNumber()
                    + " | contenu = " + page.getContent()
                    + " | modifiée = " + page.isModified()
                );
            }
        }
    }
    
	private MemoryPage findMemoryPage(int pageNumber) {
        for (int i = 0; i < memory.length; i++) {
            if (!memory[i].isFree()
                    && memory[i].getPageNumber() == pageNumber) {
                return memory[i];
            }
        }
        return null;
	}

}