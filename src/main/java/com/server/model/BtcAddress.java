package com.server.model;

public class BtcAddress {
    Long id;
    Long batchNo;
    AddressType addressType;
    String address;
    Long batchIndex;
    long createTime;
    AddressStatus status = AddressStatus.init;

    public BtcAddress() {
    }

    public BtcAddress(Long id, Long batchNo, AddressType type, String address, Long index) {
        this.id = id;
        this.batchNo = batchNo;
        this.addressType = type;
        this.address = address;
        this.batchIndex = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String name) {
        this.address = name;
    }

    public Long getBatchIndex() {
        return batchIndex;
    }

    public void setBatchIndex(Long index) {
        this.batchIndex = index;
    }

    public AddressStatus getStatus() {
        return status;
    }

    public void setStatus(AddressStatus status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }


    @Override
    public String toString() {
        return "BtcAddress{" +
                "id=" + id +
                ", batchNo=" + batchNo +
                ", addressType=" + addressType +
                ", address='" + address + '\'' +
                ", batchIndex=" + batchIndex +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}