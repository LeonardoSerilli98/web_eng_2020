/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO;
import data.DataException;
import data.DataLayer;
import data.Data_ItemProxy;
import data.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            update = connection.prepareStatement("UPDATE Immagine SET tipo=?, nome=?, taglia=?, stagioneID=?, programmaID=?, version=? WHERE idImmagine=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Immagine where idImmagine=?");
            
            readAll = connection.prepareStatement("SELECT idImmagine FROM Immagine");
            
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
    public List<Immagine> getAll() throws DataException{
        List<Immagine> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Immagine) read(rs.getInt("idImmagine")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Immagine", ex);
        }
        return result;
    }

    @Override
    public void create(Immagine item) throws DataException{
        if (item.getKey() != null && item.getKey() > 0) { 
                update(item);
            }else{ 
                try {                 
                    
                 update.setString(1, item.getTipo());
                 update.setString(2, item.getNome());
                 update.setLong(3, item.getTaglia()); 
                 update.setInt(4, item.getStagione().getKey());
                 update.setInt(5, item.getProgramma().getKey());
                    
                    if(create.executeUpdate() == 1){                       
                        ResultSet keys = create.getGeneratedKeys();
                        if(keys.next()){
                            item.setKey(keys.getInt(1));
                            dataLayer.getCache().add(Immagine.class, item);
                        }
                    }
                } catch (SQLException ex) {
                    throw new DataException("Unable to create Immagine", ex);
                }
                
            }
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

        try {

            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                 return;
             }
             
             long versione = (long) item.getVersion();
         
 
             update.setString(1, item.getTipo());
             update.setString(2, item.getNome());
             update.setLong(3, item.getTaglia()); 
             update.setInt(4, item.getStagione().getKey());
             update.setInt(5, item.getProgramma().getKey());
             update.setLong(6, versione+1);
             
             update.setInt(7, item.getKey());
             update.setLong(8, versione);
             
             if(update.executeUpdate() == 0){
                 throw new OptimisticLockException(item);
             }
             
             item.setVersion(versione + 1);
             
         } catch (SQLException ex) {
             throw new DataException("Unable to update Immagine", ex);
         }
    
    
    }

    @Override
    public void delete(Immagine item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

       
}
