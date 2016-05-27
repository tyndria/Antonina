package servlets;


import common.Message;
import logging.Log;
import org.json.simple.parser.ParseException;
import storage.InMemoryMessageStorage;
import storage.MessageStorage;
import storage.Portion;
import utils.MessageHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Антонина on 14.05.16.
 */

@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {

    private MessageStorage messageStorage = new InMemoryMessageStorage();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int index = MessageHelper.parseToken(token);
        Portion portion = new Portion(index, messageStorage.size());
        List<Message> messages = messageStorage.getPortion(portion);
        String responseBody = MessageHelper.buildServerResponseBody(messages, messageStorage.size(), (String)req.getSession().getAttribute("username"));
        resp.getWriter().println(responseBody);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {
        }
    }
}
