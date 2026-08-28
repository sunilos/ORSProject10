package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.DocumentDTO;
import com.sunilos.exception.DuplicateRecordException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Document Service interface.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface DocumentServiceInt extends BaseServiceInt<DocumentDTO> {
        public DocumentDTO update(Long id, MultipartFile file, String description, UserContext userContext)
                        throws DuplicateRecordException, IOException;

        public DocumentDTO add(MultipartFile file, String description, UserContext userContext)
                        throws DuplicateRecordException, IOException;

}