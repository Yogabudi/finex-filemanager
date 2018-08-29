/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager.webviewui;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowOpeningEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowWillOpenEvent;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class WebViewUI extends JPanel
        implements WebBrowserListener {

  private JFrame windowUtama;
  private JWebBrowser browser;
  private PendengarWebBrowser pendengarWeb;

  public WebViewUI(JFrame windowUtama) {
    this.windowUtama = windowUtama;
    this.pendengarWeb = null;
    
    this.browser = new JWebBrowser();
    this.browser.setMenuBarVisible(false);
    this.browser.setBarsVisible(false);
    this.browser.setButtonBarVisible(false);
    this.browser.setJavascriptEnabled(true);
    this.browser.addWebBrowserListener(this);

    this.setLayout(new BorderLayout());
    this.add(browser, BorderLayout.CENTER);
  }

  public JWebBrowser dapatkanKomponenUtama() {
    return browser;
  }
  
  public void setPendengarWebBrowser(PendengarWebBrowser pdg) {
    this.pendengarWeb = pdg;
  }
  
  public void setURL(String url) {
    this.browser.navigate(url);
  }
  
  public void eksekusiJavascript(String js) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        WebViewUI.this.browser.executeJavascript(js);
      }
    });
  }
  
  public Object eksekusiJavascriptDenganHasil(String js) {
    return this.browser.executeJavascriptWithResult(js);
  }

  public static void inisialisasi() {
    NativeInterface.open();
    UIUtils.setPreferredLookAndFeel();
  }

  public static void jalankanEventPump() {
    NativeInterface.runEventPump();
  }

  @Override
  public void windowWillOpen(WebBrowserWindowWillOpenEvent wbwwoe) {
  }

  @Override
  public void windowOpening(WebBrowserWindowOpeningEvent wbwoe) {
  }

  @Override
  public void windowClosing(WebBrowserEvent wbe) {
  }

  @Override
  public void locationChanging(WebBrowserNavigationEvent wbne) {
  }

  @Override
  public void locationChanged(WebBrowserNavigationEvent wbne) {
  }

  @Override
  public void locationChangeCanceled(WebBrowserNavigationEvent wbne) {
  }

  @Override
  public void loadingProgressChanged(WebBrowserEvent wbe) {
    JWebBrowser webBrowser = wbe.getWebBrowser();
    
    if(this.pendengarWeb != null) {
      if(webBrowser.getLoadingProgress() >= 100) {
        this.pendengarWeb.saatSelesaiLoading(wbe, webBrowser);
      }
    }
  }

  @Override
  public void titleChanged(WebBrowserEvent wbe) {
  }

  @Override
  public void statusChanged(WebBrowserEvent wbe) {
  }

  @Override
  public void commandReceived(WebBrowserCommandEvent wbce) {
    JWebBrowser webBrowser = wbce.getWebBrowser();
    
    if(this.pendengarWeb != null) {
      this.pendengarWeb.saatPerintahDiterima(wbce, webBrowser);
    }
  }

}
