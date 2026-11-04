package zut.lafs.model;

public class LostItem extends Item {
    private String ownerContact;

    protected LostItem() { }

    public LostItem(int id, String name, String category, String location, String date, String ownerContact) {
        super(id, name, category, location, date);
        this.ownerContact = ownerContact;
    }

    public String getOwnerContact() { return ownerContact; }

    @Override
    public String getKind() { return "LOST"; }

    @Override
    public String getSummary() {
        return "LOST: " + getName() + " (last seen at " + getLocation() + "). Contact: " + ownerContact;
    }
}
