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
import models.Canale;
import models.Immagine;
import models.Immagine_Imp;

/**
 *
 * @author leonardo
 */
public class ImmagineDeserializer extends JsonDeserializer<Immagine> {

    @Override
    public Immagine deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        
        Immagine item = new Immagine_Imp();
        
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("id")) {
            item.setKey(node.get("id").asInt());
        }

        if (node.has("nome")) {
            item.setNome(node.get("nome").asText());
        }
        
        if (node.has("tipo")) {
            item.setTipo(node.get("tipo").asText());
        }
        if (node.has("taglia")) {
            item.setTaglia(node.get("taglia").asLong());
        }

        return item;
    }
    
}