package org.nero.kitten.kitten.core;

import java.io.Serializable;

/**
 * Author :  root
 * Email  :  nerosoft@outlook.com
 * Date   :  16-11-17
 * Time   :  下午4:36
 */
public class KittenResponse implements Serializable {

    private String requestId;
    private Throwable error;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isError() {
        return error == null?true:false;
    }




    @Override
    public String toString() {
        return "KittenResponse{" +
                "requestId='" + requestId + '\'' +
                ", error=" + error +
                ", result=" + result +
                '}';
    }
}
