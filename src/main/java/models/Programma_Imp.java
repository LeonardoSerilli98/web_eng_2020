/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class Programma_Imp extends DataItemImpl<Integer> implements Programma{
    
    private String nome;
    private String descrizione;
    private Genere genere;
    private Boolean isSerie;
    private String approfondimento;
    private Immagine immagine;
    
    public Programma_Imp(){
        
        super();
        nome = "";
        descrizione = "";
        genere = null;
        isSerie = false;
        approfondimento = "";
        immagine = null;
        
    }
    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescrizione(){
        return this.descrizione;
    }
    public void setDescrizione(String descrizione){
        this.descrizione = descrizione;
    }

    public Genere getGenere(){
        return this.genere;
    }
    public void setGenere(Genere genere){
        this.genere = genere;
    }

    public Boolean getIsSerie(){
        return this.isSerie;
    }
    public void setIsSerie(Boolean isSerie){
        this.isSerie = isSerie;
    }

    public String getApprofondimento(){
        return this.approfondimento;
    }
    public void setApprofondimento(String approfondimento){
        this.approfondimento = approfondimento;
    }


    public Immagine getImmagine(){
        return this.immagine;
    }
    public void setImmagine(Immagine immagine){
        this.immagine = immagine;
    }

    @Override
    public List<Episodio> getEpisodi() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEpisodi(List<Episodio> episodi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
