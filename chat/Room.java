package chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class Room {

   private LinkedList<User> usersInThisRoomList = new LinkedList<>();
   private String name;



    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public ArrayList<String> getUsersInThisRoomList() { //извлекаем имена из списка пользователей в строковый список
        ArrayList<String> listOfUsers = new ArrayList<>();
        for (User eachUser : usersInThisRoomList){
            listOfUsers.add(eachUser.getUserName());
        }
        return listOfUsers;
    }

    public int getCountOfUsers() {
        return usersInThisRoomList.size();
    }


    public void sendMessageToAllUsersInTheRoom(Message message){
        for (User eachUser : usersInThisRoomList){
            eachUser.getConnection().sendMessage(message);
        }
    }

    public void addUser(User user){
        usersInThisRoomList.add(user);
    }

    public void removeUser(User user){
        usersInThisRoomList.remove(user);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
