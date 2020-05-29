/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javax.ws.rs.ext.ContextResolver;
import models.Canale;
import models.Episodio;
import models.Genere;
import models.Immagine;
import models.Palinsesto;
import models.Programma;

/**
 *
 * @author leonardo
 */
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper>{
    
    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //abilitiamo una feature nuova...
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule customSerializer = new SimpleModule("CustomSerializersModule");

        //configuriamo i nostri serializzatori custom
        
        customSerializer.addSerializer(Canale.class, new CanaleSerializer());
        customSerializer.addDeserializer(Canale.class, new CanaleDeserializer());
       
        
        customSerializer.addSerializer(Immagine.class, new ImmagineSerializer());
        customSerializer.addDeserializer(Immagine.class, new ImmagineDeserializer());
		
	customSerializer.addSerializer(Palinsesto.class, new PalinsestoSerializer());
        customSerializer.addDeserializer(Palinsesto.class, new PalinsestoDeserializer());
		
	customSerializer.addSerializer(Episodio.class, new EpisodioSerializer());
        customSerializer.addDeserializer(Episodio.class, new EpisodioDeserializer());
		
	customSerializer.addSerializer(Programma.class, new ProgrammaSerializer());
        customSerializer.addDeserializer(Programma.class, new ProgrammaDeserializer());
		
	customSerializer.addSerializer(Genere.class, new GenereSerializer());
        customSerializer.addDeserializer(Genere.class, new GenereDeserializer());

        mapper.registerModule(customSerializer);

        return mapper;
    }
}
