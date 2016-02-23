package chat; /**
 * Created by Антонина on 09.02.16.
 */

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Message implements Comparable<Message>{
    private String id;
    private String text;
    private String author;
    private Timestamp time;

    public Message() {
        this.time = new Timestamp(new Date().getTime());
        this.text = this.author = null;
        Random random = new Random();
        this.id = Long.toString(Math.abs(random.nextLong()));
    }

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        this.time = new Timestamp(new Date().getTime());
        Random random = new Random();
        this.id = Long.toString(Math.abs(random.nextLong()));
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Timestamp getTime() {
        return time;
    }

    public String toString() {
        return "Id: "+ this.id + "; Author: " + this.author + " - " + "'" + this.text + "'" + "; At: " + " " + this.time.toString();
    }

    public int compareTo(Message m) {
        return this.author.compareTo(m.author);
    }
}
