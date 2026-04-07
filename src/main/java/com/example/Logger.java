package com.example;

public class Logger {
    public void log(String message) {
        try{
            
        }catch(Exception e){
            logger.erro("Error: ",e);
        }
        //System.out.println("LOG: " + message);
         System.out.println("LOG: " + message);
    }
}
