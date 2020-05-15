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
import models.Immagine;
import proxys.Immagine_Proxy;

/**
 *
 * @author leonardo
 */
public class Immagine_DAO_Imp extends DAO implements Immagine_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Immagine_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            create = connection.prepareStatement("INSERT INTO Immagine (tipo, nome, taglia, stagioneID, programmaID), values(?,?,?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Immagine WHERE idImmagine=?");
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
    public Immagine_Proxy makeObj() {
        return new Immagine_Proxy(getDataLayer());
    }

    @Override
    public Immagine_Proxy makeObj(ResultSet rs) throws DataException {
         Immagine_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idImmagine"));
            a.setNome(rs.getString("nome"));
            a.setTipo(rs.getString("tipo"));
            a.setTaglia(rs.getLong("taglia"));
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
    public void create(Immagine item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Immagine read(int key) throws DataException {
        
        Immagine item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Immagine.class, key)){
            item = dataLayer.getCache().get(Immagine.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Immagine.class, item);    
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Immagine_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
        
        
    }

    @Override
    public void update(Immagine item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Immagine item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

       
}
