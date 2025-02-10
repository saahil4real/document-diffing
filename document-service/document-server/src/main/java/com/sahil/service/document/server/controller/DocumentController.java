package com.sahil.service.document.server.controller;

import com.sahil.service.diff.client.DiffList;
import com.sahil.service.document.client.MultipartFileImpl;
import com.sahil.service.document.client.OldNewFiles;
import com.sahil.service.highlight.client.HighlightRequest;
import com.sahil.service.highlight.client.HighlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
@RequestMapping("/v1")
public class DocumentController {

  @Autowired
  RestTemplate template;

  @PostMapping("/compare")
  public OldNewFiles compare(@RequestBody OldNewFiles oldNewFiles) {
    DiffList diffList =
      template.postForObject("http://diff-service/diffs", oldNewFiles, DiffList.class);

    HighlightRequest highlightRequest = new HighlightRequest(oldNewFiles, diffList);
    HighlightResponse highlightResponse =
      template.postForObject("http://highlight-service/highlight", highlightRequest,
        HighlightResponse.class);
    MultipartFileImpl oldFile = highlightResponse.getOldFileHighlighted();
    MultipartFileImpl newFile = highlightResponse.getNewFileHighlighted();

    return new OldNewFiles(oldFile, newFile);
  }
}
