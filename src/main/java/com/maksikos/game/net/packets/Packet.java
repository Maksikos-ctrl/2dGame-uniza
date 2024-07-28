package com.maksikos.game.net.packets;

import com.maksikos.game.net.Client;
import com.maksikos.game.net.Server;


import lombok.Getter;

public abstract class Packet {

    @Getter
    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);

        private final int packetId;

        PacketTypes(int packetId) {
            this.packetId = packetId;
        }
    }

    @Getter
    private final byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(Client client);

    public abstract void writeData(Server server);

    public String readData(byte[] data) {
        return new String(data).trim().substring(2);
    }

    public abstract byte[] getData();

    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }

    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes type : PacketTypes.values()) {
            if (type.getPacketId() == id) {
                return type;
            }
        }
        return PacketTypes.INVALID;
    }
}
