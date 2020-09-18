package student.adventure;

public class Direction {
    private String name;
    private String connectingRoom;

    public Direction(){
    }

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
