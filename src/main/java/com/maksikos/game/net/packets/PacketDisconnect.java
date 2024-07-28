package com.maksikos.game.net.packets;

import com.maksikos.game.net.Client;
import com.maksikos.game.net.Server;
import lombok.Getter;

@Getter
public class PacketDisconnect extends Packet {

    private final String username;

    public PacketDisconnect(byte[] data) {
        super(01);
        this.username = readData(data);
    }

    public PacketDisconnect(String username) {
        super(01);
        this.username = username;
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
        return ("01" + this.username).getBytes();
    }
}
