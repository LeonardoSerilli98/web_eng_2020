/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import java.util.HashMap;
import java.util.List;
import models.Canale;
import models.Fascia;
import models.Palinsesto;
import models.Programma;
import models.Ricerca;

/**
 *
 * @author leonardo
 */
public interface Palinsesto_DAO extends DAO_Interface<Palinsesto>{
    public List<Palinsesto> getPalinsestiByCanale(Canale canale) throws DataException ;
    public List<Palinsesto> getPalinsestiByCanale(Canale canale, String data) throws DataException ;
    public List<Palinsesto> getPalinsestiByCanale(Canale canale, Fascia fascia) throws DataException ;
    public List<Palinsesto> getPalinsestiByProgramma(Programma programma) throws DataException ;
    public List<Palinsesto> ricerca(Ricerca r) throws DataException ;
    public Palinsesto checkExistence(Palinsesto p) throws DataException;
    public List<Palinsesto> ricerca(Ricerca r, int pagina) throws DataException ;
}
