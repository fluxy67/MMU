package mmu.storage;



public interface PageStorage {


    
	String readPage(int pageNumber) ;
    void writePage(int pageNumber, String content);
}
