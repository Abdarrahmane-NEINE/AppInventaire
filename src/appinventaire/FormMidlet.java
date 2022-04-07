/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appinventaire;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;

/**
 * @author kaber
 */
public class FormMidlet extends MIDlet implements CommandListener{
    
    public Display display;
    public Form form;
    public Command command;
    //public TextField saisie, designation;  
    //public StringItem dateDay; 
    public StringItem result;
    //public StringItem quantityOfStock; 
   // public StringItem newQuantity;
    public TextField saisie, designation,dateDay, quantityOfStock, newQuantity;

    
    public void startApp() {
        display = Display.getDisplay(this);
        form = new Form("gestion d'inventaire");
        command = new Command("ok", Command.OK, 1);
        saisie = new TextField("Saisie", "", 30, TextField.ANY);
        designation = new TextField("Designation", "", 30, TextField.ANY);
        result = new StringItem("resultat", "");
        //dateDay = new StringItem("la date", "");
        dateDay = new TextField("la date", "", 30, TextField.ANY);
        quantityOfStock = new TextField("quantité de Stock", "", 30, TextField.ANY);
        newQuantity = new TextField("nouvelle quantité de Stock", "", 30, TextField.ANY);
        
        form.addCommand(command);
        form.append(saisie);
        form.append(designation);
        form.append(dateDay);
        form.append(quantityOfStock);
        form.append(newQuantity);
        form.setCommandListener(this);
        form.append(result);
        
        display.setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    
    public void commandAction(Command c, Displayable d) {
        if(c==command){
           result.setText("\nSaisie: \n "+ saisie.getString()+"\nDesignation: \n"+designation.getString()+"\nla date :\n "+dateDay.getString()+"\nquantité de Stock :\n "+quantityOfStock.getString()+"\n nouvelle quantité de Stock :\n "+newQuantity.getString());
        }
    }

    
   // public void commandAction(Command c, Displayable d) {
     //   throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   // }
}
