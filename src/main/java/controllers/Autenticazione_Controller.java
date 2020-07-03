/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataException;
import data.GuidaTV_DataLayer;
import static java.util.UUID.randomUUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import resultsHandler.FailureResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Utente;
import models.Utente_Imp;
import resultsHandler.TemplateManagerException;
import resultsHandler.TemplateResult;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class Autenticazione_Controller extends Base_Controller {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, TemplateManagerException {      
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("authentication.html", request, response);
    }
    
    private boolean action_login(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, TemplateManagerException {      
        try {
            Utente u = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().checkUtente(request.getParameter("email"), request.getParameter("password"));
            
            if(u != null && u.getNotAuthFlag()==false){                
                SecurityLayer.createSession(request, u.getEmail(), u.getKey());
                return true;
            }
     
        
        } catch (DataException ex) {
            Logger.getLogger(Autenticazione_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
    
    private boolean action_signUp(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException {      
        try {
            
            if(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().checkUtente(request.getParameter("email"), request.getParameter("password")) == null){
                Utente u = new Utente_Imp();
                u.setEmail(request.getParameter("email"));
                u.setPassword(request.getParameter("password"));
                String UUID = randomUUID().toString().replace("-", "");
                u.setUUID(UUID);
                ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().create(u);
                System.out.println(request.getRequestURL().toString()+"/validateAccount?UUID="+UUID);
                return true;
            }
        } catch (DataException ex) {
            Logger.getLogger(Autenticazione_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void action_logout(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, TemplateManagerException {           
            SecurityLayer.disposeSession(request);            
    }
        
    private boolean action_validateAccount(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, DataException {      
        if(request.getParameter("UUID")!=null && request.getParameter("UUID")!=""){
            
            Utente u = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUUID(request.getParameter("UUID"));
           
            if(u!=null && u.getNotAuthFlag()){
                
                u.setNotAuthFlag(false);
                u.setUUID("");
                ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().update(u);
                
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{

         try {
             
            String msg = null;
            String url = request.getRequestURL().toString();

            
            if(SecurityLayer.checkSession(request)!=null){
                msg= "already logged in";
                if (url.toLowerCase().indexOf("logout") != -1){
                    action_logout(request, response);
                    msg= "logged out successfully";
                }
            }else{
                
                if (url.toLowerCase().indexOf("login") != -1 && request.getMethod().equals("POST")){                   
                    if(action_login(request, response) && request.getParameter("username")!="" && request.getParameter("password")!=""){
                        msg= "logged successfully";
                        request.setAttribute("user", request.getParameter("username"));
                    }else{
                        msg="check your credentials";
                    }
                }else if(url.toLowerCase().indexOf("signup") != -1 && request.getMethod().equals("POST")){ 
                    if(action_signUp(request, response) && request.getParameter("username")!="" && request.getParameter("password")!=""){
                        msg= "check your mail for confirmation";
                    }else{
                        msg="try other credentials";
                    }                                                    
                }else if(url.toLowerCase().indexOf("validateaccount") != -1){ 
                    if(action_validateAccount(request, response)){
                        msg="account validated, now you can to log in";
                    }else{
                        msg="impossible to validate registration";
                    }
                }
           }
            request.setAttribute("msg", msg);
            action_default(request, response);
             
         }catch (TemplateManagerException | DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } 
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
