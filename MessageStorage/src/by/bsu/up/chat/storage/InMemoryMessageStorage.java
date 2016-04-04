package by.bsu.up.chat.storage;

import by.bsu.up.chat.Constants;
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
            if (messages.get(i).getId().equals(message.getId())) {
                Message newMessage = messages.get(i);
                newMessage.setText(message.getText());
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
            if (messages.get(i).getId().equals(messageId)) {
                messages.remove(i);
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
        try(BufferedReader reader = new BufferedReader(new StringReader(DEFAULT_PERSISTENCE_FILE))) {
            String jsonArrayString = reader.readLine();
            JSONArray jsonArray = (JSONArray) MessageHelper.getJsonParser().parse(jsonArrayString);
            for (int i = 0; i < jsonArray.size(); i ++) {
                Message message = new Message();
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                message.setText((String)jsonObject.get(Constants.Message.FIELD_TEXT));
                message.setAuthor((String)jsonObject.get(Constants.Message.FIELD_AUTHOR));
                message.setId((String)jsonObject.get(Constants.Message.FIELD_ID));
                message.setTimestamp((Long)jsonObject.get(Constants.Message.FIELD_TIMESTAMP));
                messages.add(message);
            }
        } catch (IOException e) {

        } catch (ParseException e) {

        }
    }
}