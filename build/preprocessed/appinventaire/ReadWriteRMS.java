/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appinventaire;

import java.util.Enumeration;
import java.util.Hashtable;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class ReadWriteRMS extends MIDlet implements CommandListener {
    //private static final String kUser = "user";

  //private static final String kPassword = "password";
  private static final String saisie = "saisie";
  private static final String designation = "designation";
  private static final String dateDay = "date";
  private static final String quantityOfStock = "quantityOfStock";
  private static final String newQuantity = "newQuantity";

  private Preferences mPreferences;

  private Form mForm;

  private TextField mUserField, mPasswordField, saisieField, dateDayField, designationField, quantityOfStockField, newQuantityField;

  public ReadWriteRMS() {
    try {
      mPreferences = new Preferences("preferences");
    } catch (RecordStoreException rse) {
      mForm = new Form("Exception");
      mForm.append(new StringItem(null, rse.toString()));
      mForm.addCommand(new Command("Exit", Command.EXIT, 0));
      mForm.setCommandListener(this);
      return;
    }

    mForm = new Form("Gestion d'inventaire");
    //mUserField = new TextField("Name", mPreferences.get(kUser), 32, 0);
    //mPasswordField = new TextField("Password", mPreferences.get(kPassword), 32, 0);
    saisieField = new TextField("Saisie", mPreferences.get(saisie), 32, 0);
    designationField = new TextField("designation", mPreferences.get(designation), 32, 0);
    dateDayField = new TextField("date", mPreferences.get(dateDay), 32, 0);
    quantityOfStockField = new TextField("quantité de Stock", mPreferences.get(quantityOfStock), 32, 0);
    newQuantityField = new TextField("nouvelle quantité de Stock", mPreferences.get(newQuantity),32, 0);
    
    //mForm.append(mUserField);
    //mForm.append(mPasswordField);
    mForm.append(saisieField);
    mForm.append(designationField);
    mForm.append(dateDayField);
    mForm.append(quantityOfStockField);
    mForm.append(newQuantityField);

    mForm.addCommand(new Command("Exit", Command.EXIT, 0));
    mForm.setCommandListener(this);
  }

  public void startApp() {
    Display.getDisplay(this).setCurrent(mForm);
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    // Save the user name and password.
    //mPreferences.put(kUser, mUserField.getString());
    //mPreferences.put(kPassword, mPasswordField.getString());
    mPreferences.put(saisie, saisieField.getString());
    mPreferences.put(designation, designationField.getString());
    mPreferences.put(designation, dateDayField.getString());
    mPreferences.put(quantityOfStock, quantityOfStockField.getString());
    mPreferences.put(newQuantity, newQuantityField.getString());
    try {
      mPreferences.save();
    } catch (RecordStoreException rse) {
    }
  }

  public void commandAction(Command c, Displayable s) {
    if (c.getCommandType() == Command.EXIT) {
      destroyApp(true);
      notifyDestroyed();
    }
  }
}

class Preferences {
  private String mRecordStoreName;

  private Hashtable mHashtable;

  public Preferences(String recordStoreName) throws RecordStoreException {
    mRecordStoreName = recordStoreName;
    mHashtable = new Hashtable();
    load();
  }

  public String get(String key) {
    return (String) mHashtable.get(key);
  }

  public void put(String key, String value) {
    if (value == null)
      value = "";
    mHashtable.put(key, value);
  }

  private void load() throws RecordStoreException {
    RecordStore rs = null;
    RecordEnumeration re = null;

    try {
      rs = RecordStore.openRecordStore(mRecordStoreName, true);
      re = rs.enumerateRecords(null, null, false);
      while (re.hasNextElement()) {
        byte[] raw = re.nextRecord();
        String pref = new String(raw);
        // Parse out the name.
        int index = pref.indexOf('|');
        String name = pref.substring(0, index);
        String value = pref.substring(index + 1);
        put(name, value);
      }
    } finally {
      if (re != null)
        re.destroy();
      if (rs != null)
        rs.closeRecordStore();
    }
  }

  public void save() throws RecordStoreException {
    RecordStore rs = null;
    RecordEnumeration re = null;
    try {
      rs = RecordStore.openRecordStore(mRecordStoreName, true);
      re = rs.enumerateRecords(null, null, false);

      // First remove all records, a little clumsy.
      while (re.hasNextElement()) {
        int id = re.nextRecordId();
        rs.deleteRecord(id);
      }

      // Now save the preferences records.
      Enumeration keys = mHashtable.keys();
      while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();
        String value = get(key);
        String pref = key + "|" + value;
        byte[] raw = pref.getBytes();
        rs.addRecord(raw, 0, raw.length);
      }
    } finally {
      if (re != null)
        re.destroy();
      if (rs != null)
        rs.closeRecordStore();
    }
  }
}


