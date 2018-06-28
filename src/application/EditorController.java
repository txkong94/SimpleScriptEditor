package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;







import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EditorController implements Initializable{
	
	//Status
	@FXML
	private Label statusLabel;
	
	
	//Menu
	@FXML
	private MenuItem openMenuItem;
	
	@FXML
	private MenuItem saveMenuItem;
	
	@FXML
	private MenuItem importMenuItem;
	
	@FXML
	private MenuItem exportMenuItem;
	
	@FXML
	private MenuItem closeMenuItem;
	
	@FXML
	private MenuItem exitMenuItem;
	
	
	//Buttons
	
	@FXML
	private Button commitButton;
	
	@FXML
	private Button nextLineButton;
	
	@FXML
	private Button previousLineButton;
	

	//TextFields
	
	@FXML
	private TextField originalLineField;
	
	@FXML
	private TextArea translationField;
	
	@FXML
	private TextField originalActorField;
	
	@FXML
	private TextField translatedActorField;
	
	
	//Table
	
	@FXML
	private TableView<TranslateableLine> linesTable;
	
	@FXML
	private TableColumn<TranslateableLine, Integer> lineColumn;
	
	@FXML
	private TableColumn<TranslateableLine, String> originalActorColumn;
	
	@FXML
	private TableColumn<TranslateableLine, String> translatedActorColumn;
	
	@FXML
	private TableColumn<TranslateableLine, String> originalLineColumn;
	
	@FXML
	private TableColumn<TranslateableLine, String> translatedLineColumn;
	
	
	//ClipBoard
	final Clipboard clipboard = Clipboard.getSystemClipboard();
	
	
	
	//Variables related to other things
	private static Stage parentStage = null;
	private File currentFile = null;
	private String scriptType = "";
	private TranslateableLine translatedLine;
	private ObservableList<TranslateableLine> lines = null;
	private ArrayList<TranslateableActor> actors = null;
	
	public static void setStage(Stage stage) {
		parentStage = stage;
	}
	
	
	private void copyOriginalLine(){
		if(!originalLineField.getText().isEmpty()){
			final ClipboardContent originalLineContent= new ClipboardContent();
			originalLineContent.putString(originalLineField.getText());
			clipboard.setContent(originalLineContent);
		}
	}
	
	private boolean commitLine(){
		if(translatedLine != null){
			String translatedActor = translatedActorField.getText();
			String translation = translationField.getText();
			translatedLine.setTranslatedActor(translatedActor);
			translatedLine.setTranslatedLine(translation);
			status("Line commited.");
			return true;
		}
		else {
			error("No line was selected.");
			return false;
		}
	}
	
	private boolean nextLine(){
			linesTable.getSelectionModel().selectNext();
			return true;
	}
	
	private boolean previousLine() {
		linesTable.getSelectionModel().selectPrevious();
		return true;
	}
	
	private void error(String errorMessage){
		statusLabel.setText(errorMessage);
		statusLabel.setTextFill(Color.RED);
	}
	
	private void status(String statusMessage){
		statusLabel.setText(statusMessage);
		statusLabel.setTextFill(Color.BLACK);
	}
	
	public void onClick(ActionEvent event){
		boolean commited = commitLine();
			if(commited){
				nextLine();
			}
	}
	
	public void onPreviousLine(ActionEvent event){
		previousLine();
	}
	
	public void onNextLine(ActionEvent event){
		nextLine();
	}
	
	public void onCopyOriginalLine(ActionEvent event) {
		copyOriginalLine();
	}
	
	public void onOpenMenuClick(ActionEvent event) {
		FileChooser openFileChooser = new FileChooser();
		openFileChooser.setTitle("Open Script (.sses)");
		openFileChooser.getExtensionFilters().add(new ExtensionFilter("Simple Script Editor Scripts", "*.sses"));
		if (currentFile != null) openFileChooser.setInitialDirectory(currentFile.getParentFile());
		File selectedFile = openFileChooser.showOpenDialog(parentStage);
		if (selectedFile != null) loadSSES(selectedFile);
		selectedFile = null;
	}
	
	public void onSaveMenuClick(ActionEvent event) {
		saveSSES();
		
	}
	
	private void saveSSES() {
		if(currentFile != null) {
		ArrayList<TranslateableLine> linesToSave = new ArrayList<TranslateableLine>();
		for(TranslateableLine l : lines) {
			linesToSave.add(l);
		}
		SSESParser.save(currentFile.getPath(), linesToSave, scriptType);
		status(currentFile.getName() + " has been saved.");
		}else {
			error("No file has been opened yet.");
		}
	}
	
	public void onImportMenuClick(ActionEvent event) {
		FileChooser openFileChooser = new FileChooser();
		openFileChooser.setTitle("Import Script");
		openFileChooser.getExtensionFilters().add(new ExtensionFilter("ks file", "*.ks"));
		if (currentFile != null) openFileChooser.setInitialDirectory(currentFile.getParentFile());
		File selectedFile = openFileChooser.showOpenDialog(parentStage);
		if (selectedFile != null) loadKS(selectedFile);
		selectedFile = null;
	}
	
	public void onExportMenuClick(ActionEvent event) {
		File selectedFile = null;
		if(currentFile != null) {
			saveSSES();
			selectedFile = currentFile;
		}
		else {
			FileChooser openFileChooser = new FileChooser();
			openFileChooser.setTitle("Select Script To Export (.sses)");
			openFileChooser.getExtensionFilters().add(new ExtensionFilter("Simple Script Editor Scripts", "*.sses"));
			if (currentFile != null) openFileChooser.setInitialDirectory(currentFile.getParentFile());
			selectedFile = openFileChooser.showOpenDialog(parentStage);
		}
		if (selectedFile != null){
			try {
				boolean status = KSParser.exportKSFile(selectedFile.getPath());
				if(status) status(selectedFile.getName() + " has been exported.");
				else error(selectedFile.getName() + " did not export properly.");
				selectedFile = null;
			} catch (FileNotFoundException e) {
				System.out.println("Life sucks");
				e.printStackTrace();
			}
		}
	}
	
	public void onCloseMenuClick(ActionEvent event){
		boolean closed = closeScript();
		if(closed) {
			status("Script closed.");
		}
		else error("Script could not be closed.");
	}
	
	private boolean closeScript(){
		currentFile = null;
		scriptType = "";
		translatedLine = null;
		lines = null;
		linesTable.setItems(lines);
		return true;
	}
	
	public void onExitMenuClick(ActionEvent event){
		parentStage.close();
	}
	
	public void onActorMenuClick(ActionEvent event) {
		try {
			ActorView.start(new Stage(), parentStage, actors);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadKS(File ksFile) {
		try {
			loadSSES(KSParser.importKSFile(ksFile.getPath()));
			status(ksFile.getName() + " has been imported.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadSSES(File ssesFile) {
		try {
			lines = FXCollections.observableArrayList(SSESParser.load(ssesFile.getPath()));
			currentFile = ssesFile;
			scriptType = SSESParser.getFileType();
			linesTable.setItems(lines);
			linesTable.getSelectionModel().selectFirst();
			status(currentFile.getName() + " has been opened.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void commitOnEnter(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			event.consume();
			boolean commited = commitLine();
			if(commited){
				nextLine();
			}

		}
	}
	
	public void onTranslationKeyPressed(KeyEvent event){
		commitOnEnter(event);
	}
	
	public void onActorKeyPressed(KeyEvent event){
		commitOnEnter(event);
	}
	
	public void setTranslatedLine(TranslateableLine line) {
		translatedLine = line;
	}
	
	public void updateEditor(){
		if(translatedLine != null){
			originalActorField.setText(translatedLine.getOriginalActor());
			translatedActorField.setText(translatedLine.getTranslatedActor());
			originalLineField.setText(translatedLine.getOriginalLine());
			translationField.setText(translatedLine.getTranslatedLine());
		}
		else {
			originalActorField.setText("");
			translatedActorField.setText("");
			originalLineField.setText("");
			translationField.setText("");
		}
		copyOriginalLine();
	}
	
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lineColumn.setCellValueFactory(new PropertyValueFactory<TranslateableLine, Integer>("currentLine"));
		originalActorColumn.setCellValueFactory(new PropertyValueFactory<TranslateableLine, String>("originalActor"));
		translatedActorColumn.setCellValueFactory(new PropertyValueFactory<TranslateableLine, String>("translatedActor"));
		originalLineColumn.setCellValueFactory(new PropertyValueFactory<TranslateableLine, String>("originalLine"));
		translatedLineColumn.setCellValueFactory(new PropertyValueFactory<TranslateableLine, String>("translatedLine"));
		
		linesTable.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<TranslateableLine>() {

					@Override
					public void changed(ObservableValue<? extends TranslateableLine> observable, TranslateableLine oldValue, TranslateableLine newValue) {
								setTranslatedLine(newValue);
								updateEditor();
					}
					
				}
			);
		
	}
	
	
	
	

}
