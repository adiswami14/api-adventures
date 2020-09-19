package student.adventure;

public class Direction {
    private String name; //name of the direction (i.e. East)
    private String connectingRoom; //room it connects to through a particular Direction

    /**
     * @return the name of the Direction
     */
    public String getName() {
        return name;
    }

    /**
     * @return the Room object this particular Direction leads to
     */
    public String getConnectingRoom() {
        return connectingRoom;
    }
}
