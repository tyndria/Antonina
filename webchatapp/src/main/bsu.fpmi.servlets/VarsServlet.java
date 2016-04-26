package main.bsu.fpmi.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;


/** execute doGet
 * Created by Антонина on 25.04.16.
 */
@WebServlet(value = "/vars")

public class VarsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String[] vars = {"JAVA_HOME", "M2_HOME", "CATALINA_HOME","USERNAME", "PATH"};
        for (String var : vars) {
            resp.setCharacterEncoding("Windows-1251");
            resp.getWriter().println(String.format("%s=%s", var, System.getenv(var)));
        }
    }
}
