package com.victor.stream.consumer;

public class TaskProcessorImpl implements TaskProcessor {

    @Override
    public void process(String taskName) {
        System.out.println("Start to process task: " + taskName);
    }

    @Override
    public void terminateProcessing(String taskName) {
        System.out.println("Terminate processing task: " + taskName);
    }
}
