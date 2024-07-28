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
import java.util.ArrayList;
import java.util.List;



public class Server extends Thread {

    private DatagramSocket socket;
    private Game game;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public Server(Game game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
                this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            case LOGIN:
                packet = new PacketLogin(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                        + ((PacketLogin) packet).getUsername() + " has connected...");
                PlayerMP player = new PlayerMP(game.level, 100, 100, ((PacketLogin) packet).getUsername(), address, port);
                this.addConnection(player, (PacketLogin) packet);
                break;
            case DISCONNECT:
                packet = new PacketDisconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                        + ((PacketDisconnect) packet).getUsername() + " has left...");
                this.removeConnection((PacketDisconnect) packet);
                break;
            case MOVE:
                packet = new PacketMove(data);
                this.handleMove(((PacketMove) packet));
                break;
            default:
                System.out.println("Received unexpected packet type: " + type);
                break;
        }
    }

    public void addConnection(PlayerMP player, PacketLogin packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if (p.getIpAddress() == null) {
                    p.setIpAddress(player.getIpAddress());
                }
                if (p.getPort() == -1) {
                    p.setPort(player.getPort());
                }
                alreadyConnected = true;
            } else {
                sendData(packet.getData(), p.getIpAddress(), p.getPort());
                packet = new PacketLogin(p.getUsername(), p.getX(), p.getY());
                sendData(packet.getData(), player.getIpAddress(), player.getPort());
            }
        }
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(PacketDisconnect packet) {
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }

    public PlayerMP getPlayerMP(String username) {
        for (PlayerMP player : this.connectedPlayers) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerMPIndex(String username) {
        for (int index = 0; index < connectedPlayers.size(); index++) {
            if (connectedPlayers.get(index).getUsername().equals(username)) {
                return index;
            }
        }
        return -1;
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        if (socket != null && !socket.isClosed()) {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
            try {
                this.socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            sendData(data, p.getIpAddress(), p.getPort());
        }
    }

    private void handleMove(PacketMove packet) {
        PlayerMP player = getPlayerMP(packet.getUsername());
        if (player != null) {
            player.setX(packet.getX());
            player.setY(packet.getY());
            player.setMoving(packet.isMoving());
            player.setMovingDir(packet.getMovingDir());
            player.setNumSteps(packet.getNumSteps());
            packet.writeData(this);
        }
    }
}