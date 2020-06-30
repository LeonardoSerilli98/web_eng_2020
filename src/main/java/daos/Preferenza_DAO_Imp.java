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

import models.Canale;
import models.Preferenza;
import models.Preferenza_Imp;
import models.Ricerca;
import proxys.Preferenza_Proxy;

/**
 *
 * @author leonardo
 */
public class Preferenza_DAO_Imp extends DAO implements Preferenza_DAO{
    
    private PreparedStatement create, createCanaliPreferiti, read, update, delete, readAll, deleteCanaliPreferiti;
    private PreparedStatement checkExistence;
    
    public Preferenza_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Preferenza(fasciaID) VALUES(?)",  Statement.RETURN_GENERATED_KEYS);
            createCanaliPreferiti = connection.prepareStatement("INSERT INTO Preferenza_has_Canale(preferenzaID, canaleID) VALUES (?,?)",  Statement.RETURN_GENERATED_KEYS);
            read = connection.prepareStatement("SELECT * FROM Preferenza WHERE idPreferenza=?");
            update = connection.prepareStatement("UPDATE Preferenza SET fasciaID=?, version=? WHERE idPreferenza=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Preferenza where idPreferenza=?");
            deleteCanaliPreferiti = connection.prepareStatement("DELETE FROM Preferenza_has_Canale WHERE preferenzaID=?");
            
            readAll = connection.prepareStatement("SELECT idPreferenza FROM Preferenza");
            checkExistence = connection.prepareStatement("SELECT p.idPreferenza, p.fasciaID, p.version FROM Preferenza as p INNER JOIN Utente as u WHERE u.email=? AND p.idPreferenza=u.preferenzaID");


        }catch (SQLException ex) {
            throw new DataException("Errore d'inizializzazione Data Layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        try{
            
            create.close();
            createCanaliPreferiti.close();
            read.close();
            update.close();
            delete.close();
            readAll.close();
            deleteCanaliPreferiti.close();
            checkExistence.close();
            
        }catch (SQLException ex) {
            throw new DataException("Errore di chiusura Data Layer", ex);
        }
        super.destroy();
    }


    @Override
    public Preferenza_Proxy makeObj() {
        return new Preferenza_Proxy(getDataLayer());
    }

    @Override
    public Preferenza_Proxy makeObj(ResultSet rs) throws DataException {
        Preferenza_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idPreferenza"));
            a.setVersion(rs.getLong("version"));
                       
            a.setFascia_key(rs.getInt("fasciaID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;    }

    @Override
    public List<Preferenza> getAll() throws DataException{
        List<Preferenza> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Preferenza) read(rs.getInt("idPreferenza")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Preferenza", ex);
        }
        return result;
    }

    @Override
    public void create(Preferenza item) throws DataException{
        if (item.getKey() != null && item.getKey() > 0){
            update(item);
        } else {
            try {                
                if(item.getFascia()==null || item.getFascia().getKey()==0){
                    create.setNull(1, java.sql.Types.INTEGER);                
                }else{
                    create.setInt(1, item.getFascia().getKey());
                }
                if (create.executeUpdate() == 1){
                    ResultSet keys = create.getGeneratedKeys();
                    while ( keys.next()) {
                        item.setKey(keys.getInt(1));

                        // ricordiamo di inserire l'oggetto appena creato in cache
                        dataLayer.getCache().add(Preferenza.class, item);
                    }
                }

                for (Canale c : item.getCanali()) {
                    createCanaliPreferiti.setInt(1, item.getKey());
                    createCanaliPreferiti.setInt(2, c.getKey());
                    createCanaliPreferiti.executeUpdate();
                }

            } catch (SQLException ex) {
                throw new DataException("Unable to create preferenza", ex);
            }
        }
    }

    @Override
    public Preferenza read(int key) throws DataException {
        Preferenza item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Preferenza.class, key)){
            item = dataLayer.getCache().get(Preferenza.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Preferenza.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Preferenza_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Preferenza item) throws DataException{
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
            update.setLong(2, versione + 1);

            update.setInt(3, item.getKey());
            update.setLong(4, versione);

            if (update.executeUpdate() == 0){
                throw new OptimisticLockException(item);
            }

            item.setVersion(versione + 1);

            deleteCanaliPreferiti.setInt(1, item.getKey());
            deleteCanaliPreferiti.executeUpdate();

            for (Canale c : item.getCanali()) {
                createCanaliPreferiti.setInt(1, item.getKey());
                createCanaliPreferiti.setInt(2, c.getKey());
                createCanaliPreferiti.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to update preferenza", ex);
        }
    }

    @Override
    public void delete(Preferenza item) throws DataException{
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
    public Preferenza checkExistence(String username) throws DataException {
        Preferenza p = null;
        try {
            
            checkExistence.setString(1, username);   
            
            try(ResultSet rs = checkExistence.executeQuery()){
                while (rs.next()) {
                    p = makeObj(rs);
                }
            }                     
        } catch (SQLException ex) {
           throw new DataException("Unable to check existence of preferenza", ex);
        }
        return p;
    }
    
}
