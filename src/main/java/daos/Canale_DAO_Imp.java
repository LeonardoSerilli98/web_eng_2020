/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO;
import data.DataException;
import data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            
            create = connection.prepareStatement("INSERT INTO Canale (nome, immagineID, ) VALUES (?, ?)");
            read = connection.prepareStatement("SELECT * FROM Canale WHERE idCanale=?");
            update = connection.prepareStatement("");
            delete = connection.prepareStatement("");
            
            readAll = connection.prepareStatement("");            
            
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
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Canale> getAll() throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Canale item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
