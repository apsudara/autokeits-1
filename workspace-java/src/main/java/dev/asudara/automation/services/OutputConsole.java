package dev.asudara.automation.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.util.ResourceUtils;

public class OutputConsole {
    File stdoutput;
    String stdoutpath;
    File stderror;
    String stderrpath;
    String configName;
    public OutputConsole() {
    }
    public OutputConsole(String host,String configName) throws IOException {
        this.stdoutpath = "" + ResourceUtils.getFile("classpath:static/").getPath() + "/logs/"  + host +".stdoutput";
        this.stdoutput = new File(stdoutpath);
        this.stdoutput.createNewFile();
        this.stderrpath = "" + ResourceUtils.getFile("classpath:static/").getPath() + "/logs/"  + host+".stderror";
        this.stderror = new File(stderrpath);
        this.configName = configName+host;
    }

    public File getStdoutput() {
        return stdoutput;
    }
    public void setStdoutput(File stdoutput) {
        this.stdoutput = stdoutput;
    }
    public String getStdoutpath() {
        return stdoutpath;
    }
    public void setStdoutpath(String stdoutpath) {
        this.stdoutpath = stdoutpath;
    }
    public File getStderror() {
        return stderror;
    }
    public void setStderror(File stderror) {
        this.stderror = stderror;
    }
    public String getStderrpath() {
        return stderrpath;
    }
    public void setStderrpath(String stderrpath) {
        this.stderrpath = stderrpath;
    }
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public void writeOutputFile(String output) throws IOException{
        FileWriter wr = new FileWriter(stdoutput);
        wr.write(output);
        wr.close();
    }
    public void writeErrFile(String output) throws IOException{
        FileWriter wr = new FileWriter(stderror);
        wr.write(output);
        wr.close();
    }
    public String getOutput() throws FileNotFoundException{
        String res = "";
        System.out.println( "[DEBUG] getOutput: "+ this.stdoutpath);
        Scanner lectura = new Scanner(stdoutput);
        while(lectura.hasNextLine()){
            res = res +"\n" + lectura.nextLine();
        }
        // System.out.println( "[DEBUG 3] getOutput: "+ res);
        lectura.close();
        return res;
    }
    public String getError() throws FileNotFoundException{
        String res = "";
        Scanner lectura = new Scanner(stderror);
        while(lectura.hasNextLine()){
            res = res +"\n" + lectura.nextLine();
        }
       // System.out.println( "[DEBUG] getError: "+ res);
        lectura.close();
        return res;
    }
}
