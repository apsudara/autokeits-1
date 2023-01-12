package dev.asudara.automation.errors;

public class AutomationException extends Exception {
    private String msg;
    public AutomationException(String msg){
       this.msg = msg;
    }
    @Override
    public String getMessage(){
        return this.msg;
    }
}
