package com.twtsearch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;

public class RESTApiCaller
{
	public static String callGetAPI(String apiUrl, Map<String, String> params, byte[] httpAuthenticationBase64)
	{
		try 
		{
			URL url;
			
			url = new URL(apiUrl+paramsToUrl(params));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setReadTimeout(15*1000);
			
			if(httpAuthenticationBase64 != null)
			{
				conn.setRequestProperty("Authorization", new String(httpAuthenticationBase64));
			}
		    
			conn.connect();
			
		    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) 
			{
				return conn.getResponseMessage();
			}	
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			String response;
			while ((response = br.readLine()) != null)
			{
				stringBuilder.append(response).append("\n");
			}
			
			conn.disconnect();
			
			return stringBuilder.toString().trim();
			
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String callPostAPI(String apiUrl, Map<String, String> params, byte[] httpAuthenticationBase64, boolean isBasicAuth)
	{
		return callPostAPI(apiUrl, paramsToJsonString(params), httpAuthenticationBase64);
	}

	public static String callPostAPI(String apiUrl, String params, byte[] httpAuthenticationBase64)
	{
		try
		{
			URL url;
			
			url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			if(httpAuthenticationBase64 != null)
			{
				conn.setRequestProperty("Authorization", new String(httpAuthenticationBase64));
			}
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setReadTimeout(30*1000);
		    conn.connect();
		    
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(params);
		    wr.flush();
		    wr.close();
		    
		    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK && 
		    		conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) 
			{
		    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String response;
				while ((response = br.readLine()) != null)
				{
					stringBuilder.append(response).append("\n");
				}
				
				if(stringBuilder.length() > 0)
					return stringBuilder.toString().trim();
				
				return conn.getResponseMessage();
			}
		    
		    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			String response;
			while ((response = br.readLine()) != null)
			{
				stringBuilder.append(response).append("\n");
			}
			
			conn.disconnect();
			
			return stringBuilder.toString().trim();
		    
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String paramsToJsonString(Map<String, String> params)
	{
		JSONObject json = new JSONObject(params);
		return json.toString();
	}
	
	public static String paramsToUrl(Map<String, String> params)
	{
		if(params == null)
			return "";
		
		StringBuilder urlBuilder = new StringBuilder("?");
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			try 
			{
				urlBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		
		return urlBuilder.toString().replaceFirst(".$", "");
	}
}
