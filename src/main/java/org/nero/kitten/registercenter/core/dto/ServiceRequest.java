package org.nero.kitten.registercenter.core.dto;

import java.io.Serializable;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/15
 * Time   下午4:41
 */
public class ServiceRequest<T> implements Serializable {
    private String requestId;
    private T data;

    public ServiceRequest(String requestId, T data) {
        this.requestId = requestId;
        this.data = data;
    }

    public ServiceRequest() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId='" + requestId + '\'' +
                ", data=" + data +
                '}';
    }
}
