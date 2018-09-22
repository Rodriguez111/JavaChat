package chat;
/*
* для передачи информации о комнате и колчестве участников в ней на сторону клиента
* */

import java.io.Serializable;

public class RoomInfo implements Serializable {
    private String theNameOfTheRoom;
    private int countOfUsers;

    public RoomInfo(String theNameOfTheRoom, int countOfUsers) {
        this.theNameOfTheRoom = theNameOfTheRoom;
        this.countOfUsers = countOfUsers;
    }

    public String getTheNameOfTheRoom() {
        return theNameOfTheRoom;
    }

    public int getCountOfUsers() {
        return countOfUsers;
    }
}
