package main.java.filters;

import main.java.helpers.FileAssistance;
import main.java.helpers.Hashcode;
import main.java.models.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Антонина on 17.05.16.
 */

@WebFilter(value = "/chat", dispatcherTypes = DispatcherType.REQUEST)

public class AuthenticationFilter extends HttpServlet implements Filter {

    ArrayList<User> usersArrayList;


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


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String path = servletRequest.getServletContext().getRealPath("/") + "usersFile.txt";
        usersArrayList = FileAssistance.getLoginHistory(path);

        String password = servletRequest.getParameter("pass");
        String login = servletRequest.getParameter("username");

        String encryptedPassword = Hashcode.encryptPassword(password);

        User user = new User(login, encryptedPassword);

        if (isUserFound(user)) {
            ((HttpServletResponse) servletResponse).sendRedirect("html/homepage.html");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletRequest.setAttribute("errorMsg", "You entered wrong password or login");
            RequestDispatcher rd = servletRequest.getServletContext().getRequestDispatcher("/jsp/welcomePage.jsp");
            rd.forward(servletRequest, servletResponse);
        }
    }

    public void destroy() {

    }
}
