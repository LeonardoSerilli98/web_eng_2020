/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import models.Programma;
import models.Stagione;

/**
 *
 * @author leonardo
 */
public interface Stagione_DAO  extends DAO_Interface<Stagione>{
    public Stagione getStagioneByProgramma(Programma p, int num) throws DataException;
    
}
