package com.sunilos.common.mail;

import java.io.File;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.sunilos.common.UserContext;
import com.sunilos.common.attachment.AttachmentDTO;
import com.sunilos.common.attachment.AttachmentServiceInt;
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

	/**
	 * Get messages from database
	 */
	@Autowired
	public MessageServiceInt messageService;

	/**
	 * Get attached filed by ids
	 */
	@Autowired
	private AttachmentServiceInt attachmentService;

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
				AttachmentDTO fileDto = attachmentService.findById(id, ctx);
				if (fileDto != null) {
					ByteArrayResource file = new ByteArrayResource(fileDto.getDoc());
					helper.addAttachment(fileDto.getName(), file);
				}
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			public void run() {
				emailSender.send(message);
			}
		}).start();

	}
}
