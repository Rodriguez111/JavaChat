package chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Message<T> implements Serializable {
    private MessageType messageType;
    private String messageBody;
    private String clientName;
    private Date date = new Date();
    private String roomName;
    private ArrayList<T> list; //список комнат или пользователей



    //для сигнального сообщения - при первичном запросе передаем свое имя
    public Message(MessageType messageType, String clientName) {
        this.messageType = messageType;
        this.clientName = clientName;
    }

   //для сигнального сообщения - пользователь добавлен или покинул чат - передаем сразу обновленный список пользователей
    public Message(MessageType messageType, String clientName,ArrayList<T> list ) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.list = list;
    }




    public Message(MessageType messageType, String clientName, String roomName, String messageBody) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.roomName = roomName;
        this.messageBody = messageBody;
    }


    //конструктор для сигнальных сообщений с передачей имени и комнаты
    public Message(MessageType messageType, String clientName, String roomName) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.roomName = roomName;
    }





    //конструктор для сигнальных сообщений с передачей списка комнат или пользователей
    public Message(MessageType messageType, ArrayList<T> list) {
        this.messageType = messageType;
        this.list = list;
    }


    //конструктор для сигнальных сообщений без передачи имени
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }





    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDate() {
      SimpleDateFormat sfd = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss");
       return sfd.format(date);
    }

    public ArrayList<T> getList() {
        return list;
    }


    public String getRoomName() {
        return roomName;
    }
}