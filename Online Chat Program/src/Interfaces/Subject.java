package Interfaces;

import java.net.Socket;


public interface Subject {
	public void addOnlineUsers();
	public void removeOnlineUsers(Socket newSocket);
	public void notifyClients(int option, String message, Socket Originator);
}
