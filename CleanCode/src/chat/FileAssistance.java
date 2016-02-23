package chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** This class load and save history to file
 * Created by Антонина on 23.02.16.
 */
public class FileAssistance {
    private Scanner scannerFile;

    public FileAssistance() {
        try {
            scannerFile = new Scanner(new File("Output.json"));
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public Scanner getScannerFile() {
        return scannerFile;
    }

    public ArrayList loadHistory() {
        ArrayList<Message> historyFile = new ArrayList<>();
        return this.fromJsonToJavaFormat(historyFile);
    }

    public ArrayList fromJsonToJavaFormat(ArrayList history) {
        try {
            String historyInJson = scannerFile.nextLine();

            JsonArray messageArray;
            try (JsonReader reader = Json.createReader(new StringReader(historyInJson))) {
                messageArray = reader.readArray();
            }
            Gson gson = new GsonBuilder().create();
            for (JsonValue value : messageArray) {
                history.add(gson.fromJson(value.toString(), Message.class));
            }
        } catch(NoSuchElementException e) {
            System.out.println("History is empty");
        }
        return history;
    }

    public void saveHistory(ArrayList<Message> history) {
        Gson gson = new GsonBuilder().create();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("Output.json", true), "UTF-8")) {
            String historyToFile = (gson.toJson(history, history.getClass()));
            writer.write(historyToFile);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

}
