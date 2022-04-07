/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinventaire;

//import javax.microedition.midlet.*;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

/**
 * @author kaber
 */
public final class RecordMidlet extends MIDlet implements CommandListener {
    private RecordStore myRecordStore;
    private static final int FIRST_EXECUTION_VAL = 1;
    private boolean FIRST_EXECUTION = false;
    private final Display myDisplay;
    private final Form myForm = new Form("Gestion d'inventaire");
    private final Command exitCommand = new Command("Quitter", Command.EXIT, 1);
    private final Command deleteCommand = new Command("Suprimer RS", Command.EXIT, 1);
    public TextField saisie, designation,dateDay, quantityOfStock, newQuantity;
    //spublic  StringItem result;
    private final StringItem myStringItem;
    private int executionNumber = -1;     
    public RecordMidlet() {
        openRecordStore();
        readRecord();
        writeRecord();                    
        myDisplay = Display.getDisplay(this);        
        myStringItem = new StringItem("Enregistrement", "" + executionNumber );
        saisie = new TextField("Saisie", "", 30, TextField.ANY);
        designation = new TextField("Designation", "", 30, TextField.ANY);
        //dateDay = new StringItem("la date", "");
        dateDay = new TextField("la date", "", 30, TextField.ANY);
        quantityOfStock = new TextField("quantité de Stock", "", 30, TextField.ANY);
        newQuantity = new TextField("nouvelle quantité de Stock", "", 30, TextField.ANY);
        myForm.append(myStringItem);
       // myForm.append(result);
        myForm.addCommand(exitCommand);
        myForm.addCommand(deleteCommand);
        myForm.append(saisie);
        myForm.append(designation);
        myForm.append(dateDay);
        myForm.append(quantityOfStock);
        myForm.append(newQuantity);
        myForm.setCommandListener(this);
    }
    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        closeRecordStore();
        //deleteRecordStore();
        notifyDestroyed();
    }
    protected void pauseApp() {
    }
    protected void startApp() throws MIDletStateChangeException {
        myDisplay.setCurrent(myForm);
    }
    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            try {
                destroyApp(false);
            } catch (MIDletStateChangeException e) {
                e.printStackTrace();
            }
        }
        else if (c==deleteCommand) {
            closeRecordStore();
            deleteRecordStore();
        }
        // my add
        //else {
          //  myStringItem.setText("\nSaisie: \n "+ saisie.getString()+"\nDesignation: \n"+designation.getString()+"\nla date :\n "+dateDay.getString()+"\nquantité de Stock :\n "+quantityOfStock.getString()+"\n nouvelle quantité de Stock :\n "+newQuantity.getString());
        //}
    }
    public void openRecordStore() {
        try {
            myRecordStore = RecordStore.openRecordStore("Mon enregistrementm", true);
            if (myRecordStore.getNumRecords()==0) {
                FIRST_EXECUTION = true;
            }
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }
    public void writeRecord() {
        byte[] record;
        try {
            if (FIRST_EXECUTION) {
                record = intToBytesArray(FIRST_EXECUTION_VAL); //First Execution
                myRecordStore.addRecord(record, 0, record.length);
            }
            else {
                record = intToBytesArray(++executionNumber);
                myRecordStore.setRecord(1, record, 0, record.length);
            }
        } catch (RecordStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void readRecord() {
        byte[] record = new byte[4];
        if (!FIRST_EXECUTION)
            try {
                record = myRecordStore.getRecord(1);
                executionNumber = byteArrayToInt(record);
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        else
            executionNumber = FIRST_EXECUTION_VAL;
        record = null;
    }
    public void closeRecordStore() {
        try {
            myRecordStore.closeRecordStore();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }
    public void deleteRecordStore() {
        try {
            RecordStore.deleteRecordStore("HelloWorldRS");
        } catch (RecordStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private byte[] intToBytesArray(int i) {
          return new byte[] {
            (byte) ((i >>> 24) & 0xff),
            (byte) ((i >>> 16) & 0xff),
            (byte) ((i >>> 8) & 0xff),
            (byte) (i & 0xff),
          };
        }
    private int byteArrayToInt(byte[] b) 
    {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }
}
