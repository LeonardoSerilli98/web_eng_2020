/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import authResources.LoggedFilter;
import authResources.AuthRes;
import authResources.Logged;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import jackson.ObjectMapperContextResolver;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import resources.CanaleRes;
import resources.CanaliRes;
import resources.EpisodiRes;
import resources.PalinsestiRes;
import resources.PalinsestoRes;
import resources.ProgrammaRes;
import resources.ProgrammiRes;

/**
 *
 * @author leonardo
 */

//qui esponiamo il path iniziale di tutte le risorse che il servizio offrirà
@ApplicationPath("api")
public class RESTApp extends Application{

    
    private final Set<Class<?>> classes;

    public RESTApp() {
        HashSet<Class<?>> c = new HashSet<>();
        
        //TODO-> agiungiamo a c le classi delle risorse della nostra applicazione
    
        c.add(AuthRes.class);  
        c.add(LoggedFilter.class);
        
        c.add(CanaliRes.class);
        c.add(CanaleRes.class);
        c.add(PalinsestoRes.class);
        
        c.add(PalinsestiRes.class);
        
        c.add(ProgrammiRes.class);
        c.add(ProgrammaRes.class); 
        c.add(EpisodiRes.class);
        
        //JAXRS sa solo quello che gli diamo in merito alle classi, quindi gli passiamo anche il Providere di Jackson
        //che contiene i metodi per la de/serializzazione delle risorse
        c.add(JacksonJsonProvider.class);
        //l'objectMapperContextResolver è necessario per la de/serializzazione dei nostri modelli in json (è una feature di jackson)
        c.add(ObjectMapperContextResolver.class);
 
        classes = Collections.unmodifiableSet(c);
    }
    
 
   //Dobbiamo dire a JAXRS tutte le classi che la nostra applicazione comprenderà
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
    
    
}
