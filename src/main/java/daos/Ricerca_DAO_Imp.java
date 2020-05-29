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

import models.Ricerca;
import models.Utente;
import proxys.Ricerca_Proxy;

/**
 *
 * @author leonardo
 */
public class Ricerca_DAO_Imp extends DAO implements Ricerca_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Ricerca_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Ricerca(fasciaID, programmaID, genereID, canaleID) VALUES(?,?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Ricerca WHERE idRicerca=?");
            update = connection.prepareStatement("UPDATE Ricerca SET fasciaID=? programmaID=? genereID=? canaleID=? version=? WHERE idRicerca=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Ricerca where idRicerca=?");
            
            readAll = connection.prepareStatement("SELECT idRicerca FROM Ricerca");

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
                create.setInt(1, item.getFascia().getKey());
                create.setInt(2, item.getProgramma().getKey());
                create.setInt(3, item.getGenere().getKey());
                create.setInt(4, item.getCanale().getKey());
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

            update.setInt(1, item.getFascia().getKey());
            update.setInt(2, item.getProgramma().getKey());
            update.setInt(3, item.getGenere().getKey());
            update.setInt(4, item.getCanale().getKey());
            update.setLong(5, versione + 1);

            update.setInt(6, item.getKey());
            update.setLong(7, versione);

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
