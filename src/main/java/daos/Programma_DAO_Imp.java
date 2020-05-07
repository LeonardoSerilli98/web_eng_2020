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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import models.Programma;
import proxys.Programma_Proxy;

/**
 *
 * @author leonardo
 */
public class Programma_DAO_Imp extends DAO implements Programma_DAO, DAO_Interface{

    public Programma_DAO_Imp(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void destroy() throws DataException {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Programma_Proxy makeObj() {
        return new Programma_Proxy(getDataLayer());
    }

    @Override
    public Programma_Proxy makeObj(ResultSet rs) throws DataException {
        Programma_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("ID"));
            a.setNome(rs.getString("nome"));
            a.setIsSerie(rs.getBoolean("isSerie"));
            a.setDescrizione(rs.getString("dscrizione"));
            a.setApprofondimento(rs.getString("approfondimento"));
            
            a.setImmagine_key(rs.getInt("immagineID"));
            a.setGenere_key(rs.getInt("genereID"));
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }

    @Override
    public List getAll(ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Programma read(int key) throws DataException {
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
    
}
