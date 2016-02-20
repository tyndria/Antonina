import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xml.internal.serializer.utils.Messages;

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
    private List<Remark> remarks;
    private Scanner scanner;
    private PrintWriter writerForLogfile;
    public Chat() {
        super();
        try {
            remarks = new ArrayList<Remark>();
            //writerForLogfile = new PrintWriter("logfile.txt");
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
            remarks.add(new Remark("Exception", e.getClass().getName(), "trying to get history", e.getMessage()));
        }
        scanner = new Scanner(System.in);


    }

    public boolean add(Message message) {
        return super.add(message);
    }

    public PrintWriter getWriterForLogfile() {
        return this.writerForLogfile;
    }

    public Scanner getScanner() { return this.scanner;}

    public void showHistory () {
        System.out.println("If you want to see all history enter 'all'." + "\n" + "If you want to see history for the definite period enter 'def'");
        String choice = scanner.next();
        switch (choice) {
            case "all":
                this.showAllHistory();
                break;
            case "def":
                this.showDefiniteHistory();
                break;
        }
    }

    public void showAllHistory() {
        for (Message mes: this) {
            System.out.println(mes.toString());
        }
    }

    public void addMessage() {
        Message m = new Message();
        System.out.println("Enter you name.");
        m.setAuthor(scanner.next());
        scanner.nextLine();
        System.out.println("Enter you message.");
        m.setMessage(scanner.nextLine());
        m.setTime();
        m.setId();
        this.add(m);
    }

    public void showDefiniteHistory() {
        int COUNT_PARAM_TIMESTAMP = 7;
        String[] timestamp = new String[COUNT_PARAM_TIMESTAMP];
        timestamp[0] = "1970";
        for (int i = 1; i < timestamp.length - 1; i ++) {
            timestamp[i] = "01";
        }
        timestamp[6] = "0";
        scanner.nextLine();
        System.out.println("Enter the date after what you want to see history. You can write '2015' or '2015 02' or '2015 02 14' and etc. To separate use only ' '");
        Timestamp dateAfter = fromStringToTimestamp(scanner.nextLine(), timestamp);
        scanner.nextLine();
        System.out.println("Enter the date before what you want to see history. You can write '2015' or '2015 02' or '2015 02 14' and etc. To separate use only ' '");
        Timestamp dateBefore =  fromStringToTimestamp(scanner.nextLine(), timestamp);
        if (this.isEmpty()) {
            System.out.println("History is empty");
            remarks.add(new Remark("warning", "no name", "trying to get history", "history is empty"));
        }
        int frequency = 0;
        for(Message mes: this) {
            if (mes.getTime().after(dateAfter))
                if (mes.getTime().before(dateBefore)) {
                    System.out.println(mes.toString());
                    frequency ++;
                }
        }
        remarks.add(new Remark("request", "search messages for the definite period", "from " + dateAfter.toString() + " to " + dateBefore, "number of founded messages is " + Integer.toString(frequency)));
    }

    public void deleteById() {
        Scanner in = new Scanner(System.in);
        int flag = 0;
        String idForDelete;
        System.out.println("Enter id to delete message");
        idForDelete = in.next();
        List<Message> messagesForDelete = new ArrayList<Message>();
        for (Message mes : this) {
            if (mes.getId().equals(idForDelete)) {
                messagesForDelete.add(mes);
                flag = 1;
            }
        }
        for (Message mes: messagesForDelete) {
            this.remove(mes);
        }
        if (flag == 1) {
            System.out.println("The message with such id was deleted");
            remarks.add(new Remark("request", "delete the message by id", idForDelete,"the message with such id was deleted" ));
        }
        else {
            remarks.add(new Remark("request", "delete the message by id", idForDelete,"the message with such id wasn't deleted because of wrong id" ));
            System.out.println("Wrong id");
        }
    }

    public void searchByAuthor() {
        int frequency = 0;
        String authorForSearch;
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
            remarks.add(new Remark("request", "search for author", authorForSearch, "number of the elements with such author: " + Integer.toString(frequency)));
        }
        else {
            System.out.println("Wrong author");
            remarks.add(new Remark("request", "search for author", authorForSearch, "no such author"));
        }
    }

    public void searchByRegularExpression() {
        int frequency = 0;
        String stringForPattern;
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
            remarks.add(new Remark("request", "search for regular expressions", stringForPattern, "no such expressions"));
        }
        else {
            remarks.add((new Remark("request", "search for regular expressions", stringForPattern, "number of the elements: " + Integer.toString(frequency))));
        }
    }

    public void searchByKeyword() {
        int frequency = 0;
        String wordForSearch;
        System.out.println("Enter key word you want to find");
        wordForSearch = scanner.next();
        for (Message mes: this) {
            if (mes.getMessage().contains(wordForSearch)) {
                System.out.println(mes.toString() + "\n");
                frequency ++;
            }
        }
        if (frequency > 0) {
            remarks.add(new Remark("request", "search for key word", wordForSearch, "number of the messages with such word: " + Integer.toString(frequency)));
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

    public void writeToFile() throws IOException {
        //this.getWriterForLogfile().close();
        Gson gson = new GsonBuilder().create();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("Output.json"), "UTF-8")) {
            String history = (gson.toJson(this, this.getClass())).toString();
            writer.write(history);
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("logfile.txt", true), "UTF-8")) {
            writer.write(remarks.toString());
        }
    }
}
