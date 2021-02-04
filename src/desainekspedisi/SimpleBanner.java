/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desainekspedisi;

/**
 *
 * @author rxxxx
 */
/* Simple banner applet
    This applet creates athread that scrolls
    the message containted in msg right to left
    sccross the applet's window.
*/
import java.awt.*;
import java.applet.*;
/*
<applet code="SimpleBanner" width=300 height=50>
<param name=message value="java makes the web move!">
</applet>
*/
public class SimpleBanner extends Applet implements Runnable {
    String msg = " A Simple Moving Banner.";
    Thread t = null;
    int state;
    boolean stopFlag;
    
    // Set colors and initializa thread.
    public void init() {
        setBackground(Color.cyan);
        setForeground(Color.red);
        
    }
    
    //Start a thread
    public void start() {
        t = new Thread(this);
        stopFlag = false;
        t.start();
    }
    
    // Entry point for the thread that runs the banner.
    public void run() {
        char ch;
        
        // Display banner
        for( ; ; ) {
            try{
            repaint();
            Thread.sleep(250);
            ch = msg.charAt(0);
            msg = msg.substring(1, msg.length());
            msg += ch;
            if(stopFlag)
                break;
        }  catch(InterruptedException e) {}
        } 
    }
    //pause the banner
    public void stop(){
    stopFlag = true;
    t = null;
}
    //Display the banner.
    public void paint(Graphics g) {
        g.drawString(msg, 50, 30);
    }
    
}

            
            