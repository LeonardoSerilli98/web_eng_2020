/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataException;
import data.GuidaTV_DataLayer;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import resultsHandler.FailureResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Canale;
import models.Palinsesto;
import resultsHandler.TemplateManagerException;
import resultsHandler.TemplateResult;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class Canale_Controller extends Base_Controller {

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
            Canale canale =  ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getCanaleDAO().read(id);
            if(request.getParameter("data")!="" && request.getParameter("data")!= null){
                canale.setPalinsesti(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPalinsestoDAO().getPalinsestiByCanale(canale, request.getParameter("data")));
            }
            request.setAttribute("canale", canale);
            res.activate("channel.html", request, response);
                   
        } catch (DataException ex) {
            
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
     
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{

        try {
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
