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
import data.Data_ItemProxy;
import data.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Episodio;
import models.Programma;
import models.Stagione;
import proxys.Episodio_Proxy;

/**
 *
 * @author leonardo
 */
public class Episodio_DAO_Imp extends DAO implements Episodio_DAO {
    
    private PreparedStatement create, read, update, delete, readAll;
    private PreparedStatement episodioByProgramma;
    private PreparedStatement episodioByStagione;
    
    
    public Episodio_DAO_Imp(DataLayer d) {
        super(d);
        
    }

     @Override
    public void init() throws DataException {
        super.init(); 
        
        try {
            
            create = connection.prepareStatement("INSERT INTO Episodio (titolo, numero, programmaID, stagioneID) VALUES(?, ?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Episodio WHERE idEpisodio=?");
            update = connection.prepareStatement("UPDATE Episodio SET titolo=?, numero=?, programmaID=?, stagioneID=?, version=? WHERE idEpisodio=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Episodio WHERE idEpisodio=?");
            
            readAll = connection.prepareStatement("SELECT idEpisodio FROM Episodio");              

            episodioByProgramma = connection.prepareStatement("SELECT * FROM Episodio WHERE programmaID=?");
            episodioByStagione = connection.prepareStatement("SELECT * FROM Episodio WHERE stagioneID=?");
            
        } catch (SQLException ex) {
            throw new DataException("Errore di inizializzazione per 'GuidaTV Data Layer'", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        
        try {
            
            create.close();
            read.close();
            update.close();
            delete.close();
            readAll.close();
            
            episodioByProgramma.close();
            episodioByStagione.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Episodio_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy(); 
    }

    @Override
    public Episodio_Proxy makeObj() {
        return new Episodio_Proxy(getDataLayer());
    }

    @Override
    public Episodio_Proxy makeObj(ResultSet rs) throws DataException {
        Episodio_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idEpisodio"));
            a.setNumero(rs.getInt("numero"));
            a.setTitolo(rs.getString("titolo"));
            
            a.setStagione_key(rs.getInt("stagioneID"));
            a.setSerie_key(rs.getInt("programmaID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Episodio> getAll() throws DataException{
        List<Episodio> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Episodio) read(rs.getInt("idEpisodio")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Episodio", ex);
        }
        return result;
    }

    @Override
    public void create(Episodio item) throws DataException{

        if (item.getKey() != null && item.getKey() > 0) { 
                update(item);
            }else{ 
                try {                 
                    
                    update.setString(1, item.getTitolo());
                    update.setInt(2, item.getNumero());
                    update.setInt(3, item.getSerie().getKey()); 
                    update.setInt(4, item.getStagione().getKey());
                    
                    if(create.executeUpdate() == 1){                       
                        ResultSet keys = create.getGeneratedKeys();
                        if(keys.next()){
                            item.setKey(keys.getInt(1));
                            dataLayer.getCache().add(Episodio.class, item);
                        }
                    }
                } catch (SQLException ex) {
                    throw new DataException("Unable to create Episodio", ex);
                }
                
            }
            if(item instanceof Data_ItemProxy){
                ((Data_ItemProxy) item).setDirty(false);
            }
    }

    @Override
    public Episodio read(int key) throws DataException {
        Episodio item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Episodio.class, key)){
            item = dataLayer.getCache().get(Episodio.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Episodio.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Episodio_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Episodio item) throws DataException{
        try {

            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                 return;
             }
             
             long versione = (long) item.getVersion();
         
 
             update.setString(1, item.getTitolo());
             update.setInt(2, item.getNumero());
             update.setInt(3, item.getSerie().getKey()); 
             update.setInt(4, item.getStagione().getKey());
             update.setLong(5, versione+1);
             
             update.setInt(6, item.getKey());
             update.setLong(7, versione);
             
             if(update.executeUpdate() == 0){
                 throw new OptimisticLockException(item);
             }
             
             item.setVersion(versione + 1);
             
         } catch (SQLException ex) {
             throw new DataException("Unable to update Canale", ex);
         }
        
    }

    @Override
    public void delete(Episodio item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Episodio> getEpisodiByProgramma(Programma programma) throws DataException {
         List<Episodio> result = new ArrayList();
        try {
            episodioByProgramma.setInt(1, programma.getKey());
            try (ResultSet rs = episodioByProgramma.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere canali by preferenza", ex);
        }
        return result;
    }

    @Override
    public List<Episodio> getEpisodiByStagione(Stagione stagione) throws DataException {
         List<Episodio> result = new ArrayList();
        try {
            episodioByStagione.setInt(1, stagione.getKey());
            try (ResultSet rs = episodioByProgramma.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere canali by stagione", ex);
        }
        return result;
    }
    
    

   
    
    
}
