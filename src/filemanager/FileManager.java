package filemanager;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import filemanager.webviewui.*;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FileManager extends JFrame implements PendengarWebBrowser {

  public WebViewUI ui;
  public NavMajuMundur nav;
  public ListBreadcrumbBerkas bcBerkas = new ListBreadcrumbBerkas();
  
  public List<Berkas> jejakNav = new ArrayList<>();
  public ArrayList<Berkas> holderBerkas = new ArrayList<>();
  
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
    ui.setURL(getClass().getResource("/ui/index.html").toExternalForm());
    this.getContentPane().add(ui);
    
    nav = new NavMajuMundur(ui);
  }

  public void ketengahkan() {
    Dimension dimensiLayar = getToolkit().getScreenSize();
    int x = (int) (dimensiLayar.getWidth() - this.getWidth()) / 2;
    int y = (int) (dimensiLayar.getHeight() - this.getHeight()) / 2;

    this.setLocation(x, y);
  }

  public static void main(String[] args) {
    WebViewUI.inisialisasi();
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new FileManager().setVisible(true);
      }
    });
    
    WebViewUI.jalankanEventPump();
  }

  @Override
  public void saatSelesaiLoading(WebBrowserEvent wbe, JWebBrowser browser) {
    Berkas berkas = new Berkas(ui, System.getProperty("user.home"));
    nav.majuKe(berkas).tampilkanListBerkas();
    
    bcBerkas.isiDariPath(berkas.pecahPathAbsolut(), ui);
    bcBerkas.tandaiYangTerakhir();
    
    Berkas.setTextPathPadaJS(ui, berkas.getObjekFile().getAbsolutePath());
    System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
  }

  @Override
  public void saatPerintahDiterima(WebBrowserCommandEvent wbce, JWebBrowser browser) {
    String perintah = wbce.getCommand();
    Object[] param = wbce.getParameters();
    
    if(perintah.equals("log")) {
      System.out.println(param[0].toString().toUpperCase() + " : " + param[1]);
    }
//    else if(perintah.equals("tesArray")) {
//      for(int i = 0; i < param.length; i++) {
//        System.out.println((String)param[i]);
//      }
//    }
    else if(perintah.equals("tampilkanListBerkas")) {
      String pathAbsolut = param[0].toString();
      
      try {
        Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
        
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkasTerpilih).tampilkanListBerkas();
        bcBerkas.isiDariPath(berkasTerpilih.pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);

        Berkas.setTextPathPadaJS(ui, berkasTerpilih.getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      catch(NullPointerException nullex) {
        JOptionPane.showMessageDialog(this,
                  "Maaf, anda tidak diizinkan membuka folder ini",
                  "Anda Bukan Root!",
                  JOptionPane.ERROR_MESSAGE);
        
        Berkas.sembunyikanCirclePadaJS(ui);
          
        if(!nav.sampaiRoot()) {
          nav.mundur().tampilkanListBerkas();
          bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);

          nav.majuKe(nav.getBerkasTerpilih());
        }
        else {
          Berkas berkas = new Berkas(ui, "/");
          nav.majuKe(berkas).tampilkanListBerkas();
        }
      }
      
    }
    else if(perintah.equals("tampilkanListBerkasDariBreadcrumb")) {
      String pathAbsolut = (String)param[0];
      String labelBc = (String)param[1];
      
      Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
      
      Berkas.tampilkanCirclePadaJS(ui);
      nav.mundurKe(berkasTerpilih).tampilkanListBerkas();
      bcBerkas.getBreadcrumb(labelBc).tandaiPadaJS();
      Berkas.sembunyikanCirclePadaJS(ui);
      
      Berkas.setTextPathPadaJS(ui, berkasTerpilih.getObjekFile().getAbsolutePath());
      System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
    else if(perintah.equals("tampilkanBerkasSebelumnya")) {
      if(!nav.sampaiRoot()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.mundur().tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
    }
    else if(perintah.equals("tampilkanBerkasKedepan")) {
      Berkas.tampilkanCirclePadaJS(ui);
      nav.maju().tampilkanListBerkas();
      bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
      Berkas.sembunyikanCirclePadaJS(ui);
      
      Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
    else if(perintah.equals("keyEnterDitekan")) {
      String namaKomponen = (String)param[0];
      String pathAbsolut = (String)param[1];
      
      if(namaKomponen.equals("#txPath")) {
        if(!pathAbsolut.equals("")) {
          Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);

          if(berkasTerpilih.berkasTersedia()) {
            Berkas.tampilkanCirclePadaJS(ui);
            nav.majuKe(berkasTerpilih).tampilkanListBerkas();
            bcBerkas.isiDariPath(berkasTerpilih.pecahPathAbsolut(), ui);
            Berkas.sembunyikanCirclePadaJS(ui);
            
            System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
          }
          else {
            JOptionPane.showMessageDialog(this,
                "Path yang anda masukkan tidak valid!\n" +
                "Pastikan folder tujuan anda tersedia",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          JOptionPane.showMessageDialog(this,
                "Anda belum memasukkan path folder tujuan anda!\n" +
                "Mohon masukkan path folder tujuan anda",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else if(perintah.equals("loncatKeDokumen")) {
      String path = System.getProperty("user.home") + "/Documents";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeFoto")) {
      String path = System.getProperty("user.home") + "/Pictures";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeMusik")) {
      String path = System.getProperty("user.home") + "/Music";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeVideo")) {
      String path = System.getProperty("user.home") + "/Videos";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeUnduhan")) {
      String path = System.getProperty("user.home") + "/Downloads";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeRoot")) {
      String path = "/";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeHome")) {
      String path = System.getProperty("user.home");
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("buatFolderBaru")) {
      String namaFolder = (String)param[0];
      boolean tersembunyi = (boolean)param[1];
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      
      try {
        if(!namaFolder.equals("")) {
          if(!namaFolder.contains("/")) {
            if(!tersembunyi) {
              Berkas.buatFolderBaru(lokasiSekarang + "/" + namaFolder, ui);
              Berkas.tandaiBerkasPadaJS(namaFolder, ui);
              Berkas.scrollKeBawahPadaJS(ui);
            }
            else {
              Berkas.buatFolderBaru(lokasiSekarang + "/." + namaFolder, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFolder, ui);
              Berkas.scrollKeBawahPadaJS(ui);
            }   
          }
          else {
            JOptionPane.showMessageDialog(this,
              "Nama folder yang anda masukkan tidak valid!\n" +
              "Disarankan menggunakan karakter huruf dan angka",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          JOptionPane.showMessageDialog(this,
              "Anda belum memasukkan nama folder baru!\n" +
              "Mohon masukkan nama folder baru",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      catch(AccessDeniedException accdenex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan membuat folder disini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(FileAlreadyExistsException faee) {
        JOptionPane.showMessageDialog(this,
              "Maaf, nama folder yang anda masukkan sudah ada!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(IOException ioe) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat membuat folder, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("buatFileBaru")) {
      String namaFile = (String)param[0];
      boolean tersembunyi = (boolean)param[1];
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      
      try {
        if(!namaFile.equals("")) {
          if(!namaFile.contains("/")) {
            if(!tersembunyi) {
              Berkas.buatFileBaru(lokasiSekarang + "/" + namaFile, ui);
              Berkas.tandaiBerkasPadaJS(namaFile, ui);
              Berkas.scrollKeBawahPadaJS(ui);
            }
            else {
              Berkas.buatFileBaru(lokasiSekarang + "/." + namaFile, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFile, ui);
              Berkas.scrollKeBawahPadaJS(ui);
            }   
          }
          else {
            JOptionPane.showMessageDialog(this,
              "Nama file yang anda masukkan tidak valid!\n" +
              "Disarankan menggunakan karakter huruf dan angka",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          JOptionPane.showMessageDialog(this,
              "Anda belum memasukkan nama file baru!\n" +
              "Mohon masukkan nama file baru",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      catch(AccessDeniedException accdenex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan membuat file disini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(FileAlreadyExistsException faee) {
        JOptionPane.showMessageDialog(this,
              "Maaf, nama file yang anda masukkan sudah ada!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(IOException ioe) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat membuat file, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("bukaTerminal")) {
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      String fileSh = "src/filemanager/programsh/bukaterminal.sh";
      
      try {
        ProcessBuilder pb = new ProcessBuilder(fileSh, lokasiSekarang);
        Process proc = pb.start();
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat membuka terminal, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
      }
      
    }
    else if(perintah.equals("kosongkanTrash")) {
      try {
        ProcessBuilder pb = new ProcessBuilder("gio", "trash", "--empty");
        Process proc = pb.start();
      }
      catch(IOException ioex) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat mengosongkan trash, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        ioex.printStackTrace();
      }
      
    }
    else if(perintah.equals("cutBerkas")) {
      String namaBerkas = (String)param[0];
      Berkas berkas = new Berkas(ui, namaBerkas);
      
      holderBerkas.clear();
      holderBerkas.add(berkas);
    }
    else if(perintah.equals("tempelBerkas")) {
      
    }
    else if(perintah.equals("hideBerkas")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        
        Berkas berkas = new Berkas(ui, pathAbsolut);
        berkas.hideBerkas();
      }
    }
    else if(perintah.equals("unhideBerkas")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];

        Berkas berkas = new Berkas(ui, pathAbsolut);
        berkas.unhideBerkas();
      }
    }
    else if(perintah.equals("duplikatBerkas")) {
      String pathAbsolut = (String)param[0];
      
      Berkas berkas = new Berkas(ui, pathAbsolut);
      
      try {
        Berkas duplikatan = berkas.duplikatDanTampilkan();
        
      }
      catch(IOException ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("ubahNama")) {
      String pathAbsolut = (String)param[0];
      String namaBaru = (String)param[1];
      
      Berkas berkas = new Berkas(ui, pathAbsolut);
      berkas.ubahNamaDanTampilkan(namaBaru);
    }
    else if(perintah.equals("hapusKeTrash")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        Berkas berkas = new Berkas(ui, pathAbsolut);

        try {
          berkas.hapusKeTrash();
        }
        catch(IOException ioex) {
          JOptionPane.showMessageDialog(this,
                "Terjadi Kesalahan saat menghapus berkas, silahkan coba lagi!\n",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
          ioex.printStackTrace();
        }
      }
    }
    else if(perintah.equals("hapusBerkasPermanen")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        Berkas berkas = new Berkas(ui, pathAbsolut);

        try {
          berkas.hapusPermanen();

        }
        catch(IOException ex) {
          JOptionPane.showMessageDialog(this,
                "Terjadi Kesalahan saat menghapus berkas, silahkan coba lagi!\n",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    }
    else if(perintah.equals("tampilkanInfoBerkas")) {
      String pathAbsolut = (String)param[0];
      Berkas berkas = new Berkas(ui, pathAbsolut);
      
      try {
        berkas.tampilkanInfoBerkas();
      }
      catch(IOException ex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan melihat detail berkas ini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(NullPointerException ex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan melihat detail berkas ini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("cariBerkas")) {
      String teks = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            ArrayList<Berkas> dataCari =
                    Berkas.cariBerkas(Berkas.BERDASAR_NAMA, teks, berkasTempat);
            
            for(int i = 0; i < dataCari.size(); i++) {
              dataCari.get(i).buatBerkasPadaJS();
            }
          }
          catch (IOException ex){
            ex.printStackTrace();
          }
        }
      });

      execService.shutdown();
    }
    else if(perintah.equals("cariBerkasBerdasarTglDibuat")) {
      String tgl = (String)param[0];
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            ArrayList<Berkas> dataCari =
                  Berkas.cariBerkas(Berkas.BERDASAR_TGL_DIBUAT, tgl, berkasTempat);
            
            for(int i = 0; i < dataCari.size(); i++) {
              dataCari.get(i).buatBerkasPadaJS();
            }
          }
          catch (IOException ex){
            ex.printStackTrace();
          }
        }
      });

      execService.shutdown();
    }
  }

  public void tampilkanInfoBerkas(Berkas berkasTerpilih) {
    String namaBerkas = berkasTerpilih.getObjekFile().getName();
    String jenis = (berkasTerpilih.getObjekFile().isDirectory()) ? "folder" : "file";
    String path = berkasTerpilih.getObjekFile().getAbsolutePath();

    String info = "" +
    "NAMA BERKAS : " + namaBerkas + "\n" +
    "JENIS : " + jenis + "\n" +
    "PATH : " + path + "\n" +
    "PARENT : " + berkasTerpilih.getObjekFile().getParent();

    JOptionPane.showMessageDialog(this, info);
  }
  
}
