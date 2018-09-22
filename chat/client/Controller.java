package chat.client;

import chat.*;


import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {


    private final Socket socket;
    private final Connection connection;
    private MyDialogWindow myDialogWindow; //диалоговое окно запроса имени


    private final Model model = new Model();


   private ListOfRoomsView listOfRoomsView = new ListOfRoomsView(this);
   private RoomView roomView;
   private PrivateView privateView;



    public Controller(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.connection = new Connection(socket);
        ChatUtils.writeToConsole("Соединение с сервером установлено");
    }


    public static void main(String[] args) {
        try {
            Controller controller = new Controller("localhost", 5001);
            //Controller controller = new Controller("95.67.60.141", 5001);

            controller.runController();
        } catch (IOException e) {
            ChatUtils.writeToConsole("Не удалось получить ответ от сервера");
        }
    }

    public void runController(){

        primaryNegotiationaWithServer();
        mainLoopRecievingMessages();

    }


 //-------------------------------------------первичный диалог с сервером-----------------------
    protected void primaryNegotiationaWithServer(){

        while (true) {
            //получаем сообщение от сервера (запрос имени)
            Message message = connection.receiveMessage();
            System.out.println(message.getMessageType());
            //если сообщение - запрос имени - запрашиваем у пользователя имя

            if (message.getMessageType() == MessageType.NAME_REQUEST) {

                try {
                    getUserName();
                }catch (NullPointerException e){
                    System.out.println("Нуль поинтер ексепшн");
                }

            }

            else if(message.getMessageType() == MessageType.NAME_ACCEPTED){

                myDialogWindow.dispose(); //закрываем окно запроса имени
                model.setMyName(message.getClientName());
                writeClientNameToWindowTitle(); //обновляем заголовок окна
            }

            else if(message.getMessageType() == MessageType.NAME_REJECTED){

                try {
                    myDialogWindow.getInfoPane().setText(null);
                    myDialogWindow.getInfoPane().getStyledDocument().insertString(0, "Пользователь с таким именем уже зарегистрирован в чате,\nвведите другое имя ", TextStyles.footerWrong(myDialogWindow.getInfoPane()));
                    myDialogWindow.existingUserIconConfiguration();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            else if(message.getMessageType() == MessageType.NEW_ROOM_ACCEPTED){
                updateListOfRooms(message.getList()); //обновляем список комнат
                writeInfoToFooterOfTheListOfRoomsView("Комната принята", "ok" );
            }
            else if(message.getMessageType() == MessageType.NEW_ROOM_REJECTED){
                writeInfoToFooterOfTheListOfRoomsView("Такая комната уже есть", "wrong");
            }

            else if (message.getMessageType() == MessageType.LIST_OF_ROOMS_UPDATE){
                updateListOfRooms(message.getList());

            }

            else if (message.getMessageType() == MessageType.LIST_OF_USERS_UPDATE){
                updateListOfUsers(message.getList());
                break;
            }

            ChatUtils.sleep();
        }
    }
 //-------------------------------------------первичный диалог с сервером-----------------------

 //-------------------------------------------главный цикл--------------------------------------


    protected void mainLoopRecievingMessages(){
        while (true){

            Message message = connection.receiveMessage();
            System.out.println("mainLoopRecievingMessages - получен тип сообщения от сервера "+message.getMessageType());
            if(message.getMessageType() == MessageType.TEXT){
                receiveMainMessage(message.getDate(),message.getClientName(), message.getMessageBody());
            }

            else if (message.getMessageType() == MessageType.LIST_OF_USERS_UPDATE){
                updateListOfUsers(message.getList());
            }

            else if(message.getMessageType() == MessageType.USER_ADDED){
                userAdded(message);
            }
            else if(message.getMessageType() == MessageType.EXIT_ROOM){
                    userLeftRoom(message);
            }
            else if (message.getMessageType() == MessageType.LIST_OF_ROOMS_UPDATE){
                updateListOfRooms(message.getList());
            }
            else if(message.getMessageType() == MessageType.USER_REMOVED){
                userRemoved(message);
            }
            else if(message.getMessageType() == MessageType.NEW_ROOM_ACCEPTED){
                updateListOfRooms(message.getList()); //обновляем список комнат
                writeInfoToFooterOfTheListOfRoomsView("Комната принята", "ok");
            }
            else if(message.getMessageType() == MessageType.NEW_ROOM_REJECTED){
                writeInfoToFooterOfTheListOfRoomsView("Такая комната уже есть", "wrong");
            }


            else if(message.getMessageType() == MessageType.PRIVATE_REQUEST){
                incomingPrivateWaiter(message.getClientName(), message.getRoomName());
            }
            else if(message.getMessageType() == MessageType.PRIVATE_ACCEPTED){
                yourPrivateWasAccepted(message.getClientName());
            }
            else if(message.getMessageType() == MessageType.PRIVATE_REJECTED){
                yourPrivateWasRejected(message.getClientName());
            }
            else if(message.getMessageType() == MessageType.PRIVATE_TEXT){
                receivePrivateMessage(message.getDate(), message.getClientName(), message.getMessageBody());
            }
            else if(message.getMessageType() == MessageType.MY_OWN_PRIVATE_TEXT){
                receiveMyOwnPrivateMessage(message.getDate(), message.getClientName(), message.getMessageBody());
            }
            else if(message.getMessageType() == MessageType.PRIVATE_CLOSED){
                receiveSystemNotificationToPrivate(message.getClientName(), "Собеседник покинул приватный чат");
            }


            else {
                break;}
            ChatUtils.sleep();
        }
    }
 //-------------------------------------------главный цикл--------------------------------------



    protected void getUserName() {
         myDialogWindow = new MyDialogWindow(this);
         myDialogWindow.setLocationRelativeTo(null);
         Thread newThread = new Thread(myDialogWindow); //запускаем окно ввода имени в отдельном потоке
        newThread.start();
        synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    protected void sendUserNameToServer(String userName){
            connection.sendMessage(new Message(MessageType.USERNAME, userName));
    }


    protected void updateListOfUsers(ArrayList<String> users) {
        model.updateListOfUsers(users);
        roomView.writeToListOfUsersArea(model.getUsers());

    }


    protected void updateListOfRoomsServerRequest(){
        connection.sendMessage(new Message(MessageType.LIST_OF_ROOMS_REQUEST));

    }

    protected void updateListOfRooms(ArrayList<RoomInfo> rooms){
        model.setRooms(rooms);
        listOfRoomsView.writeToListOfRooms(model.getRooms());
    }


    protected void userAdded(Message message) {
        model.updateListOfUsers(message.getList());
        roomView.writeToListOfUsersArea(model.getUsers());
        roomView.writeSystemNotificationToChatTextArea(message.getDate()+" "+message.getClientName()+" вошел в комнату");
    }

    protected void userLeftRoom(Message message){
        model.updateListOfUsers(message.getList());  //обновляем список пользователей в модели
        roomView.writeToListOfUsersArea(model.getUsers()); //обновляем правую часть окна чата со списком пользователей
        roomView.writeSystemNotificationToChatTextArea(message.getDate()+" "+message.getClientName()+" покинул комнату");

        if(model.getPrivates().containsKey(message.getClientName())){ //если был открыт приват с покинувшим пользователем
            receiveSystemNotificationToPrivate(message.getClientName(), "Собеседник вышел из комнаты");
        }
    }


    protected void userRemoved(Message message) {
        model.updateListOfUsers(message.getList());  //обновляем список пользователей в модели
        roomView.writeToListOfUsersArea(model.getUsers()); //обновляем правую часть окна чата со списком пользователей
        roomView.writeSystemNotificationToChatTextArea(message.getDate()+" "+message.getClientName()+" покинул чат");

        if(model.getPrivates().containsKey(message.getClientName())){ //если был открыт приват с покинувшим пользователем
            receiveSystemNotificationToPrivate(message.getClientName(), "Собеседник вышел из чата");
        }
    }


    protected void sendMainMessage(String messageBody){
        connection.sendMessage(new Message(MessageType.TEXT, getMyName(), getMyRoom(), messageBody));
    }



    protected void receiveMainMessage(String date, String clientName, String messageBody) {
        roomView.writeToChatTextArea(date, clientName, messageBody);
    }


    protected String getMyName(){
        return model.getMyName();
    }

    protected String getMyRoom(){
        return model.getMyRoom();
    }





    protected void writeClientNameToWindowTitle() {
        listOfRoomsView.writeClientNameToWindowTitle(model.getMyName());
    }

    //------------private--------------------

    protected void outcomingPrivateRequest(String part2ClientName, String roomName) {
        connection.sendMessage(new Message(MessageType.PRIVATE_REQUEST, part2ClientName, roomName));
    }


    protected void incomingPrivateWaiter(String part1ClientName, String roomName) {
    //анонимный класс. Запускаем в отдельном потоке, чтобы ожидание ответа не мешало принимать сообщения в общем чате.
        Thread privateChatHandler = new Thread(){

            @Override
            public void run() {

                int answer =  new Dialogs(roomView.getMainWindow()).incomingPrivateRequestWindow(part1ClientName);

                if(answer ==0){ //ответ Да
                    acceptPrivate(part1ClientName, roomName);
                }
                else { //ответ Нет
                    rejectPrivate(part1ClientName, roomName);
                }

            }
        } ;
        privateChatHandler.setDaemon(true);
        privateChatHandler.start();

    }

    Object acceptPrivateMutex = new Object();
    protected void acceptPrivate(String part1ClientName, String roomName){ //мы принимаем приглашение в приват
        synchronized (acceptPrivateMutex) {
            connection.sendMessage(new Message(MessageType.PRIVATE_ACCEPTED, part1ClientName, roomName));
            privateView = new PrivateView(this, part1ClientName);
            model.getPrivates().put(part1ClientName, privateView); //добавляем в список наших приватов
        }
    }

    Object rejectPrivateMutex = new Object();
    protected void rejectPrivate(String part1ClientName, String roomName){ //мы отказываемся от привата

        synchronized (rejectPrivateMutex) {
            connection.sendMessage(new Message(MessageType.PRIVATE_REJECTED, part1ClientName, roomName));
        }
    }



    protected void yourPrivateWasAccepted(String part2ClientName) { //наше приглашение в приват принято
        System.out.println("наше приглашение в приват принято, имя удаленного клиента "+part2ClientName);
        privateView = new PrivateView(this, part2ClientName);
        model.getPrivates().put(part2ClientName, privateView); //добавляем в список наших приватов
    }


    protected void yourPrivateWasRejected(String part2ClientName) { //наше приглашение в приват отвергнуто
        //анонимный класс. Запускаем в отдельном потоке, чтобы ожидание подтверждения сообщения об отказе не мешало принимать сообщения в общем чате.
        Thread privateChatHandler = new Thread(){

            @Override
            public void run() {
                new Dialogs(roomView.getMainWindow()).yourPrivateWasRejectedWindow(part2ClientName);

            }
        } ;
        privateChatHandler.setDaemon(true);
        privateChatHandler.start();
    }


    protected void sendMessageToPrivateChat(String destinationClientName, String messageBody) {
        connection.sendMessage(new Message(MessageType.PRIVATE_TEXT, destinationClientName, null,  messageBody));
    }


    protected void receivePrivateMessage(String date, String destinationClientName, String messageBody) {
      PrivateView currentPrivateView = model.getPrivates().get(destinationClientName); //извлекаем из списка приватов окно для этого собеседника
        currentPrivateView.writeToChatTextArea(date, destinationClientName,  messageBody);
    }


    protected void receiveMyOwnPrivateMessage(String date, String destinationClientName, String messageBody) {
        PrivateView currentPrivateView = model.getPrivates().get(destinationClientName); //извлекаем из списка приватов окно для этого собеседника
        //но имя подставляем свое:
        currentPrivateView.writeToChatTextArea(date, model.getMyName(),  messageBody);
    }


    protected void sendSystemNotificationToPrivate(String destinationClientName, String roomName) {
            if (model.getPrivates().containsKey(destinationClientName)) { //если мы закрываем окно уже неработающего чата,
                // (то есть, если собеседник вышел из привата), то сигнальное сообщение PRIVATE_CLOSED посылать не нужно
               connection.sendMessage(new Message(MessageType.PRIVATE_CLOSED, destinationClientName, roomName));
                model.getPrivates().remove(destinationClientName); //удаляем имя собеседника из списка приватов
            }
    }


    protected void receiveSystemNotificationToPrivate(String destinationClientName, String infoMessage) {
        PrivateView currentPrivateView = model.getPrivates().get(destinationClientName);
        currentPrivateView.writeSystemNotificationToChatTextArea(infoMessage);
        currentPrivateView.setInputUnavailable();
        model.getPrivates().remove(destinationClientName); //удаляем имя собеседника из списка приватов
    }


    public HashMap<String, PrivateView> getPrivatesList(){
        return model.getPrivates();
    }

    public ArrayList<RoomInfo> getRooms() {
        return model.getRooms();
    }

    //------------private--------------------

    //----------------------enter to the room--------------------


    protected void enterToRoom(String roomName, String userName){
        //roomInfoAutoUpdater.setThisWindowIsAlive(false); //завершаем поток автообновления окна
        model.setMyRoom(roomName); //запоминаем выбранную комнату в переменную
        roomView = new RoomView(this); //открываем окно комнаты
        roomView.runView();
        connection.sendMessage(new Message(MessageType.ENTER_ROOM, getMyName(), roomName)); //отправляем на сервер инф. о выбранной комнате


    }

    protected void exitToRoom(String roomName){
        model.setMyRoom(null); //Обнуляем содержимое переменной с выбранной комнатой
        roomView.mainWindow.dispose();
        connection.sendMessage(new Message(MessageType.EXIT_ROOM, getMyName(), roomName)); //отправляем на сервер инф. о выходе

        listOfRoomsView = new ListOfRoomsView(this);
        writeClientNameToWindowTitle(); //обновляем заголовок окна
        updateListOfRoomsServerRequest();

    }

    public void youMustSelectRoomDialog(){
       writeInfoToFooterOfTheListOfRoomsView("Не выбрана комната", "wrong");
    }


    //----------------------enter to the room--------------------

    //----------------------create new room--------------------

    protected void newRoomCreated(String newRoomName){
        connection.sendMessage(new Message(MessageType.NEW_ROOM_REQUEST, getMyName(), newRoomName)); //отправляем на сервер инф. о создании комнаты
    }

    protected void writeInfoToFooterOfTheListOfRoomsView(String infoMessage, String param){
        listOfRoomsView.writeInfoToFooterOfTheListOfRoomsView(infoMessage, param);
    }

    //----------------------create new room--------------------

}
