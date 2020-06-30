/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataException;
import data.GuidaTV_DataLayer;
import resultsHandler.FailureResult;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Palinsesto;
import models.Programma;
import resultsHandler.TemplateManagerException;
import resultsHandler.TemplateResult;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class Programma_Controller extends Base_Controller {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
        private void action_default(HttpServletRequest request, HttpServletResponse response, int id) 
            throws ServletException, TemplateManagerException {
        
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            Programma programma =  ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getProgrammaDAO().read(id);
            System.out.println(programma.getNome());
            List<Palinsesto> palinsesti = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPalinsestoDAO().getPalinsestiByProgramma(programma);
            request.setAttribute("programma", programma);
            request.setAttribute("palinsesti", palinsesti);
            res.activate("program.html", request, response);
                   
        } catch (DataException ex) {
            
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
     
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
                try {
            //TODO -> Sanitizzazione request.getParameters("data")
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));
            action_default(request, response, id);
        } catch (NumberFormatException ex) {
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Canale_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
