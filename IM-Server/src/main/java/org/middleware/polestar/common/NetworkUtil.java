package org.middleware.polestar.common;




import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {

    public static String getLocalIP(){
        String localIp = null;// 本地IP，如果没有配置外网IP则返回它
        String netIp = null;// 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
        InetAddress ip;
        boolean find = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !find) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        &&!ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")){// 外网IP
                    netIp = ip.getHostAddress();
                    find = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        &&!ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")){// 内网IP
                    localIp = ip.getHostAddress();
                }
            }
        }
        if (netIp != null && !"".equals(netIp)) {
            return netIp;
        } else {
            return localIp;

        }
    }

    public static String getProcessId(){
        String pName = ManagementFactory.getRuntimeMXBean().getName();
        return pName.split("@")[0];
    }
}
