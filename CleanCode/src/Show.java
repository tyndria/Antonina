/**
 * Created by Антонина on 09.02.16.
 */
import java.io.*;

public class Show {
    public static void main (String[] args) throws IOException {
        Chat chat = new Chat();
        String menu = "";
        while (!menu.equals("0")) {
            System.out.println("1. Press '1' to start chatting." + "\n"
                    + "2. Press '2' to see history." + "\n"
                    + "3. Press '3' to delete a message. " + "\n"
                    + "4. Press '4' to search by an author." + "\n"
                    + "5. Press '5'to search for regular expressions" + "\n"
                    + "6. Press '6' to search for a key word" + "\n"
                    + "7. Press '7' to save all messages to the history" + "\n"
                    + "8. Press '8' to load all messages from the history" + "\n"
                    + "If you want to exit enter '0'");
            menu = chat.getScanner().next();
            switch (menu) {
                case "1":
                    chat.addMessage();
                    break;
                case "2":
                    chat.showHistory();
                    break;
                case "3":
                    chat.deleteMessage();
                    break;
                case "4":
                    chat.searchByAuthor();
                    break;
                case "5":
                    chat.searchByRegularExpression();
                    break;
                case "6":
                    chat.searchByKeyword();
                    break;
                case "7":
                    chat.saveToFile();
                    break;
                case "8":
                    chat.loadHistory();
                    break;
            }
        }
            chat.writeLogfile();
    }
}


