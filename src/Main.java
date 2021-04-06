//Omar Dalal 1180171 Section 2
import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	//CSS Style used for different boxes
	final String BOX_STYLE = "-fx-background-color:#fff;-fx-background-radius:10px;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.16), 25px, 0, 0, 0);";
	
	public static void main(String[] args) {
		launch();
	}
	
	//Universal variables used throughout the project
	String inputDir="";
	String fileName="";
	int mode = 0; // 0=Compressing / 1=Decompressing
	FileInfoBox infoBox;
	HufNode[] nodes;
	HufNode root;
	EncodedChar[] codes;
	String postFix;
	String header;
	Scene scene;
	SizeBox sizeBox;
	VBox loadingOverlay;
	StackPane mainRoot;
	CharsWindow tableWindow;
	@Override
	public void start(Stage pS) {
		mainRoot = new StackPane();
		VBox root = new VBox(25);
		mainRoot.setStyle("-fx-font-size:18px;");
		root.setPadding(new Insets(0,50,0,50));
		
		SelectInputBox inputBox = createInputBox(pS);
		VBox.setMargin(inputBox, new Insets(25, 0, 0, 0));
		inputBox.setStyle(BOX_STYLE);
		
		scene = new Scene(mainRoot, 850, 700);
		
		tableWindow = new CharsWindow(mode);
		loadingOverlay = getLoadingPane();
		HBox bottomContainer = new HBox(10);
		VBox rightContainer = new VBox(10);
		infoBox = new FileInfoBox(scene);
		infoBox.setStyle(BOX_STYLE);
		VBox saveBox = getSaveBox(pS);
		VBox viewTableBox = getTButtonBox();
		rightContainer.getChildren().addAll(infoBox, viewTableBox, saveBox);
		sizeBox = new SizeBox(scene);
		sizeBox.setStyle(BOX_STYLE);
		bottomContainer.getChildren().addAll(sizeBox, rightContainer);
		
		root.getChildren().addAll(inputBox, bottomContainer);
		mainRoot.getChildren().addAll(root, loadingOverlay);
		
		pS.setScene(scene);
		pS.setResizable(false);
		pS.setTitle("Huffman Compressor");
		pS.show();
	}
	
	//Initialize VBox that has save button
	private VBox getSaveBox(Stage pS) {
		VBox root = new VBox(10);
		root.setStyle(BOX_STYLE);
		root.setPrefHeight(100);
		root.setAlignment(Pos.CENTER);
		
		Label titleLbl = new Label("Perform Action");
		titleLbl.setStyle("-fx-font-weight:bold;");
		
		Button runBtn = new Button("Run");
		runBtn.setId("runBtn");
		runBtn.setAlignment(Pos.CENTER);
		runBtn.setOnAction(e-> {
			DirectoryChooser chooser = new DirectoryChooser();
			File outputDir = chooser.showDialog(pS);
			try {
				if (outputDir==null) {
					throw new NullPointerException();
				}
				if (mode==0) {
					loadingLbl.setText("Compressing");
					loading(() -> {
						Encode.writeEncodedFile(outputDir.getAbsolutePath()+"/"+fileName+".huff", inputDir, header, codes);
					});
				} else {
					loadingLbl.setText("Decompressing");
					loading(() -> {
						Decode.writeDecodedFile(inputDir, outputDir.getAbsolutePath()+"/"+fileName);
					});
				}
			} catch (NullPointerException ex) {
			}
		});
		
		root.getChildren().addAll(titleLbl, runBtn);
		return root;
	}
	
	//Method to perform actions (Loading / Compressing / Decompressing) in the background and showing the loading overlay in the meantime
	public void loading(loadingInterface backgroundProccess) {
		loadingOverlay.setVisible(true);
		Service<Void> load = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						backgroundProccess.load();
						return null;
					}
				};
			}
			
		};
		load.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				loadingOverlay.setVisible(false);
			}
		});
		load.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				new ErrorAlert("Input file not found!");
				loadingOverlay.setVisible(false);
			}
		});
		load.restart();
	}
	
	//Interface used to make a callback method called load() that will be used in the method loading()
	private interface loadingInterface {
		public void load();
	}
	
	//Method to fill characters table according to mode (compressing/decompressing)
	private void fillTable() {
		for (int i=0; i<codes.length; i++) {
			String asciiCode = ""+toUsnignedByte((byte)codes[i].getCh());
			String character = ""+codes[i].getCh();
			String huffCode = codes[i].getCode();
			if (mode==0) {
				try {
					HufNode currentNode = AuxMethods.findNode(codes[i].getCh(), nodes);
					int freq = currentNode.getFreq();
					String frequency = nodes==null?"-":""+currentNode.getFreq();
					String sizeBefore = AuxMethods.formatSize(freq);
					String sizeAfter = AuxMethods.getEncodedNodeSize(currentNode, codes)+" bits";
					TableRow row = new TableRow(asciiCode, character, huffCode, frequency, sizeBefore, sizeAfter);
					tableWindow.addItem(row);
				} catch (NullPointerException ex) {
					System.out.println("NODE_LOCATION_FAILED");
				}
			} else {
				TableRow row = new TableRow(asciiCode, character, huffCode);
				tableWindow.addItem(row);
			}
		}
	}
	
	//Convert signed byte into an unsigned byte
	public static int toUsnignedByte(byte b) {
	    return b & 0xFF;
	  }
	
	//Initializing the input box that contains the browse button
	private SelectInputBox createInputBox(Stage pS) {
		Button browseBtn = new Button();
		SelectInputBox inputBox = new SelectInputBox(pS, browseBtn);
		browseBtn.setOnAction(e-> {
			tableWindow.closeWindow();
			FileChooser chooser = new FileChooser();
			File inputFile = chooser.showOpenDialog(pS);
			try {
				inputDir = inputFile.getAbsolutePath();
				if (AuxMethods.getFileLength(inputDir)==0) {
					inputDir = "";
					new ErrorAlert("File is Empty!");
					throw new NullPointerException();
				}
				if (AuxMethods.getExtension(inputDir).equals("huff")) {
					if (!Decode.checkValid(inputDir)) {
						inputDir = "";
						new ErrorAlert("Invalid File!");
						throw new NullPointerException();
					}
				}
				fileName = AuxMethods.removeExt(inputFile.getName());
				String ext = AuxMethods.getExtension(inputDir);
				mode = ext.equals("huff")?1:0;
				infoBox.setLblValue(fileName, "nameLbl");
				infoBox.setLblValue("."+ext, "extLbl");
				infoBox.setLblValue(AuxMethods.getFileSize(inputDir), "sizeLbl");
				Button btn = (Button)scene.lookup("#runBtn");
				btn.setText(ext.equals("huff")?"Decompress":"Compress");
				adjustFileBox();
			} catch (NullPointerException ex) {
			}
		});
		return inputBox;
	}
	
	//Update file information box
	private void adjustFileBox() {
		loadingLbl.setText("Loading");
		loading(new loadingInterface() {
			@Override
			public void load() {
				Platform.runLater(() -> {
					long ogSize = 0;
					long headerSize = 0;
					long encodedSize = 0;
					long newSize = 0;
					if (mode==0) {
						nodes = Encode.generateNodesFromFile(inputDir);
						root = Encode.buildHufTree(nodes);
						codes = PostFix.generateCodes(root, nodes.length);
						
						ogSize = AuxMethods.getFileLength(inputDir);
						postFix = PostFix.generatePostFix(root);
						header = AuxMethods.generateHeader(inputDir, postFix);
						headerSize = AuxMethods.getHeaderSize(header);
						encodedSize = AuxMethods.getEncodedSize(codes, nodes);
						newSize = headerSize+encodedSize;
					} else {
						nodes=null;
						header="";
						String[] headerArr = Decode.readHeaderFromEncodedFile(inputDir);
						for (int i=0; i<headerArr.length; i++) {
							header+=headerArr[i]+(i==headerArr.length-1?"":"/");
						}
						int charCount = PostFix.getCharCountFromPostFix(headerArr[3]);
						root = PostFix.buildTreeFromPostFix(headerArr[3], charCount);
						codes = PostFix.generateCodes(root, charCount);
						
						newSize = AuxMethods.getFileLength(inputDir);
						headerSize = AuxMethods.getHeaderSize(header);
						encodedSize = newSize - headerSize;
						ogSize = Integer.parseInt(headerArr[1]);
					}
					sizeBox.setLblValue(AuxMethods.formatSize(ogSize), "size0");
					sizeBox.setLblValue(AuxMethods.formatSize(newSize), "size1");
					sizeBox.setLblValue(AuxMethods.formatSize(encodedSize), "size2");
					sizeBox.setLblValue(AuxMethods.formatSize(headerSize), "size3");
					long maxSize = Long.max(Long.max(newSize, headerSize), Long.max(encodedSize, ogSize));
					sizeBox.setMaxSize(maxSize);
					sizeBox.setRectHeight(ogSize, "rect0");
					sizeBox.setRectHeight(newSize, "rect1");
					sizeBox.setRectHeight(encodedSize, "rect2");
					sizeBox.setRectHeight(headerSize, "rect3");
					
					float compressRatio = 100-((float)newSize/(float)ogSize)*100;
					sizeBox.setRatio(String.format("%.2f", compressRatio));
				});
			}
		});
	}
	
	//Loading VBox that will be shown during (Loading / Compressing / Decompressing)
	Label loadingLbl;
	private VBox getLoadingPane() {
		VBox root = new VBox();
		root.setVisible(false);
		root.setPadding(new Insets(0,300,0,300));
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color:rgba(230,230,230, 0.75)");
		
		VBox loadingBox = new VBox(10);
		loadingBox.setAlignment(Pos.CENTER);
		loadingBox.setPrefHeight(150);
		loadingBox.setStyle(BOX_STYLE);
		
		ProgressIndicator indicator = new ProgressIndicator();
		
		loadingLbl = new Label("Proccessing");
		loadingLbl.setStyle("-fx-font-size:24px");
		loadingLbl.setAlignment(Pos.CENTER);
		
		loadingBox.getChildren().addAll(loadingLbl, indicator);
		root.getChildren().add(loadingBox);
		return root;
	}
	
	boolean headerWindowOpen;
	//Initialize box that contains the button to open characters table
	private VBox getTButtonBox() {
		VBox root = new VBox(10);
		root.setStyle(BOX_STYLE);
		root.setPrefHeight(150);
		root.setAlignment(Pos.CENTER);
		
		Label titleLbl = new Label("Characters Info");
		titleLbl.setStyle("-fx-font-weight:bold");
		Button tableBtn = new Button("Char Table");
		tableBtn.setWrapText(true);
		tableBtn.setAlignment(Pos.CENTER);
		
		headerWindowOpen=false;
		Button headerBtn = new Button("Header");
		headerBtn.setOnAction(e-> {
			if (header!=null&&!header.isEmpty()&&!headerWindowOpen) {
				Stage headerWindow = new Stage();
				headerWindow.setTitle("Header");
				headerWindow.setResizable(false);
				VBox headerRoot = new VBox (10);
				headerRoot.setStyle("-fx-font-size:18px;-fx-background-color:white");
				headerRoot.setAlignment(Pos.CENTER);
				Label headerTitle = new Label("Header");
				headerTitle.setAlignment(Pos.CENTER);
				headerTitle.setStyle("-fx-font-weight:bold;");
				TextArea headerArea = new TextArea();
				headerArea.setPrefSize(700, 250);
				headerArea.setEditable(false);
				headerArea.setText(header);
				headerRoot.getChildren().addAll(headerTitle, headerArea);
				Scene headerScene = new Scene(headerRoot, 700, 300);
				headerWindow.setScene(headerScene);
				headerWindow.show();
				headerWindowOpen = true;
				headerWindow.setOnCloseRequest(ev->headerWindowOpen=false);
			}
		});

		tableBtn.setOnAction(e-> {
			if (!inputDir.isEmpty()) {
				tableWindow = new CharsWindow(mode);
				fillTable();
				tableWindow.showWindow();
			}
		});
		
		root.getChildren().addAll(titleLbl, tableBtn, headerBtn);
		return root;
	}
	
}
