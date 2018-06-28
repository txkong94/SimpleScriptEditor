package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TranslateableLine {
	private SimpleStringProperty originalLine;
	public SimpleStringProperty translatedLine;
	private SimpleStringProperty originalActor;
	private SimpleStringProperty translatedActor;
	private SimpleIntegerProperty currentLine;
	
	private static int count = 0;
	
	
	public TranslateableLine(String originalLine) {
		setOriginalLine(originalLine);
		setTranslatedLine("");
		setOriginalActor("");
		setTranslatedActor("");
		incrementCount();
		setCurrentLine(count);
	}
	
	
	public TranslateableLine(String originalActor, String originalLine) {
		setOriginalLine(originalLine);
		setTranslatedLine("");
		setOriginalActor(originalActor);
		setTranslatedActor("");
		incrementCount();
		setCurrentLine(count);
	}
	
	

	public String getOriginalLine(){
		if(originalLine == null) originalLine = new SimpleStringProperty("");
		return originalLine.get();
	}
	
	public String getTranslatedLine(){
		if(translatedLine == null) translatedLine = new SimpleStringProperty("");
		return translatedLine.get();
	}
	
	private void setOriginalLine(String originalLineInput) {
		if(originalLine == null) originalLine = new SimpleStringProperty(originalLineInput);
		else originalLine.set(originalLineInput);
	}
	
	public void setTranslatedLine(String translatedLineInput) {
		if(translatedLine == null) translatedLine = new SimpleStringProperty(translatedLineInput);
		else translatedLine.set(translatedLineInput);
	}
	
	public String getOriginalActor(){
		if(originalActor == null) originalActor = new SimpleStringProperty("");
		return originalActor.get();
	}
	
	public String getTranslatedActor(){
		if(translatedActor == null) translatedActor = new SimpleStringProperty("");
		return translatedActor.get();
	}
	
	private void setOriginalActor(String originalActorInput) {
		if(originalActor == null) originalActor = new SimpleStringProperty(originalActorInput);
		else originalActor.set(originalActorInput);
	}
	
	public void setTranslatedActor(String translatedActorInput) {
		if(translatedActor == null) translatedActor = new SimpleStringProperty(translatedActorInput);
		else translatedActor.set(translatedActorInput);;
	}

	public static int getCount() {
		return count;
	}

	private static void incrementCount() {
		TranslateableLine.count++;
	}

	public void setCurrentLine(int currentLine) {
		if(this.currentLine == null) this.currentLine = new SimpleIntegerProperty(currentLine);
		else this.currentLine.set(currentLine);
	}
	
	public int getCurrentLine(){
		return currentLine.get();
	}
	
	public SimpleStringProperty originalActorProperty(){
		return originalActor;
	}
	
	public SimpleStringProperty originalLineProperty(){
		return originalLine;
	}
	
	public SimpleStringProperty translatedActorProperty(){
		return translatedActor;
	}
	
	public SimpleStringProperty translatedLineProperty(){
		return translatedLine;
	}
	
	public SimpleIntegerProperty currentLineProperty(){
		return currentLine;
	}
	
	
}
