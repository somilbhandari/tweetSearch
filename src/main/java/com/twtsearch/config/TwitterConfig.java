package com.twtsearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties("twitter")
public class TwitterConfig
{
	@NestedConfigurationProperty
	public TC twitter;

	public static class TC
	{
		public String getHost()
		{
			return host;
		}
		public void setHost(String host)
		{
			this.host = host;
		}
		public String getApiKey()
		{
			return apiKey;
		}
		public void setApiKey(String apiKey)
		{
			this.apiKey = apiKey;
		}
		public String getApiSecret()
		{
			return apiSecret;
		}
		public void setApiSecret(String apiSecret)
		{
			this.apiSecret = apiSecret;
		}

		private String host;
		private String apiKey;
		private String apiSecret;
		
	}
}