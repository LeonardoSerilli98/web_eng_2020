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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Canale;
import models.Fascia;
import models.Palinsesto;
import models.Programma;
import models.Ricerca;
import proxys.Palinsesto_Proxy;

/**
 *
 * @author leonardo
 */
public class Palinsesto_DAO_Imp extends DAO implements Palinsesto_DAO {

    private PreparedStatement create, read, update, delete, readAll;
    private PreparedStatement palinsestiByCanale, palinsestiByProgramma, ricerca, ricercaWithPagina, checkExistence, getPalinsestiByCanaleAndFascia;

    public Palinsesto_DAO_Imp(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            create = connection.prepareStatement("INSERT INTO Palinsesto(inizio, fine, data, programmaID, episodioID, fasciaID, canaleID) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            read = connection.prepareStatement("SELECT * FROM Palinsesto WHERE idPalinsesto=?");
            update = connection.prepareStatement("UPDATE Palinsesto SET inizio=?, fine=?, data=?, programmaID=?, episodioID=?, fasciaID=?, canaleID=?, versione=? WHERE idPalinsesto=? and version=?");
            delete = connection.prepareStatement("DELETE FROM Palinsesto where idPalinsesto=?");

            readAll = connection.prepareStatement("SELECT idPalinsesto FROM Palinsesto ORDER BY data ASC, inizio ASC");

            palinsestiByCanale = connection.prepareStatement("SELECT * FROM Palinsesto WHERE canaleID=? AND data=? ORDER BY data ASC, inizio ASC");
            palinsestiByProgramma = connection.prepareStatement("SELECT * FROM Palinsesto WHERE programmaID=? ORDER BY data ASC, inizio ASC");
            ricerca = connection.prepareStatement("SELECT * FROM Palinsesto WHERE data=? UNION SELECT * FROM Palinsesto WHERE inizio>=? UNION SELECT * FROM Palinsesto WHERE inizio<=? UNION SELECT * FROM Palinsesto WHERE canaleID=? UNION SELECT * FROM Palinsesto WHERE fasciaID=? UNION SELECT pa.idPalinsesto, pa.inizio, pa.fine, pa.data, pa.programmaID, pa.episodioID, pa.fasciaID, pa.canaleID, pa.version FROM Palinsesto AS pa INNER JOIN Programma AS pr ON pr.idProgramma = pa.programmaID WHERE pr.nome LIKE ? UNION SELECT pa.idPalinsesto, pa.inizio, pa.fine, pa.data, pa.programmaID, pa.episodioID, pa.fasciaID, pa.canaleID, pa.version FROM Palinsesto AS pa INNER JOIN Programma AS pr ON pr.idProgramma = pa.programmaID WHERE pr.genereID=?  ORDER BY data ASC, inizio ASC ");
            checkExistence = connection.prepareCall("SELECT * from Palinsesto WHERE inizio=? AND fine=? AND data=? AND programmaID=? AND episodioID=? AND fasciaID=? AND canaleID=? AND version=? ");
            getPalinsestiByCanaleAndFascia = connection.prepareStatement("SELECT * FROM Palinsesto WHERE canaleID=? AND fasciaID=? and data=? ORDER BY data ASC, inizio DESC");
            ricercaWithPagina = connection.prepareStatement("SELECT * FROM Palinsesto WHERE data=? UNION SELECT * FROM Palinsesto WHERE inizio>=? UNION SELECT * FROM Palinsesto WHERE inizio<=? UNION SELECT * FROM Palinsesto WHERE canaleID=? UNION SELECT * FROM Palinsesto WHERE fasciaID=? UNION SELECT pa.idPalinsesto, pa.inizio, pa.fine, pa.data, pa.programmaID, pa.episodioID, pa.fasciaID, pa.canaleID, pa.version FROM Palinsesto AS pa INNER JOIN Programma AS pr ON pr.idProgramma = pa.programmaID WHERE pr.nome=? UNION SELECT pa.idPalinsesto, pa.inizio, pa.fine, pa.data, pa.programmaID, pa.episodioID, pa.fasciaID, pa.canaleID, pa.version FROM Palinsesto AS pa INNER JOIN Programma AS pr ON pr.idProgramma = pa.programmaID WHERE pr.genereID=?  ORDER BY data ASC, inizio ASC  LIMIT ?,?");
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
            palinsestiByCanale.close();
            palinsestiByProgramma.close();
            ricerca.close();
            checkExistence.close();
            getPalinsestiByCanaleAndFascia.close();
            ricercaWithPagina.close();

        } catch (SQLException ex) {
            throw new DataException("Errore di chiusura Data Layer", ex);
        }
        super.destroy();
    }

