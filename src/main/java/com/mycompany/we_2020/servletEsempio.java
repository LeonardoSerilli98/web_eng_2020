/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.we_2020;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    private void action_query(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException {
        
       Class.forName(getServletContext().getInitParameter("data.jdbc.driver"));
         
        Connection c = DriverManager.getConnection("jdbc:mysql//localhost/we_database_2020?serverTimezone=UTC", "leonardo", "Rorschach69");
        Statement s = c.createStatement();
        ResultSet rs  = s.executeQuery("SELECT * FROM author WHERE name LIKE '%" + "leonardo" + "%'");
        while(rs.next()){
            System.out.println("beccato");
        } 
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            {
       
        try {
            
            action_query(request, response);
        } catch (ClassNotFoundException ex) {
            System.out.println("classNotFoundException");
            Logger.getLogger(servletEsempio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("SQLEXCEPTION");
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