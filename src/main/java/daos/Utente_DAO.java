/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import java.util.List;
import models.Utente;

/**
 *
 * @author leonardo
 */
public interface Utente_DAO extends DAO_Interface<Utente>{
    public Utente checkUtente(String username, String password) throws DataException;
    public Utente getUtenteByUsername(String username) throws DataException;
    public Utente getUtenteByUUID(String UUID) throws DataException;
    public boolean isAdmin(String username) throws DataException;
    public Utente getUtenteByToken(String token) throws DataException;
    public List<Utente> getUtentiMailAbilitate() throws DataException;
    
}
