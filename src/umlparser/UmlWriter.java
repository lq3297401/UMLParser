package umlparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;


public class UmlWriter {
	public void drawPNG(String UMLString, String outPutDir) {
		try {
			String onlinePNGLink = "http://yuml.me/diagram/plain/class/" + UMLString + ".png";
			URL url = new URL(onlinePNGLink);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Accept", "application/json");
			
			if(httpURLConnection.getResponseCode() != 200) {
				throw new RuntimeException( "Got HTTP error code : " + httpURLConnection.getResponseCode());
			}
			
			OutputStream outputStream = new FileOutputStream(new File(outPutDir));
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = httpURLConnection.getInputStream().read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
			httpURLConnection.disconnect();
		} catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void draw(String UMLString, String outPutDir) {
		//UMLString = UMLString.replaceAll ("^[ |\t]*\n$", "").trim();
		System.out.println(UMLString);
		SourceStringReader sourceStringReader = new SourceStringReader(UMLString);
		try {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
		    // Write the first image to "os"
		 	sourceStringReader.generateImage(os, new FileFormatOption(FileFormat.SVG));
		    os.close();
		    FileOutputStream fos = new FileOutputStream(outPutDir);

		    sourceStringReader.generateImage(fos, new FileFormatOption(FileFormat.PNG));
		    fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
}
