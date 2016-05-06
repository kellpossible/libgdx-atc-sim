// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: main/proto/PredictionDisplay_Proto.proto

package com.atc.simulator.PredDis;

public final class PredToDisp {
  private PredToDisp() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PrediMesOrBuilder extends
      // @@protoc_insertion_point(interface_extends:PrediMes)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required string ID = 1;</code>
     */
    boolean hasID();
    /**
     * <code>required string ID = 1;</code>
     */
    java.lang.String getID();
    /**
     * <code>required string ID = 1;</code>
     */
    com.google.protobuf.ByteString
        getIDBytes();

    /**
     * <code>repeated double positionPred = 2;</code>
     */
    java.util.List<java.lang.Double> getPositionPredList();
    /**
     * <code>repeated double positionPred = 2;</code>
     */
    int getPositionPredCount();
    /**
     * <code>repeated double positionPred = 2;</code>
     */
    double getPositionPred(int index);

    /**
     * <code>repeated double positionPast = 3;</code>
     */
    java.util.List<java.lang.Double> getPositionPastList();
    /**
     * <code>repeated double positionPast = 3;</code>
     */
    int getPositionPastCount();
    /**
     * <code>repeated double positionPast = 3;</code>
     */
    double getPositionPast(int index);

    /**
     * <code>optional double Heading = 4;</code>
     */
    boolean hasHeading();
    /**
     * <code>optional double Heading = 4;</code>
     */
    double getHeading();
  }
  /**
   * Protobuf type {@code PrediMes}
   */
  public static final class PrediMes extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:PrediMes)
      PrediMesOrBuilder {
    // Use PrediMes.newBuilder() to construct.
    private PrediMes(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private PrediMes(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final PrediMes defaultInstance;
    public static PrediMes getDefaultInstance() {
      return defaultInstance;
    }

    public PrediMes getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private PrediMes(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
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
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              iD_ = bs;
              break;
            }
            case 17: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                positionPred_ = new java.util.ArrayList<java.lang.Double>();
                mutable_bitField0_ |= 0x00000002;
              }
              positionPred_.add(input.readDouble());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002) && input.getBytesUntilLimit() > 0) {
                positionPred_ = new java.util.ArrayList<java.lang.Double>();
                mutable_bitField0_ |= 0x00000002;
              }
              while (input.getBytesUntilLimit() > 0) {
                positionPred_.add(input.readDouble());
              }
              input.popLimit(limit);
              break;
            }
            case 25: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                positionPast_ = new java.util.ArrayList<java.lang.Double>();
                mutable_bitField0_ |= 0x00000004;
              }
              positionPast_.add(input.readDouble());
              break;
            }
            case 26: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004) && input.getBytesUntilLimit() > 0) {
                positionPast_ = new java.util.ArrayList<java.lang.Double>();
                mutable_bitField0_ |= 0x00000004;
              }
              while (input.getBytesUntilLimit() > 0) {
                positionPast_.add(input.readDouble());
              }
              input.popLimit(limit);
              break;
            }
            case 33: {
              bitField0_ |= 0x00000002;
              heading_ = input.readDouble();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          positionPred_ = java.util.Collections.unmodifiableList(positionPred_);
        }
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          positionPast_ = java.util.Collections.unmodifiableList(positionPast_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.atc.simulator.PredDis.PredToDisp.internal_static_PrediMes_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.atc.simulator.PredDis.PredToDisp.internal_static_PrediMes_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.atc.simulator.PredDis.PredToDisp.PrediMes.class, com.atc.simulator.PredDis.PredToDisp.PrediMes.Builder.class);
    }

    public static com.google.protobuf.Parser<PrediMes> PARSER =
        new com.google.protobuf.AbstractParser<PrediMes>() {
      public PrediMes parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new PrediMes(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<PrediMes> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ID_FIELD_NUMBER = 1;
    private java.lang.Object iD_;
    /**
     * <code>required string ID = 1;</code>
     */
    public boolean hasID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required string ID = 1;</code>
     */
    public java.lang.String getID() {
      java.lang.Object ref = iD_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          iD_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string ID = 1;</code>
     */
    public com.google.protobuf.ByteString
        getIDBytes() {
      java.lang.Object ref = iD_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        iD_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int POSITIONPRED_FIELD_NUMBER = 2;
    private java.util.List<java.lang.Double> positionPred_;
    /**
     * <code>repeated double positionPred = 2;</code>
     */
    public java.util.List<java.lang.Double>
        getPositionPredList() {
      return positionPred_;
    }
    /**
     * <code>repeated double positionPred = 2;</code>
     */
    public int getPositionPredCount() {
      return positionPred_.size();
    }
    /**
     * <code>repeated double positionPred = 2;</code>
     */
    public double getPositionPred(int index) {
      return positionPred_.get(index);
    }

    public static final int POSITIONPAST_FIELD_NUMBER = 3;
    private java.util.List<java.lang.Double> positionPast_;
    /**
     * <code>repeated double positionPast = 3;</code>
     */
    public java.util.List<java.lang.Double>
        getPositionPastList() {
      return positionPast_;
    }
    /**
     * <code>repeated double positionPast = 3;</code>
     */
    public int getPositionPastCount() {
      return positionPast_.size();
    }
    /**
     * <code>repeated double positionPast = 3;</code>
     */
    public double getPositionPast(int index) {
      return positionPast_.get(index);
    }

    public static final int HEADING_FIELD_NUMBER = 4;
    private double heading_;
    /**
     * <code>optional double Heading = 4;</code>
     */
    public boolean hasHeading() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional double Heading = 4;</code>
     */
    public double getHeading() {
      return heading_;
    }

    private void initFields() {
      iD_ = "";
      positionPred_ = java.util.Collections.emptyList();
      positionPast_ = java.util.Collections.emptyList();
      heading_ = 0D;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasID()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getIDBytes());
      }
      for (int i = 0; i < positionPred_.size(); i++) {
        output.writeDouble(2, positionPred_.get(i));
      }
      for (int i = 0; i < positionPast_.size(); i++) {
        output.writeDouble(3, positionPast_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeDouble(4, heading_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getIDBytes());
      }
      {
        int dataSize = 0;
        dataSize = 8 * getPositionPredList().size();
        size += dataSize;
        size += 1 * getPositionPredList().size();
      }
      {
        int dataSize = 0;
        dataSize = 8 * getPositionPastList().size();
        size += dataSize;
        size += 1 * getPositionPastList().size();
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(4, heading_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.atc.simulator.PredDis.PredToDisp.PrediMes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.atc.simulator.PredDis.PredToDisp.PrediMes prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code PrediMes}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:PrediMes)
        com.atc.simulator.PredDis.PredToDisp.PrediMesOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.atc.simulator.PredDis.PredToDisp.internal_static_PrediMes_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.atc.simulator.PredDis.PredToDisp.internal_static_PrediMes_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.atc.simulator.PredDis.PredToDisp.PrediMes.class, com.atc.simulator.PredDis.PredToDisp.PrediMes.Builder.class);
      }

      // Construct using com.atc.simulator.PredDis.PredToDisp.PrediMes.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        iD_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        positionPred_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        positionPast_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        heading_ = 0D;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.atc.simulator.PredDis.PredToDisp.internal_static_PrediMes_descriptor;
      }

      public com.atc.simulator.PredDis.PredToDisp.PrediMes getDefaultInstanceForType() {
        return com.atc.simulator.PredDis.PredToDisp.PrediMes.getDefaultInstance();
      }

      public com.atc.simulator.PredDis.PredToDisp.PrediMes build() {
        com.atc.simulator.PredDis.PredToDisp.PrediMes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.atc.simulator.PredDis.PredToDisp.PrediMes buildPartial() {
        com.atc.simulator.PredDis.PredToDisp.PrediMes result = new com.atc.simulator.PredDis.PredToDisp.PrediMes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.iD_ = iD_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          positionPred_ = java.util.Collections.unmodifiableList(positionPred_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.positionPred_ = positionPred_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          positionPast_ = java.util.Collections.unmodifiableList(positionPast_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.positionPast_ = positionPast_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000002;
        }
        result.heading_ = heading_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.atc.simulator.PredDis.PredToDisp.PrediMes) {
          return mergeFrom((com.atc.simulator.PredDis.PredToDisp.PrediMes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.atc.simulator.PredDis.PredToDisp.PrediMes other) {
        if (other == com.atc.simulator.PredDis.PredToDisp.PrediMes.getDefaultInstance()) return this;
        if (other.hasID()) {
          bitField0_ |= 0x00000001;
          iD_ = other.iD_;
          onChanged();
        }
        if (!other.positionPred_.isEmpty()) {
          if (positionPred_.isEmpty()) {
            positionPred_ = other.positionPred_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensurePositionPredIsMutable();
            positionPred_.addAll(other.positionPred_);
          }
          onChanged();
        }
        if (!other.positionPast_.isEmpty()) {
          if (positionPast_.isEmpty()) {
            positionPast_ = other.positionPast_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensurePositionPastIsMutable();
            positionPast_.addAll(other.positionPast_);
          }
          onChanged();
        }
        if (other.hasHeading()) {
          setHeading(other.getHeading());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasID()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.atc.simulator.PredDis.PredToDisp.PrediMes parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.atc.simulator.PredDis.PredToDisp.PrediMes) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object iD_ = "";
      /**
       * <code>required string ID = 1;</code>
       */
      public boolean hasID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required string ID = 1;</code>
       */
      public java.lang.String getID() {
        java.lang.Object ref = iD_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            iD_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string ID = 1;</code>
       */
      public com.google.protobuf.ByteString
          getIDBytes() {
        java.lang.Object ref = iD_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          iD_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string ID = 1;</code>
       */
      public Builder setID(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        iD_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string ID = 1;</code>
       */
      public Builder clearID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        iD_ = getDefaultInstance().getID();
        onChanged();
        return this;
      }
      /**
       * <code>required string ID = 1;</code>
       */
      public Builder setIDBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        iD_ = value;
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Double> positionPred_ = java.util.Collections.emptyList();
      private void ensurePositionPredIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          positionPred_ = new java.util.ArrayList<java.lang.Double>(positionPred_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public java.util.List<java.lang.Double>
          getPositionPredList() {
        return java.util.Collections.unmodifiableList(positionPred_);
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public int getPositionPredCount() {
        return positionPred_.size();
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public double getPositionPred(int index) {
        return positionPred_.get(index);
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public Builder setPositionPred(
          int index, double value) {
        ensurePositionPredIsMutable();
        positionPred_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public Builder addPositionPred(double value) {
        ensurePositionPredIsMutable();
        positionPred_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public Builder addAllPositionPred(
          java.lang.Iterable<? extends java.lang.Double> values) {
        ensurePositionPredIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, positionPred_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated double positionPred = 2;</code>
       */
      public Builder clearPositionPred() {
        positionPred_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Double> positionPast_ = java.util.Collections.emptyList();
      private void ensurePositionPastIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          positionPast_ = new java.util.ArrayList<java.lang.Double>(positionPast_);
          bitField0_ |= 0x00000004;
         }
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public java.util.List<java.lang.Double>
          getPositionPastList() {
        return java.util.Collections.unmodifiableList(positionPast_);
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public int getPositionPastCount() {
        return positionPast_.size();
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public double getPositionPast(int index) {
        return positionPast_.get(index);
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public Builder setPositionPast(
          int index, double value) {
        ensurePositionPastIsMutable();
        positionPast_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public Builder addPositionPast(double value) {
        ensurePositionPastIsMutable();
        positionPast_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public Builder addAllPositionPast(
          java.lang.Iterable<? extends java.lang.Double> values) {
        ensurePositionPastIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, positionPast_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated double positionPast = 3;</code>
       */
      public Builder clearPositionPast() {
        positionPast_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
        return this;
      }

      private double heading_ ;
      /**
       * <code>optional double Heading = 4;</code>
       */
      public boolean hasHeading() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional double Heading = 4;</code>
       */
      public double getHeading() {
        return heading_;
      }
      /**
       * <code>optional double Heading = 4;</code>
       */
      public Builder setHeading(double value) {
        bitField0_ |= 0x00000008;
        heading_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional double Heading = 4;</code>
       */
      public Builder clearHeading() {
        bitField0_ = (bitField0_ & ~0x00000008);
        heading_ = 0D;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:PrediMes)
    }

    static {
      defaultInstance = new PrediMes(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:PrediMes)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PrediMes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PrediMes_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n(main/proto/PredictionDisplay_Proto.pro" +
      "to\"S\n\010PrediMes\022\n\n\002ID\030\001 \002(\t\022\024\n\014positionPr" +
      "ed\030\002 \003(\001\022\024\n\014positionPast\030\003 \003(\001\022\017\n\007Headin" +
      "g\030\004 \001(\001B\'\n\031com.atc.simulator.PredDisB\nPr" +
      "edToDisp"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_PrediMes_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_PrediMes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_PrediMes_descriptor,
        new java.lang.String[] { "ID", "PositionPred", "PositionPast", "Heading", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}