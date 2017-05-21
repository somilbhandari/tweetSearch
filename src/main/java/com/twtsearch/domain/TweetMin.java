package com.twtsearch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tweet_min")
public class TweetMin
{
	@Id
	@Column(name="id")
	private Long id;

	@Column(name="hash")
	private String hash;
	
	@Column(name="user_name")
	private String userName;

	@Column(name="profile_image_url")
	private String profileImageUrl;
	
	@Column(name="text")
	private String text;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getHash()
	{
		return hash;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getProfileImageUrl()
	{
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl)
	{
		this.profileImageUrl = profileImageUrl;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public TweetMin(Long id, String hash, String userName, String profileImageUrl, String text)
	{
		super();
		this.id = id;
		this.hash = hash;
		this.userName = userName;
		this.profileImageUrl = profileImageUrl;
		this.text = text;
	}

	public TweetMin()
	{
		super();
	}


}
