package main.java.core.request;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:13
 */
public class RegisterRequest implements Serializable {

    private String ip;
    private Integer port;
    private String appName;
    private String serviceName;
    private Class<?>[] methods;
    private Class<?>[][] parameterTypes;

    public RegisterRequest() {
    }

    public RegisterRequest(String ip, Integer port, String appName, String serviceName, Class<?>[] methods, Class<?>[][] parameterTypes) {
        this.ip = ip;
        this.port = port;
        this.appName = appName;
        this.serviceName = serviceName;
        this.methods = methods;
        this.parameterTypes = parameterTypes;
    }


    @Override
    public String toString() {
        return "RegisterRequest{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", appName='" + appName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", methods=" + Arrays.toString(methods) +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Class<?>[] getMethods() {
        return methods;
    }

    public void setMethods(Class<?>[] methods) {
        this.methods = methods;
    }

    public Class<?>[][] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[][] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

}
