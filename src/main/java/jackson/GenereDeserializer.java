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
import models.Genere_Imp;

/**
 *
 * @author leonardo
 */
public class GenereDeserializer extends JsonDeserializer<Genere> {

    @Override
    public Genere deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        
        Genere item = new Genere_Imp();
        
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("id")) {
            item.setKey(node.get("id").asInt());
        }

        if (node.has("nome")) {
            item.setNome(node.get("nome").asText());
        }

        return item;
    }
    
}