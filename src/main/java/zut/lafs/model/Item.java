package zut.lafs.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LostItem.class, name = "LOST"),
        @JsonSubTypes.Type(value = FoundItem.class, name = "FOUND")
})
public abstract class Item {
    private int id;
    private String name;
    private String category;
    private String location;
    private String date;
    private boolean returned;

    protected Item() { }   // needed to load from the JSON file

    protected Item(int id, String name, String category, String location, String date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public boolean isReturned() { return returned; }

    public void markReturned() { this.returned = true; }

    public abstract String getKind();      // polymorphism
    public abstract String getSummary();   // polymorphism
}