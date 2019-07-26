package io.github.kvverti.animation.schema;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBuffers {

    private static final String WD = "/io/github/kvverti/animation/schema/";
    private static Gson gson;
    private static ResourceManager manager;
    private static Identifier wd;

    @BeforeAll
    public static void init() {
        gson = new Gson();
        manager = new MockResourceManager();
        wd = new Identifier(WD);
    }

    private byte[][] storage(String resource) throws IOException {
        JsonArray json;
        try(Resource rsc = manager.getResource(new Identifier(WD + resource))) {
            Reader rd = new InputStreamReader(rsc.getInputStream());
            json = gson.fromJson(rd, JsonArray.class);
        }
        return Buffers.fromJson(manager, wd, json);
    }

    @Test
    @DisplayName("Single buffer")
    public void testSingleBuffer() throws IOException {
        byte[][] storage = storage("buffer/correct.json");
        byte[] expected = "hello_world_lol".getBytes();
        byte[] actual = storage[0];
        assertArrayEquals(expected, actual, "hello.bin");
    }

    @Test
    @DisplayName("Multiple buffer")
    public void testMultiBuffer() throws IOException {
        byte[][] storage = storage("buffer/multi.json");
        byte[] expected = "hello_world_lol".getBytes();
        byte[] actual = storage[0];
        assertArrayEquals(expected, actual, "hello.bin");
        expected = "abcd efgh ijkl ``~~".getBytes();
        actual = storage[1];
        assertArrayEquals(expected, actual, "abcd.bin");
    }

    @Test
    @DisplayName("Nonexistent buffer")
    public void testNonexistentBuffer() {
        assertThrows(JsonIOException.class, () -> storage("buffer/nonexistent.json"));
    }

    @Test
    @DisplayName("Bad specified length")
    public void testBadLengthBuffer() {
        assertThrows(SchemaException.class, () -> storage("buffer/bad_length.json"));
    }
}
