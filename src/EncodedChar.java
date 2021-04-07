//Used to create objects that contain every character and it's code
public class EncodedChar {
	
	//Attributes
	private char ch;
	private String code;
	
	//Constructor
	public EncodedChar(char ch, String code) {
		this.ch = ch;
		this.code = code;
	}

	//Setters and Getters
	public char getCh() {
		return ch;
	}

	public void setCh(char ch) {
		this.ch = ch;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	//Convert to string
	@Override
	public String toString() {
		return "Character: "+ch+"\tCode: "+code;
	}
}
