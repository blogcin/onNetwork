package xyz.miffle.onnetwork;

import com.google.gson.Gson;
import xyz.miffle.onnetwork.broadcast.BroadCastData;
import xyz.miffle.onnetwork.broadcast.BroadCastDataType;
import xyz.miffle.onnetwork.network.NetworkProp;
import java.util.ArrayList;

public class OnNetwork {

    private NetworkEngine networkEngine;

    private OnNetwork() {
        networkEngine = NetworkEngine.getInstance();
    }

    public static OnNetwork generate() {
        return new OnNetwork();
    }

    public void sendBroadCast(ArrayList<NetworkProp.NetworkAdpaterInfo> addresses, String nickname, String message) {
        if (nickname != null && message != null && addresses != null) {
            Gson gson = new Gson();

            for(NetworkProp.NetworkAdpaterInfo info : addresses) {
                BroadCastData broadCastData = new BroadCastData(message, nickname, info.getIpAddress(),
                        BroadCastDataType.DATA_TYPE_FROM_SERVER);

                String payload = gson.toJson(broadCastData);
                networkEngine.sendBroadCast(info, payload);
            }
        }
    }

    public void receiveBroadCastStart(OnNetworkImpl callback) {
        networkEngine.listenBroadCastStart(callback);
    }

    public void receiveBroadCastStop() {
        networkEngine.listenBroadCastStop();
    }

    /*
    public static void main(String[] args) {
        OnNetwork onNetwork = OnNetwork.generate();
        NetworkEngine.getInstance().listenBroadCastStart(new OnNetworkImpl() {
            @Override
            public void onBroadCastReceived(byte[] data) {
                System.out.println("Received : " + new String(data));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NetworkEngine.getInstance().listenBroadCastStop();
            }
        }).start();

        onNetwork.sendBroadCast(NetworkProp.getInstance().getAdaptersAddress(), "Hello!", "This is message");
    }*/

}
