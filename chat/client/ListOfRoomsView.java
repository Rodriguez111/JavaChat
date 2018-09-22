package chat.client;

import chat.RoomInfo;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.*;
import java.awt.*;

import java.net.URL;
import java.util.ArrayList;

public class ListOfRoomsView  {

    protected final Controller controller;

    protected JFrame mainWindow = new JFrame("Список комнат чата");
    Listeners listeners;
    protected JTable tableOfRooms = new JTable();




    private JLabel headerLabel = new JLabel();  //наклейка для картинки хедера
    private JScrollPane tableScrollPane = new JScrollPane(tableOfRooms);
    private JTextPane footer = new JTextPane();
    private JPanel buttonPanel = new JPanel();

    private JButton buttonEnter = new JButton("Войти");
    private JButton buttonCreate = new JButton("Создать комнату");
    private JButton buttonUpdate = new JButton("Обновить");
    protected JTextField inputField = new JTextField();


    public ListOfRoomsView(Controller controller) {
        this.controller = controller;
        this.listeners = new Listeners(controller, this);
        runListOfRooms();
    }



    private void runListOfRooms(){

        mainWindowConfiguration(mainWindow);
        headerLabelConfiguration();
        tableConfiguration();
        footerConfiguration();
        buttonPanelConfiguration();




        listeners.tableListener(tableOfRooms);
        listeners.enterButtonListener(tableOfRooms, buttonEnter);
        listeners.createButtonListener(inputField,  buttonCreate, footer);
        listeners.updateButtonListener(buttonUpdate);

    }



    protected void mainWindowConfiguration(JFrame mainWindow) {
        mainWindow.setResizable(false);
        mainWindow.setMinimumSize(new Dimension(800,600));
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setLayout(new GridBagLayout()); //устанавливаем менеджер размещения объектоа в окне


        GridBagConstraints constraintsForHeaderLabel = new GridBagConstraints(); //для верхней наклейки
        constraintsForHeaderLabel.gridx = 0; //начинать с 0 по оси x
        constraintsForHeaderLabel.gridy = 0; //начинать с 0 по оси y

        constraintsForHeaderLabel.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForHeaderLabel.gridheight = 1; //занимать по высоте 1 клетку;
        constraintsForHeaderLabel.fill = GridBagConstraints.BOTH; //заполнять ячейку по обеим осям

        constraintsForHeaderLabel.insets = new Insets(0, 0, 7, 0);//отступы от краев

        mainWindow.add(headerLabel, constraintsForHeaderLabel); //добавили к главному окну верхнюю панель


        GridBagConstraints constraintsForTable = new GridBagConstraints(); //для таблицы списка комнат
        constraintsForTable.gridx = 0; //начинать с 0 по оси x
        constraintsForTable.gridy = 1; //начинать с 0 по оси y

        constraintsForTable.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForTable.gridheight = 1; //занимать по высоте 1 клетку;

        //constraintsForTable.fill = GridBagConstraints.BOTH; //заполнять ячейку по обеим осям

        mainWindow.add(tableScrollPane, constraintsForTable); //добавили к главному окну верхнюю панель


        GridBagConstraints constraintsForFooter = new GridBagConstraints(); //для футера
        constraintsForTable.gridx = 0; //начинать с 0 по оси x
        constraintsForTable.gridy = 2; //начинать с 2 по оси y

        constraintsForTable.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForTable.gridheight = 1; //занимать по высоте 1 клетку;
        constraintsForTable.fill = GridBagConstraints.BOTH; //заполнять ячейку по обеим осям

        mainWindow.add(footer, constraintsForTable); //добавили к главному окну верхнюю панель



        GridBagConstraints constraintsForButtonPanel = new GridBagConstraints();
        constraintsForButtonPanel.gridx = 0; //начинать с 0 по оси x
        constraintsForButtonPanel.gridy = 3; //начинать с 3 по оси y

        constraintsForButtonPanel.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForButtonPanel.gridheight = 1; //занимать по высоте 1 клетку;

        constraintsForButtonPanel.fill = GridBagConstraints.BOTH; //заполнять ячейку по обеим осям
        mainWindow.add(buttonPanel, constraintsForButtonPanel); //добавили к главному окну панель кнопок

        mainWindow.setVisible(true);

    }


