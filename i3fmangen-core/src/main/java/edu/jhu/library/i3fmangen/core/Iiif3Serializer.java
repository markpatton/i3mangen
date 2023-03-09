package edu.jhu.library.i3fmangen.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.BiFunction;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class Iiif3Serializer {
    private static final String IIIF_PRESENTATION_CONTEXT = "http://iiif.io/api/presentation/3/context.json";

    private ArtifactUris uris;
    private ArtifactLoader loader;

    public Iiif3Serializer(ArtifactUris uris, ArtifactLoader loader) {
        this.uris = uris;
        this.loader = loader;
    }

    public void serialize(Artifact doc, OutputStream manifest_out) throws IOException {
        serialize(doc, manifest_out, null);
    }

    public void serialize(Artifact doc, OutputStream manifest_out, BiFunction<Artifact, ArtifactPart, OutputStream> anno_func)
            throws IOException {
        try (JsonGenerator g = Json.createGenerator(manifest_out)) {
            write_manifest(doc, anno_func, g);
        }
    }

    private void write_manifest(Artifact doc, BiFunction<Artifact, ArtifactPart, OutputStream> anno_func, JsonGenerator g)
            throws IOException {
        g.writeStartObject();
        g.write("@context", IIIF_PRESENTATION_CONTEXT);

        g.write("id", uris.getIiifManifestUri(doc));
        g.write("type", "Manifest");
        write_label(doc.getId(), g);

        g.writeStartArray("behavior");
        g.write("paged");
        g.writeEnd();

        // TODO PDF included as seeAlso or as rendering?
//        g.writeStartArray("seeAlso");
//        g.writeStartObject();
//        g.write("id", uris.getPdfUri(doc));
//        g.write("type", "Text");
//        g.write("format", "application/pdf");
//        // TODO profile?
//        g.writeEnd();
//        g.writeEnd();

//        g.writeStartArray("service");
//        g.writeStartObject();
//        g.write("@context", "http://iiif.io/api/search/1/context.json");
//        g.write("id", uris.getIiifSearchService(doc));
//        g.write("profile", "http://iiif.io/api/search/1/search");
//        g.writeEnd();
//        g.writeEnd();

        // TODO navDate, add metadata once we have it
        // TODO thumbnail

        g.writeStartArray("items");
        loader.streamPages(doc).forEach(p -> {
            write_canvas(doc, p, g);

            if (p.getWordAnnotations() != null && p.getWordAnnotations().size() > 0) {
                try (JsonGenerator anno_g = Json.createGenerator(anno_func.apply(doc, p))) {
                    write_annotations(doc, p, anno_g);
                }
            }
        });
        g.writeEnd();

        g.writeEnd();
    }

    // TODO Make image service configurable

    // Canvas contains an AnnotationPage contains an Annotation with an Image body
    private void write_canvas(Artifact doc, ArtifactPart page, JsonGenerator g) {
        g.writeStartObject();
        g.write("id", uris.getIiifCanvasUri(doc, page));
        g.write("type", "Canvas");
        write_label("Page: " + (page.getIndex() + 1), g);
        g.write("width", page.getWidth());
        g.write("height", page.getHeight());

        write_thumbnail(doc, page, g);

        g.writeStartArray("items");
        g.writeStartObject();
        g.write("id", uris.getIiifPaintingAnnotationPageUri(doc, page));
        g.write("type", "AnnotationPage");
        g.writeStartArray("items");

        g.writeStartObject();
        g.write("id", uris.getIiifAnnotationImageUri(doc, page));
        g.write("type", "Annotation");
        g.write("motivation", "painting");
        g.write("target", uris.getIiifCanvasUri(doc, page));
        g.writeStartObject("body");
        g.write("id", uris.getJpegImageUri(doc, page));
        g.write("type", "Image");
        g.write("format", "image/jpeg");
        g.write("width", page.getWidth());
        g.write("height", page.getHeight());
        g.writeStartArray("service");
        g.writeStartObject();
        g.write("id", uris.getIiifImageBaseUri(doc, page));
        g.write("type", "ImageService2");
        g.write("profile", "level2");
        g.writeEnd();
        g.writeEnd();
        g.writeEnd();
        g.writeEnd();

        g.writeEnd();
        g.writeEnd();
        g.writeEnd();

        if (page.getWordAnnotations() != null) {
            g.writeStartArray("annotations");
            g.writeStartObject();
            g.write("id", uris.getIiifTextAnnotationPageUri(doc, page));
            g.write("type", "AnnotationPage");
            g.writeEnd();
            g.writeEnd();
        }

        g.writeEnd();
    }

    private void write_thumbnail(Artifact doc, ArtifactPart p, JsonGenerator g) {
        g.writeStartArray("thumbnail");
        g.writeStartObject();
        g.write("id", uris.getJpegThumbUri(doc, p));
        g.write("type", "Image");
        g.write("format", "image/jpeg");
        g.writeStartArray("service");
        g.writeStartObject();
        g.write("id", uris.getIiifImageBaseUri(doc, p));
        g.write("type", "ImageService2");
        g.write("profile", "level2");
        g.writeEnd();
        g.writeEnd();

        g.writeEnd();
        g.writeEnd();
    }

    private void write_annotations(Artifact doc, ArtifactPart p, JsonGenerator g) {
        g.writeStartObject();
        g.write("@context", IIIF_PRESENTATION_CONTEXT);

        g.write("id", uris.getIiifTextAnnotationPageUri(doc, p));
        g.write("type", "AnnotationPage");

        g.writeStartArray("items");
        for (ArtifactAnnotation a : p.getWordAnnotations()) {
            g.writeStartObject();

            g.write("id", uris.getIiifAnnotationTextUri(doc, p, a));
            g.write("type", "Annotation");
            g.write("motivation", "supplementing");

            g.writeStartObject("body");
            g.write("type", "TextualBody");
            g.write("format", "text/plain");
            g.write("language", "en");
            g.write("value", a.getText());
            g.writeEnd();

            g.write("target", uris.getIiifCanvasTarget(doc, p, a));

            g.writeEnd();
        }
        g.writeEnd();

        g.writeEnd();
    }

    private void write_label(String label, JsonGenerator g) {
        g.writeStartObject("label");
        g.writeStartArray("en");
        g.write(label);
        g.writeEnd();
        g.writeEnd();
    }
}
