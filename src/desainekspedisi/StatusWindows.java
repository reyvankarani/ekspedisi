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
import java.awt.*;
import java.applet.*;
/*
<applet code="StatusWindow" width=300 height=50>
</applet
*/
public class StatusWindows extends Applet{
    public void init() {
        setBackground(Color.cyan);
    }
    
    //Display msg in applet window.
    public void paint(Graphics g) {
        g.drawString("This is in the applet window.", 10, 20);
        showStatus("This is shown in the status window.");
    }
    
}
