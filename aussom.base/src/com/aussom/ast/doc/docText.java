package com.aussom.ast.doc;

public class docText {
    private docType type = docType.TEXT;
    private String text = "";

    public docText() { }
    public docText(String Text) { this.text = Text; }

    public docType getType() {
        return type;
    }

    public void setType(docType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
