package Database.SingleUserDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ij.ImagePlus;
import logs.Logs;
import miscellaneous.CSVReader;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;

public class JEXReader {
	
	public synchronized static ArrayList<ArrayList<String>> readCSVFileToDataMap(File f)
	{
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		try
		{
			CSVReader reader = new CSVReader(new FileReader(f));
			while (!reader.isEOF())
			{
				ArrayList<String> fields = new ArrayList<String>();
				reader.readFields(fields);
				ret.add(fields);
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public synchronized static <T extends RealType<T>> Img<T> getSingleImage(String path)
	{
		Logs.log("Opening image - " + path, JEXReader.class);
		ImagePlus im = new ImagePlus(path);
		return ImageJFunctions.wrapReal(im);
		//		if(path == null)
		//		{
		//			return null;
		//		}
		//		
		//		ImgOpener imgOpener = new ImgOpener(IJ2PluginUtility.ij().getContext());
		//
		//		// Always get the first image because that is how JEX stores images.
		//		Img<T> img;
		//		try
		//		{
		//			img = (Img<T>) imgOpener.openImgs(path).get(0);
		//			return img;
		//		} catch(ImgIOException e)
		//		{
		//			JEXDialog.messageDialog("Couldn't open file as image. Check input images. File = " + path );
		//			e.printStackTrace();
		//			return null;
		//		}		
	}
	
}
