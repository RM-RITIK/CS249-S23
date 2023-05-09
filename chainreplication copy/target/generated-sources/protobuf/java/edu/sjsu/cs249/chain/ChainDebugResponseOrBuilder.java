// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chain_debug.proto

package edu.sjsu.cs249.chain;

public interface ChainDebugResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:chain.ChainDebugResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;string, uint32&gt; state = 1;</code>
   */
  int getStateCount();
  /**
   * <code>map&lt;string, uint32&gt; state = 1;</code>
   */
  boolean containsState(
      java.lang.String key);
  /**
   * Use {@link #getStateMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Integer>
  getState();
  /**
   * <code>map&lt;string, uint32&gt; state = 1;</code>
   */
  java.util.Map<java.lang.String, java.lang.Integer>
  getStateMap();
  /**
   * <code>map&lt;string, uint32&gt; state = 1;</code>
   */

  int getStateOrDefault(
      java.lang.String key,
      int defaultValue);
  /**
   * <code>map&lt;string, uint32&gt; state = 1;</code>
   */

  int getStateOrThrow(
      java.lang.String key);

  /**
   * <pre>
   * the last xid processed in the state
   * </pre>
   *
   * <code>uint32 xid = 2;</code>
   * @return The xid.
   */
  int getXid();

  /**
   * <pre>
   * updates that have not been processed
   * </pre>
   *
   * <code>repeated .chain.UpdateRequest sent = 3;</code>
   */
  java.util.List<edu.sjsu.cs249.chain.UpdateRequest> 
      getSentList();
  /**
   * <pre>
   * updates that have not been processed
   * </pre>
   *
   * <code>repeated .chain.UpdateRequest sent = 3;</code>
   */
  edu.sjsu.cs249.chain.UpdateRequest getSent(int index);
  /**
   * <pre>
   * updates that have not been processed
   * </pre>
   *
   * <code>repeated .chain.UpdateRequest sent = 3;</code>
   */
  int getSentCount();
  /**
   * <pre>
   * updates that have not been processed
   * </pre>
   *
   * <code>repeated .chain.UpdateRequest sent = 3;</code>
   */
  java.util.List<? extends edu.sjsu.cs249.chain.UpdateRequestOrBuilder> 
      getSentOrBuilderList();
  /**
   * <pre>
   * updates that have not been processed
   * </pre>
   *
   * <code>repeated .chain.UpdateRequest sent = 3;</code>
   */
  edu.sjsu.cs249.chain.UpdateRequestOrBuilder getSentOrBuilder(
      int index);

  /**
   * <code>repeated string logs = 4;</code>
   * @return A list containing the logs.
   */
  java.util.List<java.lang.String>
      getLogsList();
  /**
   * <code>repeated string logs = 4;</code>
   * @return The count of logs.
   */
  int getLogsCount();
  /**
   * <code>repeated string logs = 4;</code>
   * @param index The index of the element to return.
   * @return The logs at the given index.
   */
  java.lang.String getLogs(int index);
  /**
   * <code>repeated string logs = 4;</code>
   * @param index The index of the value to return.
   * @return The bytes of the logs at the given index.
   */
  com.google.protobuf.ByteString
      getLogsBytes(int index);
}
