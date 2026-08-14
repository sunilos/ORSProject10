package com.sunilos.common.attachment;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Contains attached file information and data
 * 
 * @author Sunil Sahu
 * @Copyright (c) SunilOS Infotech Pvt Ltd
 */

@Entity
@Table(name = "NCS_ATTACHMENT")
public class AttachmentSummaryDTO extends AttachmentBaseDTO {

}