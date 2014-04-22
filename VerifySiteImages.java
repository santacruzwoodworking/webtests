import com.shipcore.sdk.*;
import java.util.*;
import org.apache.log4j.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.helper.*;
import org.jsoup.Jsoup;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
public class VerifySiteImages implements com.shipcore.sdk.IShipCore{
	
	
	public ArrayList<TestResult> execute(HashMap<String, String> inputs){
		
		ArrayList<TestResult> tResults = new ArrayList<TestResult>();
		ArrayList<String> resultLogs = new ArrayList<String>();
		
		TestResult firstTestResult = null;
		
		
		try{
			
			// get the input from the uer
			String websiteUrl = (String)inputs.get("Test URL");
			
			// append http if not added
			if(!websiteUrl.toLowerCase().startsWith("http")){
				websiteUrl = "http://" + websiteUrl;
			}
				
			// log the endpoint we are about to load		
			resultLogs.add("About to try to load: " + websiteUrl);
			
			// parse the site
			Document doc = Jsoup.connect(websiteUrl).get();
			String title = doc.title();
		
			
			Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
		
			
			// cycle through the imaages and look at the sizes
			for (Element image : images) {
				
				boolean imageLoaded = false;
				String absoluteImageUrl = "";
				try{
		 
					absoluteImageUrl = image.absUrl("src");
					resultLogs.add("About to try to load image: " + absoluteImageUrl);
					URL url = new URL(absoluteImageUrl);
					BufferedImage bi = ImageIO.read(url);
					
					int height = bi.getHeight();
					int width = bi.getWidth();
			
					resultLogs.add(absoluteImageUrl + " loaded, it has a height of " + height + " and a width of " + width);
					imageLoaded = true;
					
				}catch (Exception exe){
			
					exe.printStackTrace();
					resultLogs.add(exe.getMessage());
					StackTraceElement[] stackTrace = exe.getStackTrace();
				
					for(StackTraceElement st : stackTrace){
						resultLogs.add(st.toString());
					}
					
					imageLoaded = false;
				}
				
				
				if(imageLoaded){
					
					firstTestResult = new TestResult(TestResult.PASS, absoluteImageUrl, resultLogs);
					tResults.add(firstTestResult);
					
				}else{
					firstTestResult = new TestResult(TestResult.FAIL, absoluteImageUrl, resultLogs);
					tResults.add(firstTestResult);
					
				}
				
			}
			
			
			
			
					
		}catch (Exception e){
		
			e.printStackTrace();
			resultLogs.add(e.getMessage());
			StackTraceElement[] stackTrace =  e.getStackTrace();
		
			for(StackTraceElement st : stackTrace){
				resultLogs.add(st.toString());
			}
			firstTestResult = new TestResult(TestResult.FAIL, "The test enountered an exception during execution.", resultLogs);
		
		}
		
		tResults.add(firstTestResult);
		return tResults;
		
		
	}
	
	
	public String getDescription(){
		return "Verifies that all of the images from the front page of the website load.";
	}

	
	public ArrayList<TestInput> getInputs(){
		
		ArrayList<TestInput> inputs = new ArrayList<TestInput>();
		
		// we want the user to pass in a String we call "Test URL". So
		// create that input, give it a type and name, and add it to
		// our list. In this case, we don't provide a default value
		// for the input. 
		
		TestInput urlInput = new TestInput(TestInput.TEXT, "Test URL", "http://www.bluedata.com");
		inputs.add(urlInput);
		
		return inputs;
	}
	
}
