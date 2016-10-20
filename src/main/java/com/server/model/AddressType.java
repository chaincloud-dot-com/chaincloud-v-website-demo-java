package com.server.model;

/**
 * Created by classic1999 on 16/9/9.
 */
public enum AddressType {
    hotSend(1), coldReceive(2);
    Integer num;

    AddressType(Integer i) {
        this.num = i;
    }

    public Integer getNum() {
        return num;
    }
}
