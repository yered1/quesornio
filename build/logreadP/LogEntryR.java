/*
 * Copyright 2015 yered.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package logreadP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author yered
 */
public class LogEntryR {

    private boolean isTprovided = false;
    private boolean isKprovided = false;
    private boolean isEprovided = false;
    private boolean isGprovided = false;
    private boolean isAprovided = false;
    private boolean isLprovided = false;
    private boolean isLogprovided = false;

    private long timestamp;
    private String token;
    private String employeeName;
    private String guestName;
    private long roomID = -1;
    private String logPath;
    private String b64Hash;
    //private boolean isRoomprovided = false;

    LogEntryR() {

    }
    
    LogEntryR(String parameters) {
        String[] list = parameters.split("\\|");
        setTimestamp(list[0]);
        if (!list[1].equals("null"))
            setEmployeeName(list[1]);
        if (!list[2].equals("null"))
            setGuestName(list[2]);
        setIsAprovided(Boolean.parseBoolean(list[3]));
        setIsLprovided(Boolean.parseBoolean(list[4]));
        try{
            if (!(Long.parseLong(list[5]) < 0))
                setRoomID(list[5]);
            else if (Long.parseLong(list[5]) == -2){
                this.roomID = -2;
            }
        }catch (Exception x){
            System.out.print("invalid\n");
            System.exit(255);
        }
        setB64Hash(list[6]);
    }

    /**
     * @return the isTprovided
     */
    public boolean isTprovided() {
        return isTprovided;
    }

    /**
     * @param isTprovided the isTprovided to set
     */
    public void setIsTprovided(boolean isTprovided) {
        this.isTprovided = isTprovided;
    }

    /**
     * @return the isKprovided
     */
    public boolean isKprovided() {
        return isKprovided;
    }

    /**
     * @param isKprovided the isKprovided to set
     */
    public void setIsKprovided(boolean isKprovided) {
        this.isKprovided = isKprovided;
    }

    /**
     * @return the isEprovided
     */
    public boolean isEprovided() {
        return isEprovided;
    }

    /**
     * @param isEprovided the isEprovided to set
     */
    public void setIsEprovided(boolean isEprovided) {
        this.isEprovided = isEprovided;
    }

    /**
     * @return the isGprovided
     */
    public boolean isGprovided() {
        return isGprovided;
    }

    /**
     * @param isGprovided the isGprovided to set
     */
    public void setIsGprovided(boolean isGprovided) {
        this.isGprovided = isGprovided;
    }

    /**
     * @return the isAprovided
     */
    public boolean isAprovided() {
        return isAprovided;
    }

    /**
     * @param isAprovided the isAprovided to set
     */
    public void setIsAprovided(boolean isAprovided) {
        if (!this.isLprovided) {
            this.isAprovided = isAprovided;
        } else if (isAprovided){
            System.out.print("invalid\n");
            System.exit(255);
        }
    }

    /**
     * @return the isLprovided
     */
    public boolean isLprovided() {
        return isLprovided;
    }

    /**
     * @param isLprovided the isLprovided to set
     */
    public void setIsLprovided(boolean isLprovided) {
        if (!this.isAprovided) {
            this.isLprovided = isLprovided;
        } else if (isLprovided){
            System.out.print("invalid\n");
            System.exit(255);
        }
    }

    /**
     * @return the isLogprovided
     */
    public boolean isLogprovided() {
        return isLogprovided;
    }

    /**
     * @param isLogprovided the isLogprovided to set
     */
    public void setIsLogprovided(boolean isLogprovided) {
        this.isLogprovided = isLogprovided;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        try {
            this.timestamp = Long.parseLong(timestamp);
            if (this.timestamp < 0) {
                throw new Exception();
            }
            this.isTprovided = true;
        } catch (Exception x) {
            System.out.print("invalid\n");
            System.exit(255);
        }
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        if (token.matches("[0-9a-zA-Z]+")) {
            this.token = token;
            this.isKprovided = true;
        } else {
            System.out.print("invalid\n");
            System.exit(255);

        }
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        if (employeeName.matches("[a-zA-Z]+") && !this.isGprovided) {
            this.employeeName = employeeName;
            this.isEprovided = true;
        } else {
            System.out.print("invalid\n");
            System.exit(255);

        }
    }

    /**
     * @return the guestName
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * @param guestName the guestName to set
     */
    public void setGuestName(String guestName) {
        if (guestName.matches("[a-zA-Z]+") && !this.isEprovided) {
            this.guestName = guestName;
            this.isGprovided = true;
        } else {
            System.out.print("invalid\n");
            System.exit(255);
        }
    }

