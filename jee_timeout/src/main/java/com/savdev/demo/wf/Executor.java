package com.savdev.demo.wf;

@FunctionalInterface
public interface Executor {
  /**
   * Wraps an execution
   */
  void execute();
}