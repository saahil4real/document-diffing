package com.sahil.shared.util;

import com.sahil.shared.CustomMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartUtils {
  public static MultipartFile reconstructMultipartFile(String fileName, String contentType,
                                                       byte[] bytes) {
    return new CustomMultipartFile(fileName, contentType, bytes);
  }
}
