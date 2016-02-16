import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonArray;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Антонина on 16.02.16.
 */

public class Chat extends ArrayList<Message>{
    private Scanner scanner;
    private PrintWriter writerForLogfile;
    public Chat() {
        super();
        try {
            writerForLogfile = new PrintWriter("logfile.txt");
            Scanner scannerFile = new Scanner(new File("Output.json"));
            String historyInJson = scannerFile.nextLine();
            JsonReader reader = Json.createReader(new StringReader(historyInJson));
            JsonArray messageArray = reader.readArray();
            reader.close();
            Gson gson = new GsonBuilder().create();
            for (JsonValue value: messageArray) {
                this.add(gson.fromJson(value.toString(), Message.class));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            writerForLogfile.println(new Remark("Exception", e.getClass().getName(), "trying to get history", e.getMessage()));
        }
        scanner = new Scanner(System.in);


    }

    @Override
    public boolean add(Message message) {
        return super.add(message);
    }

    public PrintWriter getWriterForLogfile() {
        return this.writerForLogfile;
    }

    public void showHistory() {
       System.out.println(this.toString());
    }

    public void showDefiniteHistory() {
        int COUNT_PARAM_TIMESTAMP = 7;
        String[] timestamp = new String[COUNT_PARAM_TIMESTAMP];
        timestamp[0] = "1970";
        for (int i = 1; i < timestamp.length - 1; i ++) {
            timestamp[i] = "01";
        }
        timestamp[6] = "0";
        System.out.println("Enter the date after what you want to see history. Format: yyyy [m]m [d]d hh mm ss f");
        Timestamp dateAfter = fromStringToTimestamp(scanner.nextLine(), timestamp);
        System.out.println("Enter the date before what you want to see history. Format: yyyy-[m]m-[d]d hh:mm:ss[.f...]");
        Timestamp dateBefore =  fromStringToTimestamp(scanner.nextLine(), timestamp);
        if (this.isEmpty()) {
            System.out.println("History is empty");
            writerForLogfile.println(new Remark("Warning", "No name", "trying to get history", "history is empty"));
        }
        int frequency = 0;
        for(Message mes: this) {
            if (mes.getTime().after(dateAfter))
                if (mes.getTime().before(dateBefore)) {
                    System.out.println(mes.toString());
                    frequency ++;
                }
        }
        writerForLogfile.println(new Remark("Request", "search messages for the definite period", "from " + dateAfter.toString() + " to " + dateBefore, "number of founded messages is " + Integer.toString(frequency)));
    }

    public void deleteById() {
        Scanner in = new Scanner(System.in);
        int flag = 0;
        String idForDelete = "";
        System.out.println("Enter id to delete message");
        idForDelete = in.next();
        for (Message mes : this)
            if (mes.getId().equals(idForDelete)) {
                this.remove(mes);
                flag = 1;
            }
        if (flag == 1) {
            System.out.println("The message with such id was deleted");
            writerForLogfile.println(new Remark("request", "delete the message by id", idForDelete,"The message with such id was deleted" ));
        }
        else {
            writerForLogfile.println(new Remark("request", "delete the message by id", idForDelete,"The message with such id wasn't deleted because of wrong id" ));
            System.out.println("Wrong id");
        }
    }

    public void searchByAuthor() {
        int frequency = 0;
        String authorForSearch = "";
        System.out.println("Enter author for search");
        authorForSearch = scanner.next();
        for(Message m: this) {
            if (m.getAuthor().equals(authorForSearch)) {
                System.out.println("Message from this author: " + m.getMessage() + "\n" +
                        "Time: " + m.getTime());
                frequency ++;
            }
        }
        if (frequency > 0) {
            writerForLogfile.println(new Remark("Request", "Search for author", authorForSearch, "Number of the elements with such author: " + Integer.toString(frequency)));
        }
        else {
            System.out.println("Wrong author");
            writerForLogfile.println(new Remark("Request", "Search for author", authorForSearch, "No such author"));
        }
    }

    public void searchByRegularExpression() {
        int frequency = 0;
        String stringForPattern = "";
        Matcher matcher;
        System.out.println("Enter regular expression. For example, '.+@(mail|bk|inbox|list)\\.ru' ");
        stringForPattern = scanner.next();
        Pattern p = Pattern.compile(stringForPattern);
        for (Message mes: this) {
            matcher = p.matcher(mes.getMessage());
            while (matcher.find()) {
                System.out.println("Found: " + matcher.group());
                frequency ++;
            }
        }
        if (frequency == 0) {
            System.out.println("No emails");
            writerForLogfile.println(new Remark("Request", "Search for regular expressions", stringForPattern, "No such expressions"));
        }
        else {
            writerForLogfile.println((new Remark("Request", "Search for regular expressions", stringForPattern, "Number of the elements: " + Integer.toString(frequency))));
        }
    }

    public void searchByKeyword() {
        int frequency = 0;
        String wordForSearch = "";
        System.out.println("Enter key word you want to find");
        wordForSearch = scanner.next();
        for (Message mes: this) {
            if (mes.getMessage().contains(wordForSearch)) {
                System.out.println("Message contains " + wordForSearch + ": " + mes.getMessage());
                frequency ++;
            }
        }
        if (frequency > 0) {
            writerForLogfile.println(new Remark("Request", "Search for key word", wordForSearch, "Number of the messages with such word: " + Integer.toString(frequency)));
        }
    }

    private Timestamp fromStringToTimestamp(String date, String[] timestamps) {
        StringTokenizer dateStringTokenizer = new StringTokenizer(date);
        int i = 0;
        while (dateStringTokenizer.hasMoreTokens()) {
            timestamps[i] = dateStringTokenizer.nextToken();
            i ++;
        }
        String dateString = timestamps[0] + "-" + timestamps[1] + "-" + timestamps[2] + " " + timestamps[3] + ":" + timestamps[4] + ":" + timestamps[5] + "." + timestamps[6];
        return new Timestamp((Timestamp.valueOf(dateString)).getTime());
    }
}
