package chat.client;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class TextStyles {





    public static Style chatArea(JTextPane textPane, String param){

         StyledDocument document = textPane.getStyledDocument();

        StyledDocument chatTextAreaDocument = textPane.getStyledDocument();

         Style styleOfSystemNotification = textPane.addStyle("systemNotification", null);
         Style styleOfTyme = textPane.addStyle("time", null);
         Style styleOfName = textPane.addStyle("name", null);
         Style styleOfMessage = textPane.addStyle("message", null);


        StyleConstants.setFontSize(styleOfSystemNotification,12); //размер шрифта для системного уведомления
        StyleConstants.setBold(styleOfSystemNotification, true); //жирный шрифт для системного уведомления


        StyleConstants.setFontSize(styleOfTyme,11); //размер шрифта для времени
        StyleConstants.setItalic(styleOfTyme, true); //наклон шрифта для времени

        StyleConstants.setFontSize(styleOfName,16); //размер шрифта для имени
        StyleConstants.setBold(styleOfName, true); //жирный шрифт для имени

        StyleConstants.setFontSize(styleOfMessage,14); //размер шрифта для сообщения

        Style style = null;
        switch (param){
           case "time": style = styleOfTyme;
           break;

            case "name": style =  styleOfName;
            break;

            case "message": style =  styleOfMessage;
            break;

            case "systemNotification": style =  styleOfSystemNotification;
                break;
        }
        return style;

    }


    public static Style inputName(JTextPane textPane){
        StyledDocument headerDocument = textPane.getStyledDocument();
        Style inputNameMessage = textPane.addStyle("inputNameMessage", null);

        StyleConstants.setFontSize(inputNameMessage,13); //размер шрифта
        StyleConstants.setBold(inputNameMessage, true); //жирный шрифт
        StyleConstants.setAlignment(inputNameMessage, StyleConstants.ALIGN_CENTER);
        headerDocument.setParagraphAttributes(0,0,inputNameMessage,false);
        return inputNameMessage;

    }




    public static Style headerFirstLine(JTextPane textPane){
        StyledDocument headerDocument = textPane.getStyledDocument();

        Style firstLine = textPane.addStyle("firstLine", null);

        StyleConstants.setFontSize(firstLine,26); //размер шрифта для первой строки
        StyleConstants.setBold(firstLine, true); //жирный шрифт для первой строки
        StyleConstants.setAlignment(firstLine, StyleConstants.ALIGN_CENTER);
        headerDocument.setParagraphAttributes(0,0,firstLine,false);

        return firstLine;
    }

    public static Style headerSecondLine(JTextPane textPane){
        StyledDocument headerDocument = textPane.getStyledDocument();

        Style secomdLine = textPane.addStyle("secomdLine", null);

        //StyleConstants.setFontFamily(secomdLine,"LucidaSans"); //Тип шрифта для второй строки
        //StyleConstants.setItalic(secomdLine, true); //наклонный шрифт для второй строки
        StyleConstants.setFontSize(secomdLine,18); //размер шрифта для второй строки
        StyleConstants.setBold(secomdLine, true); //жирный шрифт для второй строки
        StyleConstants.setAlignment(secomdLine, StyleConstants.ALIGN_CENTER);
        headerDocument.setParagraphAttributes(0,0,secomdLine,false);
        return secomdLine;
    }








    public static Style footerOk(JTextPane textPane){
        StyledDocument footerDocument = textPane.getStyledDocument();
        Style ok = textPane.addStyle("ok", null);

        StyleConstants.setFontSize(ok,13); //размер шрифта для первой строки
        StyleConstants.setForeground(ok, new Color(11, 175, 0));
        StyleConstants.setBold(ok, true); //жирный шрифт для первой строки
        StyleConstants.setItalic(ok, true); //наклонный
        StyleConstants.setAlignment(ok, StyleConstants.ALIGN_CENTER);
        footerDocument.setParagraphAttributes(0,0,ok,false);
return ok;
    }

    public static Style footerWrong(JTextPane textPane){
        StyledDocument footerDocument = textPane.getStyledDocument();
        Style wrong = textPane.addStyle("wrong", null);

        StyleConstants.setFontSize(wrong,13); //размер шрифта для первой строки
        StyleConstants.setForeground(wrong, new Color(255, 0, 0));
        StyleConstants.setBold(wrong, true); //жирный шрифт для первой строки
        StyleConstants.setItalic(wrong, true); //наклонный
        StyleConstants.setAlignment(wrong, StyleConstants.ALIGN_CENTER);
        footerDocument.setParagraphAttributes(0,0,wrong,false);
        return wrong;
    }


}
