/*
 * Questa classe implementa una semplice cache per gli oggetti-entità
 * Impedendone il ricaricamento e la duplicazione in memoria
 * 
 * This class implements a simple entity object cache
 * to avoid reloading or duplicanting them in memory
 */
package data;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Didattica
 */
public class DataCache {
    
    public Map<Class, Map<Object, Object>> cache;
    
    public DataCache() {
        this.cache = new HashMap<>();
    }
      
    public <C> void add(Class<C> c, C o, Object key) {
        //Logger.getLogger("DataCache").log(Level.INFO, "Cache add: object of class {0} with key {1}", new Object[]{c.getName(), key});
        if (!cache.containsKey(c)) {
            cache.put(c, new HashMap<>());
        }
        cache.get(c).put(key, o);
    }
    
    public void delete(Class c, Object key) {
        if (has(c, key)) {
            cache.get(c).remove(key);
        }
    }
    
    public <C> C get(Class<C> c, Object key) {
        if (has(c, key)) {
            //Logger.getLogger("DataCache").log(Level.INFO, "Cache hit: object of class {0} with key {1}", new Object[]{c.getName(), key});
            return (C) cache.get(c).get(key);
        } else {
            return null;
        }
    }
    
    public boolean has(Class c, Object key) {
        //Logger.getLogger("DataCache").log(Level.INFO, "Cache lookup: object of class {0} with key {1}", new Object[]{c.getName(), key});
        return cache.containsKey(c) && cache.get(c).containsKey(key);
    }
}
