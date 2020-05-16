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
import models.Programma;
import proxys.Programma_Proxy;

/**
 *
 * @author leonardo
 */
public class Programma_DAO_Imp extends DAO implements Programma_DAO{
    
    private PreparedStatement create, read, update, delete, readAll;

    public Programma_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            create = connection.prepareStatement("INSERT INTO Programma(nome, descrizione, isSerie, approfondimento, genereID) VALUES(?,?,?,?,?)");
            read = connection.prepareStatement("SELECT * FROM Programma WHERE idProgramma=?");
            update = connection.prepareStatement("");
            delete = connection.prepareStatement("");
            
            readAll = connection.prepareStatement("");

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
    public Programma_Proxy makeObj() {
        return new Programma_Proxy(getDataLayer());
    }

    @Override
    public Programma_Proxy makeObj(ResultSet rs) throws DataException {
        Programma_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("idProgramma"));
            a.setNome(rs.getString("nome"));
            a.setIsSerie(rs.getBoolean("isSerie"));
            a.setDescrizione(rs.getString("dscrizione"));
            a.setApprofondimento(rs.getString("approfondimento"));
            a.setVersion(rs.getLong("version"));
            
            a.setImmagine_key(rs.getInt("immagineID"));
            a.setGenere_key(rs.getInt("genereID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List<Programma> getAll() throws DataException{
        List<Programma> result = new ArrayList();

        try (ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Programma) read(rs.getInt("idProgramma")));
            }
        
        } catch (SQLException ex) {
            throw new DataException("Unable to load Programma", ex);
        }
        return result;
    }

    @Override
    public void create(Programma item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Programma read(int key) throws DataException {
        Programma item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if(dataLayer.getCache().has(Programma.class, key)){
            item = dataLayer.getCache().get(Programma.class, key);
        }else{
            try{
                read.setInt(1, key);
                try (ResultSet rs = read.executeQuery()){
                    if(rs.next()){                      
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Programma.class, item);  
                    }
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Programma_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return item;
    }

    @Override
    public void update(Programma item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Programma Item) throws DataException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
