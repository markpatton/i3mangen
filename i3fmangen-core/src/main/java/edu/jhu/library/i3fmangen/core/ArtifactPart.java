package edu.jhu.library.i3fmangen.core;

import java.util.List;

/**
 * Page of a document which has an image.
 */
public class ArtifactPart {
	private final String image_path;
	private final int width;
	private final int height;
	private final int index;
	private final String text;
	private final List<ArtifactAnnotation> word_annos;

	public ArtifactPart(String image_path, int index, int width, int height, String text, List<ArtifactAnnotation> word_annos) {
		this.image_path = image_path;
		this.index = index;
		this.width = width;
		this.height = height;
		this.text = text;
		this.word_annos = word_annos;
	}

	public int getIndex() {
		return index;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getImagePath() {
		return image_path;
	}

	public String getText() {
		return text;
	}

	public List<ArtifactAnnotation> getWordAnnotations() {
		return word_annos;
	}
}
