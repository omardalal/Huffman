//Omar Dalal 1180171 Section 2
//Class used to build rows in characters table
public class TableRow {
	
	//Attributes
	private String asciiCode;
	private String character;
	private String huffCode;
	private String frequency;
	private String sizeBefore;
	private String sizeAfter;
	
	//First Constructor - encoding mode
	public TableRow(String asciiCode, String character, String huffCode, String frequency, String sizeBefore, String sizeAfter) {
		super();
		this.asciiCode = asciiCode;
		this.character = character;
		this.huffCode = huffCode;
		this.frequency = frequency;
		this.sizeBefore = sizeBefore;
		this.sizeAfter = sizeAfter;
	}
	
	//Second Construct - Decoding Mode
	public TableRow(String asciiCode, String character, String huffCode) {
		super();
		this.asciiCode = asciiCode;
		this.character = character;
		this.huffCode = huffCode;
	}

	//Setters and Getters
	public String getAsciiCode() {
		return asciiCode;
	}

	public void setAsciiCode(String asciiCode) {
		this.asciiCode = asciiCode;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getHuffCode() {
		return huffCode;
	}

	public void setHuffCode(String huffCode) {
		this.huffCode = huffCode;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getSizeBefore() {
		return sizeBefore;
	}

	public void setSizeBefore(String sizeBefore) {
		this.sizeBefore = sizeBefore;
	}

	public String getSizeAfter() {
		return sizeAfter;
	}

	public void setSizeAfter(String sizeAfter) {
		this.sizeAfter = sizeAfter;
	}
}
