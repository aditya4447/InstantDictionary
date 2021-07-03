package in.omdev.idictionary.entity;

import java.util.ArrayList;

public class Definition {
    private String definition;
    private String example;
    private final ArrayList<String> synonyms = new ArrayList<>();

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
        if (this.example != null) {
            this.example = "\"" + this.example + "\"";
        }
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        if (synonyms == null) {
            return;
        }
        this.synonyms.addAll(synonyms);
    }
}
