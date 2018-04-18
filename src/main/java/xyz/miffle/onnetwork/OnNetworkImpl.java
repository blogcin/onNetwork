package xyz.miffle.onnetwork;

import xyz.miffle.onnetwork.broadcast.BroadCastData;

public interface OnNetworkImpl {

    void onBroadCastReceived(BroadCastData data);

}
