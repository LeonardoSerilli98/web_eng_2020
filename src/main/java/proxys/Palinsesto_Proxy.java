/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import data.DataLayer;
import java.sql.Time;
import java.util.Date;
import models.Canale;
import models.Episodio;
import models.Fascia;
import models.Palinsesto_Imp;
import models.Programma;

/**
 *
 * @author leonardo
 */
public class Palinsesto_Proxy extends Palinsesto_Imp{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int programma_key;
    protected int canale_key;
    protected int fascia_key;
    protected int episodio_key;
        
    public Palinsesto_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.programma_key = 0;
        this.canale_key = 0;
        this.fascia_key = 0;
        this.episodio_key = 0;
    }
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
    
    @Override
    public void setKey(int key) {
        super.setKey(key);
        this.dirty = true;
    }

    @Override
    public void setData(Date data) {
        super.setData(data);
        this.dirty = true;
    }
    
     @Override
    public void setFine(Time fine) {
        super.setFine(fine);
        this.dirty = true;
    }
    
    @Override
    public void setInizio(Time inizio) {
        super.setInizio(inizio); 
        this.dirty = true;
    }

    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
    @Override
    public void setEpisodio(Episodio episodio) {
        super.setEpisodio(episodio); 
        this.episodio_key = episodio.getKey();
        this.dirty = true;
    }

    @Override
    public Episodio getEpisodio() {
        return super.getEpisodio(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFascia(Fascia fascia) {
        super.setFascia(fascia);
        this.fascia_key = fascia.getKey();
        this.dirty = true;
    }

    @Override
    public Fascia getFascia() {
        return super.getFascia(); //To change body of generated methods, choose Tools | Templates.
    }

   
    @Override
    public void setCanale(Canale canale) {
        super.setCanale(canale);
        this.canale_key = canale.getKey();
        this.dirty = true;
    }

    @Override
    public Canale getCanale() {
        return super.getCanale(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setProgramma(Programma programma) {
        super.setProgramma(programma); 
        this.programma_key = programma.getKey();
        this.dirty = true;
    }

    @Override
    public Programma getProgramma() {
        return super.getProgramma(); //To change body of generated methods, choose Tools | Templates.
    }
    
    //METODI SET/GET DEI CAMPI DI TIPO LIST
    
    //METODI DEL PROXY
    
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
       return dirty;
    }
    
    //SETTER PROXY_KEYS

    public void setProgramma_key(int programma_key) {
        this.programma_key = programma_key;
        super.setProgramma(null);
    }

    public void setCanale_key(int canale_key) {
        this.canale_key = canale_key;
        super.setCanale(null);
    }

    public void setFascia_key(int fascia_key) {
        this.fascia_key = fascia_key;
        super.setFascia(null);
    }

    public void setEpisodio_key(int episodio_key) {
        this.episodio_key = episodio_key;
        super.setEpisodio(null);
    }


}
