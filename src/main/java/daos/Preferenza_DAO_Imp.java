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

    public Preferenza_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Preferenza(fasciaID) VALUES(?)");
            createCanaliPreferiti = connection.prepareStatement("INSERT INTO Preferenza_has_Canale(preferenzaID, canaleID) VALUES (?,?)");
            read = connection.prepareStatement("SELECT * FROM Preferenza WHERE idPreferenza=?");
            update = connection.prepareStatement("UPDATE Preferenza SET fasciaID=? version=? WHERE idPreferenza=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Preferenza where idPreferenza=?");
            deleteCanaliPreferiti = connection.prepareStatement("DELETE FROM Preferenza_has_Canale WHERE preferenzaID=?");
            
            readAll = connection.prepareStatement("SELECT idPreferenza FROM Preferenza");



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
                create.setInt(1, item.getFascia().getKey());
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

            update.setInt(1, item.getFascia().getKey());
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
