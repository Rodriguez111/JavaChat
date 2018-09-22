package chat;

import chat.client.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {







   // protected static final LinkedList<Room> rooms = new LinkedList();

    //protected static final LinkedList<User> users = new LinkedList();


    protected static final CopyOnWriteArrayList <Room> rooms = new CopyOnWriteArrayList<>();
    protected static final CopyOnWriteArrayList <User> users = new CopyOnWriteArrayList<>();


//    static Object objectForRooms = new Object();
//    public static LinkedList<Room> getRooms() {
//
//        synchronized (objectForRooms) {
//            return rooms;
//        }
//    }


    //----------------------------------класс  ServerHandler ------------------------------------

    private static class ServerHandler extends Thread{

        Socket newClientSocket; //соединение с новым клиентом
        Connection thisConnection = null;
        User thisUser = null;


        public ServerHandler(Socket newClientSocket) {
            this.newClientSocket = newClientSocket;
        }


        @Override
        public void run() {

            try {
                thisConnection = new Connection(newClientSocket);
                ChatUtils.writeToConsole("Создано новое соединение с клиентом");
            } catch (IOException e) {
                ChatUtils.writeToConsole("Не удалось создать соединение с клиентом");
            }

            //запрашиваем имя и присваиваем его переменной
            try {
                thisUser = primaryNegotiationsWithClient(thisConnection);
            }catch (NullPointerException e){
                ChatUtils.writeToConsole("Пользователь отказался вводить имя");
            }


            if(thisUser != null) { //если пользователь не отменил ввод имени
                mainLoop(thisUser.getUserName(), thisConnection);
            }
        }
    }
//----------------------------------класс  ServerHandler ------------------------------------


    public static void main(String[] args) {
        rooms.add(new Room("ГЛАВНАЯ КОМНАТА"));




        try(ServerSocket serverSocket = new ServerSocket(5001);) {
            ChatUtils.writeToConsole("Серверный сокет создан");

            while (true){

              Socket newClientSocket =  serverSocket.accept();
              new ServerHandler(newClientSocket).start();

                ChatUtils.sleep();
            }
        } catch (IOException e) {
            ChatUtils.writeToConsole("Ошибка создания серверного соединения");
        }
    }


//-------------------------------------------первичный диалог с клиентом-----------------------
    private static User primaryNegotiationsWithClient (Connection connection) throws NullPointerException {
        connection.sendMessage(new Message(MessageType.NAME_REQUEST)); //запрос имени пользоввателя у клиента
        User newClient = null;
        while (true) {

            Message  message = connection.receiveMessage();

           //System.out.printf("Тип сообщения: %s, тело: %s, имя клиента: %s, имя комнаты: %s \n", message.getMessageType(), message.getMessageBody(), message.getClientName(), message.getRoomName());

            if(message== null) {
                ChatUtils.writeToConsole("Пользователь отменил ввод имени"); //если пользователь отменил ввод имени
                break;
            }
            if (message.getMessageType() == MessageType.USERNAME && !message.getClientName().isEmpty()) {  //если пользователь прислал имя
                newClient = new User(message.getClientName(), connection);
                if (ChatUtils.registerNewUser(newClient)) { //если пользователь успешно добавлен в список
                    userAccepted(message.getClientName(), connection);
                    break;
                } else {
                    newClient = null;
                    userRejected(message.getMessageBody(), connection);
                }
            }
            ChatUtils.sleep();
        }
        return newClient;
    }

 //-------------------------------------------первичный диалог с клиентом-----------------------

//-------------------------------------------главный цикл--------------------------------------

    private static void mainLoop(String thisClientName, Connection connection){
        while (true){
            System.out.println("mainLoop - Ожидаем сообщения, соединение "+connection.toString());
            try {
                Message newMessage = connection.receiveMessage();

                System.out.printf("Тип сообщения: %s, тело: %s, имя клиента: %s, имя комнаты: %s \n", newMessage.getMessageType(), newMessage.getMessageBody(), newMessage.getClientName(), newMessage.getRoomName());
                if(newMessage.getMessageType() == MessageType.ENTER_ROOM){ //пользователь выбрал комнату
                    ChatUtils.addUserToTheRoom(ChatUtils.getUserOnUsername(newMessage.getClientName()), newMessage.getRoomName()); //добавляем пользователя в выбранную комнату
                    sendListOfUsers(connection, newMessage.getRoomName()); //отправляем вошедшему список участников комнаты
                    newUserInfo(thisClientName, newMessage.getRoomName()); //отправляем инфу о том,что новый пользователь вошел в чат
                }

                else  if (newMessage.getMessageType() == MessageType.EXIT_ROOM){ //если пользователь вышел из комнаты
                    userLeftRoom(thisClientName, newMessage.getRoomName());
                }

                else  if (newMessage.getMessageType() == MessageType.LIST_OF_ROOMS_REQUEST){
                    sendListOfRooms(connection);
                }

                else  if (newMessage.getMessageType() == MessageType.LIST_OF_USERS_REQUEST){ //если пользователь запросил список пользователей
                    sendListOfUsers(connection, newMessage.getRoomName());
                }

                else  if (newMessage.getMessageType() == MessageType.NEW_ROOM_REQUEST){ //если пользователь создал новую комнату
                  if(ChatUtils.createNewRoom(newMessage.getRoomName())){ //если комната успешно добавлена
                      connection.sendMessage(new Message(MessageType.NEW_ROOM_ACCEPTED, ChatUtils.getListOfRooms())); //отправляем сообщение, что комната принята
                      sendListOfRooms(connection); //обновляем список комнат
                  }
                  else {
                      connection.sendMessage(new Message(MessageType.NEW_ROOM_REJECTED)); //отправляем сообщение, что комната не принята
                  }

                }

                else if (newMessage.getMessageType() == MessageType.TEXT) { //прием текстового сообщения
                    sendChatMessage(newMessage, thisClientName, newMessage.getRoomName());
                }

                else if(newMessage.getMessageType() == MessageType.PRIVATE_REQUEST){ //прием запроса на приват
                    Connection connectionToPart2Client = ChatUtils.getUserOnUsername(newMessage.getClientName()).getConnection(); //извлекаем подключение вызываемой стороны
                    connectionToPart2Client.sendMessage(new Message(MessageType.PRIVATE_REQUEST, thisClientName, newMessage.getRoomName())); //отправляем запрос на приват вызываемой стороне
                }

                else if(newMessage.getMessageType() == MessageType.PRIVATE_ACCEPTED){ //если вызываемый согласился на приват
                    Connection connectionToPart1Client = ChatUtils.getUserOnUsername(newMessage.getClientName()).getConnection(); //извлекаем подключение вызывающей стороны
                    connectionToPart1Client.sendMessage(new Message(MessageType.PRIVATE_ACCEPTED, thisClientName, newMessage.getRoomName())); //отправляем согласие вызывающей стороне
                }

                else if(newMessage.getMessageType() == MessageType.PRIVATE_REJECTED){ //если вызываемый отказался от привата
                    Connection connectionToPart1Client = ChatUtils.getUserOnUsername(newMessage.getClientName()).getConnection(); //извлекаем подключение вызывающей стороны
                    connectionToPart1Client.sendMessage(new Message(MessageType.PRIVATE_REJECTED, thisClientName, newMessage.getRoomName())); //отправляем отказ вызывающей стороне
                }

                else if(newMessage.getMessageType() == MessageType.PRIVATE_TEXT){
                    Connection destinationClientConnection = ChatUtils.getUserOnUsername(newMessage.getClientName()).getConnection(); //извлекаем подключение удаленной стороны
                    //здесь заменяем имя в сообщении с "кому" на "от кого"
                    //и отправляем сообщение удаленному участнику:
                    destinationClientConnection.sendMessage(new Message(MessageType.PRIVATE_TEXT, thisClientName, null, newMessage.getMessageBody() ));
                    //а здесь возвращаем клиенту его же сообщение, чтобы оно отображалрось и у него тоже, но передаем другого участника, чтобы знать какое окно извлекать из списка приватов
                    connection.sendMessage(new Message(MessageType.MY_OWN_PRIVATE_TEXT, newMessage.getClientName(), null, newMessage.getMessageBody() ));
                }

                else if(newMessage.getMessageType() == MessageType.PRIVATE_CLOSED){
                    Connection destinationClientConnection = ChatUtils.getUserOnUsername(newMessage.getClientName()).getConnection(); //извлекаем подключение удаленной стороны
                    destinationClientConnection.sendMessage(new Message(MessageType.PRIVATE_CLOSED, thisClientName, newMessage.getRoomName())); //передаем имя того, кто закрыл окно приват-чата
                }

            }catch (NullPointerException e){

        //если пользователь вышел из всего чата (закрыл окно)
            userRemoved(thisClientName, ChatUtils.getUsersRoomName(thisClientName)); //если клиент закрыл окно
                break;
            }
            ChatUtils.sleep();
        }

    }

    //-------------------------------------------главный цикл--------------------------------------

    private static void userAccepted(String username, Connection connection){   //пользователь принят
        //отправляем пользователю сообщение, что его имя принято
        connection.sendMessage(new Message(MessageType.NAME_ACCEPTED,username));
        sendListOfRooms(connection); //отправляем пользователю список комнат
    }

    protected static void sendListOfRooms(Connection connection){ //передаем клиенту список комнат
        connection.sendMessage(new Message(MessageType.LIST_OF_ROOMS_UPDATE, ChatUtils.getListOfRooms()));
    }

    private static void userRejected(String username, Connection connection){ //такое имя уже есть в чате
        connection.sendMessage(new Message(MessageType.NAME_REJECTED, username, ""));
    }


    protected static void sendListOfUsers(Connection connection, String roomName){//отправляем вошедшему список участников комнаты
       connection.sendMessage(new Message(MessageType.LIST_OF_USERS_UPDATE, ChatUtils.getListOfUsersFromTheRoom(roomName)));

    }


    private static void sendBroadcast(Message message, String roomName){ //отправить сообщение всем пользователям определенной комнаты
        for(Room eachRoom : rooms){
               if (eachRoom.getName().equals(roomName)) {
                   eachRoom.sendMessageToAllUsersInTheRoom(message);
               }
        }
    }

    private static void newUserInfo(String username, String roomName){ //отправить участникам определенной комнаты сообщение о том, что пользователь вошел в комнату
        //и в этом же сообщении отправляем список пользователей
        sendBroadcast(new Message(MessageType.USER_ADDED, username, ChatUtils.getListOfUsersFromTheRoom(roomName)), roomName);


    }

    private static void userLeftRoom (String clientName, String roomName){
        ChatUtils.writeToConsole("Удаленный клиент покинул комнату "+roomName); //лог в консоль
        ChatUtils.removeUserFromTheRoom(ChatUtils.getUserOnUsername(clientName), roomName); // удаляем пользователя из комнаты
        //отправляем остальным участникам комнаты сообщение о том, что пользователь вышел из комнаты и в этом же сообщении отправляем список пользователей
        sendBroadcast(new Message(MessageType.EXIT_ROOM, clientName, ChatUtils.getListOfUsersFromTheRoom(roomName)), roomName);
    }

    private static void sendChatMessage(Message incomeMessage, String clientName, String roomName){ //отправка сообщения всем участникам комнаты
        Message messageToChat = new Message(MessageType.TEXT, clientName, null, incomeMessage.getMessageBody());
        sendBroadcast(messageToChat, roomName);
    }

    private static void userRemoved (String clientName, String roomName){
        ChatUtils.writeToConsole("Удаленный клиент отключился"); //лог в консоль

        if(roomName != null){ //если пользователь был участником какой-либо комнаты
            ChatUtils.removeUserFromTheRoom(ChatUtils.getUserOnUsername(clientName), roomName); //удаляем его из комнаты
            ChatUtils.removeUser(ChatUtils.getUserOnUsername(clientName)); //удаляем его из списка пользоваетлей чата
            //отправляем остальным участникам инфу о том, что он покинул весь чат и в этом же сообщении отправляем список пользователей
            sendBroadcast(new Message(MessageType.USER_REMOVED, clientName, ChatUtils.getListOfUsersFromTheRoom(roomName)), roomName);
        }
        else {
            ChatUtils.removeUser(ChatUtils.getUserOnUsername(clientName)); //иначе просто удаляем его из списка пользоваетлей чата
        }

    }
}