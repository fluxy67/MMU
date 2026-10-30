package mmu.replacement;

import java.util.LinkedList;
import java.util.Queue;

public class FifoPageReplacement implements PageReplacementPolicy {

    private final Queue<Integer> pages = new LinkedList<>();

    public void pageLoaded(int pageNumber) {
        pages.add(pageNumber);
    }

    public void pageAccessed(int pageNumber, boolean write) {
        // FIFO ne tient pas compte des accès.
    }

    public int getPageToRemove() {
        Integer page = pages.poll();

        if (page == null) {
            throw new IllegalStateException("Aucune page à remplacer");
        }

        return page;
    }
}