package org.nero.kitten.common.core.request;

import java.io.Serializable;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:13
 */
public class InvokeRequest implements Serializable {
    private String serviceName;


    public InvokeRequest() {
    }

    public InvokeRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "InvokeRequest{" +
                "serviceName='" + serviceName + '\'' +
                '}';
    }
}
