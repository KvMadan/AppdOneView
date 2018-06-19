/**
 * 
 */
package com.km.appd;

/**
 * @author Madan Kavarthapu
 *
 */
public class Metrics {
	
	private String metricName;
	private String metricId;
	private String metricPath;
	private String frequency;
	private MetricValues[] metricValues;
	
	public String getMetricName(){
		return metricName;
	}
	
	public String getMetricId(){
		return metricId;
	}

	public String getMetricPath(){
		return metricPath;
	}
	
	public String getFrequency(){
		return frequency;
	}
	
	public MetricValues[] getMetricValues(){
		return metricValues;
	}
	
	public void setMetricName(String metricName){
		this.metricName = metricName;
	}
	
	public void setMetricId(String metricId){
		this.metricId = metricId;
	}

	public void setMetricPath(String metricPath){
		this.metricPath = metricPath;
	}
	
	public void setFrequency(String frequency){
		this.frequency = frequency;
	}
	
	public void setMetricValues(){
		
	}
}
