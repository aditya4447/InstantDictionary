package in.omdev.idictionary.entity;

import java.util.ArrayList;

public class Meaning {
    private String partOfSpeech;
    private final ArrayList<Definition> definitions = new ArrayList<>();

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public ArrayList<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ArrayList<Definition> definitions) {
        if (definitions == null) {
            return;
        }
        this.definitions.addAll(definitions);
    }
}
