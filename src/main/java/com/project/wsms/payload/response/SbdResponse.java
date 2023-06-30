package com.project.wsms.payload.response;

import java.sql.Timestamp;

public interface SbdResponse {
    Timestamp getDay();
    Integer getTprofit();
    Integer getTrevenue();
    Integer getTsales();
    Integer getTorder();
    Integer getTproduct();
    Integer getTdiscount();
    Integer getTshipfee();
}
