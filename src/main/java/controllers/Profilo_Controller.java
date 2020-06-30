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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Canale;
import models.Preferenza;
import models.Preferenza_Imp;
import models.Ricerca;
import models.Ricerca_Imp;
import models.Utente;
import resultsHandler.TemplateManagerException;
import resultsHandler.TemplateResult;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class Profilo_Controller extends Base_Controller {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        
        if(request.getAttribute("user")!=null){
            res.activate("profile.html", request, response);
        }else{
            request.setAttribute("error", "Devi essere autenticato");
            res.activate("error.html", request, response);           
        }
    }
    
    private void action_setPreferenza(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        
        String username = (String) request.getSession().getAttribute("username");
        Preferenza preferenzaSalvata = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPreferenzaDAO().checkExistence(username);
        Preferenza p = new Preferenza_Imp();
        
        
        if(request.getParameter("fascia")!=null && request.getParameter("fascia")!= ""){
            p.setFascia(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getFasciaDAO().read(SecurityLayer.checkNumeric(request.getParameter("fascia"))));
        }
        
        List<Canale> canali = new ArrayList();
        String[] canaliIds = request.getParameterValues("canali");
        for(String c: canaliIds){
            int cID = SecurityLayer.checkNumeric(c);
            canali.add(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getCanaleDAO().read(cID));
        }
        
        p.setCanali(canali);
        
        if(preferenzaSalvata != null){
            p.setKey(preferenzaSalvata.getKey());
            p.setVersion(preferenzaSalvata.getVersion());
            ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPreferenzaDAO().update(p);
        }else{
            ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPreferenzaDAO().create(p);
               
            Utente u = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUsername((String) (request.getSession(false).getAttribute("username")));
            u.setPreferenza(p);
             
            ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().update(u);
        }
        
        
    }
    
    private void action_setMailPreference(HttpServletRequest request, HttpServletResponse response, boolean preferenzaValue) throws DataException, TemplateManagerException {
        Utente u = (Utente) request.getAttribute("user");
        u.setPreferenzaMail(preferenzaValue);
        ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().update(u);
    }
    
    private void action_removeSavedSearch(HttpServletRequest request, HttpServletResponse response) throws DataException {
        Utente u = (Utente) request.getAttribute("user");
        Ricerca r = u.getRicerca();
        Ricerca emptyR = new Ricerca_Imp();
        emptyR.setKey(0);
        u.setRicerca(emptyR);
        ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().update(u);
        ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getRicercaDAO().delete(r);
    }

    private void action_removeSavedPreferenza(HttpServletRequest request, HttpServletResponse response) throws DataException {
        Utente u = (Utente) request.getAttribute("user");
        Preferenza p = u.getPreferenza();
        Preferenza emptyP = new Preferenza_Imp();
        emptyP.setKey(0);
        u.setPreferenza(emptyP);
        ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().update(u);
        ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPreferenzaDAO().delete(p);
    }
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        try {
            if(SecurityLayer.checkSession(request)!=null){
                
                Utente u = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUsername((String) request.getSession().getAttribute("username"));
                if(u!=null){
                    request.setAttribute("user", u);
                }
                if(request.getParameter("rmRicerca")!="" && request.getParameter("rmRicerca")!=null){
                    if(SecurityLayer.checkNumeric(request.getParameter("rmRicerca")) == 1){
                        action_removeSavedSearch(request, response);
                    }
                }
                if(request.getParameter("rmPreferenza")!="" && request.getParameter("rmPreferenza")!=null){
                    if(SecurityLayer.checkNumeric(request.getParameter("rmPreferenza")) == 1){
                        action_removeSavedPreferenza(request, response);
                    }
                }
                if(request.getParameter("setPreferenzaMail")!="" && request.getParameter("setPreferenzaMail")!=null){
                    if(SecurityLayer.checkNumeric(request.getParameter("setPreferenzaMail")) == 1){
                        action_setMailPreference(request, response, true);
                    }else if(SecurityLayer.checkNumeric(request.getParameter("setPreferenzaMail")) == 0){
                        action_setMailPreference(request, response, false);
                    }
                }
                if(request.getParameterValues("canali")!=null){
                    action_setPreferenza(request, response);
                }
                
            }    
            
            action_default(request, response);
                
        } catch (DataException ex) {
                Logger.getLogger(Profilo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
                Logger.getLogger(Profilo_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
