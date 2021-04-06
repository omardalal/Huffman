//Omar Dalal 1180171 Section 2
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Class to initialize top box that contains the file selection button
public class SelectInputBox extends VBox {
	
	//Initialize root
	public SelectInputBox(Stage stage, Button selectBtn) {
		super(10);
		super.setPadding(new Insets(15,0,15,0));
		super.setAlignment(Pos.CENTER);
		super.setPrefHeight(100);
		
		Label titleLbl = new Label("Huffman Compressor");
		titleLbl.setStyle("-fx-font-size:22px; -fx-font-weight: bold;");
		titleLbl.setAlignment(Pos.CENTER);
		
		HBox inputHBox = getInputHBox(stage, selectBtn);
		
		super.getChildren().addAll(titleLbl, inputHBox);
	}
	
	//Initialize inner HBox
	public HBox getInputHBox(Stage stage, Button browseBtn) {
		HBox box = new HBox(25);
		box.setAlignment(Pos.CENTER);

		Label selectLbl = new Label("Select a File to Compress / Decompress");
		selectLbl.setId("titleLbl");
		selectLbl.setPrefHeight(37.5);
		browseBtn.setText("Browse");
		
		box.getChildren().addAll(selectLbl, browseBtn);
		return box;
	}
}
