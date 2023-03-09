package edu.jhu.library.i3fmangen.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 *
 * Load an artifact described by a CSV file consiting of IIIF image id (just the
 * path segment), width, and height.
 *
 */
public class FileArtifactLoader implements ArtifactLoader {

    @Override
    public Artifact loadArtifact(Path path) throws IOException {
        String id = path.getFileName().toString().toLowerCase();

        int i = id.lastIndexOf('.');
        if (i != -1) {
            id = id.substring(0, i);
        }

        String name = Character.toUpperCase(id.charAt(0)) + id.substring(1);

        return new Artifact(id, name, path.toString());
    }

    @Override
    public Stream<ArtifactPart> streamPages(Artifact doc) throws IOException {
        AtomicInteger index = new AtomicInteger(0);

        return Files.lines(Paths.get(doc.getPath())).map(l -> get_part(index.getAndIncrement(), l));
    }

    private ArtifactPart get_part(int index, String line) {
        String[] parts = line.split(",");

        if (parts.length != 3) {
            throw new RuntimeException("Cannot parse line: " + line);
        }

        return new ArtifactPart(parts[0], index, Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), line, null);
    }

    @Override
    public String getText(Artifact doc) throws IOException {
        return null;
    }
}
