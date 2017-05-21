package com.twtsearch.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twtsearch.config.TwitterConfig;
import com.twtsearch.domain.TweetMin;
import com.twtsearch.repository.TweetMinRepository;
import com.twtsearch.util.RESTApiCaller;

@Service
@Transactional
public class TwitterService
{
	@Inject
	private TwitterConfig		twitterConfig;
	
	@Inject
	private TweetMinRepository	tweetMinRepository;
	
	private String				bearerToken;
	
	public void setBearerToken()
	{
		// String bearerCreds = twitterConfig.twitter.getApiKey() + ":" + twitterConfig.twitter.getApiSecret();
		String bearerCreds = "2PMOBbVnVebKiY7fQS69Nn5RM" + ":" + "IZXv1NC2W8ZPmnmrqINIrz46CfUh4gHt2QRmsKERsxiMeCw2Oy";
		
		String encodedCreds = "Basic " + Base64.getEncoder().encodeToString(bearerCreds.getBytes());
		
		// String url = twitterConfig.twitter.getHost();
		String url = "https://api.twitter.com/oauth2/token";
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("grant_type", "client_credentials");
		String params = "grant_type=client_credentials";
		String response = RESTApiCaller.callPostAPI(url, params, encodedCreds.getBytes());
		
		JSONObject json;
		try
		{
			json = new JSONObject(response);
			bearerToken = "Bearer " + json.getString("access_token");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public void searchTweet(String hashTag)
	{
		if (bearerToken == null)
			setBearerToken();
		
		String url = "https://api.twitter.com/1.1/search/tweets.json";
		Map<String, String> params = new HashMap<String, String>();
		params.put("q", "#"+hashTag);
		
		String response = RESTApiCaller.callGetAPI(url, params, bearerToken.getBytes());
		saveNewTweets(response, hashTag);
	}

	public List<TweetMin> getTweetsGreaterThanId(String hashTag, long id)
	{
		return tweetMinRepository.findByHashAndIdGreaterThan(hashTag, id);
	}

	
	public List<TweetMin> getTweets(String hashTag)
	{
		Pageable pageable = new PageRequest(0, 10);
		return tweetMinRepository.findByHash(hashTag, pageable);
	}

	public List<TweetMin> getTweetsLessThanId(String hashTag, long id)
	{
		Pageable pageable = new PageRequest(0, 10);
		return tweetMinRepository.findByHashAndIdLessThan(hashTag, id, pageable);
	}

	private void saveNewTweets(String tweets, String hashTag)
	{
		TweetMin lastTweet = tweetMinRepository.findTop1ByHashOrderByIdDesc(hashTag);
		JSONObject json;
		int i;
		try
		{
			json = new JSONObject(tweets);
			JSONObject searchMetadata = json.getJSONObject("search_metadata");
			List<TweetMin> tmList = new ArrayList<TweetMin>();
			
			if (lastTweet == null || Long.parseLong(searchMetadata.get("max_id").toString()) > lastTweet.getId())
			{
				JSONArray statuses = json.getJSONArray("statuses");
				for (i = 0; i < statuses.length(); i++)
				{
					JSONObject twt = statuses.getJSONObject(i);
					if (lastTweet == null || Long.parseLong(twt.getString("id")) > lastTweet.getId())
					{
						tmList.add(new TweetMin(twt.getLong("id"), hashTag, twt.getJSONObject("user").getString("name"),
								twt.getJSONObject("user").getString("profile_image_url"), twt.getString("text")));
						
					}
				}
				tweetMinRepository.save(tmList);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}
}
