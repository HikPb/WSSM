package com.project.wsms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.exception.NotFoundException;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Message;
import com.project.wsms.payload.request.MessageRequest;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.repository.MessageRepository;
import com.project.wsms.security.OnlineUsers;
import com.project.wsms.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MessageRepository messageRepository;
 
 
    private final OnlineUsers onlineUsers;

    // Mapped as /app/users.online
    @SubscribeMapping("/users.online")
	public MessageRequest retrieveOnlineUsers() {
        return MessageRequest.builder()
        .type("ONLINE")
        .sender("system")
        .message(null)
        .data(onlineUsers.getActiveSessions().values())
        .build(); 
	}

    // Mapped as /app/public
    @MessageMapping("/public")
    @SendTo("/all")
    public MessageRequest send(@Payload MessageRequest message) throws Exception {
        return message;
    }

    // Mapped as /app/private
    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload MessageRequest message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/specific", message);
    }


    @GetMapping("/api/notification")
	@ResponseBody
	public ResponseEntity<ResponseObject> getMessageByUser(HttpServletRequest request) {
        Employee receiver = employeeService.getByUsername(request.getUserPrincipal().getName())
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
        List<Message> listMess = messageRepository.findByRecipientIdAndReadOrderByCreatedAtDesc(receiver.getId(), false);
        return new ResponseEntity<>(
                new ResponseObject("ok", "Get list notification success", listMess), 
                HttpStatus.OK
                );
		
	}

    @GetMapping("/api/notification/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getMessageById(@PathVariable(name = "id") Integer id ,HttpServletRequest request) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Not Found with this id"));
        return new ResponseEntity<>(
            new ResponseObject("ok", "Get a message success", message), 
            HttpStatus.OK
            );
		
	}

    @PostMapping("/api/notification")
	@ResponseBody
	public ResponseEntity<ResponseObject> updateReadStatusMessage(@RequestParam(name = "id") Integer id ,HttpServletRequest request) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Not Found with this id"));
        message.setRead(true);
        messageRepository.save(message);
        return new ResponseEntity<>(
            new ResponseObject("ok", "This notification was readed", ""), 
            HttpStatus.OK
            );
		
	}
}
