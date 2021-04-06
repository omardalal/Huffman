//Omar Dalal 1180171 Section 2
//Class that contains many methods related to generating and reading postfix
public class PostFix {
	//Public method used to generate the postfix from the root node of a tree
	static String PostFix="";
	public static String generatePostFix(HufNode node) {
		if (node.getLeft()==null&&node.getRight()==null) {
			return "1"+node.getCh()+"0";
		}
		PostFix = "";
		generatePostFixRec(node);
		return PostFix;
	}
	
	//Private Recursive method to go through all nodes and add them
	private static void generatePostFixRec(HufNode node) {
		if (node==null) {
			return;
		}
		generatePostFixRec(node.getLeft());
		generatePostFixRec(node.getRight());
		//If leaf then add 1 followed by the character
		//If parent then just add zero
		if (node.getLeft()==null&&node.getRight()==null) {
			PostFix+="1"+node.getCh();
		} else {
			PostFix+="0";
		}
	}
	
	//Method to generate codes from postFix
	static int codesCount = 0;
	static EncodedChar[] codes;
	static String codeVal = "";
	public static EncodedChar[] generateCodes(HufNode root, int charCount) {
		codesCount = 0;
		codes = new EncodedChar[charCount];
		codeVal = "";
		if (charCount==1) {
			return new EncodedChar[] {new EncodedChar(root.getLeft().getCh(), "0")};
		}
		createCodesArr(root, codeVal, codes);
		return codes;
	}
	
	//Private method used to create an array filed with characters and their codes
	private static void createCodesArr(HufNode root, String s, EncodedChar[] codes) { 
		if (root==null) {
			return;
		}
		if (root.getLeft() == null && root.getRight()== null) { 
	        EncodedChar ch = new EncodedChar(root.getCh(), s);
	        codes[codesCount++] = ch;
	        return; 
	    } 
		createCodesArr(root.getLeft(), s + "0", codes); 
		createCodesArr(root.getRight(), s + "1", codes); 
	}
	
	//Get the number of characters stored in the postFix
	public static int getCharCountFromPostFix(String PostFix) {
		int charCount=0;
		for (int i=0; i<PostFix.length(); i++) {
			if (PostFix.charAt(i)=='1') {
				i++;
				charCount++;
			}
		}
		return charCount;
	}	
	
	//Using a stack to build huftree heap  
	public static HufNode buildTreeFromPostFix(String PostFix, int charCount) {
		Stack nodes = new Stack(charCount);
		HufNode root = null;
		for (int i=0; i<PostFix.length(); i++) {
			if (PostFix.charAt(i)=='1') {
				HufNode node = new HufNode(PostFix.charAt(i+1), 0);
				nodes.push(node);
				i++;
			} else if (PostFix.charAt(i)=='0') {
				if (nodes.length()==1) {
					HufNode node = new HufNode(0);
					node.setLeft(nodes.pop());
					nodes.push(node);
				} else {
					HufNode node = new HufNode(0);
					node.setRight(nodes.pop());
					node.setLeft(nodes.pop());
					nodes.push(node);
				}
			}
		}
		root = nodes.get(0);
		return root;
	}
}
