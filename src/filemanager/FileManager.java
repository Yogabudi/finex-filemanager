package filemanager;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.EventQueue;

import filemanager.webviewui.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FileManager extends JFrame
implements PendengarWebBrowser {

  public WebViewUI ui;

  public FileManager() {
    super();

    this.setSize(1200, 650);
    this.setTitle("Yoga File Manager");
    this.setName("UI Utama");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.ketengahkan();
//    this.setExtendedState(JFrame.MAXIMIZED_BOTH);

    ui = new WebViewUI(this);
    ui.setPendengarWebBrowser(this);
    ui.setURL(FileManager.class.getResource("/ui/index.html").toExternalForm());
    
    this.getContentPane().add(ui);
  }

  public void ketengahkan() {
    Dimension dimensiLayar = getToolkit().getScreenSize();
    int x = (int) (dimensiLayar.getWidth() - this.getWidth()) / 2;
    int y = (int) (dimensiLayar.getHeight() - this.getHeight()) / 2;

    this.setLocation(x, y);
  }

  public static void main(String[] args) {
    WebViewUI.inisialisasi();
    
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new FileManager().setVisible(true);
      }
    });
    
    WebViewUI.jalankanEventPump();
  }

  @Override
  public void saatSelesaiLoading(WebBrowserEvent wbe, JWebBrowser browser) {
    Jembatan.pasangEventSeleksiBerkas(ui);
  }

  @Override
  public void saatPerintahDiterima(WebBrowserCommandEvent wbce, JWebBrowser browser) {
    String perintah = wbce.getCommand();
    Object[] param = wbce.getParameters();
    
    if(perintah.equals("kirimInfoBerkas")) {
      String[] infoBerkas = ((String)param[0]).split(",");
      
      String info = "NAMA BERKAS : " + infoBerkas[0] + "\n";
      info += "JENIS : " + infoBerkas[1] + "\n";
      info += "PATH : " + infoBerkas[2] + "\n";
      
      JOptionPane.showMessageDialog(FileManager.this, info);
    }
    else if(perintah.equals("tampilkanRoot")) {
      for(int i = 1; i <= 5; i++) {
        Jembatan.buatFolder(ui, "Folder kuning " + i);
      }
    }
  }

}
