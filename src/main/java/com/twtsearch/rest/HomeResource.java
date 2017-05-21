package com.twtsearch.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twtsearch.domain.TweetMin;
import com.twtsearch.service.TwitterService;


@RestController
public class HomeResource
{	
	@Inject
	private TwitterService twitterService;
	
	@RequestMapping(value = "/hashsearch/{hashTag}/{maxId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchHash(@PathVariable String hashTag, @PathVariable Long maxId)
	{
		twitterService.searchTweet(hashTag);
		List<TweetMin> tweets = twitterService.getTweetsGreaterThanId(hashTag, maxId);
		return new ResponseEntity<>(tweets,HttpStatus.OK);
	}

}
