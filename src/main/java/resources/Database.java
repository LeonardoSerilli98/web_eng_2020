/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import controllers.Base_Controller;
import data.GuidaTV_DataLayer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author leonardo
 */
public class Database {
    
    private static GuidaTV_DataLayer dl;

    static {
        try {
            
            InitialContext ctx = new InitialContext();
            DataSource datasrc = (DataSource) ctx.lookup("java:comp/env/jdbc/we_database_2020");
            Connection connection = datasrc.getConnection();
            
            try (GuidaTV_DataLayer datalayer = new GuidaTV_DataLayer(datasrc)) {
                dl =datalayer;
                dl.init();
            } catch (Exception ex) {
                Logger.getLogger(Base_Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(Base_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static GuidaTV_DataLayer getDatalayer() {
        return dl;
    }
    
}
