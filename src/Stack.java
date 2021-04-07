//Stack Class used to read postFix from an encoded file
public class Stack {

	//Attributes
	private HufNode[] arr;
	private int maxLength;
	private int count = 0;
	
	//Constructor
	public Stack(int maxLength) {
		arr = new HufNode[maxLength];
		this.maxLength = maxLength;
		for (int i=0; i<arr.length; i++) {
			arr[i] = new HufNode(0);
		}
	}
	
	//Add element
	public void push(HufNode n) {
		if (count<maxLength) {
			arr[count++] = n;
		} else {
			System.out.println("STACK_FULL");
		}
	}
	
	//Remove element
	public HufNode pop() {
		if (count>0) {
			HufNode n = arr[count-1];
			arr[count-1] = new HufNode(0);
			count--;
			return n;
		} else {
			System.out.println("STACK_EMPTY");
			return null;
		}
	}
	
	//Get the value of a given element
	public HufNode get(int index) {
		if (index<=count) {
			return arr[index];
		}
		return null;
	}
	
	//return the number of items in stack
	public int length() {
		return count;
	}
}
