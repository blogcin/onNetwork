package xyz.miffle.onnetwork.network;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class NetworkProp {

    private static NetworkProp instance;
    private static final Object instanceLock = new Object();

    private static final Pattern IP_MATCHING_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public class NetworkAdpaterInfo {
        private String ipAddress;
        private String adapterName;


        public NetworkAdpaterInfo(String ipAddress, String adapterName) {
            this.ipAddress = ipAddress;
            this.adapterName = adapterName;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getAdapterName() {
            return adapterName;
        }

        public String getBroadCastAddress() {
            if (ipAddress != null && isIpAddress(ipAddress)) {
                String[] split = ipAddress.split("\\.");

                if (split.length == 4) {
                    return split[0] + "." + split[1] + "." + split[2] + ".255";
                }

            }

            return null;
        }

        private boolean isIpAddress(String ipAddress) {
            if (ipAddress != null) {
                return IP_MATCHING_PATTERN.matcher(ipAddress).matches();
            }

            return false;
        }
    }

    public static NetworkProp getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                instance = new NetworkProp();
            }
        }

        return instance;
    }


    private NetworkProp() {

    }

    public ArrayList<NetworkAdpaterInfo> getAdaptersAddress() {
        ArrayList<NetworkAdpaterInfo> networkAdpaterInfos = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (!iface.isLoopback() && iface.isUp()) {

                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while(addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();

                        if (!(addr instanceof Inet6Address)) {
                            networkAdpaterInfos.add(new NetworkAdpaterInfo(addr.getHostAddress(), iface.getDisplayName()));
                        }

                    }
                }

            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return networkAdpaterInfos;
    }

}
