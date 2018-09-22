package chat.client;

import chat.RoomInfo;

import java.util.ArrayList;
import java.util.HashMap;


public class Model {
    private ArrayList<RoomInfo> rooms;
    private ArrayList<String> users;
    private  final HashMap<String, PrivateView> privates = new HashMap();
    private String myName = null;
    private String myRoom = null;



    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyName() {
        return myName;
    }

    public String getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(String myRoom) {
        this.myRoom = myRoom;
    }

    public ArrayList<RoomInfo> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<RoomInfo> rooms) {
        this.rooms = rooms;
    }

    public HashMap<String, PrivateView> getPrivates() {
        return privates;
    }

    private Object mutexForClients = new Object();

    public void updateListOfUsers(ArrayList<String> users){
        synchronized (mutexForClients){
            this.users = users;
        }
    }



    public ArrayList<String> getUsers() {
        synchronized (mutexForClients) {
            return users;
        }
    }



}
