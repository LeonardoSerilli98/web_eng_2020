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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.Ricerca;
import proxys.Ricerca_Proxy;

/**
 *
 * @author leonardo
 */
public class Ricerca_DAO_Imp extends DAO implements Ricerca_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Ricerca_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Ricerca(fasciaID, programmaID, genereID, canaleID) VALUES(?,?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Ricerca WHERE idRicerca=?");
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
    public Ricerca_Proxy makeObj() {
        return new Ricerca_Proxy(getDataLayer());
    }

    @Override
    public Ricerca_Proxy makeObj(ResultSet rs) throws DataException {
        Ricerca_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idRicerca"));
                       
            a.setFascia_key(rs.getInt("fasciaID"));
            a.setGenere_key(rs.getInt("genereID"));
            a.setProgramma_key(rs.getInt("programmaID"));
            a.setCanale_key(rs.getInt("canaleID"));
            a.setVersion(rs.getLong("version"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Ricerca> getAll() throws DataException{
        List<Ricerca> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Ricerca) read(rs.getInt("idRicerca")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Ricerca", ex);
        }
        return result;
    }

    @Override
    public void create(Ricerca item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Ricerca read(int key) throws DataException {
        Ricerca item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Ricerca.class, key)){
            item = dataLayer.getCache().get(Ricerca.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Ricerca.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Ricerca_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Ricerca item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Ricerca item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
