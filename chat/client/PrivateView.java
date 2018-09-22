package chat.client;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;


public class PrivateView extends RoomView {

    private final String destinationClientName;

    protected Listeners listeners;
    protected JFrame privateWindow ;



    public PrivateView(Controller controller, String destinationClientName) {
        super(controller);
        this.privateWindow = new JFrame();
        this.destinationClientName = destinationClientName;
        this.privateWindow.setTitle("Ваше имя "+controller.getMyName()+" Приватный чат с "+destinationClientName);
        this.listeners = new Listeners(controller, this);
        runView();
    }

    @Override
    protected void runView() {
        layoutForMainWindow(privateWindow);
        mainWindowConfiguration(privateWindow);
        chatTextAreaConfiguration(privateWindow);
        inputMessageFieldConfiguration(privateWindow);


        enterButtonCfg();
        inputMessageField.validate();

        privateWindowListener();
        listeners.inputMessageFieldListener(inputMessageField, 2, destinationClientName);
        listeners.buttonSendMessageListener(sendMessage, inputMessageField, 2, destinationClientName);

    }


    @Override
    protected void mainWindowConfiguration(JFrame mainWindow) {
        mainWindow.setMinimumSize(new Dimension(700,500)); //ограничиваем минимальный размер главного окна
        mainWindow.setPreferredSize(new Dimension(700,500)); //ограничиваем минимальный размер главного окна

        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.pack(); //окно такого размера, чтобы вместить все компоненты
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null); //чтобы окно появлялось в центре, эта строка должна быть последней
    }


    protected void enterButtonCfg(){

        GridBagConstraints constraintsForenterButtonPanel = new GridBagConstraints();
        JPanel panelForEnter = new JPanel();

        panelForEnter.setBackground(new Color(170, 205,238));

        constraintsForenterButtonPanel.gridx=1;
        constraintsForenterButtonPanel.gridy=2;

        constraintsForenterButtonPanel.fill = GridBagConstraints.BOTH;
        panelForEnter.add(sendMessage);
        privateWindow.add(panelForEnter,constraintsForenterButtonPanel );
        sendMessage.updateUI();
    }


    protected void privateWindowListener() {

        privateWindow.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                controller.sendSystemNotificationToPrivate(destinationClientName, controller.getMyRoom());
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }


    protected void setInputUnavailable() {
        inputMessageField.setEditable(false);
    }


    protected void writeSystemNotificationToChatTextArea(String systemMessage) {
        try {
            chatTextArea.getStyledDocument().insertString( chatTextArea.getStyledDocument().getLength(), systemMessage+"\n", TextStyles.chatArea(chatTextArea, "systemNotification"));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
