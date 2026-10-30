package mmu.replacement;

public interface PageReplacementPolicy {

    void pageLoaded(int pageNumber);

    void pageAccessed(int pageNumber, boolean write);

    int getPageToRemove();
}