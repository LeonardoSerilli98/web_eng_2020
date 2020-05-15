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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Genere;
import proxys.Genere_Proxy;

/**
 *
 * @author leonardo
 */
public class Genere_DAO_Imp extends DAO implements Genere_DAO{
    
   
     private PreparedStatement create, read, update, delete, readAll;
     
    public Genere_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try{
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Genere (nome) VALUES (?)");
            read = connection.prepareStatement("SELECT * FROM Genere WHERE idGenere=?");
            update = connection.prepareStatement("UPDATE Genere SET nome=?,version=? WHERE idGenere=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Genere WHERE idGenere=?");
            
            readAll = connection.prepareStatement("SELECT idGenere FROM Genere");
            
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
    public Genere_Proxy makeObj() {                      
        return new Genere_Proxy(getDataLayer());    
    }

    @Override
    public Genere_Proxy makeObj(ResultSet rs) throws DataException {
        Genere_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idGenere"));
            a.setNome(rs.getString("nome"));
            a.setVersion(rs.getLong("version"));

                        
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Genere> getAll() throws DataException{
        List<Genere> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Genere) read(rs.getInt("idGenere")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Genere", ex);
        }
        return result;
    }

    @Override
    public void create(Genere item) throws DataException{
            
        //se l'ID del'oggetto gia esiste chiamiamo l'update
            if (item.getKey() != null && item.getKey() > 0) { 
                update(item);
            }else{ 
                
            }
    }

    @Override
    public Genere read(int key) throws DataException {
        
        Genere item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Genere.class, key)){
            item = dataLayer.getCache().get(Genere.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Genere.class, item);    
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Genere_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
        
        
    }

    @Override
    public void update(Genere item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Genere item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
