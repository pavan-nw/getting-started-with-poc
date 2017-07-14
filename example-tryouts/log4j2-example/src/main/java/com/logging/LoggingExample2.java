package com.logging;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingExample2
{
	
	final static Logger logger = LogManager.getLogger(LoggingExample2.class);
	
	public static void main(String[] args) {
	
		LoggingExample2 obj = new LoggingExample2();
		
		try{
			obj.divide();
		}catch(ArithmeticException ex){
			logger.error("Sorry, something wrong!", ex);
		}
		
		
	}
	
	private void divide(){
		
		int i = 10 /0;

	}
	
}