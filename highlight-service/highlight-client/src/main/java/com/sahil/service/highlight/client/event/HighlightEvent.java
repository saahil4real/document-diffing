package com.sahil.service.highlight.client.event;

import com.sahil.service.highlight.client.HighlightResponse;
import com.sahil.shared.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HighlightEvent extends Event {
  private HighlightResponse highlightResponse;
}
