package com.aussom.ast;

import com.aussom.Environment;
import com.aussom.ast.doc.docAnnotation;
import com.aussom.ast.doc.docText;
import com.aussom.ast.doc.docType;
import com.aussom.types.AussomList;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

import java.util.ArrayList;
import java.util.List;

public class astAussomDoc extends astNode implements astNodeInt {
    // The full doc comment text.
    private String aussomDocText = "";

    // The parsed doc comment broken into a list of text or annotation nodes.
    private List<docText> docList = new ArrayList<docText>();


    public astAussomDoc() {
        this.setType(astNodeType.AUSSOM_DOC);
    }

    public astAussomDoc(String Text) {
        this();
        this.setAussomDocText(Text);
    }

    private void setAussomDocText(String Text) {
        // Set the text
        this.aussomDocText = Text;

        // Strip the formatting
        this.stripFormatting();

        // Parse text
        this.parseText();
    }

    private void stripFormatting() {
        String ret = "";
        String lines[] = this.aussomDocText.split("\n");
        for (String line : lines) {
            String tline = line.trim();
            if (tline.startsWith("*"))
                tline = tline.replaceFirst("^[*]+(.*)", "$1").trim();
            if (!tline.equals(""))
                ret += tline + "\n";
        }
        this.aussomDocText = ret.trim();
    }

    private void parseText() {
        List<docText> lst = new ArrayList<docText>();

        String lines[] = this.parseProcessLines();
        for(String line : lines) {
            if (line.matches("@[A-Za-z0-9_]+.*")) {
                // Annoation found.
                docAnnotation an = new docAnnotation();
                an.setText(line);
                String parts[] = line.split("\\s+");
                an.setTagName(parts[0].substring(1));
                if (parts.length > 1) {
                    an.setValue(parts[1]);
                }
                if (parts.length > 2) {
                    String desc = parts[2];
                    for (int i = 3; i < parts.length; i++) {
                        desc += " " + parts[i];
                    }
                    an.setDescription(desc);
                }
                lst.add(an);
            } else {
                lst.add(new docText(line));
            }
        }

        this.docList = lst;
    }

    /**
     * Processes all the individual lines and groups up plain text and
     * annotation nodes into individual strings.
     * @return An array of Strings with each logical line.
     */
    private String[] parseProcessLines() {
        ArrayList<String> ret = new ArrayList();

        String cur = "";
        String lines[] = this.aussomDocText.split("\n");
        for (String line : lines) {
            if (line.matches("@[A-Za-z0-9_]+.*")) {
                if (!cur.trim().equals("")) {
                    ret.add(cur.trim());
                    cur = "";
                }
                cur += line + " ";
            } else if (line.trim().equals("")) {
                if (!cur.trim().equals("")) {
                    ret.add(cur.trim());
                    cur = "";
                }
            } else {
                cur += line + " ";
            }
        }

        // If there's any left not added already.
        if (!cur.trim().equals("")) {
            ret.add(cur.trim());
        }

        return ret.toArray(new String[ret.size()]);
    }

    @Override
    public String toString(int Level) {
        String rstr = "";
        rstr += getTabs(Level) + "{\n";
        rstr += this.getNodeStr(Level + 1) + ",\n";
        rstr += getTabs(Level + 1) + "\"fileName\": \"" + this.getFileName() + "\",\n";
        rstr += getTabs(Level + 1) + "\"aussomDocText\": \"" + this.aussomDocText + "\",\n";
        return rstr;
    }

    @Override
    public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
        return null;
    }

    public AussomType getAussomdoc() {
        AussomMap ret = new AussomMap();

        ret.put("aussomDocText", new AussomString(this.aussomDocText));
        AussomList lst = new AussomList();
        for (docText dt : this.docList) {
            AussomMap dobj = new AussomMap();
            dobj.put("type", new AussomString(dt.getType().name()));
            dobj.put("text", new AussomString(dt.getText()));
            if (dt.getType() == docType.ANNOTATION) {
                docAnnotation da = (docAnnotation)dt;
                dobj.put("tagName", new AussomString(da.getTagName()));
                dobj.put("tagValue", new AussomString(da.getValue()));
                dobj.put("tagDescription", new AussomString(da.getDescription()));
            }
            lst.add(dobj);
        }
        ret.put("docList", lst);

        return ret;
    }
}
