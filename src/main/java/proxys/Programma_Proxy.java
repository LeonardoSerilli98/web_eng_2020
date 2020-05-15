/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Episodio_DAO;
import daos.Genere_DAO_Imp;
import daos.Immagine_DAO_Imp;
import data.DataException;
import data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Episodio;
import models.Genere;
import models.Immagine;
import models.Programma_Imp;

import javax.xml.crypto.Data;
import data.Data_ItemProxy;

/**
 *
 * @author leonardo
 */
public class Programma_Proxy extends Programma_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int genere_key;
    protected int immagine_key;
        
    public Programma_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.genere_key = 0;
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
    
    @Override
    public void setIsSerie(Boolean isSerie) {
        super.setIsSerie(isSerie); 
        this.dirty = true;
    }
    
    
    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione); 
        this.dirty = true;
    }
    
    @Override
    public void setApprofondimento(String approfondimento) {
        super.setApprofondimento(approfondimento); 
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
            }catch (DataException ex){
                Logger.getLogger(Programma_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getImmagine();
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
            try{
                super.setGenere(((Genere_DAO_Imp) dataLayer.getDAO(Genere.class)).read(genere_key));
            }catch (DataException ex){
                Logger.getLogger(Programma_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getGenere(); 
    }

    //METODI SET/GET DEI CAMPI DI TIPO LIST
    
    @Override
    public List<Episodio> getEpisodi() {
        if(super.getEpisodi() == null){
            try{
                super.setEpisodi(((Episodio_DAO) dataLayer.getDAO(Episodio.class)).getEpisodiByProgramma(this));
            } catch (DataException ex) {
                Logger.getLogger(Programma_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getEpisodi(); 
    }

    @Override
    public void setEpisodi(List<Episodio> episodi) {
        super.setEpisodi(episodi);
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

    public void setGenere_key(int genere_key) {
        this.genere_key = genere_key;
        super.setGenere(null);
    }

    public void setImmagine_key(int immagine_key) {
        this.immagine_key = immagine_key;
        super.setImmagine(null);
    }
    
   
}
