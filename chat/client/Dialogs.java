package chat.client;

import javax.swing.*;

public class Dialogs {

    private JFrame window;

    public Dialogs(JFrame window) {
        this.window = window;
    }


    public int incomingPrivateRequestWindow (String part1ClientName){
        Object incomingPrivateRequestWindowMutex = new Object();

        synchronized (incomingPrivateRequestWindowMutex){
            return JOptionPane.showConfirmDialog(window,
                    "Пользователь " + part1ClientName + " приглашает Вас в приватный чат.\nВы согласны?",
                    "Приглашение в приват",
                    JOptionPane.YES_NO_OPTION);
        }
    }
    public void yourPrivateWasRejectedWindow(String part2ClientName){
        Object yourPrivateWasRejectedWindowMutex = new Object();

        synchronized (yourPrivateWasRejectedWindowMutex) {
            JOptionPane.showMessageDialog(window,
                    "Пользователь " + part2ClientName + " отказался принять приглашение в приватный чат",
                    "Отказано в привате",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void youHavePrivateChatWithHimAlready(String part2ClientName){

        JOptionPane.showMessageDialog(window,
                "У вас уже открыт приватный чат с пользователем "+part2ClientName,
                "Внимание!",
                JOptionPane.ERROR_MESSAGE);
    }



}
