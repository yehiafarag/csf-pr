/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.listeners;

import java.io.Serializable;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Yehia Farag
 */
public class SessionListener implements HttpSessionListener, Serializable{

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        System.out.println("session started");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
       
        System.gc();
    }

   
    
}
