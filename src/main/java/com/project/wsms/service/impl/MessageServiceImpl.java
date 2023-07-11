package com.project.wsms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Message;
import com.project.wsms.repository.MessageRepository;
import com.project.wsms.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService   {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getBySender(Integer id) {
        return messageRepository.findBySender_id(id);
    }

    @Override
    public List<Message> getByRecipient(Integer id) {
        return messageRepository.findByRecipient_id(id);
    }

    @Override
    public void save(Message mess) {
        messageRepository.save(mess);
    }

    @Override
    public void delete(Integer id) {
        messageRepository.deleteById(id);
    }
    
}
