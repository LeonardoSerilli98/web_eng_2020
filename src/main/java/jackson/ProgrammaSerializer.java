/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import models.Programma;

/**
 *
 * @author leonardo
 */
public class ProgrammaSerializer extends StdSerializer<Programma> {
    
    public ProgrammaSerializer() {this(null);}

    public ProgrammaSerializer(Class<Programma> t) {super(t);}

    @Override
    public void serialize(Programma item, JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("id", item.getKey());
        jsonGenerator.writeObjectField("nome", item.getNome());
        jsonGenerator.writeObjectField("descrizione", item.getDescrizione());
        jsonGenerator.writeObjectField("isSerie", item.getIsSerie());
        jsonGenerator.writeObjectField("genere", item.getGenere());
        jsonGenerator.writeObjectField("approfondimento", item.getApprofondimento());
        jsonGenerator.writeObjectField("immagine", item.getImmagine());
        jsonGenerator.writeEndObject();    
    }
    
}
