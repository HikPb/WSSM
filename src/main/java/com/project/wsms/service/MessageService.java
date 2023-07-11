package com.project.wsms.service;

import java.util.List;

import com.project.wsms.model.Message;

public interface MessageService{
	
	public List<Message> getBySender(Integer id);

	public List<Message> getByRecipient(Integer id);
			
	public void save(Message mess);

    public void delete(Integer id);
}
