/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataException;
import data.GuidaTV_DataLayer;
import resultsHandler.FailureResult;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Canale;
import resultsHandler.TemplateManagerException;
import resultsHandler.TemplateResult;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class Home_Controller extends Base_Controller {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, TemplateManagerException {
            
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Canale> canali =  ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getCanaleDAO().getAll();
            request.setAttribute("canali", canali);
            
            if(request.getParameter("allDayView")!=null){
                request.setAttribute("allDayView", SecurityLayer.checkNumeric(request.getParameter("allDayView")));
            }
            res.activate("home.html", request, response);                
      
        } catch (DataException ex) {
            
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        
        try {
            action_default(request, response); 
        }catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
