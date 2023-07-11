package com.project.wsms.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OnlineUsers {
    private Map<String, String> activeSessions = new ConcurrentHashMap<>();

	public void add(String sessionId, String username) {
		activeSessions.put(sessionId, username);
	}

	public String getUsername(String sessionId) {
		return activeSessions.get(sessionId);
	}

	public void removeParticipant(String sessionId) {
		activeSessions.remove(sessionId);
	}

	public Map<String, String> getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(Map<String, String> activeSessions) {
		this.activeSessions = activeSessions;
	}
}
