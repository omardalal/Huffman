//Omar Dalal 1180171 Section 2

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//Class that contains auxiliary methods that will help in various situations throughout the project
public class AuxMethods {
	
	//Returns the number of digits a number has
	public static int getDigitsCount(int number) {
		int digits = 0;
		while (number/10>0) {
			digits++;
			number/=10;
		}
		digits++;
		return digits;
	}
	
	//Removes the extension from file name
	public static String removeExt(String name) {
		int i = name.length()-1;
		while (name.charAt(i)!='.') {
			i--;
		}
		return name.substring(0, i);
	}
	
	//Get the extension of file from selected directory
	public static String getExtension(String dir) {
		String ext = "";
		int i = dir.length()-1;
		while (dir.charAt(i)!='.') {
			i--;
		}
		i++;
		for (;i<dir.length(); i++) {
			ext+=dir.charAt(i);
		}
		return ext;
	}
	
	//return the code of a given character
	 public static String findCode(int character, EncodedChar[] codes) {
		 char ch = (char) character;
		 for (int i=0; i<codes.length; i++) {
			 if (ch==codes[i].getCh()) {
				 return codes[i].getCode();
			 }
		 }
		 return "-1";
	 }
	 
	 //Return the character of a given code
	 public static int findChar(String code, EncodedChar[] codes) {
		 for (int i=0; i<codes.length; i++) {
			 if (codes[i].getCode().equals(code)) {
				 return codes[i].getCh();
			 }
		 }
		 return -1;
	 }
	 
	 //Find a node using a character
	 public static HufNode findNode(char ch, HufNode[] nodes) {
		 for (int i=0; i<nodes.length; i++) {
			 if (ch==nodes[i].getCh()) {
				 return nodes[i];
			 }
		 }
		 return null;
	 }
	 
	 //Calculate the size of a given header
	 public static int getHeaderSize(String header) {
		 int i=0;
		 String countStr = "";
		 while (Character.isDigit(header.charAt(i))) {
			 countStr+=header.charAt(i);
			 i++;
		 }
		 return Integer.parseInt(countStr);
	 }
	 
	//Get the space taken by a character after encoding it
	//In Bits 
	 public static int getEncodedNodeSize(HufNode node, EncodedChar[] codes) {
		 return findCode((int)node.getCh(), codes).length()*node.getFreq();
	 }
	 
	 //Get the overall size of encoded characters
	 //In Bytes
	 public static int getEncodedSize(EncodedChar[] codes, HufNode[] nodes) {
		 int size = 0;
		 for (int i=0; i<nodes.length; i++) {
			 size+=getEncodedNodeSize(nodes[i], codes);
		 }
		 return size/8;
	 }
	 
	 //Calculate and return file size as a long
	 public static long getFileLength(String dir) {
		 long count = 0;
		 try {
			 FileInputStream fis = new FileInputStream(dir);
			 BufferedInputStream bis = new BufferedInputStream(fis);
			 while (bis.read()!=-1) {
				 count++;
			 }
			 fis.close();
			 bis.close();
		 } catch (FileNotFoundException e) {
			 new ErrorAlert(e.getMessage());
		 } catch (IOException e) {
			 new ErrorAlert(e.getMessage());
		 }
		 return count;
	 }
	 
	 //Calculate file size and return it as a String
	 public static String getFileSize(String dir) {
		 long bytesCount = getFileLength(dir);
		 return formatSize(bytesCount);
	 }
	 
	 //Format size (Input in bytes) and convert it to KB MB GB if necessary
	 public static String formatSize(long size) {
		 float gb = 1000000000;
		 float mb = 1000000;
		 float kb = 1000;
		 String sizeStr = "";
		 if (size>=gb) {
			 sizeStr = String.format("%.2f", (size/gb))+" GB";
		 } else if (size>=mb) {
			 sizeStr = String.format("%.2f", (size/mb))+" MB";
		 } else if (size>=kb) {
			 sizeStr = String.format("%.2f", (size/kb))+" KB";
		 } else {
			 sizeStr = size+" B";
		 }
		 return sizeStr;
	 }
	 
	 //Method used to generate header from post order (postfix)
	 public static String generateHeader(String dir, String postOrder) {
		 String ext = AuxMethods.getExtension(dir);
		 String charLength = ""+AuxMethods.getFileLength(dir);
		 String header = "/"+charLength+"/"+ext+"/"+postOrder;
		 header = (header.length()+AuxMethods.getDigitsCount(header.length()))+header;
		 return header;
	 }
	 
	 //Convert a String that contains a byte (in binary) to an actual byte type
	 public static byte byteFromStr(String str) {
		 try {
			 Integer.parseInt(str);
		 } catch (NumberFormatException ex) {
			 new ErrorAlert(ex.getMessage());
			 return 0b0;
		 }
		 byte b = 0b0;
		 for (int i=str.length()-1; i>=0; i--) {
			 b = AuxMethods.setBit(b, str.charAt(i), str.length()-1-i);
		}
		 return b;
	 }
	 
	 //Set the value of a specific bit in a byte
	 public static byte setBit(byte b, char val, int pos) {
		 byte bS = b;
		 if (val=='0') {
			 bS &= ~(1 << pos); //Set to Zero
		 } else {
			 bS |= 1 << pos; //Set to One
		 }
		 return bS;
	 }
}
