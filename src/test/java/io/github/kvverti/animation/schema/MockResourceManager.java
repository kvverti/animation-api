package io.github.kvverti.animation.schema;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;

/**
 * A ClassLoader-based resource manager, for testing purposes.
 */
final class MockResourceManager implements ResourceManager {

    @Override
    public Set<String> getAllNamespaces() {
        return Collections.emptySet();
    }

    @Override
    public Resource getResource(Identifier id) throws IOException {
        InputStream in = MockResourceManager.class.getResourceAsStream(id.getPath());
        if(in == null) {
            throw new FileNotFoundException(id.getPath());
        }
        return new ResourceImpl("", id, in, null);
    }

    @Override
    public boolean containsResource(Identifier id) {
        return true;
    }

    @Override
    public List<Resource> getAllResources(Identifier id) throws IOException {
        return Collections.singletonList(getResource(id));
    }

    @Override
    public Collection<Identifier> findResources(String path, Predicate<String> pred) {
        return Collections.emptyList();
    }

    @Override
    public void addPack(ResourcePack pack) {

    }
}
