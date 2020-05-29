/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import models.Genere;
import models.Immagine;
import models.Programma;
import models.Programma_Imp;


/**
 *
 * @author leonardo
 */
public class ProgrammaDeserializer extends JsonDeserializer<Programma> {

    @Override
    public Programma deserialize(JsonParser jsonParser, DeserializationContext dc) throws IOException, JsonProcessingException {
        Programma item = new Programma_Imp();

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.has("id")) {
            item.setKey(node.get("idProgramma").asInt());
        }
        
        if (node.has("nome")) {
            item.setNome(node.get("nome").asText());
        }
        
        if (node.has("descrizione")) {
            item.setDescrizione(node.get("descrizione").asText());
        }
        if (node.has("isSerie")) {
            item.setIsSerie(node.get("isSerie").asBoolean());
        }
        if (node.has("genere")) {
            item.setGenere(jsonParser.getCodec().treeToValue(node.get("genere"), Genere.class));
        }
        if (node.has("approfondimento")) {
            item.setApprofondimento(node.get("approfondimento").asText());
        }
        if (node.has("immagine")) {
            item.setImmagine(jsonParser.getCodec().treeToValue(node.get("immagine"), Immagine.class));
        }

        return item;
    }


}
