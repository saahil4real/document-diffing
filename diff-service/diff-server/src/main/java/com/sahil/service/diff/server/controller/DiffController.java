package com.sahil.service.diff.server.controller;

import com.sahil.service.diff.client.Diff;
import com.sahil.service.diff.client.DiffList;
import com.sahil.service.diff.server.util.CompareText;
import com.sahil.service.diff.server.util.TextExtractor;
import com.sahil.service.document.client.OldNewFiles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedList;


@RestController
public class DiffController {

  @PostMapping("/diffs")
  public DiffList createDiffs(@RequestBody OldNewFiles oldNewFiles) {
    String oldText = null, newText = null;
    try {
      oldText = new TextExtractor(oldNewFiles.getOldFile()).getText();
      newText = new TextExtractor(oldNewFiles.getNewFile()).getText();
    } catch (IOException e) {
      e.getMessage();
    }
    LinkedList<Diff> diffs = CompareText.compare(oldText, newText);
    DiffList diffList = new DiffList(diffs);
    return diffList;
  }

}
