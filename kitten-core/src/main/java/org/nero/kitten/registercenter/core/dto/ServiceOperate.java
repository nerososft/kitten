package org.nero.kitten.registercenter.core.dto;

import org.nero.kitten.registercenter.core.dto.OperateType;

import java.io.Serializable;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/15
 * Time   下午4:30
 */
public class ServiceOperate<T> implements Serializable {
    private OperateType operateType;
    private String token;
    private  T data;

    public ServiceOperate() {
    }

    public ServiceOperate(OperateType operateType, T data) {
        this.operateType = operateType;
        this.data = data;
    }

    public ServiceOperate(OperateType operateType, String token, T data) {
        this.operateType = operateType;
        this.token = token;
        this.data = data;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServiceOperate{" +
                "operateType=" + operateType +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
