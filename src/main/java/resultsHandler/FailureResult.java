package resultsHandler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 */
public class FailureResult {

    protected ServletContext context;
    private final TemplateResult template;

    public FailureResult(ServletContext context) {
        this.context = context;
        template = new TemplateResult(context);
    }

    public void activate(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String message;
        if (exception != null && exception.getMessage() != null) {
            message = exception.getMessage();
        } else if (exception != null) {
            message = exception.getClass().getName();
        } else {
            message = "Unknown Error";
        }
        activate(message, request, response);
    }

    public void activate(String message, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (context.getInitParameter("view.error_template") != null) {
                request.setAttribute("error", message);
                request.setAttribute("outline_tpl", "");
                template.activate(context.getInitParameter("view.error_template"), request, response);
            } else {
                //se non c'è un template per l'errore genereato lascia un internal server error
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            }
        } catch (Exception ex) {
            message += ". In addition, the following exception was generated while trying to display the error page: " + ex.getMessage();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            } catch (IOException ex1) {
                Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
