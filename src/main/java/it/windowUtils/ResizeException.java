package it.windowUtils;

public class ResizeException extends Exception {
    private int lastError;
    public ResizeException() {super();}
    public ResizeException(int lastError) {
        super();
        this.lastError = lastError;
    }
    public int getLastError() { return lastError; }
}
