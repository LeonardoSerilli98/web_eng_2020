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
import models.Utente;
import proxys.Utente_Proxy;

/**
 *
 * @author leonardo
 */
public class Utente_DAO_Imp extends DAO implements Utente_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Utente_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Utente(email, password, preferenzaID, ricercaID) VALUES(?,MD5(?),?,?)");
            read = connection.prepareStatement("SELECT * FROM Utente WHERE idUtente=?");

            update = connection.prepareStatement("UPDATE Utente SET email=? password=MD5(?) ricercaID=? preferenzaID=? version=? WHERE idUtente=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Utente WHERE idUtente=?");
            
            readAll = connection.prepareStatement("SELECT idUtente FROM Utente");

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
    public Utente_Proxy makeObj() {
        return new Utente_Proxy(getDataLayer());
    }

    @Override
    public Utente_Proxy makeObj(ResultSet rs) throws DataException {
        Utente_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idUtente"));
            a.setEmail(rs.getString("email"));
            a.setEmail(rs.getString("password"));
            a.setVersion(rs.getLong("version"));
            
            a.setRicerca_key(rs.getInt("ricercaID"));
            a.setPreferenza_key(rs.getInt("preferenzaID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Utente> getAll() throws DataException{
        List<Utente> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Utente) read(rs.getInt("idUtente")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Utente", ex);
        }
        return result;
    }

    @Override
    public void create(Utente item) throws DataException{
        if (item.getKey() != null && item.getKey() > 0){
            update(item);
        } else {
            try {
                create.setString(1, item.getEmail());
                create.setString(2, item.getPassword());
                create.setInt(3, item.getRicerca().getKey());
                create.setInt(4, item.getPreferenza().getKey());
                if (create.executeUpdate() == 1){
                    ResultSet keys = create.getGeneratedKeys();
                    while ( keys.next()) {
                        item.setKey(keys.getInt(1));

                        // ricordiamo di inserire l'oggetto appena creato in cache
                        dataLayer.getCache().add(Utente.class, item);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to create utente", ex);
            }
        }
    }

    @Override
    public Utente read(int key) throws DataException {
        
        Utente item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Utente.class, key)){
            item = dataLayer.getCache().get(Utente.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Utente.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Utente_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Utente item) throws DataException{
        try {
            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                return;
            }

            long versione = (long) item.getVersion();

            update.setString(1, item.getEmail());
            update.setString(2, item.getPassword());
            update.setInt(3, item.getRicerca().getKey());
            update.setInt(4, item.getPreferenza().getKey());
            update.setLong(5, versione + 1);

            update.setInt(6, item.getKey());
            update.setLong(7, versione);

            if (update.executeUpdate() == 0){
                throw new OptimisticLockException(item);
            }

            item.setVersion(versione + 1);
        } catch (SQLException ex) {
            throw new DataException("Unable to update utente", ex);
        }
    }

    @Override
    public void delete(Utente item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
