/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Immagine_DAO_Imp;
import daos.Palinsesto_DAO;
import data.DataException;
import data.DataLayer;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Canale_Imp;
import models.Immagine;
import data.Data_ItemProxy;
import java.util.List;
import models.Palinsesto;
/**
 *
 * @author leonardo
 */
public class Canale_Proxy extends Canale_Imp implements Data_ItemProxy {
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    
    protected int immagine_key;
    
    public Canale_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.immagine_key = 0;
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

  
    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
    @Override
    public void setImmagine(Immagine immagine) {
        super.setImmagine(immagine);
        this.immagine_key = immagine.getKey();
        this.dirty = true;
    }

    @Override
    public Immagine getImmagine() {
        
        if (super.getImmagine() == null && immagine_key > 0) {           
            try {               
                super.setImmagine(((Immagine_DAO_Imp) dataLayer.getDAO(Immagine.class)).read(immagine_key));               
            } catch (DataException ex) {        
                Logger.getLogger(Canale_Proxy.class.getName()).log(Level.SEVERE, null, ex);  
            }
        }
        
        return super.getImmagine();
    }
    

     //METODI SET/GET DEI CAMPI DI TIPO LIST
    
    
    @Override
    public List<Palinsesto> getPalinsesti() {
        if(super.getPalinsesti() == null){
            try{
                super.setPalinsesti(((Palinsesto_DAO) dataLayer.getDAO(Palinsesto.class)).getPalinsestiByCanale(this));
            } catch (DataException ex) {
                Logger.getLogger(Programma_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getPalinsesti(); 
    }

    @Override
    public void setPalinsesti(List<Palinsesto> palinsesti) {
        super.setPalinsesti(palinsesti);
        this.dirty = true; 
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
        
        //qui resettiamo la cache
        super.setImmagine(null);
    }
}
