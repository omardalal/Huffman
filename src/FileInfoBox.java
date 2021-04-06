//Omar Dalal 1180171 Section 2

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//Custom VBox used in scene to display the info of selected file
public class FileInfoBox extends VBox {
	
	//Constructor
	Scene scene;
	public FileInfoBox(Scene scene) {
		super(5);
		super.setPrefWidth(315);
		this.scene = scene;
		super.setPadding(new Insets(15,15,15,15));
		Label header = new Label("File Information");
		header.setStyle("-fx-font-size: 22px; -fx-font-weight:bold;");
		HBox fileNameRow = createRow("Name: ", "nameLbl");
		HBox fileExtRow = createRow("Extension: ", "extLbl");
		HBox fileSizeRow = createRow("Size: ", "sizeLbl");
		super.getChildren().addAll(header, fileNameRow, fileExtRow, fileSizeRow);
	}
	
	//Method used to set the text of a given label (using its id)
	public void setLblValue(String val, String labelId) {
		Label lbl = (Label)scene.lookup("#"+labelId);
		lbl.setText(val);
	}
	
	//Private method used to create an HBox that contains two labels
	private HBox createRow(String title, String id) {
		HBox row = new HBox(5);
		
		Label titleLbl = new Label(title);
		titleLbl.setMinWidth(90);
		titleLbl.setWrapText(true);
		titleLbl.setStyle("-fx-font-weight: bold");
		titleLbl.setPrefHeight(37.5);
		
		Label valLbl = new Label();
		valLbl.setWrapText(true);
		valLbl.setMinHeight(37.5);
		valLbl.setId(id);
		
		row.getChildren().addAll(titleLbl, valLbl);
		return row;
	}
	
}
