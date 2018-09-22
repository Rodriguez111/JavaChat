package chat;


import java.util.ArrayList;
import static chat.Server.rooms;
import static chat.Server.users;

public class ChatUtils {

    public static void writeToConsole(String string){
        System.out.println(string);
    }


    public static void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static int getCountOfUsersInChat() { //Получить число зарегистрированных пользователей чате (включая безкомнатных)
       return users.size();
    }


    public static int getCountOfUsersInAllRooms(){ //Получить число пользователей во всех комнатах
        int countOfUsers =0;
        for(Room eachRoom : Server.rooms){
            countOfUsers = countOfUsers+eachRoom.getCountOfUsers();
        }
        return countOfUsers;
    }



    public static int getCountOfUsersInRoom(String roomName){ //Получить число пользователей в определенной комнате
        int countOfUsers =0;
        for (Room eachRoom: Server.rooms) {
            if(eachRoom.getName().equals(roomName)){
                eachRoom.getCountOfUsers();
            }
        }
        return countOfUsers;
    }



    public static void addUserToTheRoom(User user, String roomName){ //добавить пользователя в комнату
        Room room = new Room(roomName); //создаем временную комнату, чтобы сравнить, есть ли комната с таким именем в списке
        if(Server.rooms.contains(room))
        for(Room eachRoom : Server.rooms){
            if(eachRoom.equals(room)){
                eachRoom.addUser(user);
            }
        }

    }

    public static void removeUserFromTheRoom(User user, String roomName){ //удалить пользователя из комнаты
        Room room = new Room(roomName); //создаем временную комнату, чтобы сравнить, есть ли комната с таким именем в списке
        if(Server.rooms.contains(room))
            for(int i = 0; i<Server.rooms.size(); i++){
                if(Server.rooms.get(i).equals(room)){
                    Server.rooms.get(i).removeUser(user);
                    if(Server.rooms.get(i).getCountOfUsers()==0 && !Server.rooms.get(i).getName().equals("ГЛАВНАЯ КОМНАТА")){ //проверяем остался ли кто-либо в комнате
                        rooms.remove(Server.rooms.get(i)); //если комната пуста и это не главная комната, удаляем ее
                    }
                }
            }
    }

    public static boolean registerNewUser(User user){
        //В классе User мы переопределили методы equals и hashCode, чтобы сравнивать только по имени.
        if(Server.users.contains(user)){ //если пользователь с таким именем уже есть в чате, возвращаем 0
            return false;
        }
        else{  //если нет - добавляем и возвращаем 1
            Server.users.add(user);
            return true;
        }
    }

    public static void removeUser(User user){
        Server.users.remove(user);
    }

    public static User getUserOnUsername(String userName){ //находим по имени пользователя в списке

        for(User eachUser : Server.users){
            if(eachUser.getUserName().equals(userName)){
                return eachUser;
            }

        }
        return null;
    }

    public static String getUsersRoomName(String userName){ //находим по имени пользователя в какой он был комнате
        for(Room eachRoom : Server.rooms){
            if(eachRoom.getUsersInThisRoomList().contains(userName)){ //если пользователь с таким именем есть в комнате
                return eachRoom.getName();  //тогда возвращаем имя комнаты
            }
        }
        return null; //иначе, если пользователь не находится ни в одной из комнат, возвращаем null
    }


    //Клиентской части требуются только названия комнат и кол-во юзеров в них, поэтому формируем список с такой информацией
    public static ArrayList<RoomInfo> getListOfRooms(){
        ArrayList<RoomInfo> listOfRooms = new ArrayList();
        for(Room eachRoom : Server.rooms){
            listOfRooms.add(new RoomInfo(eachRoom.getName(), eachRoom.getCountOfUsers())) ;
        }
        return listOfRooms;
    }



  //Клиентской части требуются только имена пользователей, поэтому извлекаем и передаем только имена, а не сам список юзеров
    public static ArrayList<String> getListOfUsersFromTheRoom(String roomName){
        for(Room eachRoom : Server.rooms){
            if(eachRoom.getName().equals(roomName)){
                return eachRoom.getUsersInThisRoomList();
            }
        }
        return null;
    }


    public static boolean createNewRoom(String newRoomName){
        Room newRoom = new Room(newRoomName);
        if(Server.rooms.contains(newRoom)){ //если комната с таким названием уже есть, возвращаем 0
            return false;
        }
        else{  //если нет - добавляем и возвращаем 1
            Server.rooms.add(newRoom);
            return true;
        }
    }

}
