/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import models.Immagine;

/**
 *
 * @author leonardo
 */
public interface Immagine_DAO extends DAO_Interface<Immagine>{
    public Immagine checkExistence(String nome) throws DataException;
    
    
    
}
