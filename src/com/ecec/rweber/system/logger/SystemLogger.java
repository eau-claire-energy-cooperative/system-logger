package com.ecec.rweber.system.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ecec.rweber.utils.SettingsReader;

public class SystemLogger {
	private Logger m_log = null;
	private SettingsReader m_settings = null;
	private boolean m_shouldRun = true;
	private String m_user = null;
	
	public SystemLogger(){
		m_log = Logger.getLogger(this.getClass());
		m_settings = new SettingsReader("resources/config.xml");
		
		//get the logged in user
		m_user = System.getProperty("user.name");
	}
	
	public void start(){
		m_log.info("Start logging for " + m_user);
		
		Thread gatherState = new Thread(new GatherState());
		gatherState.start();
	}
	
	public void stop(){
		m_log.info("Stop logging for " + m_user);
		m_shouldRun = false;
	}
	
	private void logState(){
		
	}
	
	class GatherState implements Runnable {
		private WorkstationState m_state = WorkstationState.UNKNOWN;
		private DateFormat m_dateFormat = null;
		
		public GatherState(){
			m_dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		}
		
		@Override
		public void run() {
			while (m_shouldRun) {
				int idleSec = WindowsAPI.getIdleTimeMillisWin32() / 1000;
				WorkstationState newState =
					idleSec < 30 ? WorkstationState.ONLINE :
					idleSec > 5 * 60 ? WorkstationState.AWAY : WorkstationState.IDLE;
					
				if (newState != m_state) {
					m_state = newState;
					m_log.info(m_dateFormat.format(new Date()) + " # " + m_state);
				}
				try { Thread.sleep(1000); } catch (Exception ex) {}
			}
		}
		
	}
}
