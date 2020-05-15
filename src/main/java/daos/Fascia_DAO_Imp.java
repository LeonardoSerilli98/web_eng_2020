/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO;
import data.DataException;
import data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Fascia;
import proxys.Fascia_Proxy;

/**
 *
 * @author leonardo
 */
public class Fascia_DAO_Imp extends DAO implements Fascia_DAO{
  
    private PreparedStatement create, read, update, delete, readAll;

    public Fascia_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            create = connection.prepareStatement("INSERT INTO Fascia(inizio, fine, fascia) VALUES(?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Fascia WHERE idFascia=?");
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
    public Fascia_Proxy makeObj() {
        return new Fascia_Proxy(getDataLayer());
    }

    @Override
    public Fascia_Proxy makeObj(ResultSet rs) throws DataException {
        Fascia_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idFascia"));
            a.setFascia(rs.getString("fascia"));
            a.setFine(rs.getTime("inizio"));
            a.setFine(rs.getTime("fine"));
            a.setVersion(rs.getLong("version"));
            
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
    public void create(Fascia item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Fascia read(int key) throws DataException {
        Fascia item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Fascia.class, key)){
            item = dataLayer.getCache().get(Fascia.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Fascia.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Fascia_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Fascia item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Fascia item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
