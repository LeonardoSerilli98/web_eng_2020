/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.we_2020;

import daos.Canale_DAO_Imp;
import daos.Genere_DAO;
import daos.Genere_DAO_Imp;
import daos.Immagine_DAO_Imp;
import daos.Utente_DAO_Imp;
import data.DataException;
import data.GuidaTV_DataLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import models.Canale;
import models.Canale_Imp;
import models.Genere;
import models.Genere_Imp;
import models.Immagine;
import models.Immagine_Imp;
import models.Utente;

/**
 *
 * @author leonardo
 */


public class servletEsempio extends HttpServlet {

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Resource(name = "jdbc/we_database_2020")
    private DataSource ds;
    
    private static final String SQL_GENERI = "SELECT * FROM Genere";
    
    private void action_query(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException {
        
        Class.forName(getServletContext().getInitParameter("data.jdbc.driver"));
         
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/we_database_2020?serverTimezone=UTC", getServletContext().getInitParameter("data.jdbc.username"), getServletContext().getInitParameter("data.jdbc.password"));
        Statement s = c.createStatement();
        ResultSet rs  = s.executeQuery("SELECT * FROM Genere");
        while(rs.next()){
            
            System.out.println("beccato" + rs.getString("nome"));
            
        } 
    }
    
      private void pooling_action_query(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException, NamingException {
        
      
        try (
                //connessione al database locale
                //database connection
                Connection connection = ds.getConnection();
                //POSSIAMO USARE SIA L'SQL PRECOMPILATO CHE QUELLO DIRETTO
                //WE CAN USE BOTH PRECOMPILED OR DIRECT SQL HERE
                //compiliamo la query parametrica
                //compile the parametric query
                PreparedStatement ps = connection.prepareStatement(SQL_GENERI)) {

            //eseguiamo la query (SQL diretto)
            //query execution (direct SQL)
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    System.out.println(rs.getString("nome"));
                }
            }
        }

    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            {
             try{ 
                GuidaTV_DataLayer datalayer = new GuidaTV_DataLayer(ds); 
                datalayer.init();
                
                request.setAttribute("datalayer", datalayer);
                query_esempio(request, response);
                //pooling_action_query(request, response);
                //action_query(request, response);       
            }catch(Exception e){
                    //
                }
            }

        private void query_esempio(HttpServletRequest request, HttpServletResponse response) {
       
            
       
            // Article article = ((NewspaperDataLayer) request.getAttribute("datalayer")).getArticleDAO().getArticle(k);
            //Genere genere = (Genere) (((GuidaTV_DataLayer) request.getAttribute("datalayer")).getGenereDAO().read(1));
           
            Canale c = new Canale_Imp();
            Immagine i = new Immagine_Imp();
            GuidaTV_DataLayer dl = (GuidaTV_DataLayer) request.getAttribute("datalayer");
            Canale_DAO_Imp canaleDAO = (Canale_DAO_Imp) dl.getCanaleDAO();
            Immagine_DAO_Imp immagineDAO = (Immagine_DAO_Imp) dl.getImmagineDAO();
           
        try {
           
           i = immagineDAO.read(1);
           c.setImmagine(i);
           c.setNome("tst");   
           canaleDAO.create(c);
           c.setNome("italia2");   
           canaleDAO.update(c);
                

        } catch (DataException ex) {
            Logger.getLogger(servletEsempio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
            
        
    }

       

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>



}
