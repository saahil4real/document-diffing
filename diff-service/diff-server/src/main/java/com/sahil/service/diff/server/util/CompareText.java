package com.sahil.service.diff.server.util;

import com.sahil.service.diff.client.Diff;

import java.util.LinkedList;

public class CompareText {

  public static LinkedList<Diff> compare(String oldText, String newText) {
    DiffTool differ = new DiffTool();
    differ.setDiff_Timeout(120);

    LinkedList<Diff> diffList = differ.diff_main(oldText, newText);
    differ.diff_cleanupSemantic(diffList);
    return diffList;
  }
}
