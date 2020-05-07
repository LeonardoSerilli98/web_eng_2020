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
public class Canale_DAO_Imp extends DAO implements DAO_Interface, Canale_DAO{
    
    // dichiariamo qui i prepared Statement
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
            
            canaleByPreferenza = connection.prepareStatement("SELECT canaleID FROM Preferenza WHERE idPreferenze=?");
            
        } catch (SQLException ex) {
            throw new DataException("Errore di inizializzazione per 'GuidaTV Data Layer'", ex);
        }
         
    }
    
    @Override
    public void destroy() throws DataException {
                
            //qui dentro chiudiamo i preparedStmnt
            
        try {

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
    public Canale_Proxy makeObj(ResultSet rs) throws DataException {
        Canale_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idCanale"));
            a.setNome(rs.getString("nome"));
            
            a.setImmagine_key(rs.getInt("immagineID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Canale> getAll(ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Canale read(int key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Object t, String[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object t) {
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
