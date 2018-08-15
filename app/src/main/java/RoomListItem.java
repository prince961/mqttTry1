public class RoomListItem {



    private String Name;
    private String Description ;

    public RoomListItem(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
