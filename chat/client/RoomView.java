package chat.client;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class RoomView {

    protected final Controller controller;
   Listeners listeners;

    protected JFrame mainWindow = new JFrame();
    protected Dialogs dialogs = new Dialogs(mainWindow);

    protected JTextPane chatTextArea = new JTextPane();//поле сообщений чата
    private JTextArea chatInfo = new JTextArea(1,17);//поле информации о количестве пользователей
    private JList listOfUsers = new JList(); //поле со списком пользователей
    protected JTextArea inputMessageField = new JTextArea(2,10);//поле для ввода текста
    protected JPanel buttonsPanel = new JPanel(); //панель кнопки Отправить
    protected JPanel buttonsPanel2 = new JPanel(); //панель кнопки Выйти из комнаты

    protected JButton sendMessage = new JButton("Отправить");
    protected JButton exitRoom = new JButton("Выйти из комнаты");



    public RoomView(Controller controller) {
        this.controller = controller;
        this.listeners = new Listeners(controller, this);
        mainWindow.setTitle(controller.getMyRoom()+" - Ваше имя в чате: "+controller.getMyName());
    }

    protected void runView(){


        layoutForMainWindow(mainWindow);

        mainWindowConfiguration(mainWindow);

        chatTextAreaConfiguration(mainWindow);


        inputMessageFieldConfiguration(mainWindow);
        chatInfoConfiguration();
        listOfUsersConfiguration();
        buttonPanelConfiguration(sendMessage, exitRoom);


        listeners.listOfUsersListener(listOfUsers);
        listeners.inputMessageFieldListener(inputMessageField, 1, null);
        listeners.buttonSendMessageListener(sendMessage,inputMessageField, 1, null);
        listeners.buttonExitRoomListener(exitRoom);

    }

        protected void layoutForMainWindow(JFrame mainWindow) {

        GridBagLayout gridBagLayout = new GridBagLayout();
        mainWindow.setLayout(gridBagLayout);
    }


        protected void mainWindowConfiguration(JFrame mainWindow){
        mainWindow.setMinimumSize(new Dimension(900,600)); //ограничиваем минимальный размер главного окна
        mainWindow.setPreferredSize(new Dimension(900,600)); //ограничиваем минимальный размер главного окна

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack(); //окно такого размера, чтобы вместить все компоненты
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null); //чтобы окно появлялось в центре, эта строка должна быть последней
    }


        protected void chatTextAreaConfiguration(JFrame mainWindow){


        chatTextArea.setEditable(false);
        chatTextArea.setBackground(new Color(212, 212, 212));


         JScrollPane chatTextAreaPane = new JScrollPane(); //создаем панель прокрутки


        GridBagConstraints constraintsForTextArea = new GridBagConstraints(); //задаем ограничения для панели прокрутки
        constraintsForTextArea.fill = GridBagConstraints.BOTH; //заполнять ячейку по обеим осям

        constraintsForTextArea.gridx = 0; //начинать с 0 по оси x
        constraintsForTextArea.gridy = 0; //начинать с 0 по оси y

        constraintsForTextArea.gridwidth = 2; //занимать по ширине 2 клетки;
        constraintsForTextArea.gridheight = 2; //занимать по высоте 2 клетки;

        constraintsForTextArea.weightx = 1; //растягиваем по x
        constraintsForTextArea.weighty = 1; //растягиваем по y

        mainWindow.add(chatTextAreaPane, constraintsForTextArea); //монтируем панель на главное окно

        chatTextAreaPane.getViewport().add(chatTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) ; //монтируем на панель текстовое поле

    }


    protected void inputMessageFieldConfiguration(JFrame mainWindow){

        inputMessageField.setWrapStyleWord(true);
        inputMessageField.setLineWrap(true); //автоперенос строки
        inputMessageField.setBackground(new Color(212, 212, 212));

        GridBagConstraints constraintsForInputMessageField = new GridBagConstraints();
        constraintsForInputMessageField.fill = GridBagConstraints.BOTH;

        constraintsForInputMessageField.gridx = 0; //начинать с 0 по оси x
        constraintsForInputMessageField.gridy = 2; //начинать с 2 по оси y

        constraintsForInputMessageField.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForInputMessageField.gridheight = 1; //занимать по высоте 1 клетки;

        constraintsForInputMessageField.weightx = 1; //область ввода текста растягиваем только по горизонтали
        constraintsForInputMessageField.weighty = 0;

        JScrollPane jScrollPaneInputMessageField = new JScrollPane(inputMessageField);
        jScrollPaneInputMessageField.setPreferredSize(new Dimension(580,35));//Предпочитаемый размер поля ввода текста.
        jScrollPaneInputMessageField.setMinimumSize(new Dimension(580,35));//Минимальный размер поля ввода текста.
        mainWindow.add(jScrollPaneInputMessageField,constraintsForInputMessageField);

        jScrollPaneInputMessageField.updateUI();// без этого не прорисовывается в досернем окне PrivateView
        inputMessageField.updateUI(); // без этого не прорисовывается в досернем окне PrivateView

    }


    protected void chatInfoConfiguration() {
        chatInfo.setEditable(false);
        chatInfo.setBackground(new Color(212, 212, 212));
        chatInfo.setFont(new Font("Serif", Font.BOLD, 14));
        chatInfo.setText(" Пользователей в чате: ");

        GridBagConstraints constraintsСhatInfo = new GridBagConstraints();
        constraintsСhatInfo.fill = GridBagConstraints.BOTH; //заполнять ячейку по обеим осям

        constraintsСhatInfo.gridx = 2; //начинать с 1 по оси x
        constraintsСhatInfo.gridy = 0; //начинать с 0 по оси y

        constraintsСhatInfo.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsСhatInfo.gridheight = 1; //занимать по высоте 1 клетки;

        constraintsСhatInfo.weightx = 0; //не растягиваем по x
        constraintsСhatInfo.weighty = 0; //не растягиваем по y



        JScrollPane jScrollPaneChatInfo = new JScrollPane(chatInfo);
        jScrollPaneChatInfo.setPreferredSize(new Dimension(180,25));//ограничиваем размеры скролл-поля для информации.
        jScrollPaneChatInfo.setMinimumSize(new Dimension(180,25));//Минимальный размер скролл-поля для информации.
        jScrollPaneChatInfo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //запрещаем горизонтальную полосу прокрутки
        mainWindow.add(jScrollPaneChatInfo, constraintsСhatInfo);

    }

    protected void listOfUsersConfiguration(){
        listOfUsers.setBackground(new Color(212, 212, 212));
        listOfUsers.setFont(new Font("Arial", Font.PLAIN, 12));

        GridBagConstraints constraintsForListOfUsers = new GridBagConstraints();
        constraintsForListOfUsers.fill = GridBagConstraints.BOTH;

        constraintsForListOfUsers.gridx = 2; //начинать с 1 по оси x
        constraintsForListOfUsers.gridy = 1; //начинать с 1 по оси y

        constraintsForListOfUsers.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForListOfUsers.gridheight = 1; //занимать по высоте 1 клетки;

        constraintsForListOfUsers.weightx = 0; //не растягиваем по x
        constraintsForListOfUsers.weighty = 0; //не растягиваем по y

        JScrollPane jScrollPaneListOfUsers = new JScrollPane(listOfUsers);
        jScrollPaneListOfUsers.setPreferredSize(new Dimension(180,400));//ограничиваем размеры скролл-поля для списка польз.
        jScrollPaneListOfUsers.setMinimumSize(new Dimension(180,400));//ограничиваем размеры скролл-поля для списка польз.

        mainWindow.add(jScrollPaneListOfUsers, constraintsForListOfUsers);
    }



    protected void buttonPanelConfiguration(JButton button1, JButton button2) {

        GridBagConstraints constraintsForButtonPanel1 = new GridBagConstraints();
        constraintsForButtonPanel1.fill = GridBagConstraints.BOTH;

        constraintsForButtonPanel1.gridx = 1; //начинать с 0 по оси x
        constraintsForButtonPanel1.gridy = 2; //начинать с 1 по оси y

        constraintsForButtonPanel1.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForButtonPanel1.gridheight = 1; //занимать по высоте 2 клетки;

        buttonsPanel.add(button1);

        mainWindow.add(buttonsPanel,constraintsForButtonPanel1);


//---------------------------------------

        GridBagConstraints constraintsForButtonPane2 = new GridBagConstraints();
        constraintsForButtonPane2.fill = GridBagConstraints.BOTH;

        constraintsForButtonPane2.gridx = 2; //начинать с 2 по оси x
        constraintsForButtonPane2.gridy = 2; //начинать с 2 по оси y

        constraintsForButtonPane2.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForButtonPane2.gridheight = 1; //занимать по высоте 2 клетки;
        buttonsPanel2.add(button2);

        mainWindow.add(buttonsPanel2,constraintsForButtonPane2);
    }


    public JFrame getMainWindow() {
        return mainWindow;
    }

    public void writeToChatTextArea(String data, String name, String messageBody) {

        try {
            chatTextArea.getStyledDocument().insertString( chatTextArea.getStyledDocument().getLength(), data+" ",TextStyles.chatArea(chatTextArea, "time"));
            chatTextArea.getStyledDocument().insertString( chatTextArea.getStyledDocument().getLength(), name+": ",TextStyles.chatArea(chatTextArea, "name"));
            chatTextArea.getStyledDocument().insertString( chatTextArea.getStyledDocument().getLength(), messageBody+"\n",TextStyles.chatArea(chatTextArea, "message"));
            chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength()); //чтобы автоматически сфокусировался на нижней части текстовой области.!!!

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }




    protected void writeToListOfUsersArea(ArrayList<String> users) {
        DefaultListModel model = new DefaultListModel();
        for(String eachUser: users){
            model.addElement(eachUser);
        }
        listOfUsers.setModel(model);

        //фокусируемся на последнем элементе списка:
        int lastIndex = listOfUsers.getModel().getSize() - 1;
        if (lastIndex >= 0) {
            listOfUsers.ensureIndexIsVisible(lastIndex);
        }

        chatInfo.setText(" Всего в комнате: "+users.size());
    }




    protected void writeSystemNotificationToChatTextArea(String systemMessage) {
                try {
            chatTextArea.getStyledDocument().insertString(chatTextArea.getStyledDocument().getLength(), systemMessage+"\n", TextStyles.chatArea(chatTextArea, "systemNotification"));

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
