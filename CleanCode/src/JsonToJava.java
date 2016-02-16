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
    public static void main (String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Chat chat = new Chat();
        String menu = "";
        String command;
        int frequency = 0;
        while (!menu.equals("0")) {
            Message m = new Message();
            System.out.println("1. Press '1' to start chatting." + "\n"
                    + "2. Press '2' to see history." + "\n"
                    + "3. Press '3' to delete a message. " + "\n"
                    + "4. Press '4' to search by an author." + "\n"
                    + "5. Press '5'to search for regular expressions" + "\n"
                    + "6. Press '6' to search for a key word" + "\n" +
                    "If you want to exit enter '0'");
            menu = in.next();
            switch (menu) {
                case "1":
                    System.out.println("Enter you name. If you exit enter 'help'");
                    menu = in.next();
                    m.setAuthor(menu);
                    System.out.println("Enter you message. If you want to see another options enter 'help'");
                    in.nextLine();
                    m.setMessage(in.nextLine());
                    m.setTime();
                    m.setId();
                    chat.add(m);
                    break;
                case "2":
                    System.out.println("If you want to see all history enter 'all'." + "\n" + "If you want to see history for the definite period enter 'def'");
                    String choice = in.next();
                    switch (choice) {
                        case "all":
                            chat.showHistory();
                            break;
                        case "def":
                            chat.showDefiniteHistory();
                            break;
                    }
                    break;
                case "3":
                    chat.deleteById();
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
            }
        }
        if (menu.equals("0")) {
            chat.getWriterForLogfile().close();
            Gson gson = new GsonBuilder().create();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream("Output.json"), "UTF-8")) {
                String history = (gson.toJson(chat, chat.getClass())).toString();
                writer.write(history);
            }
        }
    }
    }


