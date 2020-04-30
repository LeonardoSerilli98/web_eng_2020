/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author leonardo
 */
public interface Programma {
    
    public int getKey();
    public void setKey(int key);

    public String getNome();
    public void setNome(String nome);

    public String getDescrizione();
    public void setDescrizione(String descrizione);

    public Genere getGenere();
    public void setgenere(Genere genere);

    public Boolean getIsSerie();
    public void setIsSerie(Boolean isSerie);

    public String getApprofondimento();
    public void setApprofondimento(String approfondimento);

    public Immagine getImmagine();
    public void setImmagine(Immagine immagine);
    
}
