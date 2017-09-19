package ServiceNotify;

import java.io.Serializable;
import java.util.List;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:39
 */
public class SubscribeRequest implements Serializable {

    private String ip;
    private Integer port;
    private List<String>  serviceList;

    public SubscribeRequest() {
    }

    public SubscribeRequest(String ip, Integer port, List<String> serviceList) {
        this.ip = ip;
        this.port = port;
        this.serviceList = serviceList;
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

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public String toString() {
        return "SubscribeRequest{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", serviceList=" + serviceList +
                '}';
    }
}
