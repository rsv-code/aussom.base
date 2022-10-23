package com.aussom.ast.doc;

/**
 * Annotations within the text document.
 */
public class docAnnotation extends docText {
    /**
     * This is the annotation tag name after the '@' symbol.
     */
    private String tagName = "";

    /**
     * The value part after the tag name.
     */
    private String value = "";

    /**
     * The description is any text after the value.
     */
    private String description = "";

    public docAnnotation() {
        this.setType(docType.ANNOTATION);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
