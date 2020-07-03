package jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.Palinsesto;

import java.io.IOException;

public class PalinsestoSerializer extends StdSerializer<Palinsesto> {

    public PalinsestoSerializer() {
        this(null);
    }

    public PalinsestoSerializer(Class<Palinsesto> t) {
        super(t);
    }

    @Override
    public void serialize(Palinsesto item, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", item.getKey());
        jsonGenerator.writeObjectField("inizio", item.getInizio());
        jsonGenerator.writeObjectField("fine", item.getFine());
        jsonGenerator.writeObjectField("data", item.getData());
        
        jsonGenerator.writeObjectFieldStart("fascia");
        jsonGenerator.writeObjectField("id", item.getFascia().getKey());
        jsonGenerator.writeObjectField("orario", item.getFascia().getFascia());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeObjectFieldStart("canale");
        jsonGenerator.writeObjectField("id", item.getCanale().getKey());
        jsonGenerator.writeObjectField("nome", item.getCanale().getNome());
        jsonGenerator.writeEndObject();

        
        jsonGenerator.writeObjectFieldStart("programma");
        jsonGenerator.writeObjectField("id", item.getProgramma().getKey());
        jsonGenerator.writeObjectField("nome", item.getProgramma().getNome());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeObjectFieldStart("episodio");
        jsonGenerator.writeNumberField("id", item.getEpisodio().getKey());
        jsonGenerator.writeNumberField("numero", item.getEpisodio().getNumero());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeObjectFieldStart("stagione");
        jsonGenerator.writeNumberField("id", item.getEpisodio().getStagione().getKey());
        jsonGenerator.writeNumberField("numero", item.getEpisodio().getStagione().getNumero());
        jsonGenerator.writeEndObject();
        
        jsonGenerator.writeEndObject();
    }
}
