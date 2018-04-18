package xyz.miffle.onnetwork.broadcast;

import xyz.miffle.onnetwork.OnNetworkConstants;

public enum BroadCastDataType {

    DATA_TYPE_FROM_SERVER(OnNetworkConstants.DATA_TYPE_FROM_SERVER),
    DATA_TYPE_FROM_CLIENT(OnNetworkConstants.DATA_TYPE_FROM_CLIENT);

    private String data;

    BroadCastDataType(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}
