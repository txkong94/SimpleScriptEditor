package application;

import javafx.beans.property.SimpleStringProperty;

public class TranslateableActor {
	private SimpleStringProperty originalActor;
	private SimpleStringProperty translatedActor;
	
	public TranslateableActor(String originalActor) {
		setOriginalActor(originalActor);
	}
	
	public TranslateableActor(String originalActor, String translatedActor) {
		setOriginalActor(originalActor);
		setTranslatedActor(translatedActor);
	}
	
	public String getOriginalActor(){
		return originalActor.get();
	}
	
	public String getTranslatedActor(){
		return translatedActor.get();
	}
	
	public void setOriginalActor(String originalActorInput) {
		originalActor = new SimpleStringProperty(originalActorInput);
	}
	
	public void setTranslatedActor(String translatedActorInput) {
		translatedActor = new SimpleStringProperty(translatedActorInput);
	}
}
