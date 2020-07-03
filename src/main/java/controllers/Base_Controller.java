/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.GuidaTV_DataLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author leonardo
 */
public abstract class Base_Controller extends HttpServlet {

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {

        try {

            InitialContext ctx = new InitialContext();
            DataSource datasrc = (DataSource) ctx.lookup(getServletContext().getInitParameter("data.source"));
            Connection connection = datasrc.getConnection();

            try ( GuidaTV_DataLayer datalayer = new GuidaTV_DataLayer(datasrc)) {
                datalayer.init();
                request.setAttribute("datalayer", datalayer);
                processRequest(request, response);

            } catch (Exception ex) {
                Logger.getLogger(Base_Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(Base_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

}
