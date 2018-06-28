package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class KSParser {
	
	public final static String EXPORT_PATH = "D:\\eclipseFellesprosjekt\\export\\";
	
	public static File importKSFile(String file) throws FileNotFoundException{
		if (!file.substring(file.length()-2, file.length()).toLowerCase().equals("ks")){
			return null;
		}
		else if (new File(file.substring(0, file.length()-2) + "sses").exists()) {
				return new File(file.substring(0, file.length()-2) + "sses");
		}
		else{
		ArrayList<TranslateableLine> translateableLineList = new ArrayList<TranslateableLine>();
		File importedFile = new File(file);
		Scanner importScanner = new Scanner(importedFile);
		
		String original_actor = "";
		String original_line = "";
		
		
		boolean foundLine = false;
		
		
		while(importScanner.hasNextLine()) {
			String nextLine = importScanner.nextLine();
			System.out.println(nextLine);
			if(!foundLine){
				if (nextLine.startsWith("*SC")) foundLine = true;
			}
			else {
				if(nextLine.contains("@talk")) {
					if(nextLine.indexOf("name=") != -1) {
						original_actor = nextLine.substring(nextLine.indexOf("name=") + 5);
					}
				}
				else if (!nextLine.startsWith("@") || !nextLine.startsWith("*") || !nextLine.startsWith(";")){
					if(!nextLine.isEmpty()) {
						original_line = nextLine;
						TranslateableLine newLine = new TranslateableLine(original_actor, original_line);
						translateableLineList.add(newLine);
						original_actor = "";
						original_line = "";
						foundLine = false;
					}
				}
			}
		}
		importScanner.close();
		SSESParser.save(file.substring(0, file.length()-2) + "sses", translateableLineList, "ks");
		return new File(file.substring(0, file.length()-2) + "sses");
		}
	}
	
	public static boolean exportKSFile(String ssesFile) throws FileNotFoundException {
		if (!ssesFile.substring(ssesFile.length()-4, ssesFile.length()).toLowerCase().equals("sses")){
			return false;
		}
		else {
			ArrayList<TranslateableLine> linesToExport = SSESParser.load(ssesFile);
			File originalFile = new File(ssesFile.substring(0, ssesFile.length()-4) + SSESParser.getFileType());
			if(!originalFile.exists()) {
				System.out.println(originalFile.getPath());
				System.out.println("hi");
				return false;
			}
			File exportedFile = new File(EXPORT_PATH+originalFile.getName());
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportedFile))){
				
				Scanner exportScanner = new Scanner(originalFile);
				boolean foundLine = false;
				TranslateableLine currentLine = null;
				
				while(exportScanner.hasNextLine()){
					String nextLine = exportScanner.nextLine();
					if (!foundLine) {
						bw.write(nextLine);
						bw.newLine();
						if (nextLine.startsWith("*SC")){
							foundLine = true;
							currentLine = linesToExport.remove(0);
						}
					}
					else {
						if(nextLine.contains("@talk")) {
							if(nextLine.indexOf("name=") != -1 && !currentLine.getTranslatedActor().isEmpty()) {
								bw.write(nextLine.substring(0, nextLine.indexOf("name=") + 5) + currentLine.getTranslatedActor());
								bw.newLine();
								System.out.println("write actor");
							}
							else {
								bw.write(nextLine);
								bw.newLine();
							}
						}
						else if (!nextLine.startsWith("@") || !nextLine.startsWith("*") || !nextLine.startsWith(";")){
							if(!nextLine.isEmpty() && !currentLine.getTranslatedLine().isEmpty()) {
								bw.write(currentLine.getTranslatedLine());
								bw.newLine();
								System.out.println("write translation");
								foundLine = false;
							}
							else {
								bw.write(nextLine);
								bw.newLine();
								foundLine = false;
							}
						}
						else {
							bw.write(nextLine);
							bw.newLine();
						}
					}					
				}
				exportScanner.close();
			    bw.flush();
			    bw.close();
			    System.out.println(".ks Saved!");
			    return true;
			}
			catch (IOException e) {
				e.printStackTrace();  
				System.out.println("Crap happened");
				return false;
			}
			
			
		}
	}
}
