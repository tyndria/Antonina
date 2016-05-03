package main.bsu.fpmi.helper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Антонина on 29.04.16.
 */
public class FileAssistance {
    static Scanner scanner;

    public static ArrayList getLoginsHistory() {
        ArrayList<User> userArrayList = new ArrayList<User>();
        try {

            File file = new File("C:\\Users\\Антонина\\Documents\\Practice\\webchatapp\\src\\usersFile.txt");
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String login = scanner.next();
                String password = scanner.next();
                userArrayList.add(new User(login, password));
            }
        } catch (IOException e) {
            String s = new File(".").getAbsoluteFile().toString();
        }
        return userArrayList;
    }

    public static ArrayList getLoginHistory() {
        ArrayList<User> arrayList = new ArrayList<User>();

        StringBuilder jsonArrayString = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Антонина\\Documents\\Practice\\webchatapp\\src\\usersFile.txt"));
            while (reader.ready()) {
                jsonArrayString.append(reader.readLine());
            }
        } catch (IOException e) {
            System.out.println("Could not parse message.");
        }

        JSONArray jsonArray = new JSONArray();

        if (jsonArrayString.length() == 0) {
            return null;
        }

        try {
            JSONParser jsonParser = new JSONParser();
            jsonArray = (JSONArray) jsonParser.parse(jsonArrayString.toString());
        } catch (ParseException e) {
            System.out.println("Could not parse message.");
        }

        for (int i = 0; i < jsonArray.size(); i ++) {
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            String login = (String)jsonObject.get("login");
            String password = (String)jsonObject.get("password");
            arrayList.add(new User(login, password));
        }
        return arrayList;
    }
}
