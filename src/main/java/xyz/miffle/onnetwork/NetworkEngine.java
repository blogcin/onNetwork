package xyz.miffle.onnetwork;

import xyz.miffle.onnetwork.network.NetworkProp;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkEngine {

    private static NetworkEngine instance;
    private static final Object instanceLock = new Object();

    private static final int BROADCAST_PORT = 5766;

    private BroadCastReceiveThread broadCastReceiveThread;

    private class BroadCastReceiveThread extends Thread {
        private OnNetworkImpl callback;
        private DatagramSocket datagramSocket;
        private final AtomicBoolean running = new AtomicBoolean(false);
        private byte[] buf;
        private DatagramPacket datagramPacket;

        public BroadCastReceiveThread(OnNetworkImpl impl) throws SocketException {
            this.callback = impl;
            this.datagramSocket = new DatagramSocket(BROADCAST_PORT);
            this.buf = new byte[65535];
            this.datagramPacket = new DatagramPacket(buf, buf.length);
        }

        @Override
        public void start() {
            running.set(true);
            super.start();
        }

        public void stopThread() {
            running.set(false);
        }

        @Override
        public void run() {
            try {
                try {
                    while (running.get()) {
                        datagramSocket.receive(datagramPacket);

                        if (callback != null) {
                            callback.onBroadCastReceived(datagramPacket.getData());
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                if (datagramSocket != null && !datagramSocket.isClosed()) {
                    datagramSocket.close();
                    datagramSocket = null;
                }
            }

        }
    }

    private class BroadCastSendThread extends Thread {
        private String data;
        private InetAddress inetAddress;
        private DatagramPacket datagramPacket;
        private DatagramSocket datagramSocket;

        public BroadCastSendThread(NetworkProp.NetworkAdpaterInfo info, String data) throws UnknownHostException, SocketException {
            this.data = data;
            this.inetAddress = InetAddress.getByName(info.getBroadCastAddress());
            this.datagramSocket = new DatagramSocket();
        }

        @Override
        public void run() {
            try {
                byte[] payloadData = data.getBytes();
                datagramPacket = new DatagramPacket(payloadData, payloadData.length, inetAddress, BROADCAST_PORT);
                datagramSocket.send(datagramPacket);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listenBroadCastStart(OnNetworkImpl callback) {
        try {
            if (broadCastReceiveThread == null || !broadCastReceiveThread.isAlive()) {
                broadCastReceiveThread = new BroadCastReceiveThread(callback);
                broadCastReceiveThread.start();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void listenBroadCastStop() {
        if (broadCastReceiveThread != null && broadCastReceiveThread.isAlive()) {
            broadCastReceiveThread.stopThread();
        }
    }

    public boolean sendBroadCast(NetworkProp.NetworkAdpaterInfo info, String data) {
        try {
            BroadCastSendThread broadCastSendThread = new BroadCastSendThread(info, data);
            broadCastSendThread.start();

            return true;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return false;
    }

    private NetworkEngine() {

    }

    protected static NetworkEngine getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                instance = new NetworkEngine();
            }
        }

        return instance;
    }
}
