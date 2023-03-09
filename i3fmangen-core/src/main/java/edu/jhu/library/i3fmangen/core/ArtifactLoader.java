package edu.jhu.library.i3fmangen.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ArtifactLoader {
	Artifact loadArtifact(Path path) throws IOException;

	Stream<ArtifactPart> streamPages(Artifact doc) throws IOException;

	String getText(Artifact doc) throws IOException;
}
