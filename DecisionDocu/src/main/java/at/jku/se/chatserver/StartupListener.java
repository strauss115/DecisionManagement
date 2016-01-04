package at.jku.se.chatserver;

import java.net.InetAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class StartupListener implements ServletContextListener {

	private static final Logger log = LogManager.getLogger(StartupListener.class);
	
	private Server mServer;
	
	@Override
	public void contextDestroyed(ServletContextEvent e) {
		log.debug("Context destroyed!");
		if (mServer != null) {
			log.debug("Closing socket...");
			mServer.close();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		log.debug("Context initialized!");
		
		try {
			InetAddress ip = InetAddress.getLocalHost();
            log.debug("Server running on: " + ip);
		} catch (Exception e1) {
			log.error(e1);
		}
		
		mServer = new Server();
		new Thread(mServer).start();
	}

}
