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
import proxys.Palinsesto_Proxy;

/**
 *
 * @author leonardo
 */
public class Palinsesto_DAO_Imp extends DAO implements Palinsesto_DAO, DAO_Interface{

    public Palinsesto_DAO_Imp(DataLayer d) {
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
    public Palinsesto_Proxy makeObj() {
        return new Palinsesto_Proxy(getDataLayer());
    }

    @Override
    public Palinsesto_Proxy makeObj(ResultSet rs) throws DataException {
        Palinsesto_Proxy a = makeObj();
        try {
            
            a.setKey(rs.getInt("ID"));
            a.setData(rs.getDate("data"));
            a.setInizio(rs.getTime("inizio"));
            a.setFine(rs.getTime("fine"));
            
            a.setProgramma_key(rs.getInt("programmaID"));
            a.setEpisodio_key(rs.getInt("episodioID"));
            a.setCanale_key(rs.getInt("canaleID"));
            a.setFascia_key(rs.getInt("fasciaID"));
    
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
    public Object read(int key) throws DataException {
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
