package com.sahil.service.highlight.client;

import com.sahil.service.diff.client.DiffList;
import com.sahil.service.document.client.OldNewFiles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HighlightRequest {

  private OldNewFiles oldNewFiles;

  private DiffList diffList;

}
