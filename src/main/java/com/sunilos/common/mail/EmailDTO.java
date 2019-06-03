package com.sunilos.common.mail;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains email subjectm body and attached files to be sent
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */
public class EmailDTO {

	private Boolean isHTML = false;

	private ArrayList<String> to = new ArrayList<String>();
	private ArrayList<String> cc = new ArrayList<String>();
	private ArrayList<String> bcc = new ArrayList<String>();

	/**
	 * Subject of email
	 */
	private String subject = null;

	/**
	 * Body of email
	 */
	private String body = null;

	/**
	 * Message code that will be searched in Message Table
	 */
	private String messageCode = null;

	/**
	 * Message parameters to be replaced in message
	 */
	HashMap<String, String> messageParams = null;

	/**
	 * Files paths to be attached
	 */
	private ArrayList<String> attachedFilePath = new ArrayList<String>();

	/**
	 * File IDS to be attached from database
	 */
	private ArrayList<Long> attachedFileId = new ArrayList<Long>();

	public void clear() {
		attachedFileId = new ArrayList<Long>();
		attachedFilePath = new ArrayList<String>();
		to = new ArrayList<String>();
		cc = new ArrayList<String>();
		bcc = new ArrayList<String>();
	}

	public Boolean getIsHTML() {
		return isHTML;
	}

	public void setIsHTML(Boolean isHTML) {
		this.isHTML = isHTML;
	}

	public ArrayList<String> getTo() {
		return to;
	}

	public void setTo(ArrayList<String> to) {
		this.to = to;
	}

	public void addTo(String str) {
		this.to.add(str);
	}

	public ArrayList<String> getCc() {
		return cc;
	}

	public void setCc(ArrayList<String> cc) {
		this.cc = cc;
	}

	public void addCc(String cc) {
		this.cc.add(cc);
	}

	public ArrayList<String> getBcc() {
		return bcc;
	}

	public void setBcc(ArrayList<String> bcc) {
		this.bcc = bcc;
	}

	public void addBcc(String bcc) {
		this.bcc.add(bcc);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public HashMap<String, String> getMessageParams() {
		return messageParams;
	}

	public void setMessageCode(String messageCode, HashMap<String, String> params) {
		this.messageCode = messageCode;
		this.messageParams = params;
	}

	public ArrayList<String> getAttachedFilePath() {
		return attachedFilePath;
	}

	public void setAttachedFilePath(ArrayList<String> attachedFilePaths) {
		this.attachedFilePath = attachedFilePaths;
	}

	public void addAttachedFilePath(String path) {
		this.attachedFilePath.add(path);
	}

	public ArrayList<Long> getAttachedFileId() {
		return attachedFileId;
	}

	public void setAttachedFileId(ArrayList<Long> attachedFileId) {
		this.attachedFileId = attachedFileId;
	}

	public void addAttachedFileId(Long id) {
		this.attachedFileId.add(id);
	}

}
