package chat.client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

public class MyDialogWindow extends JDialog implements Runnable {

Controller controller;



public MyDialogWindow(Controller controller){
    this.controller = controller;
}



    private JPanel contentPanel = new JPanel();
    private JTextPane infoPane = new JTextPane();
    private JButton buttonOK = new JButton("Ok");
    private JButton buttonCancel = new JButton("Отмена");
    private JTextField input = new JTextField();
    JLabel lableIcon;
    protected String userName;



    public JTextPane getInfoPane() {
        return infoPane;
    }


    @Override
    public void run() {
        runMyDialogWindow();
    }

    public void runMyDialogWindow() {

        contentPanel.setLayout(null); //устанавливаем на панель менеджер размещение компонентов Это строка должна быть ДО реализации ограничений
        okListener();
        cancelListener();
        windowListener(); //при закрытии окна закрываем программу
        addUserIconConfiguration();
        infoPanelConfiguration();
        inputConfiguration();
        setButtonOKConfiguration();
        setButtonCancelConfiguration();
        mainFrameConfuguration();
        setButtonOKConfiguration();
    }


    private void mainFrameConfuguration(){ //главное окно (внешняя рамка)
        setSize(300,170);
        setContentPane(contentPanel); // добавляем к рамке панель
        setTitle("Введите имя");



        setModal(true); //блокирует родительское окно
        getRootPane().setDefaultButton(buttonOK); //кнопка, которая нажмется при нажатии Enter
        setResizable(false); //запрет на изменение размера
        setVisible(true);
        pack();
    }

    private void infoPanelConfiguration() { //здесь будут писаться сообщения
        infoPane.setEditable(false);
        infoPane.setBackground(new Color(240, 240, 240));
        infoPane.setSize(240, 60);
        infoPane.setLocation(55,4);
        try {
            infoPane.getStyledDocument().insertString(0, "Введите ваше имя в чате", TextStyles.inputName(infoPane));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        contentPanel.add(infoPane);



    }

    private void inputConfiguration() {
        input.setSize(220, 21);
        input.setLocation(50, 70);
        contentPanel.add(input);

    }

    private void setButtonOKConfiguration() { //кнопка Ок
        buttonOK.setSize(50, 25);
        buttonOK.setLocation(50, 105);
        contentPanel.add(buttonOK);
    }

    private void setButtonCancelConfiguration() { //кнопка Отмена
        buttonCancel.setSize(90, 25);
        buttonCancel.setLocation(150, 105);
        contentPanel.add(buttonCancel);

    }

    private void addUserIconConfiguration(){

        URL imageURL = getClass().getResource("res/AddUser.png"); //получаем путь к каталогу с программой
        ImageIcon icon = new ImageIcon(imageURL);
        lableIcon = new JLabel(icon);
        lableIcon.setLocation(5,15);
        lableIcon.setSize(50, 50);
        contentPanel.add(lableIcon);
    }

    private void deniedUserIconConfiguration(){
        lableIcon.setVisible(false);
        URL imageURL = getClass().getResource("res/UserDenied.png"); //получаем путь к каталогу с программой
        ImageIcon icon = new ImageIcon(imageURL);
        lableIcon = new JLabel(icon);
        lableIcon.setLocation(5,15);
        lableIcon.setSize(50, 50);
        contentPanel.add(lableIcon);
    }

    protected void existingUserIconConfiguration(){
        lableIcon.setVisible(false);
        URL imageURL = getClass().getResource("res/UserExists.png"); //получаем путь к каталогу с программой
        ImageIcon icon = new ImageIcon(imageURL);
        lableIcon = new JLabel(icon);
        lableIcon.setLocation(5,15);
        lableIcon.setSize(50, 50);
        contentPanel.add(lableIcon);
    }




    private void okListener(){
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                whenOkPressed();
            }
        });

    }

    private void cancelListener(){
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    System.exit(0);

            }
        });
    }

    private void windowListener(){
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
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



    public  void whenOkPressed() {

            String enteredString = input.getText();
            InputStringValidator stringValidator = new InputStringValidator(enteredString);
            int result = stringValidator.nameValidator();
            if (result == 1) {
                input.setText(null);
                infoPane.setText("Поле не может быть пустым!");
                deniedUserIconConfiguration();
            } else if (result == 2) {
                input.setText(null);
                try {
                    infoPane.setText(null);
                    infoPane.getStyledDocument().insertString(0, "Имя должно быть не менее 2\nи не более 25 символов!", TextStyles.footerWrong(infoPane));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                deniedUserIconConfiguration();
            } else if (result == 3) {
                input.setText(null);
                try {
                    infoPane.setText(null);
                    infoPane.getStyledDocument().insertString(0, "Имя должно начинаться с буквы!", TextStyles.footerWrong(infoPane));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                deniedUserIconConfiguration();
            } else if (result == 4) {
                input.setText(null);

                userName = enteredString;


                controller.sendUserNameToServer(userName); //отправляем имя на сервер

                synchronized (controller){
                    controller.notify();
                }


            }
        }

}
