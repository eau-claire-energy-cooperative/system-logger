# System Logger

This program runs a small Windows application that logs system events (logon, logoff, idle activity) based on some user defined parameters. The end result is a log that contains information on when the system was idle. This time is calculated based on how many seconds have elsapsed since the last input event recorded by Windows (mouse or keyboard). 

The program does run in the Windows system tray but does not contain any user options while running. 

## Installation

Use Maven to compile the program. A zip file will be created in the target directory that contains all the necessary files to run the application. A pre-compiled Windows wrapper, created by [Launch4j](http://launch4j.sourceforge.net/) is provided to make launching easier.

Ideally you'd want to launch this on startup via a hook in the Startup folder of Windows, or via some kind of login script in an Active Directory environment. 

## Configuration

Edit the ```config.xml``` file in the resources directory. 

* idle_time - the amount of time (in seconds) before an IDLE entry is made
* away_time - the amount of time (in minutes) before an AWAY entry is made
* log name - the name of the log file (keep empty for default)
* log location - the location of the log file (keep empty for default)

## Example Output

```
INFO 05 Oct 2018 10:41:53,464 - Log Path is: C:\git\system-logger/logs/system-logger.log 
INFO 05 Oct 2018 10:41:53,581 - Start logging for rweber 
INFO 05 Oct 2018 10:41:53,583 - using 30 seconds for idle and 5 minutes for away 
INFO 05 Oct 2018 10:41:54,194 - rweber is # ONLINE # 
INFO 05 Oct 2018 10:42:50,208 - rweber is # IDLE # 
INFO 05 Oct 2018 10:43:24,221 - rweber is # ONLINE # 
INFO 05 Oct 2018 10:44:45,252 - rweber is # IDLE # 
INFO 05 Oct 2018 10:47:37,312 - rweber is # ONLINE # 
```