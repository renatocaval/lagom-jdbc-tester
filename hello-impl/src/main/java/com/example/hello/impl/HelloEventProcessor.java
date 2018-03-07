package com.example.hello.impl;

import com.example.hello.impl.HelloEvent.GreetingMessageChanged;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import org.pcollections.PSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class HelloEventProcessor extends ReadSideProcessor<HelloEvent> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JdbcReadSide readSide;

    @Inject
    public HelloEventProcessor(JdbcReadSide readSide) {
        this.readSide = readSide;
    }

    @Override
    public ReadSideHandler<HelloEvent> buildHandler() {
        return readSide.<HelloEvent>builder("HelloEventProcessor")
                .setEventHandler(GreetingMessageChanged.class, (conn, evt) -> log.info("Read: {}", evt))
                .build();
    }

    @Override
    public PSequence<AggregateEventTag<HelloEvent>> aggregateTags() {
        return HelloEvent.TAG.allTags();
    }
}
