/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import filemanager.webviewui.WebViewUI;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class Jembatan {
  
  public static void buatFolder(WebViewUI ui, String namaFolder) {
    String js = "";
    js += "var berkas = new Berkas();";
    js += "berkas.setNama('"+namaFolder+"');";
    js += "berkas.setJenis('folder');";
    js += "berkas.setPathAbsolut('/home/folder/"+namaFolder+"');";
    js += "berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);";
    js += "berkas.pasangElemen($('.tempatBerkas'));";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void pasangEventBerkas(WebViewUI ui) {
    String js = "";
    js += "Berkas.pasangEvent(Berkas.eventSaatTerpilihBanyak,";
    js += "Berkas.eventSaatTerpilihSatu,";
    js += "Berkas.eventSaatTidakTerpilih);";
    
    ui.eksekusiJavascript(js);
  }
}
