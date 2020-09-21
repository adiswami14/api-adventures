package student.adventure;

/**
 * Class for items in player inventory and room collectibles
 */
public class Item {
    private String name; //name of the item

    /**
     * Constructor of Item class
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * @return the name of the item
     */
    public String getName() {
        return name;
    }
}
