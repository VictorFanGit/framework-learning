package com.victor.stream.consumer;

public interface TaskProcessor {
    void process(String taskName);
    void terminateProcessing(String taskName);
}
