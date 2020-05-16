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
import models.Stagione;
import proxys.Stagione_Proxy;

/**
 *
 * @author leonardo
 */
public class Stagione_DAO_Imp extends DAO implements Stagione_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Stagione_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Stagione(numero, programmaID) VALUES(?,?)");
            read = connection.prepareStatement("SELECT * FROM Stagione WHERE idStagione=?");
            update = connection.prepareStatement("UPDATE Stagione SET numero=? version=? programmaID=? WHERE idStagione=? and version=?");
            delete = connection.prepareStatement("");
            
            readAll = connection.prepareStatement("SELECT idStagione FROM Stagione");

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
    public Stagione_Proxy makeObj() {
        return new Stagione_Proxy(getDataLayer());
    }

    @Override
    public Stagione_Proxy makeObj(ResultSet rs) throws DataException {
        Stagione_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idStagione"));
            a.setNumero(rs.getInt("numero"));
            a.setVersion(rs.getLong("version"));
            
            a.setImmagine_key(rs.getInt("immagineID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Stagione> getAll() throws DataException{
        List<Stagione> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Stagione) read(rs.getInt("idStagione")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Stagione", ex);
        }
        return result;
    }

    @Override
    public void create(Stagione item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stagione read(int key) throws DataException {
        Stagione item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Stagione.class, key)){
            item = dataLayer.getCache().get(Stagione.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Stagione.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Stagione_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Stagione item) throws DataException{
        try {
            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                return;
            }

            long versione = (long) item.getVersion();

            update.setInt(1, item.getNumero());
            update.setLong(2, versione + 1);
            update.setInt(3, item.getProgramma().getKey());

            update.setInt(6, item.getKey());
            update.setLong(7, item.getVersion());

            if (update.executeUpdate() == 0){
                throw new OptimisticLockException(item);
            }

            item.setVersion(versione + 1);
        } catch (SQLException ex) {
            throw new DataException("Unable to update stagione", ex);
        }
    }

    @Override
    public void delete(Stagione item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