    @Override
    public Palinsesto_Proxy makeObj() {
        return new Palinsesto_Proxy(getDataLayer());
    }

    @Override
    public Palinsesto_Proxy makeObj(ResultSet rs) throws DataException {
        Palinsesto_Proxy a = makeObj();

        try {

            a.setKey(rs.getInt("idPalinsesto"));
            a.setData(rs.getDate("data"));
            a.setInizio(rs.getTime("inizio"));
            a.setFine(rs.getTime("fine"));
            a.setVersion(rs.getLong("version"));

            a.setProgramma_key(rs.getInt("programmaID"));
            a.setEpisodio_key(rs.getInt("episodioID"));
            a.setCanale_key(rs.getInt("canaleID"));
            a.setFascia_key(rs.getInt("fasciaID"));

        } catch (SQLException ex) {
            throw new DataException("Unable to create Palinsesto object form ResultSet asd", ex);
        }
        return a;
    }

    @Override
    public List<Palinsesto> getAll() throws DataException {
        List<Palinsesto> result = new ArrayList();

        try ( ResultSet rs = readAll.executeQuery()) {
            while (rs.next()) {
                result.add((Palinsesto) read(rs.getInt("idPalinsesto")));
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to load Palinsesto", ex);
        }
        return result;
    }

    @Override
    public void create(Palinsesto item) throws DataException {
        if (item.getKey() != null && item.getKey() > 0) {
            update(item);
        } else {
            try {

                create.setTime(1, item.getInizio());
                create.setTime(2, item.getFine());
                create.setDate(3, (Date) item.getData());
                create.setInt(4, item.getProgramma().getKey());
                create.setInt(5, item.getEpisodio().getKey());
                create.setInt(6, item.getFascia().getKey());
                create.setInt(7, item.getCanale().getKey());

                if (create.executeUpdate() == 1) {
                    ResultSet keys = create.getGeneratedKeys();
                    if (keys.next()) {
                        item.setKey(keys.getInt(1));
                        dataLayer.getCache().add(Palinsesto.class, item);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to create Palinsesto", ex);
            }

        }
        if (item instanceof Data_ItemProxy) {
            ((Data_ItemProxy) item).setDirty(false);
        }
    }

    @Override
    public Palinsesto read(int key) throws DataException {
        Palinsesto item = null;
        // Controllo se l'oggetto è nella cache, in caso prendo quello
        if (dataLayer.getCache().has(Palinsesto.class, key)) {
            item = dataLayer.getCache().get(Palinsesto.class, key);
        } else {
            try {
                read.setInt(1, key);
                try ( ResultSet rs = read.executeQuery()) {
                    if (rs.next()) {
                        item = makeObj(rs);
                        // ricorda di aggiungere l'oggetto appena creato nella cache
                        dataLayer.getCache().add(Palinsesto.class, item);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Palinsesto_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return item;
    }

    @Override
    public void update(Palinsesto item) throws DataException {
        try {

            if (item instanceof Data_ItemProxy && !((Data_ItemProxy) item).isDirty()) {
                return;
            }

            long versione = (long) item.getVersion();

            update.setTime(1, item.getInizio());
            update.setTime(2, item.getFine());
            update.setDate(3, (Date) item.getData());
            update.setInt(4, item.getProgramma().getKey());
            update.setInt(5, item.getEpisodio().getKey());
            update.setInt(6, item.getFascia().getKey());
            update.setInt(7, item.getCanale().getKey());
            update.setLong(8, versione + 1);

            update.setInt(9, item.getKey());
            update.setLong(10, versione);

            if (update.executeUpdate() == 0) {
                throw new OptimisticLockException(item);
            }

            item.setVersion(versione + 1);

        } catch (SQLException ex) {
            throw new DataException("Unable to update Palinsesto", ex);
        }
    }

    @Override
    public void delete(Palinsesto item) throws DataException {
        if (item == null) {
            return;
        }
        try {
            delete.setInt(1, item.getKey());
            delete.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete ricerca", ex);
        }
    }

    /**
     *
     * @param canale
     * @return
     * @throws DataException
     */
    @Override
    public List<Palinsesto> getPalinsestiByCanale(Canale canale) throws DataException {

        List<Palinsesto> result = new ArrayList();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        try {
            palinsestiByCanale.setInt(1, canale.getKey());
            palinsestiByCanale.setDate(2, Date.valueOf(today));
            try ( ResultSet rs = palinsestiByCanale.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere Palinsesti by Canale", ex);
        }
        return result;
    }

    @Override
    public List<Palinsesto> getPalinsestiByCanale(Canale canale, String data) throws DataException {

        List<Palinsesto> result = new ArrayList();

        try {
            palinsestiByCanale.setInt(1, canale.getKey());
            palinsestiByCanale.setDate(2, Date.valueOf(data));
            try ( ResultSet rs = palinsestiByCanale.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere Palinsesti by Canale", ex);
        }
        return result;
    }

    @Override
    public List<Palinsesto> getPalinsestiByProgramma(Programma programma) throws DataException {

        List<Palinsesto> result = new ArrayList();

        try {
            palinsestiByProgramma.setInt(1, programma.getKey());
            try ( ResultSet rs = palinsestiByProgramma.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere Palinsesti by Programma", ex);
        }
        return result;
    }

    @Override
    public List<Palinsesto> ricerca(Ricerca r) throws DataException {

        List<Palinsesto> result = new ArrayList();

        try {
            ricerca.setDate(1, r.getData());
            ricerca.setTime(2, r.getInizioMin());
            ricerca.setTime(3, r.getInizioMax());

            if (r.getCanale() == null || r.getCanale().getKey() == 0) {
                ricerca.setNull(4, java.sql.Types.INTEGER);
            } else {
                ricerca.setInt(4, r.getCanale().getKey());
            }

            if (r.getFascia() == null || r.getFascia().getKey() == 0) {
                ricerca.setNull(5, java.sql.Types.INTEGER);
            } else {
                ricerca.setInt(5, r.getFascia().getKey());
            }

            ricerca.setString(6, "%"+r.getTitolo()+"%");

            if (r.getGenere() == null || r.getGenere().getKey() == 0) {
                ricerca.setNull(7, java.sql.Types.INTEGER);
            } else {
                ricerca.setInt(7, r.getGenere().getKey());
            }
            try ( ResultSet rs = ricerca.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Palinsesto_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public Palinsesto checkExistence(Palinsesto item) throws DataException {
        Palinsesto p = null;
        try {
            checkExistence.setTime(1, item.getInizio());
            checkExistence.setTime(2, item.getFine());
            checkExistence.setDate(3, (Date) item.getData());
            checkExistence.setInt(4, item.getProgramma().getKey());
            checkExistence.setInt(5, item.getEpisodio().getKey());
            checkExistence.setInt(6, item.getFascia().getKey());
            checkExistence.setInt(7, item.getCanale().getKey());
            checkExistence.setLong(8, item.getVersion());

            try ( ResultSet rs = checkExistence.executeQuery()) {
                while (rs.next()) {
                    p = makeObj(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Palinsesto_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Override
    public List<Palinsesto> getPalinsestiByCanale(Canale canale, Fascia fascia) throws DataException {

        List<Palinsesto> result = new ArrayList();
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));


        if (fascia == null) {
            return getPalinsestiByCanale(canale);
        }

        try {

            getPalinsestiByCanaleAndFascia.setInt(1, canale.getKey());
            getPalinsestiByCanaleAndFascia.setInt(2, fascia.getKey());
            getPalinsestiByCanaleAndFascia.setDate(3, Date.valueOf(today));
            try ( ResultSet rs = palinsestiByCanale.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }

            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile ottenere Palinsesti by Canale", ex);
        }
        return result;
    }


    @Override
    public List<Palinsesto> ricerca(Ricerca r, int pagina) throws DataException {

        List<Palinsesto> result = new ArrayList();

        try {
            ricercaWithPagina.setDate(1, r.getData());
            ricercaWithPagina.setTime(2, r.getInizioMin());
            ricercaWithPagina.setTime(3, r.getInizioMax());

            if (r.getCanale() == null || r.getCanale().getKey() == 0) {
                ricercaWithPagina.setNull(4, java.sql.Types.INTEGER);
            } else {
                ricercaWithPagina.setInt(4, r.getCanale().getKey());
            }

            if (r.getFascia() == null || r.getFascia().getKey() == 0) {
                ricercaWithPagina.setNull(5, java.sql.Types.INTEGER);
            } else {
                ricercaWithPagina.setInt(5, r.getFascia().getKey());
            }

            ricercaWithPagina.setString(6, r.getTitolo());

            if (r.getGenere() == null || r.getGenere().getKey() == 0) {
                ricercaWithPagina.setNull(7, java.sql.Types.INTEGER);
            } else {
                ricercaWithPagina.setInt(7, r.getGenere().getKey());
            }
            ricercaWithPagina.setInt(8, 5*pagina);
            ricercaWithPagina.setInt(9, 5*pagina+5);
            try ( ResultSet rs = ricercaWithPagina.executeQuery()) {
                while (rs.next()) {
                    result.add(makeObj(rs));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Palinsesto_DAO_Imp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }






}
