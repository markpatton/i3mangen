package edu.jhu.library.i3fmangen.cli;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.jhu.library.i3fmangen.core.ArtifactUris;
import edu.jhu.library.i3fmangen.core.FileArtifactLoader;
import edu.jhu.library.i3fmangen.core.Iiif3Serializer;

/**
 *
 */
public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: write-iiif FILE");
			System.exit(1);
		}

		String base_data_url = System.getProperty("iiifmangen.base_data_url", "http://localhost:8080/data/");
		String base_image_url = System.getProperty("iiifmangen.base_image_url", "https://image.library.jhu.edu/iiif/");

		ArtifactUris uris = new ArtifactUris(base_data_url, base_image_url);

		switch (args[0]) {
		case "write-iiif": {
			Path file = Paths.get(args[1]);

			FileArtifactLoader loader = new FileArtifactLoader();
			new Iiif3Serializer(uris, loader).serialize(loader.loadArtifact(file), System.out);

			break;
		}

		default:
			System.err.printf("Unknown command: '%s'. Expected 'write-iiif'", args[0]);
			break;
		}

	}
}