    /**
     * @return the roomID
     */
    public long getRoomID() {
        return roomID;
    }

    /**
     * @param roomID the roomID to set
     */
    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public void setRoomID(String roomID) {
        try {
            this.roomID = Long.parseLong(roomID);
            if (this.roomID < 0) {
                throw new Exception();
            }
            //this.isRoomprovided = true;
        } catch (Exception x) {
            System.out.print("invalid\n");
            System.exit(255);
        }
    }

    /**
     * @return the logPath
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * @param logPath the logPath to set
     */
    public void setLogPath(String logPath) {
        if (logPath.matches("[a-zA-Z0-9./]+")) {
            this.logPath = logPath;
            this.isLogprovided = true;
        } else {
            System.out.print("invalid\n");
            System.exit(255);
        }
    }
    
    public void printR() throws Exception{
        File f = new File(this.logPath);
        if (!f.exists() || f.isDirectory()) {
            System.out.print("invalid\n");
            System.exit(255);
        }else{
            if (!authenticate()){
                System.out.print("integrity violation");
                System.exit(255);
            }
        }
        
        LinkedList<LogEntryR> logList = getLogList();
        String roomList = "";
        boolean added = false;
        for (int i = 0; i < logList.size(); i++)
        {
            
            if (isEprovided() && logList.get(i).isEprovided() && !logList.get(i).isLprovided() && 
                    logList.get(i).getRoomID() >= 0 && getEmployeeName().equals(logList.get(i).getEmployeeName())) {
                if (added) {
                    roomList += ",";
                }
                roomList += logList.get(i).getRoomID();
                added = true;
            } else if (isGprovided() && logList.get(i).isGprovided() && !logList.get(i).isLprovided()
                    && logList.get(i).getRoomID() >= 0 && getGuestName().equals(logList.get(i).getGuestName())) {
                if (added) {
                    roomList += ",";
                }
                roomList += logList.get(i).getRoomID();
                added = true;
            }

            
        }   
        
        System.out.print(roomList);
    }
    
    private LinkedList<LogEntryR> getLogList() throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        
        String line =  br.readLine();//first line is crypto
        //String lastLine;
        LogEntryR currLog = null;
        //LogEntry lastValidEntry = null;
        LinkedList<LogEntryR> logList = new LinkedList<>();
       

        while (line != null) {
            //lastLine = line;
            line = br.readLine();//start at second line
            if (line ==null)
                break;
            String contentString = line.substring(48);
            String saltString = line.substring(0, 24);
            String ivString = line.substring(24, 48);
            byte[] saltByte = Base64.getDecoder().decode(saltString);
            byte[] ivByte = Base64.getDecoder().decode(ivString);
            CryptoMotorR crypto = new CryptoMotorR(saltByte, token, ivByte);
            currLog = new LogEntryR(crypto.decrypt(contentString));
            if (!currLog.isHashOK()) {
                System.out.print("invalid\n");
                System.exit(255);
            }
            logList.add(currLog);
            
        }
        br.close();
        
