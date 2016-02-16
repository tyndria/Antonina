/**
 * Created by Антонина on 09.02.16.
 */
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


import javax.json.Json;
import javax.json.JsonValue;

public class JsonToJava {
    public static void main (String[] args) throws IOException{
        List<Message> history = new ArrayList<>();
        PrintWriter writerForLogfile = new PrintWriter("logfile.txt");
        List<Remark> remarks = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        String menu = "";
        String command;
        int frequency = 0;
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = new JsonArray();
        while (!menu.equals("0")) {
            Message m = new Message();
            System.out.println("If you want to chat, enter 'chat'." + "\n" + "If you want to see another options enter 'help'" + "\n" +
            "If you want to exit enter '0'");
            menu = in.next();
            switch (menu) {
                case "chat":
                    System.out.println("Enter you name. If you want to see another options enter 'help'");
                    menu = in.next();
                    if (menu.equals("help"))
                        continue;
                    m.setAuthor(menu);
                    System.out.println("Enter you message. If you want to see another options enter 'help'");
                    in.nextLine();
                    m.setMessage(in.nextLine());
                    m.setTime();
                    m.setId();
                    history.add(m);
                    jsonArray.add(gson.toJsonTree(m));
                    try (Writer writer = new OutputStreamWriter(new FileOutputStream("Output.json") , "UTF-8")) {
                        writer.write(jsonArray.toString());
                    }
                    break;
                case "help":
                    System.out.println("1.Press '1' to see history." + "\n" +"2.Press '2' to delete a message. " + "\n" + "3.Press '3' to search by an author." + "\n" + "4.Press '4'" +
                            " to search for regular expressions" + "\n" +
                    "5.Press '5' to search for a key word");
                    command = in.next();
                    switch (command) {
                        case "1":
                            System.out.println("If you want to see all history enter 'all'." + "\n"
                            + "If you want to see history for the definite period enter 'def'");
                            String choice = in.next();
                            switch (choice) {
                                case "all":
                                    try {
                                        Scanner scanner = new Scanner(new File("Output.json"));
                                        String historyInJson = scanner.nextLine();
                                        javax.json.JsonReader reader = Json.createReader(new StringReader(historyInJson));
                                        javax.json.JsonArray messageArray = reader.readArray();
                                        reader.close();
                                        for (JsonValue mes : messageArray) {
                                            System.out.println(mes.toString());
                                        }
                                    }
                                    catch (NoSuchElementException e) {
                                        System.out.println(e.getMessage());
                                        remarks.add(new Remark("Exception", e.getClass().getName(), "trying to get history", e.getMessage()));
                                        writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                                    }
                                    catch (FileNotFoundException e) {
                                        System.out.println(e.getMessage());
                                        remarks.add(new Remark("Exception", e.getClass().getName(), "trying to get history", e.getMessage()));
                                        writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                                    }

                                    break;
                                case "def":
                                    Timestamp dateAfter, dateBefore;
                                    String[] timestamp = new String[7];
                                    for (int i = 0; i < timestamp.length - 1; i ++) {
                                        timestamp[i] = "01";
                                    }
                                    timestamp[6] = "0";
                                    StringTokenizer dateAfterString;
                                    String dateBeforeString;
                                    Scanner newScanner = new Scanner(System.in);
                                    System.out.println("Enter the date after what you want to see history. Format: yyyy [m]m [d]d hh mm ss f");
                                    dateAfterString = new StringTokenizer(newScanner.nextLine());
                                    int i = 0;
                                    while (dateAfterString.hasMoreTokens()) {
                                        timestamp[i] = dateAfterString.nextToken();
                                        i ++;
                                    }
                                    String newDateAfterString = timestamp[0] + "-" + timestamp[1] + "-" + timestamp[2] + " " + timestamp[3] + ":" + timestamp[4] + ":" + timestamp[5] + "." + timestamp[6];
                                    dateAfter = new Timestamp((Timestamp.valueOf(newDateAfterString)).getTime());
                                    System.out.println("Enter the date before what you want to see history. Format: yyyy-[m]m-[d]d hh:mm:ss[.f...]");
                                    dateBeforeString = newScanner.nextLine();
                                    if (history.isEmpty()) {
                                        System.out.println("History is empty");
                                        remarks.add(new Remark("Warning", "No name", "trying to get history", "history is empty"));
                                        writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                                    }
                                    frequency = 0;
                                    for(Message mes: history) {
                                        if (mes.getTime().after(dateAfter))
                                            if (mes.getTime().before(Timestamp.valueOf(dateBeforeString))) {
                                                System.out.println(mes.toString());
                                                frequency ++;
                                            }
                                    }
                                    remarks.add(new Remark("Request", "search messages for the definite period", "from " + dateAfterString.toString() + " to " + dateBeforeString, "number of founded messages is " + Integer.toString(frequency)));
                                    writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                            }
                            break;
                        case "2":
                            int flag = 0;
                            String idForDelete = "";
                            System.out.println("Enter id to delete message");
                            idForDelete = in.next();
                            for (Message mes : history)
                                if (mes.getId().equals(idForDelete)) {
                                    history.remove(mes);
                                    flag = 1;
                                    for (JsonElement element: jsonArray) {
                                        if (element.getAsJsonObject().get("id").getAsString().equals(idForDelete))
                                            jsonArray.remove(element);
                                    }
                                }
                            if (flag == 1) {
                                System.out.println("The message with such id was deleted");
                                remarks.add(new Remark("request", "delete the message by id", idForDelete,"The message with such id was deleted" ));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                                try (Writer writer = new OutputStreamWriter(new FileOutputStream("Output.json") , "UTF-8")) {
                                    writer.write(jsonArray.toString());
                                }
                            }
                            else {
                                remarks.add(new Remark("request", "delete the message by id", idForDelete,"The message with such id wasn't deleted because of wrong id" ));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                                System.out.println("Wrong id");
                            }
                            break;
                        case "3":
                            frequency = 0;
                            String authorForSearch = "";
                            Collections.sort(history);
                            System.out.println("Enter author for search");
                            authorForSearch = in.next();
                            int index = Collections.binarySearch(history, new Message(authorForSearch));
                            if (index >= 0) {
                                System.out.println("Message from this author: " + history.get(index).getMessage() + "\n" +
                                        "Time: " + history.get(index).getTime());
                                for(Message mes: history)
                                    if (mes.getAuthor().equals(authorForSearch))
                                        frequency ++;
                                remarks.add(new Remark("Request", "Search for author", authorForSearch, "Number of the elements with such author: " + Integer.toString(frequency)));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                            }
                            else {
                                System.out.println("Wrong author");
                                remarks.add(new Remark("Request", "Search for author", authorForSearch, "No such author"));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                            }

                            break;
                        case "4":
                            frequency = 0;
                            String stringForPattern = "";
                            Matcher matcher;
                            System.out.println("Enter regular expression. For example, '.+@(mail|bk|inbox|list)\\.ru' ");
                            stringForPattern = in.next();
                            Pattern p = Pattern.compile(stringForPattern);
                            for (Message mes: history) {
                                matcher = p.matcher(mes.getMessage());
                                while (matcher.find()) {
                                    System.out.println("Found: " + matcher.group());
                                    frequency ++;
                                }
                            }
                            if (frequency == 0) {
                                System.out.println("No emails");
                                remarks.add(new Remark("Request", "Search for regular expressions", stringForPattern, "No such expressions"));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString() + "\n");
                            }
                            else {
                                remarks.add(new Remark("Request", "Search for regular expressions", stringForPattern, "Number of the elements: " + Integer.toString(frequency)));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                            }
                            break;
                        case "5":
                            frequency = 0;
                            String wordForSearch = "";
                            System.out.println("Enter key word you want to find");
                            wordForSearch = in.next();
                            for (Message mes: history) {
                                if (mes.getMessage().contains(wordForSearch)) {
                                    System.out.println("Message contains " + wordForSearch + ": " + mes.getMessage());
                                    frequency ++;
                                }
                            }
                            if (frequency > 0) {
                                remarks.add(new Remark("Request", "Search for key word", wordForSearch, "Number of the messages with such word: " + Integer.toString(frequency)));
                                writerForLogfile.println(remarks.get(remarks.size() - 1).toString());
                            }
                            break;


                    }
            }



        }
        writerForLogfile.close();
    }

}
