/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logappendP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author yered
 */
public class Logappend {

    private static String[] lineArgs;
    private boolean isBatch = false;
    //public static LinkedList<LogEntry> fileSim = new LinkedList<>();
    public static HashMap<String,ArrayList<String>> fileSim = new HashMap();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (lineArgs != null){
            args = lineArgs;
        }
        Logappend logappend = new Logappend();
        logappend.start(args);
        
        

        System.exit(0);
    }
    
    public  void start(String[] args){
        
        int i = 0;
        String arg;
        char flag;
        boolean gotArgs = false;
        LogEntry entry = new LogEntry();
        try {
            while (i < args.length) {
                if (args[i].startsWith("-")) {
                    arg = args[i + 1];

                    flag = args[i].charAt(1);
                    switch (flag) {
                        case 'T':
                            //if (vflag) System.out.println("Option x");
                            entry.setTimestamp(arg);
                            gotArgs = true;

                            break;
                        case 'K':

                            entry.setToken(arg);
                            gotArgs = true;

                            break;
                        case 'E':

                            entry.setEmployeeName(arg);
                            gotArgs = true;

                            break;
                        case 'G':

                            entry.setGuestName(arg);
                            gotArgs = true;

                            break;
                        case 'A':

                            entry.setIsAprovided(true);
                            gotArgs = true;

                            break;
                        case 'L':

                            entry.setIsLprovided(true);
                            gotArgs = true;

                            break;
                        case 'R':

                            entry.setRoomID(arg);
                            gotArgs = true;

                            break;
                        case 'B':

                            if (gotArgs) {
                                throw new Exception();
                            } else {
                                //guess nothing...

                            }

                            break;

                        default:
                            System.out.print("invalid\n");
                            System.exit(255);
                            break;
                    }

                }
                i++;

            }
        } catch (Exception x) {
            System.out.print("invalid\n");
            System.exit(255);
        }

        if (gotArgs) {
            entry.setLogPath(args[args.length - 1]);
            try {
                if (isBatch){
                    entry.saveB();
                }else{
                    entry.save();
                }
            } catch (Exception ex) {
                //Logger.getLogger(Logappend.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print("invalid\n");
                System.exit(255);
            }
        } else {
            BufferedReader br;
            try {
                if (args.length < 2) {
                    throw new Exception();
                }
                br = new BufferedReader(new FileReader(args[args.length - 1]));
                String line = br.readLine(); 
                while (line != null) {
                    Logappend l = new Logappend();
                    
                    Logappend logappend = new Logappend();
                    logappend.setIsBatch(true);
                    logappend.start(java.util.Arrays.stream(line.split(" "))
                            .filter(s -> (s != null && s.length() > 0)).toArray(String[]::new));
                    line = br.readLine();
                    
                }
                br.close();
                for (Map.Entry<String, ArrayList<String>> e : Logappend.fileSim.entrySet()) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(e.getKey()), true));
                    ArrayList<String> lines = e.getValue();
                    
                    byte[] salt = PasswordEncryption.generateSalt();
                    String encryptedPass = new String (Base91.encode(
                            PasswordEncryption.getEncryptedPassword(lines.get(0), salt)));
                    String b64Salt = new String(Base91.encode(salt));
                    bw.write(b64Salt + encryptedPass);
                    for (int z = 1; z < lines.size(); z++) {

                        bw.newLine();
                        CryptoMotor crypto = new CryptoMotor(lines.get(0));
                        String cryptoLine = crypto.encrypt(lines.get(z));
                        String tmp = crypto.getIvBytesAsB64();
                        tmp = crypto.getSaltBytesAsB64();
                        bw.write(crypto.getSaltBytesAsB64() + crypto.getIvBytesAsB64() + cryptoLine);
                    }

                    bw.flush();
                    bw.close();
                }
                
            } catch (Exception ex) {
                System.out.print("invalid\n");
                System.exit(255);
            }
            

        }
    }

    /**
     * @return the lineArgs
     */
    public String[] getLineArgs() {
        return lineArgs;
    }

    /**
     * @param lineArgs the lineArgs to set
     */
    public void setLineArgs(String[] lineArgs) {
        Logappend.lineArgs = lineArgs;
    }

    /**
     * @return the isBatch
     */
    public boolean isIsBatch() {
        return isBatch;
    }

    /**
     * @param isBatch the isBatch to set
     */
    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

}
