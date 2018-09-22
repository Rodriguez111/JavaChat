package chat;

import java.io.*;
import java.net.Socket;

public class Connection implements Serializable, Closeable {
    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());

    }


    public void sendMessage(Message message) {

        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
          ChatUtils.writeToConsole("Не удалось отправить исходящее сообщение "+message.getMessageType());
            e.printStackTrace();
        }

    }


    public Message receiveMessage() {

        try {
            return (Message)ois.readObject();
        } catch (IOException e) {
            ChatUtils.writeToConsole("Не удалось прочитать входящее сообщение");
        } catch (ClassNotFoundException e) {
            ChatUtils.writeToConsole("Не удалось прочитать входящее сообщение, не найден класс");
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        socket.close();
        oos.close();
        ois.close();

    }
}
