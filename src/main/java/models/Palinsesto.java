/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItem;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author leonardo
 */
public interface Palinsesto extends DataItem<Integer>{

    public Programma getProgramma();
    public void setProgramma(Programma programma);

    public Canale getCanale();
    public void setCanale(Canale canale);

    public Time getInizio();
    public void setInizio(Time inizio);

    public Time getFine();
    public void setFine(Time fine);

    public Fascia getFascia();
    public void setFascia(Fascia fascia);

    public Date getData();
    public void setData(Date data);

    public Episodio getEpisodio();
    public void setEpisodio(Episodio episodio);
    
    
    
}
