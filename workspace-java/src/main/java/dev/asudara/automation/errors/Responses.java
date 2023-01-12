package dev.asudara.automation.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Responses {
    private Boolean isError;
    private String msg;
    public Responses(){

    }
    public Responses(Boolean isError, String msg) {
        this.isError = isError;
        this.msg = msg;
    }
    public Boolean getIsError() {
        return isError;
    }
    public void setIsError(Boolean isError) {
        this.isError = isError;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
