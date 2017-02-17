package com.ecec.rweber.system.logger;

import org.apache.log4j.Logger;
import com.ecec.rweber.utils.SettingsReader;

public class SystemLogger {
	private Logger m_log = null;
	private SettingsReader m_settings = null;
	private boolean m_shouldRun = true;
	private String m_user = null;
	
	public SystemLogger(SettingsReader settings){
		m_log = Logger.getLogger(this.getClass());
		m_settings = settings;
		
		//get the logged in user
		m_user = System.getProperty("user.name");
	}
	
	public void start(){
		m_log.info("Start logging for " + m_user);
		
		Thread gatherState = new Thread(new GatherState(Integer.parseInt(m_settings.getSetting("idle_time")),Integer.parseInt(m_settings.getSetting("away_time"))));
		gatherState.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				m_log.info("shutting down");
			}
		});
	}
	
	public void stop(){
		m_log.info("Stop logging for " + m_user);
		m_shouldRun = false;
	}
	
	class GatherState implements Runnable {
		private int m_idleTime = 0;
		private int m_awayTime = 0;
		private WorkstationState m_state = WorkstationState.UNKNOWN;
		
		public GatherState(int idle, int away){
			m_idleTime = idle;
			m_awayTime = away;
		}
		
		@Override
		public void run() {
			m_log.info("using " + m_idleTime + " seconds for idle and " + m_awayTime + " minutes for away");
			
			while (m_shouldRun) {
				int idleSec = WindowsAPI.getIdleTimeMillisWin32() / 1000;
				WorkstationState newState =
					idleSec < m_idleTime ? WorkstationState.ONLINE :
					idleSec > m_awayTime * 60 ? WorkstationState.AWAY : WorkstationState.IDLE;
					
				if (newState != m_state) {
					m_state = newState;
					m_log.info(m_user + " is # " + m_state + " #");
				}
				try { Thread.sleep(1000); } catch (Exception ex) {}
			}
		}
		
	}
}
