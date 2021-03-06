package chat_msg_user_info;

import java.io.Serializable;
import java.net.Socket;

import Interfaces.MessageParser;

//Java Encapsulation Class that stores chat messages being set to and from 
public class ChatMessage implements Serializable, MessageParser{

	private static final long serialVersionUID = 1L;
	private String m;
	private String from;			//Data being encapsulated
	private String to;
	private Socket userSocket;
	
	//Main constructor that stores the encapsulated data
	public ChatMessage(String unparsedMsg, Socket userSocket){
		this.from = getUser(unparsedMsg);
		this.m = this.from+":"+getMsg(unparsedMsg);
		this.to = getTo(unparsedMsg);
		this.userSocket = userSocket;
	}

	// Parsing functions. This one parses for the Sending User name
	public String getUser(String temp) {
		String delims = ":";
		String[] tokens = temp.split(delims);
		String user = tokens[3];
		return user;
	}

	// Parsing functions. This one parses for the Message being sent
	public String getMsg(String temp) {
		String delims = ":";
		String[] tokens = temp.split(delims);
		String msg = tokens[4];
		return msg;
	}

	// Parsing Functions. This one parses for the Recipient user
	public String getTo(String temp) {
		String delims = ":";
		String[] tokens = temp.split(delims);
		String To = tokens[1];
		return To;
	}
	//Several Getter functions.
	public String getMessage(){
		return this.m;
	}
	public String getFrom(){
		return this.from;
	}
	public String getTo(){
		return this.to;
	}
	public Socket getSocket(){
		return this.userSocket;
	}
}
