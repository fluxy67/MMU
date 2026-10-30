package mmu;

public class MemoryPage {

    private int pageNumber;
    private String content;
    private boolean modified;

    public MemoryPage() {
        free();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getContent() {
        return content;
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isFree() {
        return pageNumber == -1;
    }

    public void init(int pageNumber, String content, boolean modified) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Numéro de page invalide");
        }

        this.pageNumber = pageNumber;
        this.content = content;
        this.modified = modified;
    }

    public void setContent(String content) {
        if (isFree()) {
            throw new IllegalStateException("Emplacement mémoire libre");
        }

        this.content = content;
        this.modified = true;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void free() {
        this.pageNumber = -1;
        this.content = null;
        this.modified = false;
    }
}