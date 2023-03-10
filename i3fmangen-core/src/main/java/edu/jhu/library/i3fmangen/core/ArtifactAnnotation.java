package edu.jhu.library.i3fmangen.core;


// Coordinates and lengths are percentages of containing image dimension.

public class ArtifactAnnotation {
	private final String text;
	private final double x, y;
	private final double width, height;

	public ArtifactAnnotation(String text, double x, double y, double width, double height) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public String getText() {
		return text;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
