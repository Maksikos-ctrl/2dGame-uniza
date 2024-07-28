package com.maksikos.game.net.packets;


import com.maksikos.game.net.Client;
import com.maksikos.game.net.Server;
import lombok.Getter;

@Getter
public class PacketLogin extends Packet {

    private final String username;
    private final int x, y;

    public PacketLogin(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
    }

    public PacketLogin(String username, int x, int y) {
        super(00);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    @Override
    public void writeData(Client client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username + "," + x + "," + y).getBytes();
    }
}