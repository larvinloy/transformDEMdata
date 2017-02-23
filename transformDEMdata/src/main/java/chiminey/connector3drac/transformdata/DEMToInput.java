package chiminey.connector3drac.transformdata;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;


public class DEMToInput 
{

	public void transformDEM(String fileName)
	{
		copyDataFile(fileName);
		int ncols,nrows;
		float cellSize;
		float rowCounter = 0;
		ArrayList<String> header = new ArrayList<String>();
		Scanner reader;
		ArrayList<String> newRows = new ArrayList<String>();
		try 
		{
			
			reader = new Scanner(new File(fileName));
			String row;
			String newRow;
			Pattern pattern = Pattern.compile("[+/-] [0-9]*.[0-9]+");
			for(int i = 0; i < 6; i++)
			{
				row = reader.nextLine();
				String[] chunks = row.split(" ");
				
//				System.out.println(chunks[1]);
				String chunk = chunks[1];
				Matcher matcher = pattern.matcher(chunk);
//				System.out.println(matcher.replaceAll(""));
				header.add(matcher.replaceAll(""));				
			}
			ncols = Integer.valueOf(header.get(0));
			nrows = Integer.valueOf(header.get(0));
			cellSize = Float.parseFloat(header.get(4));
//			System.out.println(nrows + " " + ncols + " " + cellSize);
			while(reader.hasNextLine())
			{
				row = reader.nextLine();
//				System.out.println(row);
				newRow = new String();
				String[] chunks = row.split(" ");
				if(chunks.length > 0)
				{
					newRow += rowCounter + ",\t";
					rowCounter += cellSize;
					for(int i = 0; i< (chunks.length); i++)
					{
						String chunk = chunks[i];
						Matcher matcher = pattern.matcher(chunk);
						float number = Float.parseFloat(matcher.replaceAll(""));						
						newRow += number + "\t";
					}
					newRows.add(newRow);
				}
			}
			reader.close();
			FileWriter writer = new FileWriter(new File(fileName));
			for(String r : newRows)
			{
//				System.out.println(r);
				writer.write(r + "\n");
			}
			writer.close();
		}
		catch(FileNotFoundException e1)
		{
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void copyDataFile(String original)	 
	{
		File source = new File(original);
		File dest = new File(original.concat("_old"));
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			inputChannel.close();
			outputChannel.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) 
	{
		
		DEMToInput app = new DEMToInput();
//		app.shiftBits(app.extractFileName(), app.extractChannel());
//		app.shiftBits(args[0], args[1]);
		app.transformDEM("dem_old");
		
		
	}
	
	
	
}