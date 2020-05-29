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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Palinsesto;
import proxys.Palinsesto_Proxy;

/**
 *
 * @author leonardo
 */
public class Palinsesto_DAO_Imp extends DAO implements Palinsesto_DAO{
    
    
    private PreparedStatement create, read, update, delete, readAll;

    public Palinsesto_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Palinsesto(inizio, fine, data, programmaID, episodoID, fasciaID, canaleID) VALUES (?,?,?,?,?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Palinsesto WHERE idPalinsesto=?");
            update = connection.prepareStatement("UPDATE Palinsesto SET inizio=?, fine=?, data=?, programmaID=?, episodioID=?, fasciaID=?, canaleID=?, versione=? WHERE idPalinsesto=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Palinsesto where idPalinsesto=?");
            
            readAll = connection.prepareStatement("SELECT idPalinsesto FORM Palisesto");

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
    public Palinsesto_Proxy makeObj() {
        return new Palinsesto_Proxy(getDataLayer());
    }

    @Override
    public Palinsesto_Proxy makeObj(ResultSet rs) throws DataException {
        Palinsesto_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idPalinsesto"));
            a.setData(rs.getDate("data"));
            a.setInizio(rs.getTime("inizio"));
            a.setFine(rs.getTime("fine"));
            a.setVersion(rs.getLong("version"));
            
            a.setProgramma_key(rs.getInt("programmaID"));
            a.setEpisodio_key(rs.getInt("episodioID"));
            a.setCanale_key(rs.getInt("canaleID"));
            a.setFascia_key(rs.getInt("fasciaID"));
    
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Palinsesto> getAll() throws DataException{
        List<Palinsesto> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Palinsesto) read(rs.getInt("idPalinsesto")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Palinsesto", ex);
        }
        return result;
    }

    @Override
    public void create(Palinsesto item) throws DataException {
        if (item.getKey() != null && item.getKey() > 0) { 
                update(item);
            }else{ 
                try {                 
                    
                    update.setTime(1, item.getInizio());
                    update.setTime(2, item.getFine());
                    update.setDate(3, (Date) item.getData()); 
                    update.setInt(4, item.getProgramma().getKey());
                    update.setInt(5, item.getEpisodio().getKey());
                    update.setInt(6, item.getFascia().getKey());
                    update.setInt(7, item.getCanale().getKey());
                    
                    if(create.executeUpdate() == 1){                       
                        ResultSet keys = create.getGeneratedKeys();
                        if(keys.next()){
                            item.setKey(keys.getInt(1));
                            dataLayer.getCache().add(Palinsesto.class, item);
                        }
                    }
                } catch (SQLException ex) {
                    throw new DataException("Unable to create Palinsesto", ex);
                }
                
            }
            if(item instanceof Data_ItemProxy){
                ((Data_ItemProxy) item).setDirty(false);
            }    }

    @Override
    public Palinsesto read(int key) throws DataException {
        Palinsesto item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Palinsesto.class, key)){
            item = dataLayer.getCache().get(Palinsesto.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Palinsesto.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Palinsesto_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Palinsesto item) throws DataException{
        try {

            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                 return;
             }
             
             long versione = (long) item.getVersion();
         
 
             update.setTime(1, item.getInizio());
             update.setTime(2, item.getFine());
             update.setDate(3, (Date) item.getData()); 
             update.setInt(4, item.getProgramma().getKey());
             update.setInt(5, item.getEpisodio().getKey());
             update.setInt(6, item.getFascia().getKey());
             update.setInt(7, item.getCanale().getKey());
             update.setLong(8, versione+1);
             
             update.setInt(9, item.getKey());
             update.setLong(10, versione);
             
             if(update.executeUpdate() == 0){
                 throw new OptimisticLockException(item);
             }
             
             item.setVersion(versione + 1);
             
         } catch (SQLException ex) {
             throw new DataException("Unable to update Palinsesto", ex);
         }
    }

    @Override
    public void delete(Palinsesto item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
