package application;

public class Translateable {
	private TranslateableActor actor;
	private TranslateableLine line;
	
	
	public Translateable(TranslateableActor actor, TranslateableLine line) {
		this.setActor(actor);
		this.setLine(line);
	}

	public TranslateableActor getActor() {
		return actor;
	}

	public void setActor(TranslateableActor actor) {
		this.actor = actor;
	}

	public TranslateableLine getLine() {
		return line;
	}

	public void setLine(TranslateableLine line) {
		this.line = line;
	}
	
	
}
