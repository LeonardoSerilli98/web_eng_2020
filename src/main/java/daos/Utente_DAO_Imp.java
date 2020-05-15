/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO;
import data.DAO_Interface;
import data.DataException;
import data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Utente;
import proxys.Utente_Proxy;

/**
 *
 * @author leonardo
 */
public class Utente_DAO_Imp extends DAO implements Utente_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Utente_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Utente(email, password, preferenzaID, ricercaID) VALUES(?,?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Utente WHERE idUtente=?");
            update = connection.prepareStatement("");
            delete = connection.prepareStatement("");
            
            readAll = connection.prepareStatement("");

        }catch (SQLException ex) {
            throw new DataException("Errore d'inizializzazione Data Layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        try{
            
            create.close();
            read.close();
            update.close();
            delete.close();
            readAll.close();
            
        }catch (SQLException ex) {
            throw new DataException("Errore di chiusura Data Layer", ex);
        }
        super.destroy();
    }


    @Override
    public Utente_Proxy makeObj() {
        return new Utente_Proxy(getDataLayer());
    }

    @Override
    public Utente_Proxy makeObj(ResultSet rs) throws DataException {
        Utente_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idUtente"));
            a.setEmail(rs.getString("email"));
            a.setEmail(rs.getString("password"));
            a.setVersion(rs.getLong("version"));
            
            a.setRicerca_key(rs.getInt("ricercaID"));
            a.setPreferenza_key(rs.getInt("preferenzaID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List getAll() throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Utente item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente read(int key) throws DataException {
        
        Utente item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Utente.class, key)){
            item = dataLayer.getCache().get(Utente.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Utente.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Utente_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Utente item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Utente item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
