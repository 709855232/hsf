package com.weidiango.spring.util;

import org.springframework.stereotype.Component;

/**
 @author mht
 @date 2019/9/17 16:22 */
public class Consumer {

    private String version;

    private String group;

    private String target;

    private String  connectionNum;

    private String  clientTimeout;

    private String maxWaitTimeForCsAddress;

    private String packageName;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getConnectionNum() {
        return connectionNum;
    }

    public void setConnectionNum(String connectionNum) {
        this.connectionNum = connectionNum;
    }

    public String getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(String clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public String getMaxWaitTimeForCsAddress() {
        return maxWaitTimeForCsAddress;
    }

    public void setMaxWaitTimeForCsAddress(String maxWaitTimeForCsAddress) {
        this.maxWaitTimeForCsAddress = maxWaitTimeForCsAddress;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}


