package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SSESParser {
	
	private static String fileType = "";
	
	public static ArrayList<TranslateableLine> load(String file) throws FileNotFoundException{
		if (!file.substring(file.length()-4, file.length()).toLowerCase().equals("sses")){
			return null;
		}else{
		ArrayList<TranslateableLine> translateableLineList = new ArrayList<TranslateableLine>();
		File openedFile = new File (file);
		Scanner s = new Scanner (openedFile);
		boolean typeIsLine = false;
		boolean typeIsHeader = false;
		
		String original_actor = "";
		String translated_actor = "";
		String original_line = "";
		String translated_line = "";
		String line_number = "";
		
		while (s.hasNextLine()) {
			String nextLine = s.nextLine();
			System.out.println(nextLine);
			if(nextLine.startsWith("#")){
				String type = nextLine.substring(1);
				if(type.equals("line")){
					typeIsLine = true;
				}
				else if (type.equals("header")) {
					typeIsHeader = true;
				}
				else if (type.equals("end")){
					typeIsLine = false;
					typeIsHeader = false;
				}
			}
			else if(typeIsHeader) {
				if (nextLine.startsWith("original_file")) fileType = nextLine.substring(nextLine.indexOf("=")+2);
			}
			else if(typeIsLine){
					if (nextLine.startsWith("original_actor")) original_actor = nextLine.substring(17);
					else if (nextLine.startsWith("translated_actor")) translated_actor = nextLine.substring(19);
					else if (nextLine.startsWith("original_line")) original_line = nextLine.substring(16);
					else if (nextLine.startsWith("translated_line")) translated_line = nextLine.substring(18);
					else if (nextLine.startsWith("line_number")) line_number = nextLine.substring(14);
					else {
						TranslateableLine newLine = new TranslateableLine(original_actor, original_line);
						newLine.setTranslatedActor(translated_actor);
						newLine.setTranslatedLine(translated_line);
						newLine.setCurrentLine(Integer.parseInt(line_number));
						translateableLineList.add(newLine);
						original_actor = "";
						translated_actor = "";
						original_line = "";
						translated_line = "";
					}

				}

		}
		s.close();
		return translateableLineList;
		}
	}
	
	public static boolean save(String file, ArrayList<TranslateableLine> lines, String originalFile){
		File saveToFile = new File(file);
		if (!file.substring(file.length()-4, file.length()).toLowerCase().equals("sses")){
			return false;
		}
		else {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveToFile))){
				bw.write("#header");
			    bw.newLine();
			    bw.write("original_file = " + originalFile);
			    bw.newLine();
			    bw.write("total_lines = " + Integer.toString(lines.size()));
			    bw.newLine();
			    bw.newLine();
			    bw.write("#end");
			    bw.newLine();
			    bw.newLine();
			    for(TranslateableLine line : lines) {
			    	bw.write("#line");
				    bw.newLine();
				    bw.write("line_number = " + Integer.toString(line.getCurrentLine()));
				    bw.newLine();
				    bw.write("original_actor = " + line.getOriginalActor());
				    bw.newLine();
				    bw.write("translated_actor = " + line.getTranslatedActor());
				    bw.newLine();
				    bw.write("original_line = " + line.getOriginalLine());
				    bw.newLine();
				    bw.write("translated_line = " + line.getTranslatedLine());
				    bw.newLine();
				    bw.newLine();
				    bw.write("#end");
				    bw.newLine();
			    }
			    bw.flush();
			    bw.close();
			    System.out.println("Saved!");
			    return true;
			}
			catch (IOException e) {
				e.printStackTrace();  
				System.out.println("Crap happened");
				return false;
			}
		}
	}
	
	public static String getFileType() {
		return fileType;
	}
}
