//MinHeap Class (Huff Tree)
public class Heap {
	
	//Attributes
	private HufNode[] heap;
	private int count;
	private int maxCount;
	private final int ROOT = 1;
	
	//Constructor
	public Heap(int maxCount) {
		this.heap = new HufNode[maxCount+1];
		this.count = 1;
		this.maxCount = maxCount+1;
	}
	
	//Insert new node an sort it according to its frequency
	public void insert(HufNode node) {
		if (count==ROOT) {
			heap[ROOT] = node;
			count++;
		} else if (count<maxCount) {
			heap[count] = node;
			int c = count;
			while (heap[c].compareTo(heap[p(c)])<0) {
				swapNodes(c, p(c));
				if (p(c)!=ROOT) {
					c = p(c);
				}
			}
			count++;
		}
	}
	
	//remove the root node
	public HufNode remove() {
		if (count>=1) {
			HufNode rootNode = swapNodes(ROOT, count-1);
			count--;
			fixPos(ROOT);
			return rootNode;
		}
		return null;
	}
	
	//Get the current count of the tree (number of nodes)
	public int getCount() {
		return this.count;
	}
	
	//Check if node is a leaf
	private boolean isLeaf(int index) {
		if (index<=count&&index>=count/2) {
			return true;
		} else {
			return false;
		}
	}
	
	//Get parent of index
	private int p(int index) {
		return index/2;
	}
	
	//Get left node of a parent
	private int l(int index) {
		return index*2;
	}
	
	//Get right node of a parent
	private int r(int index) {
		return index*2 + 1;
	}
	
	//Method used to fix the position of node and keep the minheap property
	private void fixPos(int index) {
		if (!isLeaf(index)) {
			if (heap[index].compareTo(heap[l(index)])>=0||heap[index].compareTo(heap[r(index)])>=0) {
				if (heap[r(index)].compareTo(heap[l(index)])>=0) {
					swapNodes(index, l(index));
					if (l(index)!=ROOT) {
						fixPos(l(index));
					}
				} else if (heap[l(index)].compareTo(heap[r(index)])>=0) {
					swapNodes(index, r(index));
					if (r(index)!=ROOT) {
						fixPos(r(index));
					}
				}
			}
		}
		if ((count-1)==2) {
			if (heap[ROOT].compareTo(heap[ROOT+1])>=0) {
				swapNodes(ROOT, ROOT+1);
			}
		}
	}
	
	//Private method used to swap the positions of two given nodes
	private HufNode swapNodes(int i1, int i2) {
		HufNode tmp = heap[i1];
		heap[i1] =  heap[i2];
		heap[i2] = tmp;
		return tmp;
	}
}
