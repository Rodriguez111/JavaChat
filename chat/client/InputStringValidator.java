package chat.client;

public class InputStringValidator {

   private String stringToValidate;

    public InputStringValidator(String stringToValidate) {
        this.stringToValidate = stringToValidate;
    }


    public int nameValidator(){
        stringToValidate =  stringToValidate.trim();


        if(stringToValidate.isEmpty()){return 1;} //поле не может быть пустым

        else if(stringToValidate.length()<2 || stringToValidate.length()>25){return 2;}//Имя должно быть не менее 2 и не более 25 символов

        else if(!stringToValidate.matches("^[A-zА-я].+")){return 3;} //Имя начинается не с буквы

        else {return 4;}  //Имя подходит

    }

    public boolean chatTextIsBlankValidator(){  //проверяем чтобы сообщение не состояло из одних пробелов
        stringToValidate =  stringToValidate.trim();
        if(stringToValidate.isEmpty()){return false;}
        return true;
    }

    public String chatTextValidator(){  //убираем множественные пробелы и переводы строки, а так же перевод строки в конце сообщения
        //String result = stringToValidate.replaceAll("[\n]{2,}", "\n").replaceAll("[\\s]{2,}", " ");
        String result = stringToValidate.replaceAll("[\n]{2,}", "\n").replaceAll("[\\s]{2,}", " ").replaceAll("$\n", "");
        return result;
    }

}




