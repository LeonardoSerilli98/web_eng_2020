package jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import models.Episodio;
import models.Episodio_Imp;
import models.Programma;
import models.Stagione;

import java.io.IOException;

public class EpisodioDeserializer extends JsonDeserializer<Episodio> {
    @Override
    public Episodio deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Episodio item = new Episodio_Imp();

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.has("id")) {
            item.setKey(node.get("id").asInt());
        }

        if (node.has("numero")) {
            item.setNumero(node.get("numero").asInt());
        }

        if (node.has("programma")) {
            item.setSerie(jsonParser.getCodec().treeToValue(node.get("programma"), Programma.class));
        }

        if (node.has("stagione")) {
            item.setStagione(jsonParser.getCodec().treeToValue(node.get("stagione"), Stagione.class));
        }

        return item;
    }
}
