package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.utils.MessageHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages;

    public InMemoryMessageStorage() {
        messages = new ArrayList<>();
        loadHistory();

    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        rewriteHistory();
    }

    @Override
    public boolean updateMessage(Message message) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().compareTo(message.getId()) == 0) {
                Message newMessage = messages.get(i);
                if (newMessage.isDeleted())
                    return false;
                if (newMessage.isEdited()) {
                    newMessage.setWasEdited(true);
                }
                newMessage.setText(message.getText());
                newMessage.setEdited(true);
                messages.set(i, newMessage);
                rewriteHistory();
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().compareTo(Long.parseLong(messageId)) == 0) {
                Message newMessage = messages.get(i);
                newMessage.setText("");
                newMessage.setDeleted(true);
                newMessage.setEdited(false);
                newMessage.setWasEdited(false);
                messages.set(i, newMessage);
                rewriteHistory();
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }

    private boolean rewriteHistory() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(DEFAULT_PERSISTENCE_FILE), "UTF-8")) {
            JSONArray array = MessageHelper.getJsonArrayOfMessages(messages);
            writer.write(array.toString());
            return true;
        } catch (IOException e) {
            logger.error("Could not parse message.", e);
            return false;
        }
    }

    private void loadHistory() {
        StringBuilder jsonArrayString = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(DEFAULT_PERSISTENCE_FILE))) {
            while (reader.ready()) {
                jsonArrayString.append(reader.readLine());
            }
        } catch (IOException e) {
            logger.error("Could not parse message.", e);
        }

        JSONArray jsonArray = new JSONArray();

        if (jsonArrayString.length() == 0) {
            return;
        }

        try {
            jsonArray = (JSONArray) MessageHelper.getJsonParser().parse(jsonArrayString.toString());
        } catch (ParseException e) {
            logger.error("Could not parse message.", e);
        }

        for (int i = 0; i < jsonArray.size(); i ++) {
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            Message message = MessageHelper.jsonObjectToMessage(jsonObject);
            messages.add(message);
        }
    }
}