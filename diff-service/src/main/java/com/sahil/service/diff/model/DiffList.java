package com.sahil.service.diff.model;

import java.util.List;

import com.sahil.service.diff.util.DiffTool.Diff;

public class DiffList {

  private List<Diff> diffs;

  public DiffList() {

  }

  public DiffList(List<Diff> diffs) {
    this.diffs = diffs;
  }

  public List<Diff> getDiffs() {
    return diffs;
  }

  public void setDiffs(List<Diff> diffs) {
    this.diffs = diffs;
  }

}
