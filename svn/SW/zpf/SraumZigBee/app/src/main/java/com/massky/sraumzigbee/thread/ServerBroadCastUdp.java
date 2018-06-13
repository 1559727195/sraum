package com.massky.sraumzigbee.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by zhu on 2017/6/14.
 */

public class ServerBroadCastUdp extends Thread {

    private String dataString;
    private DatagramSocket udpSocket;
    private byte[] buffer = null;
    private int MAX_DATA_PACKET_LENGTH;

    public ServerBroadCastUdp(String dataString, byte[] buffer, int maxDataPacketLength) {
        this.dataString = dataString;
        this.buffer = buffer;
        this.MAX_DATA_PACKET_LENGTH = maxDataPacketLength;
    }

    public void run() {
        DatagramPacket dataPacket = null;

        try {
            udpSocket = new DatagramSocket(9991);//服务器端UDP端口号，网关端口9991

            dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
            byte[] data = dataString.getBytes();
            dataPacket.setData(data);
            dataPacket.setLength(data.length);
            dataPacket.setPort(9991);

            InetAddress broadcastAddr;

            broadcastAddr = InetAddress.getByName("255.255.255.255");
            dataPacket.setAddress(broadcastAddr);
        } catch (Exception e) {
//            Log.e(LOG_TAG, e.toString());
        }
        // while( start ){
        try {
            udpSocket.send(dataPacket);
            sleep(10);
            udpSocket.close();
        } catch (Exception e) {
//            Log.e(LOG_TAG, e.toString());
        }
        // }

    }
}
