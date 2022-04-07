/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinventaire;

import java.util.Date;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;

/**
 * @author kaber
 */
public class FormDatabaseMidlet extends MIDlet implements CommandListener, ItemStateListener {
     // Screen commands
  private Command exitCommand; 

  // Display a list of product already set in database
  private Command displayCommand; 

  // Set up a new product
  private Command setCommand;

  // Display the Delete screen where 
 //multiple product can be 
//selected for deletion
  private Command deleteCommand;

  // Save product in database
  private Command saveCommand;

  // Displays a menu of commands
  private Command menuCommand;

  // Delete selected appointments
  private Command delCommand;

  //The display for this MIDlet
    public Display display;
    
     // Input fields for setting name and date values for a new product 
      
     DateField apptDate;
     TextField apptField;
     TextField saisieField;
     TextField designationField;
     TextField quantityOfStockField;
     TextField newQuantityField;
             
     private String apptName, apptTime,saisie ,designation ,quantityOfStock ,newQuantity ;
     
     
     // User Interface of the application
     Form welcomeForm, menuForm, displayForm, setForm, deleteForm;
    
      // Record store designated for storage of product information
      RecordStore rs;
      static final String REC_STORE = "MesProduits";
      
      // To allow selection of products for delete or view
      ChoiceGroup cg;
      ChoiceGroup list;
    
       // The vector contains a list of products selected for deletion
      java.util.Vector vector = new java.util.Vector();
      boolean deleted[];
      java.util.Vector delRecords = new java.util.Vector();
      
