package hcmute.kltn.vtv.model.extra;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import hcmute.kltn.vtv.util.exception.BadRequestException;

import java.util.UUID;

public class UUIDDeserializer extends StdDeserializer<UUID> {

    public UUIDDeserializer() {
        this(null);
    }

    public UUIDDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UUID deserialize(JsonParser jp, DeserializationContext ctxt) {
       try {
           JsonNode node = jp.getCodec().readTree(jp);
           String uuidAsString = node.asText();
           return UUID.fromString(uuidAsString);
       }catch (Exception e){
           throw new BadRequestException("UUID không hợp lệ.");
       }
    }
}