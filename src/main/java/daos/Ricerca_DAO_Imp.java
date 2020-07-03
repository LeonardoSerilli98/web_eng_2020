/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private PreparedStatement checkExistece;

    public Ricerca_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Ricerca(fasciaID, programmaID, genereID, canaleID, inizioMIN, inizioMax, data, titolo) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            read = connection.prepareStatement("SELECT * FROM Ricerca WHERE idRicerca=?");
            update = connection.prepareStatement("UPDATE Ricerca SET fasciaID=?, programmaID=?, genereID=?, canaleID=?, version=?, inizioMin=?, inizioMax=?, data=?, titolo=? WHERE idRicerca=? AND version=?");
            delete = connection.prepareStatement("DELETE FROM Ricerca where idRicerca=?");
            
            readAll = connection.prepareStatement("SELECT idRicerca FROM Ricerca");
            
            checkExistece = connection.prepareStatement("SELECT r.idRicerca, r.fasciaID, r.programmaID, r.genereID, r.canaleID, r.version, r.inizioMIN, r.inizioMAX, r.data, r.titolo FROM Ricerca as r INNER JOIN Utente as u WHERE u.email=? AND u.ricercaID = r.idRicerca");
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
            a.setInizioMin(rs.getTime("inizioMin"));
            a.setInizioMax(rs.getTime("inizioMax"));
            a.setData(rs.getDate("data"));
            a.setTitolo(rs.getString("titolo"));
            
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
        if (item.getKey() != null && item.getKey() > 0){
            update(item);
        } else {
            try {
                
                if(item.getFascia()==null || item.getFascia().getKey()==0){
                    create.setNull(1, java.sql.Types.INTEGER);                
                }else{
                    create.setInt(1, item.getFascia().getKey());
                }

                if(item.getProgramma()==null || item.getProgramma().getKey()==0){
                    create.setNull(2, java.sql.Types.INTEGER);                
                }else{
                    create.setInt(2, item.getProgramma().getKey());
                }
                
                if(item.getGenere()==null || item.getGenere().getKey()==0){
                    create.setNull(3, java.sql.Types.INTEGER);                
                }else{
                    create.setInt(3, item.getGenere().getKey());
                }
                
                if(item.getCanale()==null || item.getCanale().getKey()==0){
                    create.setNull(4, java.sql.Types.INTEGER);                
                }else{
                    create.setInt(4, item.getCanale().getKey());
                }
            
                create.setTime(5, item.getInizioMin());
                create.setTime(6, item.getInizioMax());
                create.setDate(7, item.getData());
                create.setString(8 , item.getTitolo());

                if (create.executeUpdate() == 1){
                    ResultSet keys = create.getGeneratedKeys();
                    while ( keys.next()) {
                        item.setKey(keys.getInt(1));

                        // ricordiamo di inserire l'oggetto appena creato in cache
                        dataLayer.getCache().add(Ricerca.class, item);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to create ricerca", ex);
            }
        }
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
        try {
            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                return;
            }

            long versione = (long) item.getVersion();
            
            if(item.getFascia()==null){
                update.setNull(1, java.sql.Types.INTEGER);                
            }else{
                if(item.getFascia().getKey()!=0){
                   update.setInt(1, item.getFascia().getKey());
                }else{
                    update.setNull(1, java.sql.Types.INTEGER); 
                }
            }
            if(item.getProgramma()==null){
                update.setNull(2, java.sql.Types.INTEGER);                
            }else{
                if(item.getProgramma().getKey()!=0){
                    update.setInt(2, item.getProgramma().getKey());
                }else{
                    update.setNull(2, java.sql.Types.INTEGER); 
                }
            }
            if(item.getGenere()==null){
                update.setNull(3, java.sql.Types.INTEGER);                
            }else{
                if(item.getGenere().getKey()!=0){
                    update.setInt(3, item.getGenere().getKey());
                }else{
                    update.setNull(3, java.sql.Types.INTEGER); 
                }
            }
            if(item.getCanale()==null){
                update.setNull(4, java.sql.Types.INTEGER);                
            }else{
                if(item.getCanale().getKey()!=0){
                    update.setInt(4, item.getCanale().getKey());
                }else{
                    update.setNull(4, java.sql.Types.INTEGER); 
                }
            }
            
            update.setLong(5, versione + 1);
            update.setTime(6,item.getInizioMin());
            update.setTime(7,item.getInizioMax());
            update.setDate(8,item.getData());
            update.setString(9, item.getTitolo());

            update.setInt(10, item.getKey());
            update.setLong(11, versione);
            if (update.executeUpdate() == 0){
                throw new OptimisticLockException(item);
            }

            item.setVersion(versione + 1);
        } catch (SQLException ex) {
            throw new DataException("Unable to update ricerca", ex);
        }
    }

    @Override
    public void delete(Ricerca item) throws DataException{
        if(item==null){
            return;
        }
        try {
            delete.setInt(1, item.getKey());
            delete.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete ricerca", ex);
        }
    }

    @Override
    public Ricerca checkExistence(String username) throws DataException {
        Ricerca r = null;
        try {
            
            checkExistece.setString(1, username);   
            
            try(ResultSet rs = checkExistece.executeQuery()){
                while (rs.next()) {
                    r = makeObj(rs);
                }
            }                     
        } catch (SQLException ex) {
            throw new DataException("Unable to checkExistence of ricerca", ex);
        }
        return r;
    }
    
}
