package texthack.corpus;

/** Represents a single ingested document/citation node in the corpus. */
public class Document {
    public final String id;
    public final String title;
    public final String content;
    public final String language;

    public Document(String id, String title, String content, String language) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.language = language;
    }
}
