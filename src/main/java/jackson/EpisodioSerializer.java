package jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.Episodio;

import java.io.IOException;

public class EpisodioSerializer extends StdSerializer<Episodio> {

    public EpisodioSerializer() {this(null);}

    public EpisodioSerializer(Class<Episodio> t) {super(t);}

    @Override
    public void serialize(Episodio item, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("id", item.getKey());
            jsonGenerator.writeObjectField("numero", item.getNumero());
            jsonGenerator.writeObjectField("programma", item.getSerie());
            jsonGenerator.writeObjectField("stagione", item.getStagione());
        jsonGenerator.writeEndObject();
    }
}
