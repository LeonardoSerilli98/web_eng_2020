/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import models.Canale;


/**
 *
 * @author leonardo
 */
public class CanaleSerializer extends StdSerializer<Canale>{
    
    public CanaleSerializer() {
        this(null);
    }

    public CanaleSerializer(Class<Canale> t) {
        super(t);
    }

    @Override
    public void serialize(Canale item, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
                jg.writeStartObject();   
                jg.writeObjectField("id", item.getKey());           
                jg.writeObjectField("nome", item.getNome());             
                jg.writeObjectField("immagine", item.getImmagine());
                jg.writeEndObject();
    }
    
}

