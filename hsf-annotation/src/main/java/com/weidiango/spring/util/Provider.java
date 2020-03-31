package com.weidiango.spring.util;

/**
 @author mht
 @date 2019/9/17 16:22 */
public class Provider{

    private String serviceGroup;

    private String serviceVersion;

    private String clientTimeout;

    private String corePoolSize;

    private String enableTXC;

    private String maxPoolSize;

    private String serializeType;

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(String clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public String getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(String corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public String getEnableTXC() {
        return enableTXC;
    }

    public void setEnableTXC(String enableTXC) {
        this.enableTXC = enableTXC;
    }

    public String getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(String maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }
}
