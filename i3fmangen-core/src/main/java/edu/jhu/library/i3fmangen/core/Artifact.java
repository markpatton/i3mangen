package edu.jhu.library.i3fmangen.core;

/**
 *
 */
public class Artifact {
	private final String id;
	private final String path;

	public Artifact(String id, String label, String path) {
		this.id = id;
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public String getPath() {
		return path;
	}
}
