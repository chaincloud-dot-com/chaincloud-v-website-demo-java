package com.server.model;

/**
 * Created by classic1999 on 16/9/8.
 */
public enum TxStatus {
    init, pending, success {
        public boolean isRunning() {
            return false;
        }
    }, fail {
        public boolean isRunning() {
            return false;
        }
    };

    public boolean isRunning() {
        return true;
    }

    public boolean isFinish() {
        return !isRunning();
    }

}
