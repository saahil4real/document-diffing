package com.sahil.service.document.client.event;

import com.sahil.service.document.client.OldNewFiles;
import com.sahil.shared.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompareEvent extends Event {
  private OldNewFiles oldNewFiles;
}
