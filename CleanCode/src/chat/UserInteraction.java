package chat;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.StringTokenizer;

/** This class help to accept information from users
 * Created by Антонина on 23.02.16.
 */
public class UserInteraction {
    Scanner scannerIn;

    public UserInteraction() {
        scannerIn = new Scanner(System.in);
    }

    public String readOptionMenu() {
        System.out.println("1. Press '1' to start chatting." + "\n"
                + "2. Press '2' to see history." + "\n"
                + "3. Press '3' to delete a message. " + "\n"
                + "4. Press '4' to search by an author." + "\n"
                + "5. Press '5'to search for regular expressions" + "\n"
                + "6. Press '6' to search for a key word" + "\n"
                + "7. Press '7' to see history for the definite period" + "\n"
                + "If you want to exit enter '0'");
        return scannerIn.next();
    }

    public Message readMessage() {
        String author;
        String text;
        System.out.println("Enter you name.");
        author = scannerIn.next();
        scannerIn.nextLine();
        System.out.println("Enter you message.");
        text = scannerIn.nextLine();
        return new Message(author, text);
    }

    public String readId() {
        System.out.println("Enter id to delete message");
        return scannerIn.next();
    }

    public String readAuthor() {
        System.out.println("Enter author for search");
        return scannerIn.next();
    }

    public String readRegularExpression() {
        System.out.println("Enter regular expression. For example, '.+@(mail|bk|inbox|list)\\.ru' ");
        return scannerIn.next();
    }

    public String readKeyword() {
        System.out.println("Enter key word you want to find");
        return scannerIn.next();
    }

    public String[] getInitialParamTimestamp() {
        int COUNT_PARAM_TIMESTAMP = 7;
        String[] timestamp = new String[COUNT_PARAM_TIMESTAMP];
        timestamp[0] = "1970";
        for (int i = 1; i < timestamp.length - 1; i ++) {
            timestamp[i] = "01";
        }
        timestamp[6] = "0";
        return timestamp;
    }

    public Timestamp readDateAfter(String[] timestamp) {
        System.out.println("Enter the date after what you want to see history. You can write '2015' or '2015 02' or '2015 02 14' and etc. To separate use only ' '");
        return fromStringToTimestamp(scannerIn.nextLine(), timestamp);
    }

    public Timestamp readDateBefore(String[] timestamp) {
        System.out.println("Enter the date before what you want to see history. You can write '2015' or '2015 02' or '2015 02 14' and etc. To separate use only ' '");
        return fromStringToTimestamp(scannerIn.nextLine(), timestamp);
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

    public void closeScannerIn() {
        scannerIn.close();
    }
}
