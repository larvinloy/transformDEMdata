package chiminey.connector3drac.transformdata;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageToRGBInput 
{

	public static void main(String[] args) 
	{
		try 
		{
			//Select image
			File file = new File(args[0]);

			BufferedImage image = ImageIO.read(file);
		    int pixels[][] = convertTo2DUsingGetRGB(image);
		    //write to txt
		    FileWriter writer = new FileWriter(new File(args[1]));
		    int counter = 0;
			for (int j = 0; j < image.getHeight(); j++) 
			{
				System.out.print(counter+",\t");
//		    	writer.write(String.valueOf(counter++)+",\t");
			    for (int k = 0; k < image.getWidth(); k++) 
			    {
			        System.out.print((pixels[j][k]) + "\t");
			        writer.write((pixels[j][k])+ "\t");
			    }
			    System.out.println("");
			    writer.write("\n");
			}
			writer.close();
//			System.out.println("Counter = " + counter);
		} 
		catch (IOException e) 
		{
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
		
	}
	
	private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

	      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

	      int[][] result = new int[height][width];
	      if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      } else {
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      }

	      return result;
	   }
	
	private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
	      int width = image.getWidth();
	      int height = image.getHeight();
	      int[][] result = new int[height][width];

	      for (int row = 0; row < height; row++) {
	         for (int col = 0; col < width; col++) {
	            result[row][col] = image.getRGB(col, row);
	         }
	      }

	      return result;
	   }


}
