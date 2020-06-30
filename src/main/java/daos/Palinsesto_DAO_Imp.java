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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Canale;
import models.Palinsesto;
import models.Programma;
import models.Ricerca;
import proxys.Palinsesto_Proxy;

/**
 *
 * @author leonardo
 */
public class Palinsesto_DAO_Imp extends DAO implements Palinsesto_DAO{
    
    
    private PreparedStatement create, read, update, delete, readAll;
    private PreparedStatement palinsestiByCanale, palinsestiByProgramma, ricerca;

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
            
            readAll = connection.prepareStatement("SELECT idPalinsesto FROM Palinsesto");
            
            palinsestiByCanale = connection.prepareStatement("SELECT * FROM Palinsesto WHERE canaleID=? AND data=?");
            palinsestiByProgramma = connection.prepareStatement("SELECT * FROM Palinsesto WHERE programmaID=?");
            ricerca = connection.prepareStatement("SELECT * FROM Palinsesto WHERE data=? UNION SELECT * FROM Palinsesto WHERE inizio>=? UNION SELECT * FROM Palinsesto WHERE inizio<=? UNION SELECT * FROM Palinsesto WHERE canaleID=? UNION SELECT * FROM Palinsesto WHERE fasciaID=? UNION SELECT pa.idPalinsesto, pa.inizio, pa.fine, pa.data, pa.programmaID, pa.episodioID, pa.fasciaID, pa.canaleID, pa.version FROM Palinsesto AS pa INNER JOIN Programma AS pr ON pr.idProgramma = pa.programmaID WHERE pr.nome=? UNION SELECT pa.idPalinsesto, pa.inizio, pa.fine, pa.data, pa.programmaID, pa.episodioID, pa.fasciaID, pa.canaleID, pa.version FROM Palinsesto AS pa INNER JOIN Programma AS pr ON pr.idProgramma = pa.programmaID WHERE pr.genereID=?");
        }
        catch (SQLException ex) {
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
            palinsestiByCanale.close();
            palinsestiByProgramma.close();
            ricerca.close();
            
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
            throw new DataException("Unable to create Palinsesto object form ResultSet asd", ex);
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
                    
                    create.setTime(1, item.getInizio());
                    create.setTime(2, item.getFine());
                    create.setDate(3, (Date) item.getData()); 
                    create.setInt(4, item.getProgramma().getKey());
                    create.setInt(5, item.getEpisodio().getKey());
                    create.setInt(6, item.getFascia().getKey());
                    create.setInt(7, item.getCanale().getKey());
                    
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

    /**
     *
     * @param canale
     * @return
     * @throws DataException
     */
    @Override
    public List<Palinsesto> getPalinsestiByCanale(Canale canale) throws DataException {   
        
        List<Palinsesto> result = new ArrayList();
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        //String tomorrow = sdf.format(java.util.Date.from(LocalDateTime.now().plusMinutes(1440L).atZone(ZoneId.systemDefault()).toInstant()));
       
        try {
            palinsestiByCanale.setInt(1, canale.getKey());  
            palinsestiByCanale.setDate(2,  Date.valueOf(today));   
            try(ResultSet rs = palinsestiByCanale.executeQuery()){
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }  
        }catch (SQLException ex) {
                 throw new DataException("Impossibile ottenere Palinsesti by Canale", ex);
        }
                return result;        
    }

    @Override
    public List<Palinsesto> getPalinsestiByCanale(Canale canale, String data) throws DataException {   
        
        List<Palinsesto> result = new ArrayList();
        
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        //String chosenDate = sdf.format(data);
       
        try {
            palinsestiByCanale.setInt(1, canale.getKey());  
            palinsestiByCanale.setDate(2,  Date.valueOf(data));   
            try(ResultSet rs = palinsestiByCanale.executeQuery()){
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }  
        }catch (SQLException ex) {
                 throw new DataException("Impossibile ottenere Palinsesti by Canale", ex);
        }
                return result;        
    }
    
    @Override
    public List<Palinsesto> getPalinsestiByProgramma(Programma programma) throws DataException {   
        
        List<Palinsesto> result = new ArrayList();
        
        try {
            palinsestiByProgramma.setInt(1, programma.getKey());  
            try(ResultSet rs = palinsestiByProgramma.executeQuery()){
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }  
        }catch (SQLException ex) {
                 throw new DataException("Impossibile ottenere Palinsesti by Programma", ex);
        }
                return result;        
    }

    @Override
    public List<Palinsesto> ricerca(Ricerca r) throws DataException {

        List<Palinsesto> result = new ArrayList();


        try {
            ricerca.setDate(1, r.getData());                
            ricerca.setTime(2, r.getInizioMin());                
            ricerca.setTime(3, r.getInizioMax());                
            ricerca.setInt(4, Integer.parseInt(params.get("canale")));
            ricerca.setInt(5, Integer.parseInt(params.get("fascia")));
            ricerca.setString(6, params.get("titolo"));
            ricerca.setInt(7, Integer.parseInt(params.get("genere")));
            

            try(ResultSet rs = ricerca.executeQuery()){
                while (rs.next()) {
                    result.add(makeObj(rs));
                }
            }      
        }catch (SQLException ex) {
            Logger.getLogger(Palinsesto_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return result;
    }

        
    
}
