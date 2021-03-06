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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Programma;
import models.Stagione;
import proxys.Stagione_Proxy;

/**
 *
 * @author leonardo
 */
public class Stagione_DAO_Imp extends DAO implements Stagione_DAO {

    private PreparedStatement create, read, update, delete, readAll;
    private PreparedStatement stagioneByProgramma;

    public Stagione_DAO_Imp(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            create = connection.prepareStatement("INSERT INTO Stagione(numero, programmaID, immagineID) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            read = connection.prepareStatement("SELECT * FROM Stagione WHERE idStagione=?");
            update = connection.prepareStatement("UPDATE Stagione SET numero=?, version=?, programmaID=?, immagineID=? WHERE idStagione=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Stagione where idStagione=?");

            readAll = connection.prepareStatement("SELECT idStagione FROM Stagione");
            stagioneByProgramma = connection.prepareStatement("SELECT * FROM Stagione WHERE programmaID=? AND numero=?");
        } catch (SQLException ex) {
            throw new DataException("Errore d'inizializzazione Data Layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {

            create.close();
            read.close();
            update.close();
            delete.close();
            readAll.close();
            stagioneByProgramma.close();

        } catch (SQLException ex) {
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
    public List<Stagione> getAll() throws DataException {
        List<Stagione> result = new ArrayList();

        try ( ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Stagione) read(rs.getInt("idStagione")));
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to load Stagione", ex);
        }
        return result;
    }

    @Override
    public void create(Stagione item) throws DataException {
        if (item.getKey() != null && item.getKey() > 0) {
            update(item);
        } else {
            try {
                create.setInt(1, item.getNumero());
                create.setInt(2, item.getProgramma().getKey());

                if (item.getImmagine() == null || item.getImmagine().getKey() == 0) {
                    create.setNull(3, java.sql.Types.INTEGER);
                } else {
                    create.setInt(3, item.getImmagine().getKey());
                }

                if (create.executeUpdate() == 1) {
                    ResultSet keys = create.getGeneratedKeys();
                    while (keys.next()) {
                        item.setKey(keys.getInt(1));

                        // ricordiamo di inserire l'oggetto appena creato in cache
                        dataLayer.getCache().add(Stagione.class, item);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to create Stagione", ex);
            }
        }
    }

    @Override
    public Stagione read(int key) throws DataException {
        Stagione item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if (dataLayer.getCache().has(Stagione.class, key)) {
            item = dataLayer.getCache().get(Stagione.class, key);
        } else {
            try {
                read.setInt(1, key);
                try ( ResultSet rs = read.executeQuery()) {
                    if (rs.next()) {
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
    public void update(Stagione item) throws DataException {
        try {
            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                return;
            }

            long versione = (long) item.getVersion();

            update.setInt(1, item.getNumero());
            update.setLong(2, versione + 1);
            update.setInt(3, item.getProgramma().getKey());
            
            if (item.getImmagine().getKey() != 0) {
                update.setInt(4, item.getImmagine().getKey());
            } else {
                update.setNull(4, java.sql.Types.INTEGER);
            }

            update.setInt(5, item.getKey());
            update.setLong(6, item.getVersion());

            if (update.executeUpdate() == 0) {
                throw new OptimisticLockException(item);
            }

            item.setVersion(versione + 1);
        } catch (SQLException ex) {
            throw new DataException("Unable to update stagione", ex);
        }
    }

    @Override
    public void delete(Stagione item) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stagione getStagioneByProgramma(Programma p, int num) throws DataException {
        Stagione s = null;
        try {

            stagioneByProgramma.setInt(1, p.getKey());
            stagioneByProgramma.setInt(2, num);

            try ( ResultSet rs = stagioneByProgramma.executeQuery()) {
                while (rs.next()) {
                    s = makeObj(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find stagione by programma", ex);
        }
        return s;
    }

}
