CREATE TABLE btc_address (
  id          BIGINT PRIMARY KEY,
  batchNo     BIGINT,
  addressType VARCHAR(30),
  address     VARCHAR(128),
  batchIndex  BIGINT,
  createTime  BIGINT,
  status      VARCHAR(50)
);

CREATE TABLE withdraw (
  id           INTEGER PRIMARY KEY,
  outs         VARCHAR(256),
  encryptOuts  VARCHAR(512),
  createTime   BIGINT,
  isDynamicFee INTEGER,
  txHash       VARCHAR(256),
  status       VARCHAR(32)
);
