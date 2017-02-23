package chiminey.connector3drac.transformdata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * Hello world!
 *
 */
public class Transform 
{
	public void transform(String fileName, String type)
	{
		switch(type)
		{
			case "r":
				transformRGB(fileName, type);
				break;
			case "g":
				transformRGB(fileName, type);
				break;
			case "b":
				transformRGB(fileName, type);
				break;
			case "l":
				transformRGB(fileName, type);
				break;
			case "dem":
				// Keep data as is
				break;
			default:
				// Keeping data as is
		}
	}
	public void transformRGB(String fileName, String type)
	{
		Scanner reader;
		ArrayList<String> newRows = new ArrayList<String>();
		try 
		{
			copyDataFile(fileName);
			reader = new Scanner(new File(fileName));
			String row;
			String newRow;
			String newNumber;
			Pattern pattern = Pattern.compile("T(-?[0-9]*)");
			while(reader.hasNextLine())
			{
				row = reader.nextLine();
				newRow = new String();
				newNumber = new String();
				String[] chunks = row.split("\\t");
				if(chunks.length > 0)
				{
					newRow += chunks[0] + "\t";
					for(int i = 1; i< (chunks.length); i++)
					{
						String chunk = chunks[i];
						Matcher matcher = pattern.matcher(chunk);
						
						int number;
						
						switch(type)
						{
						
							case "r":
								number = Integer.parseInt(matcher.replaceAll(""));
								newNumber = String.valueOf(((number & 0xff0000) >> 16));
								break;
							case "g":
								number = Integer.parseInt(matcher.replaceAll(""));
								newNumber = String.valueOf(((number & 0xff00) >> 8));
								break;
							case "b":
								number = Integer.parseInt(matcher.replaceAll(""));
								newNumber = String.valueOf(((number & 0xff)));
								break;
							case "l":
								number = Integer.parseInt(matcher.replaceAll(""));
								int red   = (number >>> 16) & 0xFF;
						    	int green = (number >>>  8) & 0xFF;
						    	int blue  = (number >>>  0) & 0xFF;
						    	float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
						    	newNumber = String.valueOf(luminance);
						    	break;
							default:
								System.out.println("Bad type!");
								System.exit(0);
								break;
									
						}
						newRow += newNumber + "\t";
					}
					newRows.add(newRow);
				}
			}
			reader.close();
			FileWriter writer = new FileWriter(new File(fileName));
			for(String r : newRows)
			{
				writer.write(r + "\n");
			}
			writer.close();
		}
		catch(FileNotFoundException e1)
		{
			System.out.println("Bad file name");
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
	
	
	
    public static void main( String[] args )
    {
        Transform app = new Transform();
        app.transform(args[0], args[1]);
        
    }
}
