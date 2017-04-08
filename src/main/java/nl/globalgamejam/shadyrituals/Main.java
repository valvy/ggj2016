package nl.globalgamejam.shadyrituals;

import nl.hvanderheijden.prutengine.Application;


public class Main{
    public static void main(String[] args) {
        System.out.println("Welcome to Shady ritual");
        System.out.println("To set the server set Server:<127.0.IP.adress>");
        
        for(String str : args){
            if(str.equals("about")){
                System.out.println("Created by Jeffrey Verbeek, Wander van der Wal");
                System.out.println("Heiko van der Heijden and Eddy Meivogel");
                return;
            }else if(str.startsWith("Server:")){
                System.out.println("Connecting to server : " + str.replace("Server:", ""));
                Globals.IP_ADRES = str.replace("Server:", "");
            }
        }
        
       
        if(System.getProperty("os.name").equals("Mac OS X")){
            //System.setProperty("javafx.macosx.embedded", "true");
 //           java.awt.Toolkit.getDefaultToolkit();

             System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        }
        Application.getInstance().loadScene(new SplashScreen());
    }
}