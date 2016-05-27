package filters;

import helpers.FileAssistance;
import helpers.Hashcode;
import models.User;

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

@WebFilter(urlPatterns = {
        "/chat",
        "/html/homepage.html"
})

public class AuthenticationFilter implements Filter {

    ArrayList<User> usersArrayList;

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private boolean isUserAuthenticated(String id) {

        for (User u : usersArrayList) {

            if (u.getId().equals(id)) {

                    return true;
            }
        }
        return false;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String realPath = servletRequest.getServletContext().getRealPath("/");
        String path = realPath.substring(0, realPath.length() - 18) + "src\\main\\resources\\usersFile.txt";
        usersArrayList = FileAssistance.getLoginHistory(path);

        String id = (String)((HttpServletRequest)servletRequest).getSession().getAttribute("id");

        if (isUserAuthenticated(id)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if(servletResponse instanceof HttpServletResponse){
            ((HttpServletResponse) servletResponse).sendRedirect("/jsp/welcomePage.jsp");
        }
    }

    public void destroy() {

    }
}
