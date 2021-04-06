//Omar Dalal 1180171 Section 2

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//Class that contains methods used in encoding a file
public class Encode {
	
	//Create a HufNodes array from a file
	public static HufNode[] generateNodesFromFile(String dir) {
		try {
			FileInputStream fis = new FileInputStream(dir);
			BufferedInputStream bis = new BufferedInputStream(fis, 8);
			int i;
			int[] freq = new int[256];
			int nonZeroCount = 0;
			while ((i = bis.read())!=-1) {
				freq[i]++;
				if (freq[i]==1) {
					nonZeroCount++;
				}
			}
			fis.close();
			bis.close();
			HufNode[] nodes = createNodes(freq, nonZeroCount);
			return nodes;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	//Private method used in creating nodes
	private static HufNode[] createNodes(int[] freq, int nonZeroCount) {
		HufNode[] nodes = new HufNode[nonZeroCount];
		int nodesCount = 0;
		for (int j=0; j<freq.length; j++) {
			if (freq[j]!=0) {
				nodes[nodesCount++] = new HufNode((char)j, freq[j]);
			}
		}
		return nodes;
	}
	
	//Method used to read file and write encoded file at the same time
	public static void writeEncodedFile(String outDir, String inDir, String header, EncodedChar[] codes) {
		try {
			FileInputStream fis = new FileInputStream(inDir);
			BufferedInputStream bis = new BufferedInputStream(fis, 8);
			FileOutputStream fos = new FileOutputStream(outDir);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 8);
			int headerC = 0;
			while (headerC<header.length()) {
				bos.write((int)header.charAt(headerC++));
			}
			int a;
			String currentByte = "";
			while ((a = bis.read())!=-1) {
				String chCode = AuxMethods.findCode(a, codes);
				if (chCode=="-1") {
					System.out.println("BIT_VALUE_CANNOT_BE_RESOLVED");
					break;
				}
				currentByte+=chCode;
				if (currentByte.length()>=8) {
					byte b = AuxMethods.byteFromStr(currentByte.substring(0, 8));
					bos.write(b);
					if (currentByte.length()>8) {
						currentByte = currentByte.substring(8);
					} else {
						currentByte="";
					}
				}
				
			}
			if (currentByte.length()>0) {
				byte b = 0b0;
				for (int i=7; i>7-currentByte.length(); i--) {
					b = AuxMethods.setBit(b, currentByte.charAt(7-i), i);
				}
				bos.write(b);
			}
			bis.close();
			bos.close();
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			new ErrorAlert(e.getMessage());
		} catch (IOException e) {
			new ErrorAlert(e.getMessage());
		}
	 }
	 
	//Method used to build a heap (hufftree) from an array of nodes
	 public static HufNode buildHufTree(HufNode[] nodes) {
		if (nodes.length==1) {
			HufNode node = new HufNode(nodes[0].getFreq());
			node.setLeft(new HufNode(nodes[0].getCh(), nodes[0].getFreq()));
			return node;
		}
		Heap heap = new Heap(nodes.length);
		for (int i=0; i<nodes.length; i++) {
			heap.insert(nodes[i]);
		}
		HufNode rootNode = null;
        while (heap.getCount()>2) {
        	HufNode left = heap.remove();
        	HufNode right = heap.remove();
        	
        	rootNode = new HufNode(left.getFreq()+right.getFreq());
        	rootNode.setLeft(left);
        	rootNode.setRight(right);
        	
        	heap.insert(rootNode);
        }
        return rootNode;
	 }
}
