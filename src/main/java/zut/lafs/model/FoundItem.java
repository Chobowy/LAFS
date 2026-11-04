package zut.lafs.model;

public class FoundItem extends Item {
    private String storedAt;

    protected FoundItem() { }

    public FoundItem(int id, String name, String category, String location, String date, String storedAt) {
        super(id, name, category, location, date);
        this.storedAt = storedAt;
    }

    public String getStoredAt() { return storedAt; }

    @Override
    public String getKind() { return "FOUND"; }

    @Override
    public String getSummary() {
        return "FOUND: " + getName() + " (found at " + getLocation() + "). Kept at: " + storedAt;
    }
}
