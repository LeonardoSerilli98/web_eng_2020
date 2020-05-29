package jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class PalinsestoDeserializer extends JsonDeserializer<Palinsesto> {

    @Override
    public Palinsesto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Palinsesto item = new Palinsesto_Imp();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.has("id")) {
            item.setKey(node.get("idPalinsesto").asInt());
        }

        if (node.has("inizio")) {
            item.setInizio(Time.valueOf(node.get("inizio").asText()));
        }

        if (node.has("fine")) {
            item.setInizio(Time.valueOf(node.get("fine").asText()));
        }

        if (node.has("data")) {
            try {
                item.setData(sdf.parse(node.get("data").asText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (node.has("programma")) {
            item.setProgramma(jsonParser.getCodec().treeToValue(node.get("programma"), Programma.class));
        }

        if (node.has("episodio")) {
            item.setFascia(jsonParser.getCodec().treeToValue(node.get("fascia"), Fascia.class));
        }

        if (node.has("canale")) {
            item.setCanale(jsonParser.getCodec().treeToValue(node.get("canale"), Canale.class));
        }

        return item;
    }
}
