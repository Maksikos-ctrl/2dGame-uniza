package com.maksikos.game.net.packets;

import com.maksikos.game.net.Client;
import com.maksikos.game.net.Server;
import lombok.Getter;

@Getter
public class PacketMove extends Packet {

    private final String username;
    private final int x, y;

    private final int numSteps;
    private final boolean isMoving;
    private final int movingDir;

    public PacketMove(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.numSteps = Integer.parseInt(dataArray[3]);
        this.isMoving = Integer.parseInt(dataArray[4]) == 1;
        this.movingDir = Integer.parseInt(dataArray[5]);
    }

    public PacketMove(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.numSteps = numSteps;
        this.isMoving = isMoving;
        this.movingDir = movingDir;
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
        return ("02" + this.username + "," + this.x + "," + this.y + "," + this.numSteps + "," + (isMoving ? 1 : 0)
                + "," + this.movingDir).getBytes();
    }
}
