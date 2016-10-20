package com.server.model;

/**
 * Created by classic1999 on 16/9/8.
 */
public class Withdraw {

    private Long id;
    private String outs;
    private String encryptOuts;
    private Long createTime;
    private Integer isDynamicFee;
    private String txHash;
    private TxStatus status = TxStatus.init;

    public Withdraw() {
    }

    public Withdraw(Long id, String outs, String encryptOuts, Long createTime, Integer isDynamicFee) {
        this.id = id;
        this.outs = outs;
        this.encryptOuts = encryptOuts;
        this.createTime = createTime;
        this.isDynamicFee = isDynamicFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOuts() {
        return outs;
    }

    public void setOuts(String outs) {
        this.outs = outs;
    }

    public String getEncryptOuts() {
        return encryptOuts;
    }

    public void setEncryptOuts(String encryptOuts) {
        this.encryptOuts = encryptOuts;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDynamicFee() {
        return isDynamicFee;
    }

    public void setIsDynamicFee(Integer isDynamicFee) {
        this.isDynamicFee = isDynamicFee;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Withdraw{" +
                "id=" + id +
                ", outs='" + outs + '\'' +
                ", encryptOuts='" + encryptOuts + '\'' +
                ", createTime=" + createTime +
                ", isDynamicFee='" + isDynamicFee + '\'' +
                ", txHash='" + txHash + '\'' +
                ", status=" + status +
                '}';
    }
}
