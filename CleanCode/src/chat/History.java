package chat;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class consists container for saving storage and functions add, search and delete messages from storage.
 * Created by Антонина on 23.02.16.
 */
public class History {
    private ArrayList<Message> storage;
    private FileAssistance fileHelper;

    public History() {
        fileHelper = new FileAssistance();
        storage = fileHelper.loadHistory();
    }

    public void deleteMessage(String id) {
        List<Message> messagesDelete = new ArrayList<>();
        for (Message mes : storage) {
            if (mes.getId().equals(id)) {
                messagesDelete.add(mes);
            }
        }
        for (Message mes: messagesDelete) {
            storage.remove(mes);
        }
    }

    public void addMessage(Message m) {
        storage.add(m);
    }

    public int searchByAuthor(String author) {
        int frequency = 0;
        for(Message m: storage) {
            if (m.getAuthor().equals(author)) {
                System.out.println("chat.Message from this author: " + m.getText() + "\n" +
                        "Time: " + m.getTime());
                frequency ++;
            }
        }
        return frequency;
    }

    public int searchByRegularExpression(String regularExpression) {
        int count = 0;
        Matcher matcher;
        Pattern p = Pattern.compile(regularExpression);
        for (Message mes: storage) {
            matcher = p.matcher(mes.getText());
            while (matcher.find()) {
                System.out.println("Found: " + matcher.group());
                count ++;
            }
        }
        return count;
    }

    public int searchByKeyword(String keyword) {
        int count = 0;
        for (Message mes: storage) {
            if (mes.getText().contains(keyword)) {
                System.out.println(mes.toString() + "\n");
                count ++;
            }
        }
        return count;
    }

    public void showAllHistory() {
        for (Message mes: storage) {
            System.out.println(mes.toString());
        }
    }

    public int showDefiniteHistory(Timestamp dateAfter, Timestamp dateBefore) {
        int count = 0;
        for(Message mes: storage) {
            if (mes.getTime().after(dateAfter)) {
                if (mes.getTime().before(dateBefore)) {
                    System.out.println(mes.toString());
                    count ++;
                }
            }
        }
        return count;
    }

    public void closeHistory() {
        fileHelper.saveHistory(storage);
        fileHelper.getScannerFile().close();
    }
}
