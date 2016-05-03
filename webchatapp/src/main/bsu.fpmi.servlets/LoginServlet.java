package main.bsu.fpmi.servlets;

import main.bsu.fpmi.helper.FileAssistance;
import main.bsu.fpmi.helper.Hashcode;
import main.bsu.fpmi.helper.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


/** execute doGet
 * Created by Антонина on 25.04.16.
 */
@WebServlet(value = "/login")

public class LoginServlet extends HttpServlet {

    ArrayList<User> usersArrayList = FileAssistance.getLoginHistory();

    private boolean isUserFound(User user) {

        for (User u: usersArrayList) {

            if (u.getName().equals(user.getName())) {

                if (u.getPassword().equals(user.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String password = req.getParameter("pass");
        String login = req.getParameter("username");

        String encryptedPassword = Hashcode.encryptPassword(password);

        User user = new User(login, encryptedPassword);

        if (isUserFound(user)) {
            resp.sendRedirect("html/homepage.html");
        } else {
            req.setAttribute("errorMsg", "You entered wrong password or login");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/welcomePage.jsp");
            rd.forward(req, resp);
        }
    }
}
