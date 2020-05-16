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
import models.Immagine_Imp;
import data.Data_ItemProxy;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Programma;
import models.Stagione;

/**
 *
 * @author leonardo
 */
public class Immagine_Proxy extends Immagine_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int stagione_key;
    protected int programma_key;
        
    public Immagine_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.stagione_key=0;
        this.programma_key=0;
        
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)

    @Override
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;
    }
        
    @Override
    public void setNome(String nome) {
        super.setNome(nome); 
        this.dirty = true;
    }
    
    @Override
    public void setTipo(String tipo) {
        super.setTipo(tipo); 
        this.dirty = true;
    }
    
    @Override
    public void setTaglia(long taglia) {
        super.setTaglia(taglia); 
        this.dirty = true;
    }


    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
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
                Logger.getLogger(Immagine_Proxy.class.getName()).log(Level.SEVERE, null, ex);  
            }
        }
        return super.getStagione(); 
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
                Logger.getLogger(Immagine_Proxy.class.getName()).log(Level.SEVERE, null, ex);  
            }
        }
        return super.getProgramma(); 
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
   
}
