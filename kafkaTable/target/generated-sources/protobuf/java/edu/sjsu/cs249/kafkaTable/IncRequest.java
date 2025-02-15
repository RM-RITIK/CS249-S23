// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package edu.sjsu.cs249.kafkaTable;

/**
 * Protobuf type {@code kafkaTable.IncRequest}
 */
public final class IncRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:kafkaTable.IncRequest)
    IncRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use IncRequest.newBuilder() to construct.
  private IncRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private IncRequest() {
    key_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new IncRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private IncRequest(
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

            key_ = s;
            break;
          }
          case 16: {

            incValue_ = input.readInt32();
            break;
          }
          case 26: {
            edu.sjsu.cs249.kafkaTable.ClientXid.Builder subBuilder = null;
            if (xid_ != null) {
              subBuilder = xid_.toBuilder();
            }
            xid_ = input.readMessage(edu.sjsu.cs249.kafkaTable.ClientXid.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(xid_);
              xid_ = subBuilder.buildPartial();
            }

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
    return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_IncRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_IncRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            edu.sjsu.cs249.kafkaTable.IncRequest.class, edu.sjsu.cs249.kafkaTable.IncRequest.Builder.class);
  }

  public static final int KEY_FIELD_NUMBER = 1;
  private volatile java.lang.Object key_;
  /**
   * <code>string key = 1;</code>
   * @return The key.
   */
  @java.lang.Override
  public java.lang.String getKey() {
    java.lang.Object ref = key_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      key_ = s;
      return s;
    }
  }
  /**
   * <code>string key = 1;</code>
   * @return The bytes for key.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getKeyBytes() {
    java.lang.Object ref = key_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      key_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int INCVALUE_FIELD_NUMBER = 2;
  private int incValue_;
  /**
   * <pre>
   * if the key does not exist, it will be created with this value, otherwise the value
   * if the existing key will be incremented by this value
   * </pre>
   *
   * <code>int32 incValue = 2;</code>
   * @return The incValue.
   */
  @java.lang.Override
  public int getIncValue() {
    return incValue_;
  }

  public static final int XID_FIELD_NUMBER = 3;
  private edu.sjsu.cs249.kafkaTable.ClientXid xid_;
  /**
   * <code>.kafkaTable.ClientXid xid = 3;</code>
   * @return Whether the xid field is set.
   */
  @java.lang.Override
  public boolean hasXid() {
    return xid_ != null;
  }
  /**
   * <code>.kafkaTable.ClientXid xid = 3;</code>
   * @return The xid.
   */
  @java.lang.Override
  public edu.sjsu.cs249.kafkaTable.ClientXid getXid() {
    return xid_ == null ? edu.sjsu.cs249.kafkaTable.ClientXid.getDefaultInstance() : xid_;
  }
  /**
   * <code>.kafkaTable.ClientXid xid = 3;</code>
   */
  @java.lang.Override
  public edu.sjsu.cs249.kafkaTable.ClientXidOrBuilder getXidOrBuilder() {
    return getXid();
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(key_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, key_);
    }
    if (incValue_ != 0) {
      output.writeInt32(2, incValue_);
    }
    if (xid_ != null) {
      output.writeMessage(3, getXid());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(key_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, key_);
    }
    if (incValue_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, incValue_);
    }
    if (xid_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getXid());
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
    if (!(obj instanceof edu.sjsu.cs249.kafkaTable.IncRequest)) {
      return super.equals(obj);
    }
    edu.sjsu.cs249.kafkaTable.IncRequest other = (edu.sjsu.cs249.kafkaTable.IncRequest) obj;

    if (!getKey()
        .equals(other.getKey())) return false;
    if (getIncValue()
        != other.getIncValue()) return false;
    if (hasXid() != other.hasXid()) return false;
    if (hasXid()) {
      if (!getXid()
          .equals(other.getXid())) return false;
    }
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
    hash = (37 * hash) + KEY_FIELD_NUMBER;
    hash = (53 * hash) + getKey().hashCode();
    hash = (37 * hash) + INCVALUE_FIELD_NUMBER;
    hash = (53 * hash) + getIncValue();
    if (hasXid()) {
      hash = (37 * hash) + XID_FIELD_NUMBER;
      hash = (53 * hash) + getXid().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static edu.sjsu.cs249.kafkaTable.IncRequest parseFrom(
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
  public static Builder newBuilder(edu.sjsu.cs249.kafkaTable.IncRequest prototype) {
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
   * Protobuf type {@code kafkaTable.IncRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:kafkaTable.IncRequest)
      edu.sjsu.cs249.kafkaTable.IncRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_IncRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_IncRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.sjsu.cs249.kafkaTable.IncRequest.class, edu.sjsu.cs249.kafkaTable.IncRequest.Builder.class);
    }

    // Construct using edu.sjsu.cs249.kafkaTable.IncRequest.newBuilder()
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
      key_ = "";

      incValue_ = 0;

      if (xidBuilder_ == null) {
        xid_ = null;
      } else {
        xid_ = null;
        xidBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return edu.sjsu.cs249.kafkaTable.Message.internal_static_kafkaTable_IncRequest_descriptor;
    }

    @java.lang.Override
    public edu.sjsu.cs249.kafkaTable.IncRequest getDefaultInstanceForType() {
      return edu.sjsu.cs249.kafkaTable.IncRequest.getDefaultInstance();
    }

    @java.lang.Override
    public edu.sjsu.cs249.kafkaTable.IncRequest build() {
      edu.sjsu.cs249.kafkaTable.IncRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public edu.sjsu.cs249.kafkaTable.IncRequest buildPartial() {
      edu.sjsu.cs249.kafkaTable.IncRequest result = new edu.sjsu.cs249.kafkaTable.IncRequest(this);
      result.key_ = key_;
      result.incValue_ = incValue_;
      if (xidBuilder_ == null) {
        result.xid_ = xid_;
      } else {
        result.xid_ = xidBuilder_.build();
      }
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
      if (other instanceof edu.sjsu.cs249.kafkaTable.IncRequest) {
        return mergeFrom((edu.sjsu.cs249.kafkaTable.IncRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(edu.sjsu.cs249.kafkaTable.IncRequest other) {
      if (other == edu.sjsu.cs249.kafkaTable.IncRequest.getDefaultInstance()) return this;
      if (!other.getKey().isEmpty()) {
        key_ = other.key_;
        onChanged();
      }
      if (other.getIncValue() != 0) {
        setIncValue(other.getIncValue());
      }
      if (other.hasXid()) {
        mergeXid(other.getXid());
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
      edu.sjsu.cs249.kafkaTable.IncRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (edu.sjsu.cs249.kafkaTable.IncRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object key_ = "";
    /**
     * <code>string key = 1;</code>
     * @return The key.
     */
    public java.lang.String getKey() {
      java.lang.Object ref = key_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        key_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string key = 1;</code>
     * @return The bytes for key.
     */
    public com.google.protobuf.ByteString
        getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string key = 1;</code>
     * @param value The key to set.
     * @return This builder for chaining.
     */
    public Builder setKey(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      key_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string key = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearKey() {
      
      key_ = getDefaultInstance().getKey();
      onChanged();
      return this;
    }
    /**
     * <code>string key = 1;</code>
     * @param value The bytes for key to set.
     * @return This builder for chaining.
     */
    public Builder setKeyBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      key_ = value;
      onChanged();
      return this;
    }

    private int incValue_ ;
    /**
     * <pre>
     * if the key does not exist, it will be created with this value, otherwise the value
     * if the existing key will be incremented by this value
     * </pre>
     *
     * <code>int32 incValue = 2;</code>
     * @return The incValue.
     */
    @java.lang.Override
    public int getIncValue() {
      return incValue_;
    }
    /**
     * <pre>
     * if the key does not exist, it will be created with this value, otherwise the value
     * if the existing key will be incremented by this value
     * </pre>
     *
     * <code>int32 incValue = 2;</code>
     * @param value The incValue to set.
     * @return This builder for chaining.
     */
    public Builder setIncValue(int value) {
      
      incValue_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * if the key does not exist, it will be created with this value, otherwise the value
     * if the existing key will be incremented by this value
     * </pre>
     *
     * <code>int32 incValue = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearIncValue() {
      
      incValue_ = 0;
      onChanged();
      return this;
    }

    private edu.sjsu.cs249.kafkaTable.ClientXid xid_;
    private com.google.protobuf.SingleFieldBuilderV3<
        edu.sjsu.cs249.kafkaTable.ClientXid, edu.sjsu.cs249.kafkaTable.ClientXid.Builder, edu.sjsu.cs249.kafkaTable.ClientXidOrBuilder> xidBuilder_;
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     * @return Whether the xid field is set.
     */
    public boolean hasXid() {
      return xidBuilder_ != null || xid_ != null;
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     * @return The xid.
     */
    public edu.sjsu.cs249.kafkaTable.ClientXid getXid() {
      if (xidBuilder_ == null) {
        return xid_ == null ? edu.sjsu.cs249.kafkaTable.ClientXid.getDefaultInstance() : xid_;
      } else {
        return xidBuilder_.getMessage();
      }
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    public Builder setXid(edu.sjsu.cs249.kafkaTable.ClientXid value) {
      if (xidBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        xid_ = value;
        onChanged();
      } else {
        xidBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    public Builder setXid(
        edu.sjsu.cs249.kafkaTable.ClientXid.Builder builderForValue) {
      if (xidBuilder_ == null) {
        xid_ = builderForValue.build();
        onChanged();
      } else {
        xidBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    public Builder mergeXid(edu.sjsu.cs249.kafkaTable.ClientXid value) {
      if (xidBuilder_ == null) {
        if (xid_ != null) {
          xid_ =
            edu.sjsu.cs249.kafkaTable.ClientXid.newBuilder(xid_).mergeFrom(value).buildPartial();
        } else {
          xid_ = value;
        }
        onChanged();
      } else {
        xidBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    public Builder clearXid() {
      if (xidBuilder_ == null) {
        xid_ = null;
        onChanged();
      } else {
        xid_ = null;
        xidBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    public edu.sjsu.cs249.kafkaTable.ClientXid.Builder getXidBuilder() {
      
      onChanged();
      return getXidFieldBuilder().getBuilder();
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    public edu.sjsu.cs249.kafkaTable.ClientXidOrBuilder getXidOrBuilder() {
      if (xidBuilder_ != null) {
        return xidBuilder_.getMessageOrBuilder();
      } else {
        return xid_ == null ?
            edu.sjsu.cs249.kafkaTable.ClientXid.getDefaultInstance() : xid_;
      }
    }
    /**
     * <code>.kafkaTable.ClientXid xid = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        edu.sjsu.cs249.kafkaTable.ClientXid, edu.sjsu.cs249.kafkaTable.ClientXid.Builder, edu.sjsu.cs249.kafkaTable.ClientXidOrBuilder> 
        getXidFieldBuilder() {
      if (xidBuilder_ == null) {
        xidBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            edu.sjsu.cs249.kafkaTable.ClientXid, edu.sjsu.cs249.kafkaTable.ClientXid.Builder, edu.sjsu.cs249.kafkaTable.ClientXidOrBuilder>(
                getXid(),
                getParentForChildren(),
                isClean());
        xid_ = null;
      }
      return xidBuilder_;
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


    // @@protoc_insertion_point(builder_scope:kafkaTable.IncRequest)
  }

  // @@protoc_insertion_point(class_scope:kafkaTable.IncRequest)
  private static final edu.sjsu.cs249.kafkaTable.IncRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new edu.sjsu.cs249.kafkaTable.IncRequest();
  }

  public static edu.sjsu.cs249.kafkaTable.IncRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<IncRequest>
      PARSER = new com.google.protobuf.AbstractParser<IncRequest>() {
    @java.lang.Override
    public IncRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new IncRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<IncRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<IncRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public edu.sjsu.cs249.kafkaTable.IncRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

