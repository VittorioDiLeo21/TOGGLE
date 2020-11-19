package it.polito.toggle;

import java.util.List;

public class ClassData {
    private List<String> tests;
    private String logFile;

    public ClassData(List<String> tests, String logFile) {
        this.tests = tests;
        this.logFile = logFile;
    }

    public List<String> getTests() {
        return tests;
    }

    public void setTests(List<String> tests) {
        this.tests = tests;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
}
