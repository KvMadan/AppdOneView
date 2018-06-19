/**
 * 
 */
package com.km.appd;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * @author Madan Kavarthapu
 *
 */
public class AppdJsonSample {

	final static Logger log = Logger.getLogger(AppdJsonSample.class);
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		AppdRestProxy proxy = new AppdRestProxy("amd-mpcs.saas.appdynamics.com", "MICROSERVICES", "balachar@amd-mpcs", "BALACHAR");
		//proxy.getData("C:\\Temp\\metric-data_responseTime_1.json");
		
		Gson gson = new Gson();

		try {
			//Reader reader = new FileReader("C:\\Temp\\metric-data_responseTime_1.json");
			// Convert JSON to Java Object
			//Metrics[] metric = gson.fromJson(reader, Metrics[].class);
			Metrics[] metric = gson.fromJson(proxy.getData(), Metrics[].class);

			for( Metrics m : metric){
				log.info("Metric Path: " + m.getMetricPath());
				log.info("Metric ID: " + m.getMetricId());
				log.info("Metric Name: " + m.getMetricName());
				
				for(MetricValues mv : m.getMetricValues()){
					log.info("Metric Value: " + mv.getValue());
					String ts = mv.getStartTimeInMillis();
					Date date = new Date(Long.parseUnsignedLong(ts));
			        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			        format.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
			        String formatted = format.format(date);
			        log.info("startTimeInMillis: " + formatted);
				}
				log.info("--Done--");
			}
			
			//System.out.println(metric[0].getMetricValues()[0].getStartTimeInMillis());
			//log.info(metric[0].getMetricValues()[0].getStartTimeInMillis());
			//log.info(metric[0].getMetricPath());

			// Convert JSON to JsonElement, and later to String
			/*
			 * JsonElement json = gson.fromJson(reader, JsonElement.class);
			 * String jsonInString = gson.toJson(json);
			 * System.out.println(jsonInString);
			 * 
			 * String[] str = TimeZone.getAvailableIDs();
				for (String s : str)
				System.out.println(s);
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
