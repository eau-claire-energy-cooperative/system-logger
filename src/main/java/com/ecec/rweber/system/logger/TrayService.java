package com.ecec.rweber.system.logger;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.SimpleLayout;

import com.ecec.rweber.utils.SettingsReader;

public class TrayService {
	private Logger m_log = null;
	private SettingsReader m_settings = null;
	private SystemLogger m_thread = null;
	
	//for the gui
	private TrayIcon m_trayIcon = null;
	
	public TrayService(){
		m_settings = new SettingsReader("resources/config.xml");
		
		setupLogger();
		
		m_thread = new SystemLogger(m_settings);
	}
	
	public void run(){
		//Check the SystemTray support
        if (!SystemTray.isSupported()) {
            m_log.error("SystemTray is not supported");
            return;
        }
        
        final SystemTray tray = SystemTray.getSystemTray();
        m_trayIcon = new TrayIcon(createImage("resources/logger-small.png", "System Logger"));
        m_trayIcon.setToolTip("System Logger");
        
        try {
            tray.add(m_trayIcon);
        } catch (AWTException e) {
            m_log.error("TrayIcon could not be added.");
            return;
        }
        
        //start the logging operation
        m_thread.start();
	}
	
	private void setupLogger(){
		String directory = System.getProperty("user.dir") + "/logs/";
		String name = "system-logger";
		
		m_log = Logger.getLogger(this.getClass());
		
		Logger rootLog = Logger.getRootLogger();
		rootLog.setLevel(Level.INFO);
		
		//check for custom locations
		if(!m_settings.getSetting("log.location").isEmpty())
		{
			directory = m_settings.getSetting("log.location");
		}
		
		if(!m_settings.getSetting("log.name").isEmpty())
		{
			name = m_settings.getSetting("log.name");
		}
		
		try{
			//setup the console
			//rootLog.addAppender(new ConsoleAppender(new SimpleLayout(),ConsoleAppender.SYSTEM_OUT));
			rootLog.addAppender(new DailyRollingFileAppender(new PatternLayout("%p %d{DATE} - %m %n"),directory + name + ".log","yyyy-ww"));
		}
		catch(Exception e)
		{
			m_log.error("Cannot write to log file");
			e.printStackTrace();
		}
		
		m_log.info("Log Path is: " + directory + name + ".log");
	}
	
	private Image createImage(String path, String description) {
       File f = new File(path);
       
        if (path == null) {
            m_log.error("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath()), description)).getImage();
        }
	}
	
	public static void main(String[] args){
		//set the look and feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			System.exit(1);
		}
		
		if(!System.getProperty("os.name").contains("Windows"))
		{
			//only runs on windows
			System.exit(1);
		}
		
		TrayService service = new TrayService();
		service.run();
	}
}
