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
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Palinsesto;
import models.Programma;
import models.Programma_Imp;
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
public class Ricerca_Controller extends Base_Controller {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        List<Palinsesto> risultati = null;
        Ricerca r = new Ricerca_Imp();
        r.setTitolo(request.getParameter("titolo"));
        r.setData(Date.valueOf(request.getParameter("data")));
        r.setInizioMin(Time.valueOf(request.getParameter("inizioMin")));
        r.setInizioMax(Time.valueOf(request.getParameter("inizioMax")));
        r.setCanale(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getCanaleDAO().read(SecurityLayer.checkNumeric(request.getParameter("canale"))));
        r.setFascia(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getFasciaDAO().read(SecurityLayer.checkNumeric(request.getParameter("fascia"))));
        r.setProgramma(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getProgrammaDAO().read(SecurityLayer.checkNumeric(request.getParameter("programma"))));
/*       
        HashMap<String, String> params = new HashMap();
        
        if(request.getParameter("titolo")!="" && request.getParameter("titolo")!= null){
            params.put("titolo", request.getParameter("titolo"));
        }else{
            params.put("titolo", "");
        } 
        if(request.getParameter("data")!="" && request.getParameter("data")!= null){
            params.put("data", request.getParameter("data"));
        }else{
            params.put("data", "");
        } 
        if(request.getParameter("inizioMin")!="" && request.getParameter("inizioMin")!= null){
            params.put("inizioMin", request.getParameter("inizioMin"));
        }else{
            params.put("inizioMin", "");
        } 
        if(request.getParameter("inizioMax")!="" && request.getParameter("inizioMax")!= null){
            params.put("inizioMax", request.getParameter("inizioMax"));
        }else{
            params.put("inizioMax", "");
        } 
        if(request.getParameter("canale")!="" && request.getParameter("canale")!= null){
            params.put("canale", request.getParameter("canale"));
        }else{
            params.put("canale", "");
        } 
        if(request.getParameter("genere")!="" && request.getParameter("genere")!= null){
            params.put("genere", request.getParameter("genere"));
        }else{
            params.put("genere", "");
        } 
        if(request.getParameter("fascia")!="" && request.getParameter("fascia")!= null){
            params.put("fascia", request.getParameter("fascia"));
        }else{
            params.put("fascia", "");
        }
                risultati = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPalinsestoDAO().ricerca(params);
*/
        
        risultati = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getPalinsestoDAO().ricerca(r);
        request.setAttribute("risultati", risultati);
        res.activate("search.html", request, response);

    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{

        try {
            if((request.getParameter("salvaRicerca") != null)&&(SecurityLayer.checkNumeric(request.getParameter("salvaRicerca")) == 1)){
                if(SecurityLayer.checkSession(request)!=null){
                    action_salvaRicerca(request, response);
                }
                 
            }       
            action_default(request, response);
            
        } catch (NumberFormatException ex) {
            action_error(request, response);
        } catch (DataException ex) {
            Logger.getLogger(Ricerca_Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Ricerca_Controller.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void action_salvaRicerca(HttpServletRequest request, HttpServletResponse response) throws DataException {
                       

        String username = (String) request.getSession().getAttribute("username");
        Ricerca ricercaSalvata = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getRicercaDAO().checkExistence(username);
        Ricerca r = new Ricerca_Imp();
        
        if(request.getParameter("canale")!=null && request.getParameter("canale")!= ""){
            r.setCanale(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getCanaleDAO().read(SecurityLayer.checkNumeric(request.getParameter("canale"))));
        }
        if(request.getParameter("fascia")!=null && request.getParameter("fascia")!= ""){
            r.setFascia(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getFasciaDAO().read(SecurityLayer.checkNumeric(request.getParameter("fascia"))));
        }
        if(request.getParameter("genere")!=null && request.getParameter("genere")!= ""){
            r.setGenere(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getGenereDAO().read(SecurityLayer.checkNumeric(request.getParameter("genere"))));
        }
        if(request.getParameter("titolo")!=null && request.getParameter("titolo")!= ""){
            r.setProgramma(((GuidaTV_DataLayer)request.getAttribute("datalayer")).getProgrammaDAO().getProgrammaByTitolo(request.getParameter("titolo")));
        }
        if(request.getParameter("inizioMin")!="null" && request.getParameter("inizioMin")!= ""){
            r.setInizioMin(Time.valueOf(request.getParameter("inizioMin")+":00"));
        }
        if(request.getParameter("inizioMax")!="null" && request.getParameter("inizioMax")!= ""){
            r.setInizioMax(Time.valueOf(request.getParameter("inizioMax")+":00"));
        }
                
        if(ricercaSalvata == null){
           
            ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getRicercaDAO().create(r);
               
            Utente u = ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUsername((String) (request.getSession(false).getAttribute("username")));
            u.setRicerca(r);
             
            ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getUtenteDAO().update(u);
             
        }else{
            r.setKey(ricercaSalvata.getKey());
            r.setVersion(ricercaSalvata.getVersion());
            ((GuidaTV_DataLayer)request.getAttribute("datalayer")).getRicercaDAO().update(r);
        }     
    }



}
