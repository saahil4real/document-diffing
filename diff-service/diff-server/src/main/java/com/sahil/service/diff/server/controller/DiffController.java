package com.sahil.service.diff.server.controller;

import com.sahil.service.diff.client.Diff;
import com.sahil.service.diff.client.DiffList;
import com.sahil.service.diff.client.event.DiffEvent;
import com.sahil.service.diff.server.util.CompareText;
import com.sahil.service.diff.server.util.TextExtractor;
import com.sahil.service.document.client.OldNewFiles;
import com.sahil.service.document.client.event.CompareEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedList;

@Slf4j
@RestController
public class DiffController {

  private static final String TOPIC = "diff-topic";

  @Autowired
  private KafkaTemplate<String, DiffEvent> kafkaTemplate;

  @KafkaListener(topics = "compare-topic", groupId = "diff-group")
  public void consumeCompareEvent(ConsumerRecord<String, CompareEvent> record) {
    CompareEvent compareEvent = record.value();
    OldNewFiles oldNewFiles = compareEvent.getOldNewFiles();
    DiffList diffList = createDiffs(oldNewFiles);

    // publish event
    DiffEvent diffEvent = new DiffEvent();
    diffEvent.setDiffList(diffList);
    diffEvent.setRequestId(compareEvent.getRequestId());
    kafkaTemplate.send(TOPIC, diffEvent);
  }

  @PostMapping("/diffs")
  public DiffList createDiffs(@RequestBody OldNewFiles oldNewFiles) {
    String oldText = null, newText = null;
    try {
      oldText = new TextExtractor(oldNewFiles.getOldFile()).getText();
      newText = new TextExtractor(oldNewFiles.getNewFile()).getText();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    LinkedList<Diff> diffs = CompareText.compare(oldText, newText);
    return new DiffList(diffs);
  }

}
