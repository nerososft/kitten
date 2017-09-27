package main.java.core.request;

import java.io.Serializable;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:14
 */
public class Request<T> implements Serializable {
    private String requestID;
    private ServiceRequestType serviceRequestType;
    private T data;


    public Request() {
    }

    public Request(String requestID, ServiceRequestType serviceRequestType, T data) {
        this.requestID = requestID;
        this.serviceRequestType = serviceRequestType;
        this.data = data;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public ServiceRequestType getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(ServiceRequestType serviceRequestType) {
        this.serviceRequestType = serviceRequestType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestID='" + requestID + '\'' +
                ", serviceRequestType=" + serviceRequestType +
                ", data=" + data +
                '}';
    }
}
