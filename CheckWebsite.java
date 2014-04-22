import com.shipcore.sdk.*;
import java.util.*;
import org.apache.log4j.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.helper.*;
import org.jsoup.Jsoup;

public class CheckWebsite implements com.shipcore.sdk.IShipCore{
	
	
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
		
			// log the title
			resultLogs.add("The title of the website is: " + title);
			
			// assert on the title
			if(!title.equals("BlueData inc")){
				resultLogs.add("The title was not as expected, it was: " + title);
				firstTestResult = new TestResult(TestResult.FAIL, "The website loaded, but the title was not what we expected", resultLogs);
			}else{
				firstTestResult = new TestResult(TestResult.PASS, "The website loaded normally.", resultLogs);
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
		return "A simple sanity check of the BlueData corporate website.";
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
