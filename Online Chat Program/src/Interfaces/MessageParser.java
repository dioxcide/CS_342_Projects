package Interfaces;

public interface MessageParser {
	// Parsing functions. This one parses for the Sending User name
	public String getUser(String temp);
	
	// Parsing functions. This one parses for the Message being sent
	public String getMsg(String temp);

	// Parsing Functions. This one parses for the Recipient user
	public String getTo(String temp);
}
