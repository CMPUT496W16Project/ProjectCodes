import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private static ArrayList<Double> differences;
	
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the old bvh file path : ");
		String originalFilePath=sc.next();
		
		System.out.println("Enter the new bvh file path : ");
		String convertedFilePath=sc.next();
		
		sc.close();
		
		getDifferences(originalFilePath);
		changeZAxisValues(convertedFilePath);
	}
	
	static void changeZAxisValues(String convertedFilePath){
		
		ArrayList<String> file = new ArrayList<String>();
		
		FileReader fileReader;
		BufferedReader bufferedReader;
		
		try{
			fileReader=new FileReader(new File(convertedFilePath));
			bufferedReader=new BufferedReader(fileReader);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
			System.out.println("BVH file not found, exit with error.");
			return;
		}
		
		//
		try {
			
			String line;	
			
			while ((line=bufferedReader.readLine()) != null){
				
				file.add(line);
				
				if (line.contains("Frame Time")){
					break;
				}
			}
			
			int index=0;
			while ((line=bufferedReader.readLine()) != null){
				
				String[] currentFrameArray = line.split("\\s+");
				currentFrameArray[1]=differences.get(index).toString();
				
				String modifiedFrame=String.join(" ",currentFrameArray);
				
				file.add(modifiedFrame);
				
				index++;
			}
			
			fileReader.close();
			bufferedReader.close();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		
		//
		
		PrintWriter printWriter;
		
		try {
			printWriter = new PrintWriter(convertedFilePath,"UTF-8");
			for(int index=0;index<file.size();index++){
				
				printWriter.println(file.get(index));
				
			}
			
			printWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	static void getDifferences(String originalFilePath){
		
		FileReader fileReader;
		BufferedReader bufferedReader;
		
		try{
			fileReader=new FileReader(new File(originalFilePath));
			bufferedReader=new BufferedReader(fileReader);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
			System.out.println("BVH file not found, exit with error.");
			return;
		}
		
		//
		try {
			
			String line;	
			
			while ((line=bufferedReader.readLine()) != null){
					
				if (line.contains("Frame Time")){
					break;
				}
			}
			
			boolean firstFrame=true;
			double referenceVal=0.0;
			
			while ((line=bufferedReader.readLine()) != null){
				
				String[] currentFrameArray = line.split("\\s+");
				
				Double zAxisVal=Double.parseDouble(currentFrameArray[1]);
				
				if(firstFrame){
					referenceVal=zAxisVal;
					differences.add(0.0);
					
					firstFrame=false;
				}
				else{
					differences.add(zAxisVal-referenceVal);
				}
			}
			
			fileReader.close();
			bufferedReader.close();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
}
