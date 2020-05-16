/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Programma_DAO_Imp;
import daos.Stagione_DAO_Imp;
import data.DataException;
import data.DataLayer;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Episodio_Imp;
import models.Programma;
import models.Stagione;
import data.Data_ItemProxy;

/**
 *
 * @author leonardo
 */
public class Episodio_Proxy extends Episodio_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int stagione_key;
    protected int serie_key; //nota bene che Serie=Programma con isSerie settato a 1
        
    public Episodio_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.stagione_key = 0;
        this.serie_key = 0;
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;
    }
    
    @Override
    public void setNumero(int numero) {
        super.setNumero(numero);  
        this.dirty = true;
    }
    
     @Override
    public void setTitolo(String titolo) {
        super.setTitolo(titolo);  
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
                
        if (super.getSerie() == null && serie_key > 0) {            
            try {               
                super.setSerie(((Programma_DAO_Imp) dataLayer.getDAO(Programma.class)).read(serie_key));                
            } catch (DataException ex) {                
                Logger.getLogger(Episodio_Proxy.class.getName()).log(Level.SEVERE, null, ex);                
            }
        }
      
        return super.getSerie();
    }

    @Override
    public void setStagione(Stagione stagione) {
        super.setStagione(stagione); 
        this.stagione_key = stagione.getKey();
        this.dirty = true;
    }

    @Override
    public Stagione getStagione() {       
        
        if (super.getStagione() == null && stagione_key > 0) {           
            try {               
                super.setStagione(((Stagione_DAO_Imp) dataLayer.getDAO(Stagione.class)).read(stagione_key));               
            } catch (DataException ex) {        
                Logger.getLogger(Episodio_Proxy.class.getName()).log(Level.SEVERE, null, ex);  
            }
        }
        return super.getStagione(); 
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
