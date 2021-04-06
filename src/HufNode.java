//Omar Dalal 1180171 Section 2

//Huffnode used to build heap (HuffTree)
public class HufNode implements Comparable<HufNode> {

	//Attributes
	private char ch;
	private int freq;
	private HufNode left;
	private HufNode right;
	private HufNode parent;
	
	//Constructor
	public HufNode(char ch, int freq) {
		this.ch = ch;
		this.freq = freq;
	}
	
	//Setters and Getters
	public HufNode(int freq) {
		this.freq = freq;
	}
	
	public char getCh() {
		return ch;
	}

	public void setCh(char ch) {
		this.ch = ch;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public HufNode getLeft() {
		return left;
	}

	public void setLeft(HufNode left) {
		this.left = left;
	}

	public HufNode getRight() {
		return right;
	}

	public void setRight(HufNode right) {
		this.right = right;
	}

	public HufNode getParent() {
		return parent;
	}

	public void setParent(HufNode parent) {
		this.parent = parent;
	}
	
	//Compare Nodes using their frequency
	@Override
	public int compareTo(HufNode node2) {
		if (this.freq>node2.getFreq()) {
			return 1;
		} else if (this.freq<node2.getFreq()) {
			return -1;
		} else { 
			return 0;
		}
	}
	//toString overriden method
	@Override
	public String toString() {
		return "Character: "+ch+", Frequency: "+freq;
	}
}
