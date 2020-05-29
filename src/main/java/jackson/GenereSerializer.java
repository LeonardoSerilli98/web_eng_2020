/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackson;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import javax.json.stream.JsonGenerator;
import models.Genere;

/**
 *
 * @author leonardo
 */
public class GenereSerializer extends StdSerializer<Genere> {
    
    public GenereSerializer() {this(null);}

    public GenereSerializer(Class<Genere> t) {super(t);}


    @Override
    public void serialize(Genere item, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException {
        jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("id", item.getKey());
            jsonGenerator.writeObjectField("nome", item.getNome());  
        jsonGenerator.writeEndObject(); 
    }

    
    
}
