package io.easyware.shared.log;

import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;

@Log
@ApplicationScoped
public class Logger {

    private final LogMessageRepository logMessageRepository;

    private String className;

    public Logger(LogMessageRepository logMessageRepository) {
        this.logMessageRepository = logMessageRepository;
    }

    public enum LogType {
        INFO, WARN, ERROR
    }

    private void log(String message) {
        this.log(message, null);
    }

    private void log(String message, LogType logType) {
        this.log(message, logType, null);
    }

    private void log(String message, LogType logType, String user) {
        switch (logType) {
            case ERROR:
                log.severe(message);
                break;
            case WARN:
                log.warning(message);
                break;
            case INFO:
                log.info(message);
                break;
            default:
                log.info(message);
        }
        this.saveLogInDatabase(message, logType, user);
    }

    private void saveLogInDatabase(String message, LogType logType, String user) {
        LogMessage logEntry = new LogMessage();
        logEntry.setType(logType.toString());
        logEntry.setUser(user);
        logEntry.setMessage(message);
        //logEntryRepository.persist(logEntry);
    }

    public void info(String message) {
        this.log(message, LogType.INFO);
    }
    public void info(String message, String user) {
        this.log(message, LogType.INFO, user);
    }

    public void severe(String message) {
        this.log(message, LogType.ERROR);
    }
    public void severe(String message, String user) {
        this.log(message, LogType.ERROR, user);
    }

    public void error(String message) {
        this.log(message, LogType.ERROR);
    }
    public void error(String message, String user) {
        this.log(message, LogType.ERROR, user);
    }

    public void warning(String message) {
        this.log(message, LogType.WARN);
    }
    public void warning(String message, String user) {
        this.log(message, LogType.WARN, user);
    }

}
