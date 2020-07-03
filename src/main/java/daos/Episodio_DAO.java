/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import java.util.List;
import models.Episodio;
import models.Programma;
import models.Stagione;

/**
 *
 * @author leonardo
 */
public interface Episodio_DAO extends DAO_Interface<Episodio>{
    
    List<Episodio> getEpisodiByProgramma(Programma programma) throws DataException;
    List<Episodio> getEpisodiByStagione(Stagione stagione) throws DataException;
    public Episodio checkExistence(Episodio e) throws DataException;
    public Episodio checkCorrectness(int progID, int StagioneNum, int episodioNum) throws DataException;

    
}
