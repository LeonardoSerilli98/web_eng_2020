/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import daos.Canale_DAO;
import daos.Canale_DAO_Imp;
import daos.Episodio_DAO;
import daos.Episodio_DAO_Imp;
import daos.Fascia_DAO;
import daos.Fascia_DAO_Imp;
import daos.Genere_DAO;
import daos.Genere_DAO_Imp;
import daos.Immagine_DAO;
import daos.Immagine_DAO_Imp;
import daos.Palinsesto_DAO;
import daos.Palinsesto_DAO_Imp;
import daos.Preferenza_DAO;
import daos.Preferenza_DAO_Imp;
import daos.Programma_DAO;
import daos.Programma_DAO_Imp;
import daos.Ricerca_DAO;
import daos.Ricerca_DAO_Imp;
import daos.Stagione_DAO;
import daos.Stagione_DAO_Imp;
import daos.Utente_DAO;
import daos.Utente_DAO_Imp;
import java.sql.SQLException;
import javax.sql.DataSource;
import models.Canale;
import models.Episodio;
import models.Fascia;
import models.Genere;
import models.Immagine;
import models.Palinsesto;
import models.Preferenza;
import models.Programma;
import models.Ricerca;
import models.Stagione;
import models.Utente;

/**
 *
 * @author leonardo
 */
public class GuidaTV_DataLayer extends DataLayer {

    public GuidaTV_DataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
    @Override
    public void init() throws DataException {
        //registriamo i DAO
        registerDAO(Canale.class, new Canale_DAO_Imp(this));    
        registerDAO(Episodio.class, new Episodio_DAO_Imp(this));  
        registerDAO(Fascia.class, new Fascia_DAO_Imp(this));        
        registerDAO(Genere.class, new Genere_DAO_Imp(this));        
        registerDAO(Immagine.class, new Immagine_DAO_Imp(this));
        registerDAO(Palinsesto.class, new Palinsesto_DAO_Imp(this));
        registerDAO(Preferenza.class, new Preferenza_DAO_Imp(this));
        registerDAO(Programma.class, new Programma_DAO_Imp(this));
        registerDAO(Ricerca.class, new Ricerca_DAO_Imp(this));
        registerDAO(Stagione.class, new Stagione_DAO_Imp(this));
        registerDAO(Utente.class, new Utente_DAO_Imp(this));   

  
    }

    //helpers    
    public Canale_DAO getCanaleDAO() {
        return (Canale_DAO) getDAO(Canale.class);
    }    
    public Episodio_DAO getEpisodioDAO() {
        return (Episodio_DAO) getDAO(Episodio.class);
        
    }    
    public Fascia_DAO getFasciaDAO() {
        return (Fascia_DAO) getDAO(Fascia.class);
    }    
    public Genere_DAO getGenereDAO() {
        return (Genere_DAO) getDAO(Genere.class);
    }    
    public Immagine_DAO getImmagineDAO() {
        return (Immagine_DAO) getDAO(Immagine.class);
    }    
    public Palinsesto_DAO getPalinsestoDAO() {
        return (Palinsesto_DAO) getDAO(Palinsesto.class);
    }    
    public Preferenza_DAO getPreferenzaDAO() {
        return (Preferenza_DAO) getDAO(Preferenza.class);
    }    
    public Programma_DAO getProgrammaDAO() {
        return (Programma_DAO) getDAO(Programma.class);
    }    
    public Ricerca_DAO getRicercaDAO() {
        return (Ricerca_DAO) getDAO(Ricerca.class);
    }    
    public Stagione_DAO getStagioneDAO() {
        return (Stagione_DAO) getDAO(Stagione.class);
    }
    public Utente_DAO getUtenteDAO() {
        return (Utente_DAO) getDAO(Utente.class);
    }


}
