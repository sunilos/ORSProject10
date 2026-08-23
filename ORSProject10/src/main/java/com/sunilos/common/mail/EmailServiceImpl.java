package com.sunilos.common.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.sunilos.common.UserContext;
import com.sunilos.dto.DocumentDTO;
import com.sunilos.service.DocumentServiceInt;
import com.sunilos.common.message.MessageDTO;
import com.sunilos.common.message.MessageServiceInt;

/**
 * Provides email services
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS*
 */
@Component
public class EmailServiceImpl {

	/**
	 * Send email
	 */
	@Autowired
	public JavaMailSender emailSender;

	@Value("${document.upload-dir}")
	String uploadDir;

	/**
	 * Get messages from database
	 */
	@Autowired
	public MessageServiceInt messageService;

	/**
	 * Get attached filed by ids
	 */
	@Autowired
	private DocumentServiceInt documentService;

	/**
	 * Sends an email
	 * 
	 * @param dto
	 * @param ctx
	 */
	public void send(EmailDTO dto, UserContext ctx) {

		// Get message from database if message code is not null
		if (dto.getMessageCode() != null) {
			MessageDTO messageDTO = messageService.findByCode(dto.getMessageCode(), ctx);

			// If message does not exist or message is active then return
			if (messageDTO == null || "Inactive".equals(messageDTO.getStatus())) {
				return;
			}

			dto.setSubject(messageDTO.getSubject(dto.getMessageParams()));
			dto.setBody(messageDTO.getBody(dto.getMessageParams()));
			dto.setIsHTML("Y".equals(messageDTO.getHtml()));

		}

		System.out.println(dto);

		MimeMessage message = emailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if (dto.getTo().size() > 0) {
				helper.setTo(dto.getTo().toArray(new String[dto.getTo().size()]));
			}

			if (dto.getCc().size() > 0) {
				helper.setCc(dto.getCc().toArray(new String[dto.getCc().size()]));
			}

			if (dto.getBcc().size() > 0) {
				helper.setBcc(dto.getBcc().toArray(new String[dto.getBcc().size()]));
			}

			helper.setSubject(dto.getSubject());

			helper.setText(dto.getBody(), dto.getIsHTML());

			// Attach files from file system path
			Iterator<String> it = dto.getAttachedFilePath().iterator();
			while (it.hasNext()) {
				FileSystemResource file = new FileSystemResource(new File(it.next()));
				helper.addAttachment(file.getFilename(), file);
			}

			// Attached files from database
			Iterator<Long> itid = dto.getAttachedFileId().iterator();
			while (itid.hasNext()) {
				Long id = itid.next();
				DocumentDTO fileDto = documentService.findById(id, ctx);
				if (fileDto != null) {
					File sourceFile = new File(uploadDir, fileDto.getName());
					ByteArrayResource file = new ByteArrayResource(Files.readAllBytes(sourceFile.toPath()));
					helper.addAttachment(fileDto.getName(), file);
				}
			}

		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			public void run() {
				emailSender.send(message);
			}
		}).start();

	}
}
