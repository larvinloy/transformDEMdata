package chiminey.connector3drac.transformdata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class CreateImage 
{
	private void createImage(String fileName, String type) 
	{
		switch(type)
		{
		case "l":
				createImageFromLuminance(fileName);
				break;
		case "r":
				createImageFromRGB(fileName, type);
				break;
		case "g":
				createImageFromRGB(fileName, type);
				break;
		case "b":
				createImageFromRGB(fileName, type);
				break;
		case "dem":
			createImageFromDEM(fileName);
			break;
		}
		
			
			
	}
	
	public void createImageFromRGB(String fileName, String type)
	{
		Scanner reader;
		String[] chunks;
		Color[][] image; 
		ArrayList<Integer> rgbValues = new ArrayList<Integer>();
		int valueCount = 0;
		int width,height;
		try 
		{
			
			reader = new Scanner(new File(fileName));
			String row;
			Pattern pattern = Pattern.compile("T(-?[0-9]*)");
			
			while(reader.hasNextLine())
			{
				row = reader.nextLine();
				
				chunks = row.split("\\t");
				if(valueCount == 0)
					valueCount = chunks.length -1;
				if(chunks.length > 0)
				{
					
					for(int i = 1; i< (chunks.length); i++)
					{
						String chunk = chunks[i];
						Matcher matcher = pattern.matcher(chunk);
						int number = Integer.parseInt(matcher.replaceAll(""));
						rgbValues.add(number);						
					}
					
				}
			}
			reader.close();
			width = valueCount;
			height = rgbValues.size()/valueCount;
			image = new Color[height][width];
			System.out.println(rgbValues.size());
			System.out.println(image.length);
			System.out.println(image[0].length);
		
			int c = 0;
			int mag;
			for(int i = 0; i < image.length; i++)
			{
				for(int j = 0; j < image[i].length; j++)
				{
					
					switch(type)
					{
						case "r":
							mag = rgbValues.get(c);
							image[i][j] = new Color(mag, 0, 0);
							c++;
							break;
						case "g":
							mag = rgbValues.get(c);
							image[i][j] = new Color(0, mag, 0);
							c++;
							break;
						case "b":
							mag = rgbValues.get(c);
							image[i][j] = new Color(0, 0, mag);
							c++;
							break;
						
					}	
				}
			}
			writeImage(image, fileName);
			
		}
		catch(FileNotFoundException e1)
		{
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void writeImage(Color[][] image, String fileName)
	{
		BufferedImage bufferedImage = new BufferedImage(image[0].length,image.length ,
		        BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < image.length; i++) 
		{
		    for (int j = 0; j < image[0].length; j++) 
		    {
		        bufferedImage.setRGB(j, i, image[i][j].getRGB());
		    }
		}
		File outputfile = new File(fileName.concat(".png"));
        try 
        {
			ImageIO.write(bufferedImage, "png", outputfile);
		} 
        catch (IOException e)
        {
			e.printStackTrace();
		}
	}
	public void createImageFromLuminance(String fileName)
	{
		Scanner reader;
		String[] chunks;
		Color[][] image; 
		ArrayList<Float> lumaValues = new ArrayList<Float>();
		int valueCount = 0;
		int width,height;
		try 
		{
			
			reader = new Scanner(new File(fileName));
			String row;
			Pattern lumaPattern = Pattern.compile("[-]?1|(([0]*[.])?[0-9]+)");

			while(reader.hasNextLine())
			{
				row = reader.nextLine();
				chunks = row.split("\\t");
				if(valueCount == 0)
					valueCount = chunks.length -1;
				if(chunks.length > 0)
				{
					
					for(int i = 1; i< (chunks.length); i++)
					{
						String chunk = chunks[i];
						Matcher matcher = lumaPattern.matcher(chunk);
						float number = Float.parseFloat(chunk);
						lumaValues.add(number);		
					}
					
				}
			}
			reader.close();
			width = valueCount;
			height = lumaValues.size()/valueCount;
			image = new Color[height][width];
			System.out.println(lumaValues.size());
			System.out.println(image.length);
			System.out.println(image[0].length);
			
			int c = 0;
			for(int i = 0; i < image.length; i++)
			{
				for(int j = 0; j < image[i].length; j++)
				{					
					float x = lumaValues.get(c);
					int y = (int) (x * 255);
					image[i][j] = new Color(y, y, y);
					c++;
					
				}
			}
			writeImage(image, fileName);
		}
		catch(FileNotFoundException e1)
		{
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void createImageFromDEM(String fileName)
	{
		Scanner reader;
		String[] chunks;
		Color[][] image; 
		float maxValue = 0;
		float minValue = Float.MAX_VALUE;
		ArrayList<Float> demValues = new ArrayList<Float>();
		
		int valueCount = 0;
		int width,height;
		try 
		{
			
			reader = new Scanner(new File(fileName));
			String row;
			Pattern lumaPattern = Pattern.compile("[+/-] [0-9]*.[0-9]+");

			while(reader.hasNextLine())
			{
				row = reader.nextLine();
				chunks = row.split("\\t");
				if(valueCount == 0)
					valueCount = chunks.length -1;
				if(chunks.length > 0)
				{
					
					for(int i = 1; i< (chunks.length); i++)
					{
						String chunk = chunks[i];
						Matcher matcher = lumaPattern.matcher(chunk);
						float number = Float.parseFloat(chunk);
						demValues.add(number);	
						if(maxValue < number)
							maxValue = number;
						if(minValue > number && number != -9999)
							minValue = number;
					}
					
				}
			}
			reader.close();
			System.out.println(maxValue);
			System.out.println(minValue);
			width = valueCount;
			height = demValues.size()/valueCount;
			image = new Color[height][width];
			System.out.println(demValues.size());
			System.out.println(image.length);
			System.out.println(image[0].length);
			
			int c = 0;
			for(int i = 0; i < image.length; i++)
			{
				for(int j = 0; j < image[i].length; j++)
				{					
					float x = demValues.get(c);
					if(x == -9999)
					{
						image[i][j] = new Color(255, 0, 0);
					}
					else
					{
						float z = (((x - minValue)/ (maxValue - minValue)) * (255 - 0) + 0);
						int y = (int) z;
//						int y = (int) ((x/maxValue) * 255);
						image[i][j] = new Color(y, y, y);
					}
					c++;
					
				}
			}
			writeImage(image, fileName);
		}
		catch(FileNotFoundException e1)
		{
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		CreateImage app = new CreateImage();
		app.createImage(args[0], args[1]);
	}
}
