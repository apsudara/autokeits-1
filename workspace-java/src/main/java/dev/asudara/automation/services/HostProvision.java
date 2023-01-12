package dev.asudara.automation.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

import org.springframework.util.ResourceUtils;

public class HostProvision {
    private Session session;
    private String host;
    public HostProvision() {
    }

    public void desconectSession() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
    public String executeCommand(String cmd) throws JSchException{
        String outputstd = "";

        ChannelExec canalExec;
        canalExec = (ChannelExec) session.openChannel("exec");
        canalExec.setCommand(cmd);

        try {
            InputStream salida = canalExec.getInputStream();
            canalExec.connect();
            
            byte[] tmp = new byte[1024];
            while (true) {
                while (salida.available() > 0) {
                    int i = salida.read(tmp, 0, 1024);
                    if (i < 0){
                        break;
                    }
                    outputstd =  outputstd + new String(tmp, 0, i);
                }
                if (canalExec.isClosed()) {
                    if (salida.available() > 0)
                        continue;
                        outputstd = outputstd + "\n[EXIT STATUS] "+canalExec.getExitStatus()+"\n";
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[DEBUG] execCommad end");
        canalExec.disconnect();
        return outputstd;

    }
    public void startSession(String user, String password, String host) throws JSchException {
        // NOTA
        // para no poner de forma manual todos los hosts al que conectar
        // "jsch.setKnownHosts(System.getProperty("user.home")+"/.ssh/known_hosts");"
        // Con devolver true a este metodo, se resuelve la "reject HostKey"
        // "public boolean promptYesNo(String str){return false;}"
        this.host = host;
        JSch jsch = new JSch();
        // String host="127.0.0.1";
        // String user="root";
        session = jsch.getSession(user, host, 22);

        class Aux implements UserInfo {
            String passwd = "password";

            public void setPassWord(String password) {
                passwd = password;
            }

            public boolean promptYesNo(String str) {
                return true;
            }

            public boolean promptPassphrase(String message) {
                return true;
            }

            public String getPassphrase() {
                return null;
            }

            public String getPassword() {
                return passwd;
            }

            public void showMessage(String message) {
                System.out.println(message);
            }

            public boolean promptPassword(String message) {
                return true;
            };

        }
        ;
        Aux userinfo = new Aux();
        userinfo.setPassWord(password);

        session.setUserInfo(userinfo);

        // Abre session: conectar al host remoto
        session.connect();
    }

    public boolean sendFile(String srcFilePath, String destFilePath)
            throws JSchException, SftpException, FileNotFoundException {
        // String srcFilePath = "/src/main/resources/static/script.sh";
        // String destFilePath = "/tmp/script.sh";
        System.out.println("[DEBUG] source file path:" + srcFilePath + "  des file path:" + destFilePath);
        boolean res = false;
        if (session == null || !this.session.isConnected()) {
            System.out.println("[DEBUG] session");
            return false;
        }

        // Abrir canal sftp
        Channel canal = session.openChannel("sftp");
        canal.connect();
        ChannelSftp canalsftp = (ChannelSftp) canal;

        File file = new File("" + ResourceUtils.getFile("classpath:static/" + srcFilePath + "").getPath());

        InputStream is = new FileInputStream(file);

        // Subir el fichero al host
        canalsftp.put(is, destFilePath);

        res = true;
        // Desconectar
        canal.disconnect();

        return res;
    }

    public String execCommand(String cmd,String configName ,String scriptName) throws JSchException,IOException {
        String outputstd = "";
        File fileoutput = new File("" + ResourceUtils.getFile("classpath:static/").getPath() + "/logs/"  + host + ".stdoutput");
        System.out.println("[DEBUG] execCommand host="+ host +"  " + ResourceUtils.getFile("classpath:static/").getPath() + "/logs/" + configName + host +".stdoutput");
        fileoutput.createNewFile();
        
        File fileerror = new File("" + ResourceUtils.getFile("classpath:static/").getPath() + "/logs/"  + host + ".error");
        System.out.println("[DEBUG] execCommand host=" +host+"  "+ ResourceUtils.getFile("classpath:static/").getPath() + "/logs/" + configName + host  +".error");
        fileerror.createNewFile();

        FileWriter fw = new FileWriter(fileoutput, true);
        ChannelExec canalExec;
        canalExec = (ChannelExec) session.openChannel("exec");
        
        FileOutputStream error=new FileOutputStream(fileerror);
        canalExec.setErrStream(error);
        System.out.println("[DEBUG] execCommad start" + cmd);
 
        canalExec.setCommand(cmd);

        try {
            InputStream salida = canalExec.getInputStream();
            canalExec.connect();
            fw.append("\n\n[START]>>>>>>>>>> Script:"+ scriptName+ ">>>>>>>>>>>\n");
            byte[] tmp = new byte[1024];
            while (true) {
                while (salida.available() > 0) {
                    int i = salida.read(tmp, 0, 1024);
                    if (i < 0){
                        fw.close();
                        break;
                    }
                    outputstd =  outputstd + new String(tmp, 0, i);
                    fw.append(new String(tmp, 0, i) );
                }
                if (canalExec.isClosed()) {
                    if (salida.available() > 0)
                        continue;
                        
                    fw.append("[EXIT STATUS] "+canalExec.getExitStatus()+"\n");
                    fw.append("[END] <<<<<<<<<<< Script:"+ scriptName+ "<<<<<<<<<<<<<<<<<<<<<<\n");
                    fw.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[DEBUG] execCommad end");
        canalExec.disconnect();
        return outputstd;
    }
}
