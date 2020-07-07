/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restClient;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leonardo
 */
public class PageBuilder {

    public static String build(HttpServletRequest request, HttpServletResponse response, String htmlPath, String[] jsPaths, String[] cssPaths) throws IOException, ServletException, ServletException {

        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RestclientServlet</title>");
            out.println("<style>");

            for (String path : cssPaths) {
                request.getRequestDispatcher(path).include(request, response);
            }
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            request.getRequestDispatcher(htmlPath).include(request, response);

            out.println("    <script   src=\"https://code.jquery.com/jquery-3.5.1.min.js\"\n"
                    + "			  integrity=\"sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=\"\n"
                    + "              crossorigin=\"anonymous\">\n"
                    + "    </script>");
            out.println("<script type='text/javascript'>");

            for (String path : jsPaths) {
                request.getRequestDispatcher(path).include(request, response);
            }

            out.println("</script>");

            out.println("</body>");
            out.println("</html>");

        }
        return null;

    }

}
