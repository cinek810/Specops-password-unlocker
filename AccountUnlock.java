package accountUnlocker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.sync.BasicResponseHandler;
import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.sync.methods.HttpGet;
import org.apache.hc.client5.http.sync.methods.HttpPost;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.ResponseHandler;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class accountUnlocker {
	static Element viewState;
	static Element eventValidation;
	static CloseableHttpClient client;
	static ResponseHandler<String> handler=new BasicResponseHandler();
	static final String pageURL="https://company-webpage/reset/Reset.aspx";
	public static void showViewState() {
		System.out.println(viewState.val());
	}
	public static void getFirstPage() throws IOException, HttpException 
	{
		HttpGet getPage = new HttpGet(pageURL);
		getPage.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:55.0) Gecko/20100101 Firefox/55.0");
		getPage.addHeader("Host","pwreset.company.com");
		getPage.addHeader("Accept-Encoding","gzip, deflate, br");
		getPage.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		
		CloseableHttpResponse response=client.execute(getPage);
	
		System.out.println(response);
		String body=handler.handleResponse(response);
		Document doc=Jsoup.parse(body);
		viewState=doc.getElementById("__VIEWSTATE");
		eventValidation=doc.getElementById("__EVENTVALIDATION");
		
	}
	public static String sendReply(String myKey, String value,String button) throws HttpException, IOException
	{
		HttpPost answerQuestion=new HttpPost(pageURL);
		answerQuestion.addHeader("Accept-Encoding","gzip, deflate");
		List <NameValuePair> nvps3 = new ArrayList <NameValuePair>();
		nvps3.add(new BasicNameValuePair("__VIEWSTATE", viewState.val()));
		nvps3.add(new BasicNameValuePair("__EVENTVALIDATION", eventValidation.val()));
		nvps3.add(new BasicNameValuePair("ctl00$ScriptManager1","ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton"));
		nvps3.add(new BasicNameValuePair(myKey,value));
	    nvps3.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton",button));
		answerQuestion.setEntity(new UrlEncodedFormEntity(nvps3));
		CloseableHttpResponse response = null;
		try {
			response = client.execute(answerQuestion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String body=handler.handleResponse(response);
		Document doc=Jsoup.parse(body);
		viewState=doc.getElementById("__VIEWSTATE");
		eventValidation=doc.getElementById("__EVENTVALIDATION");
		
		return body;
	}
	public static String answerQuestion(String ans) throws HttpException, IOException 
	{
		HttpPost answerQuestion=new HttpPost(pageURL);
		answerQuestion.addHeader("Accept-Encoding","gzip, deflate");
		List <NameValuePair> nvps3 = new ArrayList <NameValuePair>();
		nvps3.add(new BasicNameValuePair("__VIEWSTATE", viewState.val()));
		nvps3.add(new BasicNameValuePair("__EVENTVALIDATION", eventValidation.val()));
		nvps3.add(new BasicNameValuePair("ctl00$ScriptManager1","ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton"));
		nvps3.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$ResetWizard$QuestionAnswerTextBox",ans));
	        nvps3.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton","Dalej"));
		answerQuestion.setEntity(new UrlEncodedFormEntity(nvps3));
		CloseableHttpResponse response = null;
		try {
			response = client.execute(answerQuestion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String body=handler.handleResponse(response);
		Document doc=Jsoup.parse(body);
		viewState=doc.getElementById("__VIEWSTATE");
		eventValidation=doc.getElementById("__EVENTVALIDATION");
w
		return body;
	}
	
	public static void main(String[] args)
	{
		client = HttpClients.createDefault();
		CloseableHttpResponse response1;
		try {
			getFirstPage();
				
			System.out.println(viewState.val());
			System.out.println(eventValidation.val());
		
			HttpPost zeroQuestion = new HttpPost("https://pwreset.company.com/specopspassword/reset/Reset.aspx");
			zeroQuestion.addHeader("Accept-Encoding","gzip, deflate, br");
 			
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$ResetWizard$UserNameTextBox", "NETID"));
			nvps.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$ResetWizard$LogonDomainDropList","DOMAIN"));
			nvps.add(new BasicNameValuePair("__VIEWSTATE", viewState.val()));
			nvps.add(new BasicNameValuePair("__EVENTVALIDATION",eventValidation.val()));
        		nvps.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$ResetWizard$StartNavigationTemplateContainerID$StartNextButton","Dalej"));
			
			zeroQuestion.setEntity(new UrlEncodedFormEntity(nvps));
			response1 = client.execute(zeroQuestion); 
			String body = handler.handleResponse(response1);
			
			Document doc=Jsoup.parse(body);
			viewState=doc.getElementById("__VIEWSTATE");
			eventValidation=doc.getElementById("__EVENTVALIDATION");

			answerQuestion("XXX");
		    	answerQuestion("XXX");
			String unlockPage=answerQuestion("XXX");
			showViewState();
			
			//System.out.println(unlockPage);
			Document  htmlUnlockPage=Jsoup.parse(unlockPage);
			Element unlockRadio=htmlUnlockPage.getElementById("ContentPlaceHolder1_ResetWizard_UnlockMethodList_1");
			
			try {
				System.out.println(unlockRadio.tag());
				if(unlockRadio.val() == "2")
				{
					System.out.println("unlock");
					sendReply("ctl00$ScriptManager1","ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton&ctl00$ContentPlaceHolder1$ResetWizard$UnlockMethodList=2","Dalej");
					sendReply("ctl00$ScriptManager1","ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$FinishNavigationTemplateContainerID$FinishButton","Zakończ");
				}
			}
			catch(NullPointerException e) {
				System.out.println("Account is not locked");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
