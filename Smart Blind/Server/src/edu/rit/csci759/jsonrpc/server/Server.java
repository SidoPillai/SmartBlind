package edu.rit.csci759.jsonrpc.server;

public class Server {
	
	public static void main(String [] args) {
		
		System.out.println("Initiating server...");
		new JsonRPCServer().start();
	}

}