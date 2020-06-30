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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Canale;
import models.Preferenza;
import proxys.Canale_Proxy;

/**
 *
 * @author leonardo
 */
public class Canale_DAO_Imp extends DAO implements Canale_DAO{
    
    // dichiariamo qui i prepared Statement
    private PreparedStatement create, read, update, delete, readAll;
    private PreparedStatement canaleByPreferenza;

    
    public Canale_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    //implementiamo i metodi init e destroy ereditati da DAO

    @Override
    public void init() throws DataException {
        super.init();
        
        //qui dentro inizializziamo i prepareStmnt (prepare non prepared!)
        
        try {            
            
            create = connection.prepareStatement("INSERT INTO Canale (nome, immagineID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            read = connection.prepareStatement("SELECT * FROM Canale WHERE idCanale=?");
            update = connection.prepareStatement("UPDATE Canale SET nome=?, immagineID=?, version=? WHERE idCanale=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Canale WHERE idCanale=?");
            
            readAll = connection.prepareStatement("SELECT idCanale FROM Canale");            
            
            canaleByPreferenza = connection.prepareStatement("SELECT canaleID FROM Preferenza WHERE idPreferenze=?");
            
            
        } catch (SQLException ex) {
            throw new DataException("Errore di inizializzazione per 'GuidaTV Data Layer'", ex);
        }
         
    }
    
    @Override
    public void destroy() throws DataException {
                
            //qui dentro chiudiamo i preparedStmnt
            
        try {
            
            create.close();
            read.close();
            update.close();
            delete.close();
            readAll.close();

            canaleByPreferenza.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Canale_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy();
    }

    
    //CRUD makeObj e getAll ereditati da DAO_Interdace
    
    @Override
    public Canale_Proxy makeObj() {
        return new Canale_Proxy(getDataLayer());
    }

    @Override
    public Canale_Proxy makeObj(ResultSet rs) throws DataException{
        Canale_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idCanale"));
            a.setNome(rs.getString("nome"));
            a.setVersion(rs.getLong("version"));
            
            a.setImmagine_key(rs.getInt("immagineID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create Canale form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Canale> getAll() throws DataException{
        List<Canale> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Canale) read(rs.getInt("idCanale")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Canale", ex);
        }
        return result; 
    }

    @Override
    public void create(Canale item) throws DataException{
                    
        //se l'ID del'oggetto gia esiste chiamiamo l'update altrimenti ne creiamo uno
            if (item.getKey() != null && item.getKey() > 0) { 
                update(item);
            }else{ 
                try {
                    
                    create.setString(1, item.getNome());
                    create.setInt(2, item.getImmagine().getKey());
                    
                    if(create.executeUpdate() == 1){
                        // per leggere la chiave generata dal db usiamo il 
                        // metodo getGeneratedKeys sullo statement
                        //che ritorna un resultset
                        ResultSet keys = create.getGeneratedKeys();
                        if(keys.next()){
                            item.setKey(keys.getInt(1));
                            
                            // ricordiamo di inserire l'oggetto appena creato in cache
                            dataLayer.getCache().add(Canale.class, item);
                        }
                    }
                } catch (SQLException ex) {
                    throw new DataException("Unable to create Canale", ex);
                }
                
            }
            
            // infine che sia stato creato o aggiornato dobbiamo resettare l'attributo 
            // dirty del proxy
            if(item instanceof Data_ItemProxy){
                ((Data_ItemProxy) item).setDirty(false);
            }
    }

    @Override
    public Canale read(int key) throws DataException {
        Canale item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Canale.class, key)){
            item = dataLayer.getCache().get(Canale.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Canale.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Canale_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Canale item) throws DataException{
                
         try {
             // se l'item risulta un proxy che non ha subito modifiche non c'è motivo di effettuarne l'update
             if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                 return;
             }
             
             // compiliamo i field della query
             long versione = (long) item.getVersion();
             int immagineID = item.getImmagine().getKey();
 
             update.setString(1, item.getNome());
             update.setInt(2, immagineID);
             update.setLong(3, versione+1);
             
             update.setInt(4, item.getKey());
             update.setLong(5, versione);
             
             // nel caso la query non va a buon fine è molto probabile che la 
             // versione sul db sia diversa da quella del nostro item
             // essendo la tecnica che sfrutta la versione l'optimisticLock
             // chiamiamo la relativa eccezzione
             if(update.executeUpdate() == 0){
                 throw new OptimisticLockException(item);
             }
             
             // infine  ricordiamo di aggiornare la vesione del nostro Item
             item.setVersion(versione + 1);
             
         } catch (SQLException ex) {
             throw new DataException("Unable to update Canale", ex);
         }
    }

    @Override
    public void delete(Canale item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //qui implementiamo i metodi particolari descritti derivati da Canale_Dao

    @Override
    public List<Canale> getCanaliByPreferenza(Preferenza preferenza) throws DataException {
        List<Canale> result = new ArrayList();
        try {
            canaleByPreferenza.setInt(1, preferenza.getKey());
            try (ResultSet rs = canaleByPreferenza.executeQuery()) {
                while (rs.next()) {
                    result.add(read(rs.getInt("canaleID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere canali by preferenza", ex);
        }
        return result;
    }
    
   
    
    
}