    protected void headerLabelConfiguration(){
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("res/header.png")).getImage().getScaledInstance(780, 100, Image.SCALE_DEFAULT));
        headerLabel.setIcon(imageIcon); //нарисовали на наклейке картинку

    }




    protected void tableConfiguration(){

        tableScrollPane.setPreferredSize(new Dimension(740, 390));
        tableScrollPane.setMinimumSize(new Dimension(740, 390));
        tableScrollPane.setMaximumSize(new Dimension(740, 390));


        tableOfRooms.setShowGrid(false);
    }







    private void logoConfiguration(){

//        URL imageURL = getClass().getResource("res/Chat.png"); //получаем путь к каталогу с картинкой
//        ImageIcon icon = new ImageIcon(imageURL);
//        lableIcon = new JLabel(icon);
//
//        lableIcon.setPreferredSize(new Dimension(100,100));
//        lableIcon.setMinimumSize(new Dimension(100,100));
//        lableIcon.setMaximumSize(new Dimension(100,100));

    }


    protected void footerConfiguration(){
        footer.setEditable(false);
        footer.setPreferredSize(new Dimension(700,30));
        footer.setMinimumSize(new Dimension(700,30));
        footer.setMaximumSize(new Dimension(700,30));
        footer.setBackground(new Color(234, 234, 234));
    }






    protected void buttonPanelConfiguration() {

        buttonPanel.setLayout(new GridBagLayout()); //устанавливаем менеджер размещения объектов (кнопок) на панели

        GridBagConstraints constraintsForEnterButton = new GridBagConstraints(); //для кнопки Войти
        constraintsForEnterButton.gridx = 0; //начинать с 0 по оси x
        constraintsForEnterButton.gridy = 0; //начинать с 3 по оси y
        constraintsForEnterButton.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForEnterButton.gridheight = 1; //занимать по высоте 1 клетку;
        constraintsForEnterButton.insets = new Insets(0,0,0,120);
        buttonPanel.add(buttonEnter, constraintsForEnterButton);

        GridBagConstraints constraintsForCreateButton = new GridBagConstraints(); //для кнопки Создать комнату
        constraintsForCreateButton.gridx = 1; //начинать с 0 по оси x
        constraintsForCreateButton.gridy = 0; //начинать с 0 по оси y
        constraintsForCreateButton.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForCreateButton.gridheight = 1; //занимать по высоте 1 клетку;
        constraintsForCreateButton.insets = new Insets(0,-50,0,5);
        buttonPanel.add(buttonCreate, constraintsForCreateButton);

        GridBagConstraints constraintsForInputField = new GridBagConstraints(); //для поля ввода названия комнаты
        constraintsForInputField.gridx = 2; //начинать с 0 по оси x
        constraintsForInputField.gridy = 0; //начинать с 0 по оси y
        constraintsForInputField.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForInputField.gridheight = 1; //занимать по высоте 1 клетку;
        constraintsForInputField.insets = new Insets(0,0,0,30);

        inputField.setMinimumSize(new Dimension(220, 22));
        inputField.setPreferredSize(new Dimension(220, 22));
        inputField.setMaximumSize(new Dimension(220, 22));
        buttonPanel.add(inputField, constraintsForInputField);


        GridBagConstraints constraintsForUpdateButton = new GridBagConstraints(); //для кнопки Обновить
        constraintsForUpdateButton.gridx = 3; //начинать с 0 по оси x
        constraintsForUpdateButton.gridy = 0; //начинать с 0 по оси y
        constraintsForUpdateButton.gridwidth = 1; //занимать по ширине 1 клетки;
        constraintsForUpdateButton.gridheight = 1; //занимать по высоте 1 клетку;
        constraintsForUpdateButton.insets = new Insets(0,20,0,0);
        buttonPanel.add(buttonUpdate, constraintsForUpdateButton);

    }



    public void writeToListOfRooms(ArrayList<RoomInfo> rooms){
        TableModel model = new TableModel(){

            @Override
            public int getRowCount() {
                return rooms.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int columnIndex) {
                String columnName = null;
                if(columnIndex == 0 )columnName ="Название комнаты";
                else if(columnIndex == 1 )columnName ="Кол-во участников";
                return columnName;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 0)return String.class;
                else if(columnIndex == 1)return Integer.class;
                return null;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if(columnIndex == 0){
                    //return rooms.get(rowIndex).getName();
                    return controller.getRooms().get(rowIndex).getTheNameOfTheRoom();
                }
                if(columnIndex == 1){
                    //return rooms.get(rowIndex).getCountOfUsers();
                    return controller.getRooms().get(rowIndex).getCountOfUsers();
                }

                return null;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        };
        int rowIndex=0;
        int columnIndex=0;
        model.getColumnName(0);
        model.getColumnName(1);

        for(RoomInfo element : rooms){
            model.getValueAt(0,0);
            rowIndex++;
            columnIndex++;

        }
        tableOfRooms.setModel(model);
        tableOfRooms.getColumnModel().getColumn(0).setMinWidth(400); //задаем минимальную ширину 1 столбца
        tableOfRooms.getColumnModel().getColumn(1).setMinWidth(120);//задаем минимальную ширину 2 столбца

    }




    protected void writeClientNameToWindowTitle(String name){
        mainWindow.setTitle("-=Список комнат чата=-  Ваше имя в чате: "+name);

    }


    protected void writeInfoToFooterOfTheListOfRoomsView(String infoMessage, String param) {
        footer.setText(null);
if(param.equals("ok")) {
    try {
        footer.getStyledDocument().insertString(0, infoMessage, TextStyles.footerOk(footer));
    } catch (BadLocationException e1) {
        e1.printStackTrace();
    }
}
 else{
    try {
        footer.getStyledDocument().insertString(0, infoMessage, TextStyles.footerWrong(footer));
    } catch (BadLocationException e1) {
        e1.printStackTrace();
    }
 }
}


    protected void selectingRoom(String roomName){
      String selectedRoomName =(String)tableOfRooms.getValueAt(tableOfRooms.getSelectedRow(), 0);
      controller.enterToRoom(selectedRoomName, controller.getMyName());
      mainWindow.dispose(); //закрываем окно выбора комнаты
  }


}
