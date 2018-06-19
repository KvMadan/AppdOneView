/**
 * 
 */
package com.km.appd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

/**
 * @author Madan Kavarthapu
 *
 */
public class AppdRestProxy {
	
	final static Logger log = Logger.getLogger(AppdRestProxy.class);
	
	protected static final String BASE_APPD_API_URL = "https://%s/controller/rest/applications/%s/metric-data?metric-path=%s&output=json&rollup=false&time-range-type=BEFORE_NOW&duration-in-mins=1440";
	protected static final String CONTENT_TYPE_XML = "application/json";
	protected static final List<Integer> validStatusCodes = Arrays
			.asList(new Integer[] { Integer.valueOf(200), Integer.valueOf(201),
					Integer.valueOf(202), Integer.valueOf(204) });
	
	private String serverName;
	private String application;
	private String username;
	private String password;
	private String baseURL;
	
	private HttpClient client;
	private HttpContext context;
	private CookieStore cookieStore;

	@SuppressWarnings("deprecation")
	public AppdRestProxy(String serverName, String application, String username, String password){
			this.serverName = serverName;
			this.application = application;
			this.username = username;
			this.password = password;
			
/*			@SuppressWarnings("deprecation")
			PoolingClientConnectionManager cxMgr = new PoolingClientConnectionManager(
					SchemeRegistryFactory.createDefault());
			cxMgr.setMaxTotal(2);
			cxMgr.setDefaultMaxPerRoute(2);

			this.client = new DefaultHttpClient(cxMgr);*/
			this.client = new DefaultHttpClient();
			this.context = new BasicHttpContext();
			this.cookieStore = new BasicCookieStore();
			this.context.setAttribute("http.cookie-store", this.cookieStore);
	}
	
	public String getData() throws Exception{
		
		this.baseURL = String.format(
				BASE_APPD_API_URL, new Object[] {
						serverName, application, "Business%20Transaction%20Performance%7CBusiness%20Transactions%7CPLAB_MS_payment_search%7C%2FgetPaymentSearchResults%7CAverage%20Response%20Time%20%28ms%29" });
		
        HttpHost target = new HttpHost("genproxy.amdocs.com", 8080, "https");
        HttpHost proxy = new HttpHost("genproxy.amdocs.com", 8080, "https");

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
               .build();
		
		HttpGet getRequest = new HttpGet(baseURL);
/*		String userNameAndPassword = username + ":" + password;
		String encodedCredentials = Base64Encoder.encode(userNameAndPassword.getBytes());
		getRequest.addHeader("Authorization",
				String.format("Basic %s", new Object[] { encodedCredentials }));*/
		getRequest.addHeader("Authorization",
				String.format("Basic %s", new Object[] { "YmFsYWNoYXJAYW1kLW1wY3M6QkFMQUNIQVI=" }));
		getRequest.setConfig(config);
		HttpResponse response = executeRequest(getRequest);
		//OutputStream out = new FileOutputStream(localFilePath);
		InputStream in = response.getEntity().getContent();
		
		String data = IOUtils.toString(in);
		//IOUtils.copy(in, out);
		//IOUtils.closeQuietly(in);
		//IOUtils.closeQuietly(out);
		return data;

	}
	
	protected HttpResponse executeRequest(HttpRequestBase request)
			throws IOException {
		System.out.println(request.getURI().toURL());
		HttpResponse response = this.client.execute(request, this.context);
		if (!isOk(response)) {
			log.debug(response.getStatusLine());
		}
		return response;
	}
	
	public static boolean isOk(HttpResponse response) {
		System.out.println(response.getStatusLine().getStatusCode());
		return validStatusCodes.contains(Integer.valueOf(response
				.getStatusLine().getStatusCode()));
	}
}
