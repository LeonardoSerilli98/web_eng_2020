/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Canale_DAO_Imp;
import daos.Fascia_DAO_Imp;
import daos.Genere_DAO_Imp;
import daos.Programma_DAO_Imp;
import data.DataException;
import data.DataLayer;
import models.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import data.Data_ItemProxy;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author leonardo
 */
public class Ricerca_Proxy extends Ricerca_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int canale_key;
    protected int programma_key;
    protected int genere_key;
    protected int fascia_key;
            
    public Ricerca_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.canale_key = 0;
        this.programma_key = 0;
        this.genere_key = 0;
        this.fascia_key = 0;
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)    
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;
    }
    
    @Override
    public void setData(Date data) {
        super.setData(data); 
        this.dirty = true;
    }
    
    @Override
    public void setInizioMin(Time inizioMin) {
        super.setInizioMin(inizioMin); 
        this.dirty = true;
    }
    
    @Override
    public void setInizioMax(Time inizioMax) {
        super.setInizioMax(inizioMax); 
        this.dirty = true;
    }
        
    @Override
    public void setTitolo(String titolo){
        super.setTitolo(titolo);
        this.dirty = true;
    }

    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
    @Override
    public void setFascia(Fascia fascia) {
        super.setFascia(fascia); 
        this.fascia_key = fascia.getKey();
        this.dirty = true;
    }
    
    @Override
    public Fascia getFascia() {
        if(super.getFascia() == null && fascia_key > 0){
            try{
                super.setFascia(((Fascia_DAO_Imp) dataLayer.getDAO(Fascia.class)).read(fascia_key));
            } catch (DataException ex) {
                Logger.getLogger(Ricerca_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getFascia();
    }
    
    @Override
    public void setGenere(Genere genere) {
        super.setGenere(genere); 
        this.genere_key = genere.getKey();
        this.dirty = true;
    }

    @Override
    public Genere getGenere() {
        if(super.getGenere() == null && genere_key > 0){
            try {
                super.setGenere(((Genere_DAO_Imp) dataLayer.getDAO(Genere.class)).read(genere_key));
            } catch (DataException ex) {
                Logger.getLogger(Ricerca_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getGenere();
    }

    @Override
    public void setProgramma(Programma programma) {
        super.setProgramma(programma); 
        this.programma_key = programma.getKey();
        this.dirty = true;
    }

    @Override
    public Programma getProgramma() {
        if(super.getProgramma() == null && programma_key > 0){
            try{
                super.setProgramma(((Programma_DAO_Imp) dataLayer.getDAO(Programma.class)).read(programma_key));
            } catch(DataException ex){
                Logger.getLogger(Ricerca_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getProgramma();
    }

    @Override
    public void setCanale(Canale canale) {
        super.setCanale(canale); 
        this.canale_key = canale.getKey();
        this.dirty = true;
    }

    @Override
    public Canale getCanale() {
        if(super.getCanale() == null && canale_key > 0) {
            try{
                super.setCanale(((Canale_DAO_Imp) dataLayer.getDAO(Canale.class)).read(canale_key));
            } catch (DataException ex){
                Logger.getLogger(Ricerca_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCanale();
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

    public void setCanale_key(int canale_key) {
        this.canale_key = canale_key;
        super.setCanale(null);
    }

    public void setProgramma_key(int programma_key) {
        this.programma_key = programma_key;
        super.setProgramma(null);
    }

    public void setGenere_key(int genere_key) {
        this.genere_key = genere_key;
        super.setGenere(null);
    }

    public void setFascia_key(int fascia_key) {
        this.fascia_key = fascia_key;
        super.setFascia(null);
    }
  
}
