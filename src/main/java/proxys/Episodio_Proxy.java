/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import data.DataLayer;
import models.Episodio_Imp;
import models.Programma;
import models.Stagione;

/**
 *
 * @author leonardo
 */
public class Episodio_Proxy extends Episodio_Imp{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int stagione_key;
    protected int serie_key;
        
    public Episodio_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.stagione_key = 0;
        this.serie_key = 0;
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
    
    @Override
    public void setKey(int key) {
        super.setKey(key); 
        this.dirty = true;
    }
    
    @Override
    public void setNumero(int numero) {
        super.setNumero(numero);  
        this.dirty = true;
    }


    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
    @Override
    public void setSerie(Programma serie) {
        super.setSerie(serie); 
        this.serie_key = serie.getKey();
        this.dirty = true;
    }

    @Override
    public Programma getSerie() {
        return super.getSerie(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStagione(Stagione stagione) {
        super.setStagione(stagione); 
        this.stagione_key = stagione.getKey();
        this.dirty = true;
    }

    @Override
    public Stagione getStagione() {
        return super.getStagione(); //To change body of generated methods, choose Tools | Templates.
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

    public void setStagione_key(int stagione_key) {
        this.stagione_key = stagione_key;
        super.setStagione(null);
    }

    public void setSerie_key(int serie_key) {
        this.serie_key = serie_key;
        super.setSerie(null);
    }


        
   
}
