//Omar Dalal 1180171 Section 2

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//Class that contains file decoding methods
public class Decode {
	
	//Private Method used to decode remaining bytes that were not decoded by the buffer
	private static String decodeExtraBuffer(String encodedFile, EncodedChar[] codes, HufNode rootNode) {
		String decoded = "";
		int i = 0;
		boolean running = true;
		while (running&&i<encodedFile.length()) {
			boolean charRead = false;
			String charCode = "";
			HufNode root = rootNode;
			int j = i;
			while (!charRead) {
				if (j>=encodedFile.length()) {
					for (int k=0; k<codes.length; k++) {
						if (codes[k].getCode().equals(charCode)) {
							decoded+=codes[k].getCh();
							break;
						}
					}
					running = false;
					break;
				}
				if (encodedFile.charAt(j)=='0') {
					if (root.getLeft()!=null) {
						root = root.getLeft();
						charCode+="0";
						j++;
					} else {
						for (int k=0; k<codes.length; k++) {
							if (codes[k].getCode().equals(charCode)) {
								decoded+=codes[k].getCh();
								charRead=true;
								i+=charCode.length();
								break;
							}
						}
					}
				} else {
					if (root.getRight()!=null) {
						root = root.getRight();
						charCode+="1";
						j++;
					} else {
						for (int k=0; k<codes.length; k++) {
							if (codes[k].getCode().equals(charCode)) {
								decoded+=codes[k].getCh();
								charRead=true;
								i+=charCode.length();
								break;
							}
						}
					}
				}
			}
		}
		return decoded;
	}
	
	//Public method that gets the encoded file and save directory as an input and prints the decoded file to the save directory
	public static void writeDecodedFile(String encodedDir, String saveDir) {
		String[] header = readHeaderFromEncodedFile(encodedDir);
		int charCount = PostFix.getCharCountFromPostFix(header[3]);
		HufNode root = PostFix.buildTreeFromPostFix(header[3], charCount);
		EncodedChar[] codes = PostFix.generateCodes(root, charCount);
		try {
			writeDecoded(saveDir+"."+header[2], encodedDir, Integer.parseInt(header[0]), Integer.parseInt(header[1]), codes, root);
		} catch (NumberFormatException e) {
			new ErrorAlert(e.getMessage());
		}
	}
	
	//Private method to read encoded file and write to decoded file at the same time
	private static void writeDecoded(String saveDir, String encodedDir, int headerLength, int bodyLength, EncodedChar[] codes, HufNode rootNode) {
		try {
			FileInputStream fis = new FileInputStream(encodedDir);
			BufferedInputStream bis = new BufferedInputStream(fis, 8);
			FileOutputStream fos = new FileOutputStream(saveDir);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 8);
			int fVal = 0;
			bis.skip(headerLength);
			int bytesCount=0;
			String currentByte="";
			while ((fVal = bis.read())!=-1&&bytesCount<bodyLength) {
				String byteVAL = Integer.toBinaryString(fVal);
				if (byteVAL.length()<8) {
					int length = byteVAL.length();
					for (int i=0; i<(8-length); i++) {
						byteVAL="0"+byteVAL;
					}
				}
				String charCode = "";
				HufNode root = rootNode;
				int j=0;
				currentByte += byteVAL;
				boolean noMoreChars = false;
				while (!noMoreChars&&bytesCount<bodyLength) {
					if (j>=currentByte.length()) {
						noMoreChars = true;
						break;
					}
					if (currentByte.charAt(j)=='0') {
						if (root.getLeft()!=null) {
							root = root.getLeft();
							charCode+="0";
							j++;
						} else {
							int ch = AuxMethods.findChar(charCode, codes);
							if (ch!=-1) {
								bos.write((char)ch);
								bytesCount++;
								currentByte = currentByte.substring(charCode.length());
								charCode="";
								root = rootNode;
								j=0;
							} else {
								break;
							}
						}
					} else {
						if (root.getRight()!=null) {
							root = root.getRight();
							charCode+="1";
							j++;
						} else {
							int ch = AuxMethods.findChar(charCode, codes);
							if (ch!=-1) {
								bos.write((char)ch);
								bytesCount++;
								currentByte = currentByte.substring(charCode.length());
								charCode="";
								root = rootNode;
								j=0;
							} else {
								break;
							}
						}
					}
				}
			}
			if (bytesCount<bodyLength) {
				String bufferExtra = decodeExtraBuffer(currentByte, codes, rootNode);
				for (int i=0; i<bufferExtra.length(); i++) {
					bos.write(bufferExtra.charAt(i));
				}
			}
			bis.close();
			bos.close();
			fis.close();
			fos.close();
		} catch (FileNotFoundException ex) {
			new ErrorAlert(ex.getMessage());
		} catch (IOException ex) {
			new ErrorAlert(ex.getMessage());
		}
	}
	
	public static boolean checkValid(String dir) {
		try {
			FileInputStream fis = new FileInputStream(dir);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int ch = 0;
			String hL = "";
			while (Character.isDigit((char)(ch=bis.read()))) {
				hL+=(char)ch;
			}
			int hLength = Integer.parseInt(hL);
			
			String bL = "";
			while (Character.isDigit((char)(ch=bis.read()))) {
				bL+=(char)ch;
			}
			Integer.parseInt(hL);
			int k=0;
			String ext = "";
			while ((k = bis.read())!='/') {
				ext+=(char)k;
			}
			byte[] arr = new byte[hLength];
			bis.read(arr, 0, hLength-(hL.length()+bL.length()+ext.length()+3));
			
			bis.close();
			fis.close();
		} catch (FileNotFoundException ex) {
			return false;
		} catch (IOException ex) {
			return false;
		} catch (NumberFormatException ex) {
			return false;
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
		return true;
	}

	//Method to read the header from an encoded file
	public static String[] readHeaderFromEncodedFile(String dir) {
		try {
			FileInputStream fis = new FileInputStream(dir);
			BufferedInputStream bis = new BufferedInputStream(fis, 8);
			
			int ch = 0;
			String hL = "";
			while (Character.isDigit((char)(ch=bis.read()))) {
				hL+=(char)ch;
			}
			int hLength = Integer.parseInt(hL);
			
			String bL = "";
			while (Character.isDigit((char)(ch=bis.read()))) {
				bL+=(char)ch;
			}
			int bLength = Integer.parseInt(bL);
			int k=0;
			String ext = "";
			while ((k = bis.read())!='/') {
				ext+=(char)k;
			}
			byte[] arr = new byte[hLength];
			bis.read(arr, 0, hLength-(hL.length()+bL.length()+ext.length()+3));
			String postFix = "";
			for (int i=0; i<arr.length; i++) {
				postFix+=(char)arr[i];
			}
			String[] header = {""+hLength, ""+bLength, ext, postFix};
			fis.close();
			bis.close();
			return header;
		} catch (FileNotFoundException e) {
			new ErrorAlert(e.getMessage());
		} catch (IOException e) {
			new ErrorAlert(e.getMessage());
		} catch (NumberFormatException e) {
			new ErrorAlert(e.getMessage());
		}
		return null;
	}
}
