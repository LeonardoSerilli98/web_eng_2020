package jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.Palinsesto;

import java.io.IOException;

public class PalinsestoSerializer extends StdSerializer<Palinsesto> {

    public PalinsestoSerializer() { this(null);}

    public PalinsestoSerializer(Class<Palinsesto> t) {super(t);}

    @Override
    public void serialize(Palinsesto item, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("id", item.getKey());
            jsonGenerator.writeObjectField("inizio", item.getInizio());
            jsonGenerator.writeObjectField("fine", item.getFine());
            jsonGenerator.writeObjectField("data", item.getData());
            jsonGenerator.writeObjectField("programma", item.getProgramma());
            jsonGenerator.writeObjectField("episodio", item.getEpisodio());
            jsonGenerator.writeObjectField("canale", item.getCanale());
        jsonGenerator.writeEndObject();
    }
}