        return logList;
    }
    
    public void printState() throws Exception{
        File f = new File(this.logPath);
        if (!f.exists() || f.isDirectory()) {
            System.out.print("invalid\n");
            System.exit(255);
        }else{
            if (!authenticate()){
                System.out.print("integrity violation");
                System.exit(255);
            }
        }
        LinkedList<LogEntryR> logList = getLogList();
       

        
        
        LinkedList<String> employeeList = new LinkedList<>();
        LinkedList<String> guestList = new LinkedList<>();
        LinkedList<Room> rooms = new LinkedList<>();
        
        for (int i = 0; i < logList.size(); i++) {
            if ((logList.get(i).getEmployeeName() != null
                    && !employeeList.contains(logList.get(i).getEmployeeName()))
                    || (logList.get(i).getGuestName() != null
                    && !guestList.contains(logList.get(i).getGuestName()))) {
                long empRoomID = isIn(logList.get(i).getEmployeeName(), true, false, logList);
                long guestRoomID = isIn(logList.get(i).getGuestName(), false, true, logList);
                Room currentRoom = new Room();
                if (empRoomID >= -1) {
                    employeeList.add(logList.get(i).getEmployeeName());
                    if (empRoomID >= 0) {
                        boolean roomAdded = false;
                        for (Room room : rooms) {
                            if (room.getRoomID() == empRoomID) {
                                room.addName(logList.get(i).getEmployeeName());
                                roomAdded = true;
                            }
                        }

                        if (!roomAdded) {
                            currentRoom.setRoomID(empRoomID);
                            currentRoom.addName(logList.get(i).getEmployeeName());
                            rooms.add(currentRoom);
                        }
                    }
                } else if (guestRoomID >= -1) {
                    guestList.add(logList.get(i).getGuestName());
                    if (guestRoomID >= 0) {
                        boolean roomAdded = false;
                        for (Room room : rooms) {
                            if (room.getRoomID() == guestRoomID) {
                                room.addName(logList.get(i).getGuestName());
                                roomAdded = true;
                            }
                        }

                        if (!roomAdded) {
                            currentRoom.setRoomID(guestRoomID);
                            currentRoom.addName(logList.get(i).getGuestName());
                            rooms.add(currentRoom);
                        }
                    }
                }
            }
        }
        
        Collections.sort(guestList);
        Collections.sort(employeeList);
        String employeeNames = "";
        employeeNames = employeeList.stream().map((currName) -> currName + ",").reduce(employeeNames, String::concat);
        employeeNames = employeeNames.substring(0, employeeNames.length()-1);
        employeeNames += "\n";
        String guestNames = "";
        
        for (String currGuest : guestList){
            guestNames += currGuest + ",";
        }
        guestNames = guestNames.substring(0, guestNames.length()-1);
        guestNames += "\n";
        
        System.out.print(employeeNames);
        System.out.print(guestNames);
        LinkedList<String> roomLineList = new LinkedList<>();
        for (int i =0; i <rooms.size(); i++){
            LinkedList<String> tmpNames = rooms.get(i).getNames();
            String roomLine = "" + rooms.get(i).getRoomID() + ":";
            Collections.sort(tmpNames);
            for (int e = 0; e< tmpNames.size(); e++){
                roomLine += tmpNames.get(e);
                if (e < tmpNames.size()-1){
                    roomLine += ",";
                }
            }
            
            roomLineList.add(roomLine);
        }
        Collections.sort(roomLineList);
        for (int i =0; i<roomLineList.size();i++) {
            System.out.print(roomLineList.get(i));
            if (i < rooms.size()-1)
                System.out.print("\n");
        }
        
        

    }
    //retuns the last roomID, -2 if it's outside gallery, -1 if in lobby
    public long isIn(String name, boolean isEmployee, boolean isGuest, LinkedList<LogEntryR> logList) {
        if (name == null){
            return -2;
        }
        long result = -2;
        for (LogEntryR logList1 : logList) {
            if (isEmployee) {
                String empName = logList1.getEmployeeName();
                if (empName != null && empName.equals(name)){
                    if (logList1.getRoomID() == -2){
                        result = -2;
                    }else{
                        result = logList1.getRoomID();
                    }
                }
            } else if (isGuest){
                String gustName = logList1.getGuestName();
                if (gustName != null && gustName.equals(name)){
                    if (logList1.getRoomID() == -2){
                        result = -2;
                    }else{
                        result = logList1.getRoomID();
                    }
                }
            }
        }
        return result;

    }

    public void save() throws Exception {
        File f = new File(this.logPath);
        //creating new file
        if (!f.exists() && !f.isDirectory()) {
            if (!isLprovided && isAprovided && isKprovided
                    && (isEprovided || isGprovided) && roomID == -1) {
                //FileOutputStream fo = new FileOutputStream(f);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f,false));
                byte[] salt = PasswordEncryptionR.generateSalt();
                String encryptedPass = Base64.getEncoder().encodeToString(
                        PasswordEncryptionR.getEncryptedPassword(token, salt));
                String b64Salt = Base64.getEncoder().encodeToString(salt);
                bw.write(b64Salt + encryptedPass);
                bw.newLine();
                CryptoMotorR crypto = new CryptoMotorR(token);   
                String cryptoLine = crypto.encrypt(prepareLine());
                bw.write(crypto.getSaltBytesAsB64() + crypto.getIvBytesAsB64() + cryptoLine);
                bw.flush();
                bw.close();
                return;
            }else{
                System.out.print("invalid\n");
                System.exit(255);
            }
            
        }else if (f.isDirectory()){
            System.out.print("invalid\n");
                System.exit(255);
        }

        if (f.exists() && !f.isDirectory()) {
            if (!authenticate()){
                System.out.print("invalid\n");
                System.exit(255);
            }
            //the first time is entering
            if (roomID == -1 && isAprovided){
                boolean existsInLog = existsInLog();
                long lastEntryTime = getLastEntryTime();
                if ((!existsInLog && lastEntryTime < this.getTimestamp())|| 
                        (existsInLog && lastEntryTime < this.getTimestamp() && getCurrentRoom() == -2)) {
                    write(f, true);
                } else {
                    System.out.print("invalid\n");
                    System.exit(255);
                }
                //entering to a room after being in lobby
            } else if (roomID >= 0 && isAprovided){
                //must exist previosly in lobby to enter the room && last roomID can't be -2
                long currentRoom = getCurrentRoom();
                if (existsInLog() &&  currentRoom !=-2 && currentRoom == -1) {
                    //check current timestamp is higher than last entry
                    if (getLastEntryTime() < this.timestamp) {
                        write(f,true);
                    } else {
                        System.out.print("invalid\n");
                        System.exit(255);
                    }
                }else{
                    //trying to enter a room without leaving the previous
                    System.out.print("invalid\n");
                    System.exit(255);
                }
                //leaving a room or gallery
            }else if (isLprovided){
                long currRoom = getCurrentRoom();
                if ( currRoom == getRoomID() && getLastEntryTime() < this.timestamp){//at this point getRoomID should only be >= -1
                    if (currRoom >= 0)
                        this.roomID = -1;//set the roomID to lobby
                    else if (currRoom < 0)
                        this.roomID = -2;// set the roomID to outside gallery
                    write(f, true);
                } else {
                    System.out.print("invalid\n");
                    System.exit(255);
                }
            }


        }

    }
    
    
    private long getCurrentRoom() throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        
        String line =  br.readLine();//first line is crypto
        //String lastLine;
        LogEntryR currLog = null;
        LogEntryR lastValidEntry = null;

        while (line != null) {
            //lastLine = line;
            line = br.readLine();//start at second line
            if (line ==null)
                break;
            String contentString = line.substring(48);
            String saltString = line.substring(0, 24);
            String ivString = line.substring(24, 48);
            byte[] saltByte = Base64.getDecoder().decode(saltString);
            byte[] ivByte = Base64.getDecoder().decode(ivString);
            CryptoMotorR crypto = new CryptoMotorR(saltByte, token, ivByte);
            currLog = new LogEntryR(crypto.decrypt(contentString));
            if (!currLog.isHashOK()) {
                System.out.print("invalid\n");
                System.exit(255);
            }
            if ((currLog.getEmployeeName() != null && this.isEprovided && currLog.getEmployeeName().equals(getEmployeeName())) || 
                    (currLog.getGuestName() != null && this.isGprovided && currLog.getGuestName().equals(getGuestName()))){
                lastValidEntry = currLog;
            }
            
        }
        if (lastValidEntry == null) {
            System.out.print("invalid\n");
            System.exit(255);
        }
        return lastValidEntry.getRoomID();
    }
    
    private long getLastEntryTime() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        
        String line =  br.readLine();//first line is crypto
        //String lastLine;
        LogEntryR currLog = null;

        while (line != null) {
            //lastLine = line;
            line = br.readLine();//start at second line
            if (line ==null)
                break;
            String contentString = line.substring(48);
            String saltString = line.substring(0, 24);
            String ivString = line.substring(24, 48);
            byte[] saltByte = Base64.getDecoder().decode(saltString);
            byte[] ivByte = Base64.getDecoder().decode(ivString);
            CryptoMotorR crypto = new CryptoMotorR(saltByte, token, ivByte);
            currLog = new LogEntryR(crypto.decrypt(contentString));
            if (!currLog.isHashOK()) {
                System.out.print("invalid\n");
                System.exit(255);
            }
            
        }
        return currLog.getTimestamp();

    }
    
    private void write(File f, boolean append) throws IOException, NoSuchAlgorithmException, Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(f, append));
        byte[] salt = PasswordEncryptionR.generateSalt();
        
        CryptoMotorR crypto = new CryptoMotorR(token);
        bw.newLine();
        String cryptoLine = crypto.encrypt(prepareLine());
        bw.write(crypto.getSaltBytesAsB64() + crypto.getIvBytesAsB64() + cryptoLine);        
        bw.flush();
        bw.close();
    }
    
    private boolean authenticate() throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException{
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        String line = br.readLine();
        br.close();
        if (line == null){
            System.out.print("invalid\n");
            System.exit(255);
        }
        String passString = line.substring(24);
        String saltString = line.substring(0, 24);
        byte[] passByte = Base64.getDecoder().decode(passString);
        byte[] saltByte = Base64.getDecoder().decode(saltString);
        return PasswordEncryptionR.authenticate(token, passByte, saltByte);
        //return false;
    }
   
    private boolean existsInLog() throws FileNotFoundException, IOException, Exception {
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        br.readLine();
        if (getGuestName() != null) {
            for (String line; (line = br.readLine()) != null;) {
                String contentString = line.substring(48);
                String saltString = line.substring(0, 24);
                String ivString = line.substring(24,48);
                byte[] saltByte = Base64.getDecoder().decode(saltString);
                byte[] ivByte = Base64.getDecoder().decode(ivString);
                CryptoMotorR crypto = new CryptoMotorR(saltByte,token,ivByte);
                LogEntryR currLog = new LogEntryR(crypto.decrypt(contentString));
                if (!currLog.isHashOK()) {
                    System.out.print("invalid\n");
                    System.exit(255);
                }
                if (currLog.getGuestName() != null && 
                        currLog.getGuestName().equals(this.getGuestName())){
                    return true;
                }
            }
        } else if (getEmployeeName() != null) {
            for (String line; (line = br.readLine()) != null;) {
                String contentString = line.substring(48);
                String saltString = line.substring(0, 24);
                String ivString = line.substring(24,48);
                byte[] saltByte = Base64.getDecoder().decode(saltString);
                byte[] ivByte = Base64.getDecoder().decode(ivString);
                CryptoMotorR crypto = new CryptoMotorR(saltByte, token,ivByte);
                LogEntryR currLog = new LogEntryR(crypto.decrypt(contentString));
                if (!currLog.isHashOK()) {
                    System.out.print("invalid\n");
                    System.exit(255);
                }
                if (currLog.getEmployeeName() != null && 
                        currLog.getEmployeeName().equals(this.getEmployeeName())){
                    return true;
                }
            }

        }
        return false;
    }
    
    public boolean isHashOK() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //String line = "";

        //line = line + this.timestamp + "|" + this.employeeName + "|" + this.guestName
        //        + "|" + this.isAprovided + "|" + this.isLprovided + "|" + this.roomID + "|";
        MessageDigest hash = null;
        hash = MessageDigest.getInstance("SHA-1");
        hash.reset();
        hash.update((this.timestamp + this.employeeName + this.guestName
                + this.isAprovided + this.isLprovided + this.roomID).getBytes("UTF-8"));

        String B64 = Base64.getEncoder().encodeToString(hash.digest());
        
        return B64.equals(getB64Hash());
    }

    //INCOMPLTE
    private boolean hasEnteredGallery(String name, boolean isEmployee) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(logPath));
        for(String line; (line = br.readLine()) != null; ) {
            
        }
        
        
        
        return false;
    }

    private String prepareLine() throws Exception {
        String line = "";

        line = line + this.timestamp + "|" + this.employeeName + "|" + this.guestName
                + "|" + this.isAprovided + "|" + this.isLprovided + "|" + this.roomID + "|";
        MessageDigest hash = null;
        hash = MessageDigest.getInstance("SHA-1");
        hash.reset();
        hash.update((this.timestamp + this.employeeName + this.guestName
                + this.isAprovided + this.isLprovided + this.roomID).getBytes("UTF-8"));

        String B64 = Base64.getEncoder().encodeToString(hash.digest());

        line = line + B64;

        return line;

    }

    /**
     * @return the b64Hash
     */
    public String getB64Hash() {
        return b64Hash;
    }

    /**
     * @param b64Hash the b64Hash to set
     */
    public void setB64Hash(String b64Hash) {
        this.b64Hash = b64Hash;
    }
    
    class Room{
        private long roomID;
        private LinkedList<String> names = new LinkedList<>();

        /**
         * @return the roomID
         */
        public long getRoomID() {
            return roomID;
        }

        /**
         * @param roomID the roomID to set
         */
        public void setRoomID(long roomID) {
            this.roomID = roomID;
        }

        /**
         * @return the names
         */
        public LinkedList<String> getNames() {
            return names;
        }

        /**
         * @param names the names to set
         */
        public void setNames(LinkedList<String> names) {
            this.names = names;
        }
        
        public void addName(String name){
            this.names.add(name);
        }
    }

}