package com.project.wsms.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message; 
    private Boolean read;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "emp-mess-sender")
	@JsonIgnoreProperties(value = {
		"roles", "password", "phone", "hibernateLazyInitializer"})
    private Employee sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "emp-mess-recipient")
	@JsonIgnoreProperties(value = {
		"roles", "password", "phone", "hibernateLazyInitializer"})   
    private Employee recipient;
}
