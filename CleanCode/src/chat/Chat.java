package chat;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** This class cooperate
 * Created by Антонина on 16.02.16.
 */

public class Chat{
    private List<Remark> remarks;
    private UserInteraction userReader;
    private History history;
    private int countAddedMessages;
    private int countDeletedMessages;
    public Chat() {
        remarks = new ArrayList<>();
        history = new History();
        userReader = new UserInteraction();
        countAddedMessages = 0;
        countDeletedMessages = 0;
    }

    public void startChat() {
        String menu = "";
        int count;
        while (!menu.equals("0")) {
            menu = userReader.readOptionMenu();
            switch (menu) {
                case "1":
                    history.addMessage(userReader.readMessage());
                    countAddedMessages ++;
                    break;
                case "2":
                    history.showAllHistory();
                    break;
                case "3":
                    history.deleteMessage(userReader.readId());
                    countDeletedMessages ++;
                    break;
                case "4":
                    String author = userReader.readAuthor();
                    count = history.searchByAuthor(author);
                    remarks.add(new Remark("request", "search for author", author, "number of the elements with such author: " + count));
                    break;
                case "5":
                    String regularExpression = userReader.readRegularExpression();
                    count = history.searchByRegularExpression(regularExpression);
                    remarks.add((new Remark("request", "search for regular expressions", regularExpression, "number of the elements: " + count)));
                    break;
                case "6":
                    String wordSearch = userReader.readKeyword();
                    count = history.searchByKeyword(wordSearch);
                    remarks.add(new Remark("request", "search for key word", wordSearch, "number of the messages with such word: " + count));
                    break;
                case "7":
                    String[] timestamp = userReader.getInitialParamTimestamp();
                    Timestamp dateAfter = userReader.readDateAfter(timestamp);
                    Timestamp dateBefore = userReader.readDateBefore(timestamp);
                    count = history.showDefiniteHistory(dateAfter, dateBefore);
                    remarks.add(new Remark("request", "search messages for the definite period", "from "
                            + dateAfter.toString() + " to " + dateBefore, "number of founded messages is " + count));
                    break;
            }
        }
    }

    public void stopChat() {
        remarks.add(new Remark("request", "adding messages", "-", "count of messages: " + countAddedMessages));
        remarks.add(new Remark("request", "deleting messages", "-", "count of messages: " + countDeletedMessages));
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("logfile.txt", true), "UTF-8")) {
            writer.write(remarks.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        history.closeHistory();
        userReader.closeScannerIn();
    }

}
