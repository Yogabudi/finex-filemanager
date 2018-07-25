/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager.webviewui;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

/**
 *
 * @author Yoga Budi Yulianto
 */
public interface PendengarWebBrowser {
  
  public void saatSelesaiLoading(WebBrowserEvent wbe, JWebBrowser browser);
  public void saatPerintahDiterima(WebBrowserCommandEvent wbce, JWebBrowser browser);
  
}
