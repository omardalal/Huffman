//Omar Dalal 1180171 Section 2
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Custom pane that contains size statistics info
public class SizeBox extends Pane {

	Scene scene;
	//Maximum Size (Original file or compressed file)
	private long maxSize=1;
	//maximum height of a size rectangle
	private final int MAX_HEIGHT = 350;
	
	Label titleLbl;
	//Initializing the Pane
	public SizeBox(Scene scene) {
		super();
		super.setPrefHeight(500);
		super.setPrefWidth(450);
		this.scene = scene;
		
		titleLbl = new Label("Size Statistics");
		titleLbl.setStyle("-fx-font-size:22px;-fx-font-weight:bold;-fx-padding:15px");
		
		double rectWidth = 50;
		Label[] titleLabels = {new Label("Original Size"), new Label("Compressed Size"), new Label("Encoded Size"), new Label("Header Size")};
		Label[] sizeLabels = {new Label("-"), new Label("-"), new Label("-"), new Label("-")};
		Rectangle[] rects = {new Rectangle(rectWidth, 5), new Rectangle(rectWidth, 5), new Rectangle(rectWidth, 5), new Rectangle(rectWidth, 5)};
		Rectangle line = getBottomLine();
		
		for (int i=0; i<sizeLabels.length; i++) {
			initSizeLbl(sizeLabels[i], i);
			super.getChildren().add(sizeLabels[i]);
		}
		
		for (int i=0; i<titleLabels.length; i++) {
			initTitleLbl(titleLabels[i], i);
			super.getChildren().add(titleLabels[i]);
		}
		
		for (int i=0; i<rects.length; i++) {
			initRect(rects[i], i);
			super.getChildren().add(rects[i]);
		}
		
		super.getChildren().addAll(titleLbl, line);
	}
	
	//Set the value titleLbl
	public void setRatio(String val) {
		titleLbl.setText("Size Statistics   "+val+"% Compression");
	}
	
	//Initialize given title label
	private void initTitleLbl(Label lbl, int order) {
		lbl.setStyle("-fx-font-size: 12px;");
		lbl.setMaxWidth(95);
		lbl.setWrapText(true);
		lbl.setLayoutY(450);
		lbl.setLayoutX(15+110*order);
	}
	
	//Initialize given size label
	private void initSizeLbl(Label lbl, int order) {
		lbl.setStyle("-fx-font-size: 12px;");
		lbl.setTextFill(Color.rgb(15*(order+1), 25*(order+1), 50*(order+1)));
		lbl.setMaxWidth(95);
		lbl.setWrapText(true);
		lbl.setId("size"+order);
		lbl.setLayoutY(430);
		lbl.setLayoutX(22.5+110*order);
	}
	
	//Set the text value of a given label using its id
	public void setLblValue(String val, String labelId) {
		Label lbl = (Label)scene.lookup("#"+labelId);
		lbl.setText(val);
	}
	
	//Set maximum size attribute
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	
	//Set the height of a given rectangle
	public void setRectHeight(long size, String rectId) {
		Rectangle rect = (Rectangle)scene.lookup("#"+rectId);
		rect.setHeight((double)size/maxSize*MAX_HEIGHT);
		rect.setLayoutY(420-rect.getHeight());
	}
	
	//Initialize a given rectangle
	private void initRect(Rectangle rect, int order) {
		rect.setLayoutY(420-rect.getHeight());
		rect.setLayoutX(20+110*order);
		rect.setFill(Color.rgb(15*(order+1), 25*(order+1), 50*(order+1)));
		rect.setId("rect"+order);
	}
	
	//Initialize the line that separates between rectangle and bottom labels
	private Rectangle getBottomLine() {
		Rectangle line = new Rectangle(415, 1);
		line.setFill(Color.rgb(190, 190, 190));
		line.setLayoutX(15);
		line.setLayoutY(420);
		return line;
	}
}
