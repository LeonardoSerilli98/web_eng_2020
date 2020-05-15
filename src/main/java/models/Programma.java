/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItem;
import java.util.List;

/**
 *
 * @author leonardo
 */
public interface Programma extends DataItem<Integer> {

    String getNome();
    void setNome(String nome);

    String getDescrizione();
    void setDescrizione(String descrizione);

    Genere getGenere();
    void setGenere(Genere genere);

    Boolean getIsSerie();
    void setIsSerie(Boolean isSerie);
    
    List<Episodio> getEpisodi();
    void setEpisodi(List<Episodio> episodi);

    String getApprofondimento();
    void setApprofondimento(String approfondimento);

    Immagine getImmagine();
    void setImmagine(Immagine immagine);
    
}
