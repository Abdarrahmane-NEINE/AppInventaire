/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinventaire;

// Import section
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import javax.microedition.io.*;
import java.io.*;
import java.lang.*;

/**
 * @author kaber
 */
// A first MIDlet with simple text and a few commands. The 
//CommandListener
// allows MID application to respond to screen commands while the
// ItemStateListener keep track of input 
// items values being changed.
public class TestDatabase extends MIDlet
implements CommandListener, ItemStateListener {

  // Screen commands
  private Command exitCommand; 

  // Display a list of appointments already set in database
  private Command displayCommand; 

  // Set up a new appointment
  private Command setCommand;

  // Display the Delete screen where 
 //multiple appointments can be 
//selected for deletion
  private Command deleteCommand;

  // Save appointment in database
  private Command saveCommand;

  // Displays a menu of commands
  private Command menuCommand;

  // Delete selected appointments
  private Command delCommand;

  //The display for this MIDlet
  private Display display;

  // Input fields for setting name and date values for a new appointment 
  DateField apptDate;
  TextField apptField;
  private String apptName, apptTime;


  // User Interface of the application
  Form welcomeForm, menuForm, displayForm, setForm, deleteForm;

  // Record store designated for storage of appointment information
  RecordStore rs;

  // To allow selection of appointments for delete or view
  ChoiceGroup cg;
  ChoiceGroup list;
  
  // The vector contains a list of appointments selected for deletion
  java.util.Vector vector = new java.util.Vector();
  boolean deleted[];
  java.util.Vector delRecords = new java.util.Vector();
  
  
  
  // This will retrieve appointments in name - mm/dd/yyyy hh:mm AM_PM
 //form from record store
  public void getAppointments()
  {
     // Allows single selection
       list = new ChoiceGroup("Appointments", ChoiceGroup.EXCLUSIVE);
     // Allows multiple selections
     cg = new ChoiceGroup("Appointments",ChoiceGroup.MULTIPLE);
     String name;
     String at; 
            
            try
          {
       // Open a Record store
         rs = RecordStore.openRecordStore("MyAppointments",true);
          
// Display list of appointments already set.
// Ignore appointments which 
// have been deleted and marked with a '@' 
               for (int j=0;j<rs.getNumRecords();j++)
               {
                    at = new String("");
                    name = new String("");
                    byte b[] = rs.getRecord(j);
                    // The string will comply to a ####AAAA format
                    String str = new String(b,0,b.length);

                    System.out.println(str);
                    if (!(str.startsWith("@")))
                    {
                         for (int i=0;i<str.length();i++)
                         {
                   // Digits represent time information in total 
                   // number of seconds
                              if (Character.isDigit(str.charAt(i)))
                                   at += str.charAt(i);
                              else
                                   name += str.charAt(i);
                         
                         }
                    }
               
                    long time = 0;
                    String setAt = "";
                    if (!(at.trim().equals("")))
                    {
                         time = Long.parseLong(at);
// The time in seconds are converted to Date and 
// Calendar objects to display time of appointment in 
// user-desired format
 java.util.Date date = new java.util.Date(time);
 java.util.Calendar rightNow = java.util.Calendar.getInstance();
 rightNow.setTime(date);
String year = 
 String.valueOf(rightNow.get(java.util.Calendar.YEAR));
String month =
  String.valueOf(rightNow.get(java.util.Calendar.MONTH) + 1);
String day = 
 String.valueOf(rightNow.get(java.util.Calendar.DATE));
String am = "";
   if ( rightNow.get(java.util.Calendar.AM_PM) == 0)
       am = "PM";
       else if (rightNow.get(java.util.Calendar.AM_PM) == 1)
       am = "AM";
String hr = String.valueOf(rightNow.get(java.util.Calendar.HOUR));
String min = String.valueOf(rightNow.get(java.util.Calendar.MINUTE));
                         
setAt = name + "-" + month + "/" + day + "/" + year  
+ " " + hr  + ":" + min + " " + am;
             }
             else
                setAt = name;
                    
// A list of appointments are formed for display and cancel 
// purposes     
                list.append(setAt,null);
                cg.append(setAt,null);
                at = new String("");
                name = new String("");
              }
                    
               
               
          }
          catch(javax.microedition.rms.RecordStoreException exc)
          {
                    exc.printStackTrace();
          }
          //catch(javax.microedition.rms.RecordStoreNotOpenException exc)
          //{
          
          //}
               
          catch (Exception exc)
          {
                    exc.printStackTrace();
               
          }
     
     }
     
     
     
     
  // UI elements are created and initialized
  public TestDatabase() {
  
       
  
         display = Display.getDisplay(this);
        exitCommand = 
         new Command("Exit", Command.SCREEN, 1);
         displayCommand = 
         new Command("View",Command.BACK, 1);
        setCommand = 
         new Command("Set",Command.BACK, 1);
       deleteCommand = 
         new Command("Cancel",Command.BACK, 1);
     saveCommand =
     new Command("Save",Command.SCREEN, 1);
     menuCommand =
     new Command("Options",Command.SCREEN, 1);
     delCommand =
     new Command("Delete",Command.SCREEN, 1);
     
   
  }

  // Start the MIDlet by creating the TextBox and 
  // associating the exit command and listener.
  public void startApp() {
  
  try
  {
       // The first screen of the application
     welcomeForm = new Form("Appointment");
          
     
     getAppointments();
          
     // If the list of appointments is empty, notify user
     if (list.size() == 0)
     {
          
   welcomeForm.append("No appointments are set. Follow Menu->Set");
   welcomeForm.addCommand(exitCommand);
   welcomeForm.addCommand(menuCommand);
   welcomeForm.setCommandListener(this);
   welcomeForm.setItemStateListener(this);
   display.setCurrent(welcomeForm);
     }
     // else display the list of appointments 
     else
     {
   welcomeForm.append(list);
   welcomeForm.addCommand(menuCommand);
   welcomeForm.setItemStateListener(this);
   welcomeForm.setCommandListener(this);
   display.setCurrent(welcomeForm);
     }
     
  menuForm = new Form("Options");
  menuForm.append("Select View to display appointments already set");
  menuForm.addCommand(displayCommand);
  menuForm.addCommand(setCommand);
  menuForm.addCommand(deleteCommand);
  menuForm.setCommandListener(this);
  menuForm.setItemStateListener(this);
     
 displayForm = new Form("View");
     
     
   
     
setForm = new Form("Set An Appointment");
apptField = new TextField("Name","", 10,0);
apptDate = new DateField("Set At", DateField.DATE_TIME);
setForm.append(apptField);
setForm.append(apptDate);
setForm.addCommand(saveCommand);
setForm.addCommand(menuCommand);
setForm.setCommandListener(this);
setForm.setItemStateListener(this);
     
     
     
deleteForm = new Form("Delete");
     
     
     
     
     
     
     
 }
 catch (Exception exc)
 {
      exc.printStackTrace();
 }
     
     
  }
  
 public  void itemStateChanged(Item item)
  {
    java.util.Date date;
// The date value is set to a variable when the DateField item 
//  is changed
    if (item == apptDate)
   {
         date = apptDate.getDate();
         apptTime = String.valueOf(date.getTime());
     }
 // The name of appointment is set to a variable when the 
//  name input field is changed
     if (item == apptField)
    {
        apptName = apptField.getString();
     }
          
 // If the ChoiceGroup item state on Delete form is changed, 
  //it sets an array of 
//appointments selected for deletion
    if (item == cg)
    {
    cg.getSelectedFlags(deleted);
    }
          
          
    }

  // Pause is a no-op because there are no  background
  // activities or record stores to be closed.
  public void pauseApp() { }

  // Destroy must cleanup everything not handled 
  // by the garbage collector.
  // In this case there is nothing to cleanup.
  public void destroyApp(boolean unconditional) { }

 // Respond to commands. Here we are only  implementing
 // the exit command. In the exit command,  cleanup and
 // notify that the MIDlet has been destroyed.
  public void commandAction(
  Command c, Displayable s) {
     // Exit the application after closing the record store
    if (c == exitCommand) {
     try
     {
          rs.closeRecordStore();
           destroyApp(false);
           notifyDestroyed();
     }
     catch (Exception exc) {  exc.printStackTrace(); }
    }
     // Display the Set form screen which allows to create 
     // a new appointment
     if (c== setCommand) {
     
          display.setCurrent(setForm);
          
      
     }
     // Display a list of appointments
     if (c==displayCommand)
     {
          
          getAppointments();
          
          
     
          displayForm.append(list);
          displayForm.addCommand(exitCommand);
          displayForm.addCommand(menuCommand);
          displayForm.setCommandListener(this);
          displayForm.setItemStateListener(this);
          display.setCurrent(displayForm);
          
     
     }
     
     // When hit Cancel, a selectable list of appointments is 
     //displayed. It allows to select
// multiple appointments for purpose of deletion
     if (c==deleteCommand)
     {
          
          getAppointments();
          deleteForm.append(cg);
          deleted = new boolean[cg.size()];
          deleteForm.addCommand(delCommand);
          deleteForm.addCommand(menuCommand);
          deleteForm.setCommandListener(this);
          deleteForm.setItemStateListener(this);
          display.setCurrent(deleteForm);
     }
          
// When hit Menu, the menu form displays which provides 
//options of setting appointments and cancel
     if (c==menuCommand)
          display.setCurrent(menuForm);
     
// When hit Save, insert record into record store as stream of bytes     
     if (c==saveCommand)
     {
          try
          {
          
          String appt = apptName + " " + apptTime;
          byte bytes[] = appt.getBytes();
          rs.addRecord(bytes,0,bytes.length);
       
          }
          catch (Exception exc)
          {
               exc.printStackTrace();
          }
     
     }
     // Actually delete the set of selected appointments. 
//The selected appointments are 
// marked as invalid in the record store and are 
//not displayed later
     if (c==delCommand)
     {
          try
          {
          for (int m=0;m<deleted.length;m++)
          {
               boolean ifDeleted = deleted[m];
 // In case a record is deleted, store some invalid characters
               // that signify it is inactive
               if (ifDeleted)
               {
                    String deactive = "@";
                    byte b[] = deactive.getBytes();
                    rs.setRecord(m+1, b,0,b.length);
               }
          }
          }
          catch (Exception exc)
          {
               exc.printStackTrace();
          }
     
     }
     
      }
  
  
 

}