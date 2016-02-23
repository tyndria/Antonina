package chat; /**
 * Created by Антонина on 09.02.16.
 */

import java.io.IOException;

public class Show {
    public static void main(String[] args) throws IOException {
        Chat chat = new Chat();
        chat.startChat();
        chat.stopChat();
    }
}


