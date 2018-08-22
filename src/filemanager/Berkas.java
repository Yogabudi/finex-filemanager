
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class Berkas {
  
  private File objekFile;
  private String icon;
  private int jumlahBerkas = 0;
  private long ukuranFile = 0;
  private boolean tersembunyi = false;
  private ArrayList<Berkas> duplikatanBerkas = new ArrayList<>();
  
  private WebViewUI ui;
  
  private ExecutorService execService;
  
  public Berkas(WebViewUI ui, String pathname) {
    this.objekFile = new File(pathname);
    this.jumlahBerkas = (objekFile.listFiles() != null)
                          ? objekFile.listFiles().length : 0;
    
    // konversikan ukuran file dari bytes ke KB
    this.ukuranFile = objekFile.length() / 1024;
    
    if(objekFile.isDirectory()) {
      this.icon = "assets/Icons/64/101-folder-5.png";
    }
    else {
      this.icon = "assets/Icons/64/053-document-7.png";
    }
    
    this.ui = ui;
    
    this.execService = Executors.newCachedThreadPool();
  }

  public boolean isTersembunyi() {
    return tersembunyi;
  }

  public void setTersembunyi(boolean tersembunyi) {
    this.tersembunyi = tersembunyi;
  }
  
  public long getUkuranFile() {
    return ukuranFile;
  }

  public void setUkuranFile(long ukuranFile) {
    this.ukuranFile = ukuranFile;
  }

  public int getJumlahBerkas() {
    return jumlahBerkas;
  }

  public void setJumlahBerkas(int jumlahBerkas) {
    this.jumlahBerkas = jumlahBerkas;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public WebViewUI getUi() {
    return ui;
  }

  public void setUi(WebViewUI ui) {
    this.ui = ui;
  }

  public File getObjekFile() {
    return objekFile;
  }
  
  public boolean berkasTersedia() {
    return objekFile.exists();
  }

  public Berkas[] listBerkas() {
    List<Berkas> daftarBerkas = new ArrayList<>();
    File[] daftarFile = objekFile.listFiles();
    
    for(int i = 0; i < daftarFile.length; i++) {
      Berkas berkas = new Berkas(ui, daftarFile[i].getAbsolutePath());
      berkas.setTersembunyi((berkas.getObjekFile().getName().charAt(0) == '.'));
      
      if(berkas.getObjekFile().isDirectory()) {
        berkas.setIcon("assets/Icons/64/101-folder-5.png");
      }
      else {
        berkas.setIcon("assets/Icons/64/053-document-7.png");
      }
      
      daftarBerkas.add(berkas);
    }
    
    return daftarBerkas.toArray(new Berkas[0]);
  }
  
  public void tampilkanListBerkas() {
    Berkas[] daftarBerkas = Berkas.this.listBerkas();

    Berkas.this.hapusSemuaBerkasPadaJS();
    for(int i = 0; i < daftarBerkas.length; i++) {
      if(daftarBerkas[i].objekFile.isDirectory()) {
        daftarBerkas[i].buatBerkasPadaJS();
      }
    }

    for(int i = 0; i < daftarBerkas.length; i++) {
      if(daftarBerkas[i].objekFile.isFile()) {
        daftarBerkas[i].buatBerkasPadaJS();
      }
    }
  }
  
  public void buatBerkasPadaJS() {
    String jenisBerkas = (objekFile.isDirectory()) ? "folder" : "file";
    
    String js = ""+
      "var berkas = new Berkas();"+
      "berkas.setNama('"+objekFile.getName()+"');"+
      "berkas.setJenis('"+jenisBerkas+"');"+
      "berkas.setIcon('"+icon+"');"+
      "berkas.setPathAbsolut('"+objekFile.getAbsolutePath()+"');"+
      "berkas.setJumlahBerkas("+jumlahBerkas+");"+
      "berkas.setUkuranFile("+ukuranFile+");"+
      "berkas.setTersembunyi("+tersembunyi+");"+
      "berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);"+
      "berkas.pasangElemen($('.tempatBerkas'));";

      ui.eksekusiJavascript(js);
  }
  
  public Berkas[] pecahPathAbsolut() {
    ArrayList<Berkas> hasil = new ArrayList<>();
    Path pathAbsolut = objekFile.toPath();
    String path = "/";
    
    hasil.add(new Berkas(ui, "/"));
    
    for(int i = 0; i < pathAbsolut.getNameCount(); i++) {
      path += pathAbsolut.getName(i).toString() + "/";
      
      hasil.add(new Berkas(ui, path));
    }
    
    return hasil.toArray(new Berkas[0]);
  }
  
  public void hapusKeTrash() throws IOException {
    ProcessBuilder pb = new ProcessBuilder("gio", "trash", objekFile.getAbsolutePath());
    Process proc = pb.start();
    
    hapusBerkasPadaJS(ui);
  }
  
  public void hapusPermanen() throws IOException {
    FileUtils.forceDelete(objekFile);
    
    hapusBerkasPadaJS(ui);
  }
  
  public Berkas duplikat() throws IOException {
    Berkas[] listBerkas = new Berkas(ui, objekFile.getParent()).listBerkas();
    int noSalinan = 1;
    String namaBerkas = objekFile.getName();
    
    for(int i = 0; i < listBerkas.length; i++) {
      if(listBerkas[i].getObjekFile()
              .getName().contains(namaBerkas + " (salinan ke-" + noSalinan + ")")) {
        noSalinan++;
        i = 0;
      }
      else {
        namaBerkas = objekFile.getName() + " (salinan ke-" + noSalinan + ")";
      }
    }
    
    Berkas berkasTujuan = new Berkas(ui,
            objekFile.getParent() + "/" + namaBerkas);
    Berkas berkasOri = new Berkas(ui, objekFile.getAbsolutePath());
    
    if(objekFile.isDirectory()) {
      FileUtils.copyDirectory(berkasOri.getObjekFile(),
                              berkasTujuan.getObjekFile());
    }
    else {
      FileUtils.copyFile(berkasOri.getObjekFile(),
                              berkasTujuan.getObjekFile());
    }
    
    return new Berkas(ui, berkasTujuan.getObjekFile().getAbsolutePath());
  }
  
  public Berkas duplikatDanTampilkan() throws IOException {
    Berkas berkas = duplikat();
    berkas.buatBerkasPadaJS();
    
    Berkas.tandaiBerkasPadaJS(berkas.getObjekFile().getName(), ui);
    Berkas.scrollKeBawahPadaJS(ui);
    
    return berkas;
  }
  
  public boolean ubahNama(String pathNamaBaru) {
    boolean sukses = false;
    
    if(objekFile.renameTo(new File(pathNamaBaru))) {
      sukses = true;
    }
    
    return sukses;
  }
  
  public void ubahNamaDanTampilkan(String namaBaru) {
    String pathNamaBaru = objekFile.getParent() + "/" + namaBaru;
    
    if(ubahNama(pathNamaBaru)) {
      Berkas.ubahNamaPadaJS(ui, namaBaru);
      Berkas.ubahPathAbsolutPadaJS(ui, pathNamaBaru);
    }
  }
  
  public void hideBerkas() {
    String pathNamaBaru = objekFile.getParent() + "/." + objekFile.getName();
    ubahNama(pathNamaBaru);
    
    Berkas.ubahNamaPadaJS(ui, "." + objekFile.getName());
    Berkas.ubahPathAbsolutPadaJS(ui, pathNamaBaru);
    Berkas.ubahTersembunyiPadaJS(ui, true);
  }
  
  public void unhideBerkas() {
    String pathNamaBaru = objekFile.getParent() + "/" +
                          objekFile.getName().substring(1);
    ubahNama(pathNamaBaru);
    
    Berkas.ubahNamaPadaJS(ui, objekFile.getName().substring(1));
    Berkas.ubahPathAbsolutPadaJS(ui, pathNamaBaru);
    Berkas.ubahTersembunyiPadaJS(ui, false);
  }
  
  public static Berkas buatFolderBaru(String pathFolder, WebViewUI ui)
          throws AccessDeniedException, IOException, FileAlreadyExistsException {
    Berkas berkas = null;
    
    Path folder = Files.createDirectory(Paths.get(pathFolder));
    berkas = new Berkas(ui, folder.toString());

    berkas.setTersembunyi((berkas.getObjekFile().getName().charAt(0) == '.'));
    berkas.setIcon("assets/Icons/64/101-folder-5.png");
    berkas.buatBerkasPadaJS();
      
    return berkas;
  }
  
  public static Berkas buatFileBaru(String pathFile, WebViewUI ui)
          throws AccessDeniedException, IOException, FileAlreadyExistsException {
    
    Berkas berkas = null;
    
    Path file = Files.createFile(Paths.get(pathFile));
    berkas = new Berkas(ui, file.toString());

    berkas.setTersembunyi((berkas.getObjekFile().getName().charAt(0) == '.'));
    berkas.setIcon("assets/Icons/64/053-document-7.png");
    berkas.buatBerkasPadaJS();
      
    return berkas;
  }
  
  public static boolean pindahkanFile(WebViewUI ui, Berkas berkas, Berkas tujuan)
          throws IOException {
    
    boolean sukses = false;
    
    if(berkas.getObjekFile().isFile()) {
      buatPanelOpPadaJS(ui, "", "pemindahan",
              berkas.getObjekFile().getAbsolutePath(),
              tujuan.getObjekFile().getAbsolutePath());
      
      Path op = Files.move(berkas.getObjekFile().toPath(),
              berkas.getObjekFile().toPath());

      if(op != null) {
        sukses = true;
      }
    }
    
    return sukses;
  }
  
  public static void buatPanelOpPadaJS(WebViewUI ui,
          String var, String op, String pathBerkas, String pathTujuan) {
    
    String js = ""+
    "var "+var+" = new PanelOperasiBerkas('"+op+"')"+
    ".setPathAktif('"+pathBerkas+"')"+
    ".setPathTujuan('"+pathTujuan+"');"+
    ".pasangElemen($('#rowOperasiBerkas'));";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void ubahPathAktif(WebViewUI ui, String var, String path) {
    String js = ""+
    var+".ubahPathAktif('"+path+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public void hapusBerkasPadaJS(WebViewUI ui) {
    String js = ""+
    "Berkas.hapusBerkasBerdasarNama('"+objekFile.getName()+"')";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void ubahTersembunyiPadaJS(WebViewUI ui, boolean sembunyikan) {
    String js = ""+
    "Berkas.dapatkanBerkasTerpilih().ubahTersembunyi("+sembunyikan+");";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void ubahPathAbsolutPadaJS(WebViewUI ui, String path) {
    String js = ""+
    "Berkas.dapatkanBerkasTerpilih().ubahPathAbsolut('"+path+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void ubahNamaPadaJS(WebViewUI ui, String nama) {
    String js = ""+
    "Berkas.dapatkanBerkasTerpilih().ubahNama('"+nama+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public void hapusSemuaBerkasPadaJS() {
    ui.eksekusiJavascript("Berkas.hapusSemuaBerkas();");
  }
  
  public static void tampilkanCirclePadaJS(WebViewUI ui) {
    String js = ""+
    "$('#konten').hide();"+
    "$('#loadingCircle').show();";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void sembunyikanCirclePadaJS(WebViewUI ui) {
    String js = ""+
    "$('#konten').show();"+
    "$('#loadingCircle').hide();";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void tandaiBerkasPadaJS(String namaBerkas, WebViewUI ui) {
    String js = "" +
    "Berkas.hilangkanSemuaTanda();"+
    "Berkas.tandai('"+namaBerkas+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void setTextPathPadaJS(WebViewUI ui, String path) {
    String js = ""+
    "$('#txPath').val('"+path+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void scrollKeBawahPadaJS(WebViewUI ui) {
    String js = ""+
    "$('#konten').animate({ scrollTop: $(document).height() }, 500);";
    
    ui.eksekusiJavascript(js);
  }
}