       // This will retrieve products in name - mm/dd/yyyy hh:mm AM_PM
 //form from record store
  public void getProducts()
  {
     // Allows single selection
     list = new ChoiceGroup(apptName + "  \n" + apptTime + " \n" + saisie  + "  \n" + designation  + "  \n" + quantityOfStock  + "  \n" + newQuantity , ChoiceGroup.EXCLUSIVE);
     // Allows multiple selections
     cg = new ChoiceGroup("produits",ChoiceGroup.MULTIPLE);
     String name;
     String at; 
            
     try
        {
            // Open a Record store
             rs = RecordStore.openRecordStore(REC_STORE,true);

            // Display list of products already set.
            // Ignore products which 
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
                    // Calendar objects to display time of products in 
                    // user-desired format
                    java.util.Calendar rightNow = java.util.Calendar.getInstance();
                        Date date = null;
                     rightNow.setTime(date);
                    String year = String.valueOf(rightNow.get(java.util.Calendar.YEAR));
                    String month = String.valueOf(rightNow.get(java.util.Calendar.MONTH) + 1);
                    String day = String.valueOf(rightNow.get(java.util.Calendar.DATE));
                    String am = "";
                    if ( rightNow.get(java.util.Calendar.AM_PM) == 0)
                        am = "PM";
                        else if (rightNow.get(java.util.Calendar.AM_PM) == 1)
                        am = "AM";
                         String hr = String.valueOf(rightNow.get(java.util.Calendar.HOUR));
                         String min = String.valueOf(rightNow.get(java.util.Calendar.MINUTE));

                         setAt = name + "-" + month + "/" + day + "/" + year + " " + hr  + ":" + min + " " + am;
                 } else setAt = name;

                 // A list of appointments are formed for display and cancel 
                 // purposes     
                 list.append(setAt,null);
                 cg.append(setAt,null);
                 at = new String("");
                 name = new String("");
            }
             
          }catch(javax.microedition.rms.RecordStoreException exc)
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
  public FormDatabaseMidlet() {
      
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.SCREEN, 1);
        displayCommand = new Command("View",Command.BACK, 1);
        setCommand = new Command("Set",Command.BACK, 1);
        deleteCommand = new Command("Cancel",Command.BACK, 1);
        saveCommand =  new Command("Save",Command.SCREEN, 1);
        menuCommand =  new Command("Options",Command.SCREEN, 1);
        delCommand =   new Command("Delete",Command.SCREEN, 1);
     
   
  }
  
   // public Form form;
    //public Command command;
    //public TextField saisie, designation;  
    //public StringItem dateDay; 
    //public StringItem result;
    //public StringItem quantityOfStock; 
   // public StringItem newQuantity;
   

    
 public void startApp() {
        
        try
    {
         // The first screen of the application
       welcomeForm = new Form("Product");


       getProducts();

       // If the list of products is empty, notify user
       if (list.size() == 0)
       {

          welcomeForm.append("Aucun produit n'est definit!. Suivre Menu->Definir");
          welcomeForm.addCommand(exitCommand);
          welcomeForm.addCommand(menuCommand);
          welcomeForm.setCommandListener(this);
          welcomeForm.setItemStateListener(this);
          display.setCurrent(welcomeForm);
       }
       // else display the list of products 
       else
       {
          welcomeForm.append(list);
          welcomeForm.addCommand(menuCommand);
          welcomeForm.setItemStateListener(this);
          welcomeForm.setCommandListener(this);
          display.setCurrent(welcomeForm);
       }

      menuForm = new Form("Options");
      menuForm.append("Sélectionner la Vue pour afficher les produits déjà enregistrer");
      menuForm.addCommand(displayCommand);
      menuForm.addCommand(setCommand);
      menuForm.addCommand(deleteCommand);
      menuForm.setCommandListener(this);
      menuForm.setItemStateListener(this);

     displayForm = new Form("Vue");




      setForm = new Form("Définir un produit");
      apptField = new TextField("Nom","", 20,0);
      saisieField = new TextField("Saisie","", 20,0);
      designationField = new TextField("designation","", 20,0);
      quantityOfStockField = new TextField("quantityOfStock","", 20,0);
      newQuantityField = new TextField("newQuantity","", 20,0);
      apptDate = new DateField("Définit le ", DateField.DATE_TIME);

      setForm.append(apptField);
      setForm.append(apptDate);
      setForm.append(saisieField);
      setForm.append(designationField);
      setForm.append(quantityOfStockField);
      setForm.append(newQuantityField);
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
        // The name of product is set to a variable when the 
       //  name input field is changed
         if (item == apptField)
        {
            apptName = apptField.getString();
         }

          if (item == saisieField)
        {
            saisie = saisieField.getString();
         }

          if (item == designationField)
        {
            designation = designationField.getString();
         }

          if (item == quantityOfStockField)
        {
            quantityOfStock = quantityOfStockField.getString();
         }

          if (item == newQuantityField)
        {
            newQuantity = newQuantityField.getString();
         }
        // If the ChoiceGroup item state on Delete form is changed, 
         //it sets an array of 
       //appointments selected for deletion
        if (item == cg)
        {
        cg.getSelectedFlags(deleted);
        }
   }


    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    
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
          
          getProducts();
          
          displayForm.append(list);
          displayForm.addCommand(exitCommand);
          displayForm.addCommand(menuCommand);
          displayForm.setCommandListener(this);
          displayForm.setItemStateListener(this);
          display.setCurrent(displayForm);
          
     
     }
     
     // When hit Cancel, a selectable list of products is 
     //displayed. It allows to select
// multiple products for purpose of deletion
     if (c==deleteCommand)
     {
          
          getProducts();
          deleteForm.append(cg);
          deleted = new boolean[cg.size()];
          deleteForm.addCommand(delCommand);
          deleteForm.addCommand(menuCommand);
          deleteForm.setCommandListener(this);
          deleteForm.setItemStateListener(this);
          display.setCurrent(deleteForm);
     }
          
// When hit Menu, the menu form displays which provides 
//options of setting products and cancel
     if (c==menuCommand)
          display.setCurrent(menuForm);
     
// When hit Save, insert record into record store as stream of bytes     
     if (c==saveCommand)
     {
          try
          {
          
          String appt = apptName + " " + apptTime + " " + saisie  + " " + designation  + " " + quantityOfStock  + " " + newQuantity ; 
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