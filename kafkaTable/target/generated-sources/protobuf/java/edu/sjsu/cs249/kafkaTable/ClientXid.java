// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package edu.sjsu.cs249.kafkaTable;

/**
 * Protobuf type {@code kafkaTable.ClientXid}
 */
public final class ClientXid extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:kafkaTable.ClientXid)
    ClientXidOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ClientXid.newBuilder() to construct.
  private ClientXid(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ClientXid() {
    clientid_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ClientXid();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ClientXid(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            clientid_ = s;
            break;
          }
          case 16: {

            counter_ = input.readInt32();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_ClientXid_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_ClientXid_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            edu.sjsu.cs249.kafkaTable.ClientXid.class, edu.sjsu.cs249.kafkaTable.ClientXid.Builder.class);
  }

  public static final int CLIENTID_FIELD_NUMBER = 1;
  private volatile java.lang.Object clientid_;
  /**
   * <code>string clientid = 1;</code>
   * @return The clientid.
   */
  @java.lang.Override
  public java.lang.String getClientid() {
    java.lang.Object ref = clientid_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      clientid_ = s;
      return s;
    }
  }
  /**
   * <code>string clientid = 1;</code>
   * @return The bytes for clientid.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getClientidBytes() {
    java.lang.Object ref = clientid_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      clientid_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int COUNTER_FIELD_NUMBER = 2;
  private int counter_;
  /**
   * <code>int32 counter = 2;</code>
   * @return The counter.
   */
  @java.lang.Override
  public int getCounter() {
    return counter_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(clientid_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, clientid_);
    }
    if (counter_ != 0) {
      output.writeInt32(2, counter_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(clientid_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, clientid_);
    }
    if (counter_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, counter_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof edu.sjsu.cs249.kafkaTable.ClientXid)) {
      return super.equals(obj);
    }
    edu.sjsu.cs249.kafkaTable.ClientXid other = (edu.sjsu.cs249.kafkaTable.ClientXid) obj;

    if (!getClientid()
        .equals(other.getClientid())) return false;
    if (getCounter()
        != other.getCounter()) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + CLIENTID_FIELD_NUMBER;
    hash = (53 * hash) + getClientid().hashCode();
    hash = (37 * hash) + COUNTER_FIELD_NUMBER;
    hash = (53 * hash) + getCounter();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static edu.sjsu.cs249.kafkaTable.ClientXid parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(edu.sjsu.cs249.kafkaTable.ClientXid prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code kafkaTable.ClientXid}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:kafkaTable.ClientXid)
      edu.sjsu.cs249.kafkaTable.ClientXidOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_ClientXid_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_ClientXid_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.sjsu.cs249.kafkaTable.ClientXid.class, edu.sjsu.cs249.kafkaTable.ClientXid.Builder.class);
    }

    // Construct using edu.sjsu.cs249.kafkaTable.ClientXid.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      clientid_ = "";

      counter_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_ClientXid_descriptor;
    }

    @java.lang.Override
    public edu.sjsu.cs249.kafkaTable.ClientXid getDefaultInstanceForType() {
      return edu.sjsu.cs249.kafkaTable.ClientXid.getDefaultInstance();
    }

    @java.lang.Override
    public edu.sjsu.cs249.kafkaTable.ClientXid build() {
      edu.sjsu.cs249.kafkaTable.ClientXid result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public edu.sjsu.cs249.kafkaTable.ClientXid buildPartial() {
      edu.sjsu.cs249.kafkaTable.ClientXid result = new edu.sjsu.cs249.kafkaTable.ClientXid(this);
      result.clientid_ = clientid_;
      result.counter_ = counter_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof edu.sjsu.cs249.kafkaTable.ClientXid) {
        return mergeFrom((edu.sjsu.cs249.kafkaTable.ClientXid)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(edu.sjsu.cs249.kafkaTable.ClientXid other) {
      if (other == edu.sjsu.cs249.kafkaTable.ClientXid.getDefaultInstance()) return this;
      if (!other.getClientid().isEmpty()) {
        clientid_ = other.clientid_;
        onChanged();
      }
      if (other.getCounter() != 0) {
        setCounter(other.getCounter());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      edu.sjsu.cs249.kafkaTable.ClientXid parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (edu.sjsu.cs249.kafkaTable.ClientXid) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object clientid_ = "";
    /**
     * <code>string clientid = 1;</code>
     * @return The clientid.
     */
    public java.lang.String getClientid() {
      java.lang.Object ref = clientid_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        clientid_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string clientid = 1;</code>
     * @return The bytes for clientid.
     */
    public com.google.protobuf.ByteString
        getClientidBytes() {
      java.lang.Object ref = clientid_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        clientid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string clientid = 1;</code>
     * @param value The clientid to set.
     * @return This builder for chaining.
     */
    public Builder setClientid(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      clientid_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string clientid = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearClientid() {
      
      clientid_ = getDefaultInstance().getClientid();
      onChanged();
      return this;
    }
    /**
     * <code>string clientid = 1;</code>
     * @param value The bytes for clientid to set.
     * @return This builder for chaining.
     */
    public Builder setClientidBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      clientid_ = value;
      onChanged();
      return this;
    }

    private int counter_ ;
    /**
     * <code>int32 counter = 2;</code>
     * @return The counter.
     */
    @java.lang.Override
    public int getCounter() {
      return counter_;
    }
    /**
     * <code>int32 counter = 2;</code>
     * @param value The counter to set.
     * @return This builder for chaining.
     */
    public Builder setCounter(int value) {
      
      counter_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 counter = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearCounter() {
      
      counter_ = 0;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:kafkaTable.ClientXid)
  }

  // @@protoc_insertion_point(class_scope:kafkaTable.ClientXid)
  private static final edu.sjsu.cs249.kafkaTable.ClientXid DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new edu.sjsu.cs249.kafkaTable.ClientXid();
  }

  public static edu.sjsu.cs249.kafkaTable.ClientXid getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ClientXid>
      PARSER = new com.google.protobuf.AbstractParser<ClientXid>() {
    @java.lang.Override
    public ClientXid parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ClientXid(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ClientXid> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ClientXid> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public edu.sjsu.cs249.kafkaTable.ClientXid getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

