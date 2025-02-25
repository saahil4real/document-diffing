package com.sahil.service.diff.client.event;

import com.sahil.service.diff.client.DiffList;
import com.sahil.shared.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiffEvent extends Event {
  private DiffList diffList;
}
