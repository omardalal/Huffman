//Omar Dalal 1180171 Section 2

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Window that contains characters table
public class CharsWindow {

	private Stage window = new Stage();
	private TableView<TableRow> charTable;
	private int mode; //0->Compress / 1->Decompress
	
	//Initialize root
	public CharsWindow(int mode) {
		VBox root = new VBox();
		this.mode=mode;
		root.setStyle("-fx-font-size:16px");
		root.setAlignment(Pos.CENTER);
		
		VBox charTableContainer = getTableContainer();
		
		root.getChildren().add(charTableContainer);
		Scene scene = new Scene(root, 760, 750);
		window.setScene(scene);
		window.setTitle("Characters Table");
		window.setResizable(false);
	}
	
	//Initialize table container VBox
	public VBox getTableContainer() {
		VBox charTableContainer = new VBox(25);
		charTableContainer.setPrefWidth(750);
		charTableContainer.setPrefHeight(700);
		charTableContainer.setAlignment(Pos.CENTER);
		charTableContainer.setPadding(new Insets(25, 25, 25, 25));
		
		Label titleLbl = new Label("Characters Table");
		titleLbl.setAlignment(Pos.CENTER);
		titleLbl.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");
		
		charTable = getTable();
		charTableContainer.setStyle("-fx-background-color:#fff;-fx-background-radius:10px;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.16), 25px, 0, 0, 0);");
		
		Button closeBtn = new Button("Close");
		closeBtn.setOnAction(e-> {
			window.close();
		});
		
		VBox.setMargin(charTableContainer, new Insets(0, 15, 0, 15));
		charTableContainer.getChildren().addAll(titleLbl, charTable, closeBtn);
		
		return charTableContainer;
	}
	
	//Method used to open characters window
	public void showWindow() {
		if (!isOpen()) {
			window.show();
		}
	}
	
	//Initialize characters table
	private TableView<TableRow> getTable() {
		charTable = new TableView<>();
		charTable.setPrefHeight(600);
		
		createColumn(charTable, "ASCII", 50, "asciiCode");
		createColumn(charTable, "Character", 75, "character");
		createColumn(charTable, "Huffman Code", 120, "huffCode");
		if (mode==0) {
			createColumn(charTable, "Frequency", 90, "frequency");
			createColumn(charTable, "Size Before Compression", 170, "sizeBefore");
			createColumn(charTable, "Size After Compression", 170, "sizeAfter");
		}
		
		return charTable;
	}
	
	//Add character to table
	public void addItem(TableRow item) {
		charTable.getItems().add(item);
	}
	
	//Create column and add it table
	private void createColumn(TableView<TableRow> table, String title, int width, String varVal) {
		TableColumn<TableRow, String> column = new TableColumn<>(title);
		column.setStyle("-fx-font-size:15px;-fx-font-weight:normal");
		column.setPrefWidth(width);
		column.setMinWidth(50);
		column.setCellValueFactory(new PropertyValueFactory<TableRow, String>(varVal));
		table.getColumns().add(column);
	}
	
	//Method to close window if open
	public void closeWindow() {
		if (isOpen()) {
			window.close();
		}
	}
	
	//Return true if window is open
	public boolean isOpen() {
		return window.isShowing();
	}
}
