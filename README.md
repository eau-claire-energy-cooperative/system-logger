## System Logger

This program runs a small windows application that logs system events (logon, logoff, idle activity) based on some user defined parameters. 

### Configuration

Edit the config.xml file in the resources directory. 

* idle_time - the amount of time (in seconds) before an IDLE entry is made
* away_time - the amount of time (in minutes) before an AWAY entry is made
* log name - the name of the log file (keep empty for default)
* log location - the location of the log file (keep empty for default)
