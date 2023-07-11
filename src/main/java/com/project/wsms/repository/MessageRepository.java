package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{
    List<Message> findByRecipient_id(Integer id);
    List<Message> findBySender_id(Integer id);
    List<Message> findByRecipientIdAndRead(Integer id, Boolean read);
    List<Message> findByRecipientIdAndReadOrderByCreatedAtDesc(Integer id, Boolean read);
}
