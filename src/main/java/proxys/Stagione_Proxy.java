/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Episodio_DAO;
import daos.Immagine_DAO_Imp;
import daos.Programma_DAO_Imp;
import data.DataException;
import data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Episodio;
import models.Immagine;
import models.Stagione_Imp;
import data.Data_ItemProxy;
import models.Programma;

/**
 *
 * @author leonardo
 */
public class Stagione_Proxy extends Stagione_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int immagine_key;
    protected int programma_key;
   
    public Stagione_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.immagine_key = 0;
        this.programma_key=0;
        
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
    
      @Override
    public void setNumero(int numero) {
        super.setNumero(numero); 
        this.dirty = true;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;

    }

    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
    @Override
    public void setImmagine(Immagine immagine) {
        super.setImmagine(immagine);
        this.immagine_key = immagine.getKey();
        this.dirty = true;
    }

    @Override
    public Immagine getImmagine() {
        if(super.getImmagine() == null && immagine_key > 0){
            try{
                super.setImmagine(((Immagine_DAO_Imp) dataLayer.getDAO(Immagine.class)).read(immagine_key));
            } catch (DataException ex) {
                Logger.getLogger(Stagione_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getImmagine();
    }
    
       @Override
    public void setProgramma(Programma programma) {
        super.setProgramma(programma); 
        this.programma_key = programma.getKey();
        this.dirty = true;
    }

    @Override
    public Programma getProgramma() {       
        
        if (super.getProgramma() == null && programma_key > 0) {           
            try {               
                super.setProgramma(((Programma_DAO_Imp) dataLayer.getDAO(Programma.class)).read(programma_key));               
            } catch (DataException ex) {        
                Logger.getLogger(Stagione_Proxy.class.getName()).log(Level.SEVERE, null, ex);  
            }
        }
        return super.getProgramma(); 
    }
    //METODI SET/GET DEI CAMPI DI TIPO LIST

    @Override
    public void setEpisodi(List<Episodio> episodi) {
        super.setEpisodi(episodi); 
        this.dirty = true;
    }

    @Override
    public List<Episodio> getEpisodi() {
         if(super.getEpisodi() == null){
            try{
                super.setEpisodi(((Episodio_DAO) dataLayer.getDAO(Episodio.class)).getEpisodiByStagione(this));
            } catch (DataException ex) {
                Logger.getLogger(Stagione_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getEpisodi(); 
    }
        
    //METODI DEL PROXY
    
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }
        
    //SETTER PROXY_KEYS

    public void setImmagine_key(int immagine_key) {
        this.immagine_key = immagine_key;
        super.setImmagine(null);
    }
        
        
}