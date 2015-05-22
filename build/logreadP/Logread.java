/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logreadP;

import java.util.LinkedList;



/**
 *
 * @author yered
 */
public class Logread {

    private static String[] lineArgs;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (lineArgs != null){
            args = lineArgs;
        }
        Logread logappend = new Logread();
        logappend.start(args);
        
        

        System.exit(0);
    }
    
    public  void start(String[] args){
        
        int i = 0;
        String arg;
        char flag;
        boolean printState = false;
        boolean gotR = false;
        boolean gotT = false;
        boolean gotI = false;
        
        LogEntryR entry = new LogEntryR();
        LinkedList<LogEntryR.Person> employees = new LinkedList<>();
        LinkedList<LogEntryR.Person> guests = new LinkedList<>();
        try {
            while (i < args.length) {
                if (args[i].startsWith("-")) {
                    arg = args[i + 1];

                    flag = args[i].charAt(1);
                    switch (flag) {
                        case 'S':
                            //if (vflag) System.out.println("Option x");
                            //entry.setLogPath(arg);
                            printState = true;

                            break;
                        case 'K':

                            entry.setToken(arg);
                            
                            //gotArgs = true;

                            break;
                        case 'E':

                            
                            if (gotI){
                                LogEntryR.Person p = new LogEntryR().new Person();
                                p.setName(arg);
                                employees.add(p);
                                
                            }else{
                                entry.setEmployeeName(arg);
                            }
                            //gotArgs = true;

                            break;
                        case 'G':
                            if (gotI){
                                LogEntryR.Person p = new LogEntryR().new Person();
                                p.setName(arg);
                                guests.add(p);
                            }else{
                                entry.setGuestName(arg);
                            }
                            //gotArgs = true;

                            break;
                        case 'T':

                            //entry.setIsAprovided(true);
                            //gotArgs = true;
                            //System.out.print("unimplemented");
                            //System.exit(0);
                            gotT = true;

                            break;
                        case 'I':

                            //entry.setIsLprovided(true);
                            //gotArgs = true;
                            //System.out.print("unimplemented");
                            //System.exit(0);
                            gotI = true;

                            break;
                        case 'R':

                            //entry.setRoomID(arg);
                            //gotArgs = true;
                            gotR = true;

                            break;
                        /*case 'B':

                            if (gotArgs) {
                                throw new Exception();
                            } else {
                                //guess nothing...

                            }

                            break;*/

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

        if (printState && args.length >= 4) {
            
            try {
                entry.setLogPath(args[args.length - 1]);
                entry.printState();
            } catch (Exception ex) {
                //Logger.getLogger(Logappend.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print("invalid\n");
                System.exit(255);
            }
        } else if (gotR){
            try {
                entry.setLogPath(args[args.length - 1]);
                entry.printR();
            } catch (Exception ex) {
                System.out.print("invalid\n");
                System.exit(255);
            }
        }else if (gotT && (entry.getEmployeeName() != null || entry.getGuestName() != null)){
            try{
                entry.setLogPath(args[args.length - 1]);
                entry.printT();
            }catch (Exception x){
                System.out.print("invalid\n");
                System.exit(255);
            }
        }else if (gotI){
            try{
                entry.setLogPath(args[args.length - 1]);
                entry.printI(employees, guests);
            }catch (Exception x){
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
        Logread.lineArgs = lineArgs;
    }

}
