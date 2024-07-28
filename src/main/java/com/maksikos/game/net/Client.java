package com.maksikos.game.net;

import com.maksikos.game.Game;
import com.maksikos.game.entities.PlayerMP;
import com.maksikos.game.net.packets.Packet;
import com.maksikos.game.net.packets.PacketLogin;
import com.maksikos.game.net.packets.PacketDisconnect;
import com.maksikos.game.net.packets.PacketMove;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



public class Client extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public Client(Game game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new PacketLogin(data);
                handleLogin((PacketLogin) packet, address, port);
                break;
            case DISCONNECT:
                packet = new PacketDisconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                        + ((PacketDisconnect) packet).getUsername() + " has left the world...");
                game.level.removePlayerMP(((PacketDisconnect) packet).getUsername());
                break;
            case MOVE:
                packet = new PacketMove(data);
                handleMove((PacketMove) packet);
        }
    }

    public void sendData(byte[] data) {
        if (!game.isApplet) {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin(PacketLogin packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername()
                + " has joined the game...");
        PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
        game.level.addEntity(player);
    }

    private void handleMove(PacketMove packet) {
        this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(),
                packet.isMoving(), packet.getMovingDir());
    }
}