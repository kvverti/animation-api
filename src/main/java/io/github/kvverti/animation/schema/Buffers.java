package io.github.kvverti.animation.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Handles deserializing buffers from animation JSONs.
 */
public final class Buffers {

    private static final Logger log = LogManager.getLogger();

    /**
     * Intakes the buffers from the given JSON object. The buffer URIs are
     * resolved relative to the given context.
     */
    public static ByteBuffer[] fromJson(ResourceManager manager, Identifier path, JsonArray json) {
        int len = json.size();
        ByteBuffer[] buffers = new ByteBuffer[len];
        int i = 0;
        for(JsonElement elem : json) {
            JsonObject obj = elem.getAsJsonObject();
            int blen = obj.get("byteLength").getAsInt();
            String uri = obj.get("uri").getAsString();
            Identifier id = new Identifier(path.toString() + uri);
            try(Resource rsc = manager.getResource(id)) {
                byte[] arr = new byte[blen];
                int bytesRead = rsc.getInputStream().read(arr);
                if(bytesRead != blen) {
                    log.error("{}: bytelength = {}, but file length = {}", id, blen, bytesRead);
                    throw new SchemaException("bytes read not equal to bytes expected");
                }
                buffers[i] = ByteBuffer.wrap(arr);
            } catch(IOException e) {
                log.error("Could not read buffer {}", id);
                throw new JsonIOException("Unable to read buffer", e);
            }
            i++;
        }
        return buffers;
    }
}
