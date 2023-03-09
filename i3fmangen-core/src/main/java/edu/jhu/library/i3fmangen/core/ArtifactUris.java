package edu.jhu.library.i3fmangen.core;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ArtifactUris {
	private final String base_data_url;
	private final String base_image_url;

	/**
	 * @param base_data_url  Base URL to access static data. Should probably abe absolute.
	 * @param base_image_url Base URL to IIIF image server. Must be absolute.
	 */
	public ArtifactUris(String base_data_url, String base_image_url) {
		this.base_data_url = base_data_url.endsWith("/") ? base_data_url : base_data_url + "/";
		this.base_image_url = base_image_url.endsWith("/") ? base_image_url : base_image_url + "/";
	}

	private String get_base_url(Artifact doc) {
		return base_data_url + doc.getId() + "/";
	}

	public String getIiifManifestUri(Artifact doc) {
		String s = URLEncoder.encode(getIiifManifestName(doc), StandardCharsets.UTF_8);
		return get_base_url(doc) + s;
	}

	public String getIiifManifestName(Artifact doc) {
		return "manifest.json";
	}

	public String getIiifCanvasUri(Artifact doc, ArtifactPart page) {
		return get_base_url(doc) + "canvas/" + page.getIndex();
	}

	public String getIiifPaintingAnnotationPageUri(Artifact doc, ArtifactPart page) {
		return get_base_url(doc) + "painting/" + page.getIndex();
	}

	public String getIiifTextAnnotationPageUri(Artifact doc, ArtifactPart page) {
		String s = URLEncoder.encode(getIiifTextAnnotationPageName(doc, page), StandardCharsets.UTF_8);
		return get_base_url(doc) + s;
	}

	public String getIiifTextAnnotationPageName(Artifact doc, ArtifactPart page) {
		return doc.getId() + "-page-" + page.getIndex() + ".json";
	}

	public String getIiifAnnotationImageUri(Artifact doc, ArtifactPart page) {
		return get_base_url(doc) + "image/" + page.getIndex();
	}

	public String getIiifImageBaseUri(Artifact doc, ArtifactPart page) {
		String s = URLEncoder.encode(page.getImagePath(), StandardCharsets.UTF_8);
		return base_image_url + s;
	}

	public String getJpegImageUri(Artifact doc, ArtifactPart page) {
		return getIiifImageBaseUri(doc, page) + "/full/max/0/default.jpg";
	}

	public String getPdfUri(Artifact doc) {
		String s = URLEncoder.encode(doc.getId() + ".pdf", StandardCharsets.UTF_8);
		return get_base_url(doc) + s;
	}

	// TODO Needs more thought
	public String getIiifAnnotationTextUri(Artifact doc, ArtifactPart page, ArtifactAnnotation a) {
		return get_base_url(doc) + "anno/" + page.getIndex() + "/" + a.hashCode();
	}

	public String getIiifCanvasTarget(Artifact doc, ArtifactPart p, ArtifactAnnotation a) {
		int x = (int) Math.round(a.getX() * p.getWidth());
		int y = (int) Math.round(a.getY() * p.getHeight());
		int width = (int) Math.round(a.getWidth() * p.getWidth());
		int height = (int) Math.round(a.getHeight() * p.getHeight());

		return getIiifCanvasUri(doc, p) + "#xywh=" + x + "," + y + "," + width + "," + height;
	}

	// TODO Probably have a separate base url for search
	public String getIiifSearchService(Artifact doc) {
		return get_base_url(doc) + "search/";
	}

	public String getJpegThumbUri(Artifact doc, ArtifactPart page) {
		return getIiifImageBaseUri(doc, page) + "/full/128,/0/default.jpg";
	}
}
