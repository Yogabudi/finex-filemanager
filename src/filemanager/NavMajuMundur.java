/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.util.ArrayList;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class NavMajuMundur {
  
  private ArrayList<Berkas> jejak = new ArrayList<>();
  private Berkas berkasTerpilih;
  private WebViewUI ui;
  private int index = -1;
  
  public NavMajuMundur(WebViewUI ui) {
    this.ui = ui;
  }

  public Berkas getBerkasTerpilih() {
    return berkasTerpilih;
  }

  public Berkas majuKe(Berkas berkas) {
    for(int i = 0; i < jejak.size(); i++) {
      jejak.remove(i);
    }
    
    berkasTerpilih = berkas;
    
    return berkasTerpilih;
  }
  
  public Berkas maju() {
    if(index > -1) {
      berkasTerpilih = jejak.get(index--);
    }
    
    return berkasTerpilih;
  }
  
  public Berkas mundur() {
    if(berkasTerpilih.getObjekFile().getParent() != null) {
      jejak.add(berkasTerpilih);
      index = jejak.size() - 1;
      berkasTerpilih = new Berkas(ui, berkasTerpilih.getObjekFile().getParent());
    }
    
    return berkasTerpilih;
  }
  
  public boolean sampaiRoot() {
    return berkasTerpilih.getObjekFile().getAbsolutePath().equals("/");
  }
}
