package main.ons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test{
	public static void main(String[] args){
		Logger logger = LoggerFactory.getLogger(Test.class);
		int amount= 1000;
		int balance = 333333;
		logger.info("amount: {}, balance: {}",amount,balance);
	}
}