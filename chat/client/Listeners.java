package chat.client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import java.awt.event.*;

public class Listeners {

    private Controller controller;
    private ListOfRoomsView listOfRoomsView;
    private RoomView roomView;
    private PrivateView privateView;

    public Listeners(Controller controller, ListOfRoomsView listOfRoomsView) {
        this.controller = controller;
        this.listOfRoomsView = listOfRoomsView;
    }


    public Listeners(Controller controller, RoomView roomView) {
        this.controller = controller;
        this.roomView = roomView;
    }

    public Listeners(Controller controller, PrivateView privateView) {
        this.controller = controller;
        this.privateView = privateView;
    }

    private String selectedRow = null;



    protected void enterButtonListener (JTable table, JButton button) {


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                /*
                if (!e.getValueIsAdjusting()) -
                Список позволяет выбирать сразу несколько строк. У ListSelectionEvent есть метод getValueIsAdjusting который возвращает true
                если событие вызывается по причине выбора нескольких пунктов, если нам не нужно отслеживать выделение нескольких строк, то
                пишем такое условие
                 */

    if(!table.getSelectionModel().isSelectionEmpty()) {
        if (!e.getValueIsAdjusting()) {

            selectedRow = (String) table.getValueAt(table.getSelectedRow(), 0);

        }
    }
            }
        });


        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectedRow == null) {
                      controller.youMustSelectRoomDialog();
                  } else {
                      listOfRoomsView.selectingRoom(selectedRow);
                  }


            }
        });
    }

    protected void createButtonListener (JTextField inputField, JButton button, JTextPane textInfoPane){



        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String newRoomName = inputField.getText().trim();

                if(newRoomName.isEmpty()){
                    textInfoPane.setText(null);
                    try {
                        textInfoPane.getStyledDocument().insertString(0,"Название комнаты не может быть пустым", TextStyles.footerWrong(textInfoPane) );
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }

                }
                else if(newRoomName.length()<5 || newRoomName.length()>25){
                    textInfoPane.setText(null);
                    try {
                        textInfoPane.getStyledDocument().insertString(0,"Название комнаты должно быть не короче 5 и не длиннее 25 символов", TextStyles.footerWrong(textInfoPane) );
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }

                }
                else {
                    inputField.setText(null);
                    controller.newRoomCreated(newRoomName); //вызываем метод создания комнаты
                }


            }
        });

    }

    protected void updateButtonListener (JButton button){
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.updateListOfRoomsServerRequest();

            }
        });

    }



    protected void tableListener(JTable table){

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){

                    listOfRoomsView.selectingRoom((String) table.getValueAt(table.getSelectedRow(), 0));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    //------------------------------------------------------------------------



    protected void listOfUsersListener(JList list){
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    if(!list.getSelectedValue().equals(controller.getMyName())) { //чтобы не вызвать в приват самого себя
                        if(!controller.getPrivatesList().containsKey(list.getSelectedValue())) { //если в списке приватных чатов отсутсвутет вызываемый пользователь
                            controller.outcomingPrivateRequest((String) list.getSelectedValue(), controller.getMyRoom());
                        }
                        else { //если же такой пользователь в списке уже есть - отказываем
                            roomView.dialogs.youHavePrivateChatWithHimAlready((String)list.getSelectedValue());
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }



    protected void inputMessageFieldListener(JTextArea jTextArea, int owner, String destinationClientName){

        jTextArea.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    if(new InputStringValidator(jTextArea.getText()).chatTextIsBlankValidator()) { //проверка валидатором не пустое ли сообщение
                        //Заменяем множественные переводы строки и пробелы на одинарные:
                        String string = new InputStringValidator(jTextArea.getText()).chatTextValidator();
                        switch (owner){
                            case (1):controller.sendMainMessage(string); //отправляем в общий чат
                                break;

                            case (2):controller.sendMessageToPrivateChat(destinationClientName, string); //отправляем в приват
                                //System.out.println("Мы зжесь");
                                break;
                        }
                        jTextArea.setText(null);
                        e.consume(); //пропустить событие
                    }
                }
            }
        });
    }



    //owner - метка, обозначающая какое окно обслуживает данный Listener
    //если owner = 1 - окно общего чата, если 2 - окно приватного чата. В соответствии с этим определяется куда отправлять полученный текст
    //destinationClientName - имя собеседника, которому отправлять приватное сообщение (только для приватного окна)

    protected void buttonSendMessageListener(JButton button, JTextArea jTextArea, int owner, String destinationClientName){
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(new InputStringValidator(jTextArea.getText()).chatTextIsBlankValidator()) { //проверка валидатором не пустое ли сообщение
                    //Заменяем множественные переводы строки и пробелы на одинарные:
                    String string = new InputStringValidator(jTextArea.getText()).chatTextValidator();
                    switch (owner){
                        case (1):controller.sendMainMessage(string); //отправляем в общий чат
                        break;

                        case (2):controller.sendMessageToPrivateChat(destinationClientName, string); //отправляем в приват
                            break;
                    }

                    jTextArea.setText(null);
                }

            }
        });
    }






    protected void buttonExitRoomListener(JButton button){
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               controller.exitToRoom(controller.getMyRoom());

            }
        });

    }


}
