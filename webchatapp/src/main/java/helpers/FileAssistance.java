package helpers;

import models.User;
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

    public static ArrayList getLoginHistory(String path) {

        ArrayList<User> arrayList = new ArrayList<User>();

        StringBuilder jsonArrayString = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.ready()) {
                jsonArrayString.append(reader.readLine());
            }
        } catch (IOException e) {
            System.out.println("Could not parse message.");
        }

        if (jsonArrayString.length() == 0) {
            return null;
        }
        JSONArray jsonArray =  parseJsonString(jsonArrayString.toString());

        for (int i = 0; i < jsonArray.size(); i ++) {
            arrayList.add(getUserFromJSONObject((JSONObject)jsonArray.get(i)));
        }
        return arrayList;
    }

    private static JSONArray parseJsonString(String string) {
        try {
            JSONParser jsonParser = new JSONParser();
            return (JSONArray) jsonParser.parse(string);
        } catch (ParseException e) {
            System.out.println("Could not parse message.");
        }
        return null;
    }

    private static User getUserFromJSONObject(JSONObject jsonObject) {
        String login = (String)jsonObject.get("login");
        String password = (String)jsonObject.get("password");
        String id = (String)jsonObject.get("id");
        return new User(login, password, id);
    }
}

