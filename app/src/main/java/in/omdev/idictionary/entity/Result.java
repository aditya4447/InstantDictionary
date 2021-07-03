package in.omdev.idictionary.entity;

import java.util.ArrayList;

public class Result {
    private String word;
    private final ArrayList<Phonetic> phonetics = new ArrayList<>();
    private final ArrayList<Meaning> meanings = new ArrayList<>();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(ArrayList<Phonetic> phonetics) {
        if (phonetics == null) {
            return;
        }
        this.phonetics.addAll(phonetics);
    }

    public ArrayList<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<Meaning> meanings) {
        if (meanings == null) {
            return;
        }
        this.meanings.addAll(meanings);
    }
}
