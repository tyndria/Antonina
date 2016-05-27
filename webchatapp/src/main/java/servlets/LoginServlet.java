package servlets;

/**
 * Created by Антонина on 17.05.16.
 */

import helpers.FileAssistance;
import helpers.Hashcode;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * execute doGet
 * Created by Антонина on 25.04.16.
 */
@WebServlet(value = "/login")

public class LoginServlet extends HttpServlet {

    ArrayList<User> usersArrayList;

    @Override
    public void init() throws ServletException {
        String realPath = getServletContext().getRealPath("/");
        String path = realPath.substring(0, realPath.length() - 18) + "src\\main\\resources\\usersFile.txt";
        usersArrayList = FileAssistance.getLoginHistory(path);
    }


    private String isUserFound(User user) {

        for (User u : usersArrayList) {

            if (u.getUsername().equals(user.getUsername())) {

                if (u.getPassword().equals(user.getPassword())) {
                    return u.getId();
                }
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String password = req.getParameter("pass");
        String login = req.getParameter("username");

        String encryptedPassword = Hashcode.encryptPassword(password);

        User user = new User();
        user.setPassword(encryptedPassword);
        user.setUsername(login);

        String id = isUserFound(user);
        if (id != null) {
            req.getSession().setAttribute("id", id);
            req.getSession().setAttribute("username", login);
            resp.sendRedirect("html/homepage.html");
        } else {
            req.setAttribute("errorMsg", "You entered wrong password or login");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/welcomePage.jsp");
            rd.forward(req, resp);
        }
    }
}
