
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
  private boolean pakeThumbnail = false;
  private ArrayList<Berkas> duplikatanBerkas = new ArrayList<>();
  
  private WebViewUI ui;
  private ExecutorService execService;
  
  // kriteria berdasarkan
  public static final String BERDASAR_TGL_DIBUAT = "berdasarkantgldibuat";
  public static final String BERDASAR_TGL_MODIFIKASI = "berdasarkantglmodif";
  public static final String BERDASAR_TGL_AKSES = "berdasarkantglakses";
  public static final String BERDASAR_NAMA = "berdasarkannama";
  
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

  public boolean isPakeThumbnail() {
    return pakeThumbnail;
  }

  public void setPakeThumbnail(boolean pakeThumbnail) {
    this.pakeThumbnail = pakeThumbnail;
  }

  public ExecutorService getExecService() {
    return execService;
  }

  public void setExecService(ExecutorService execService) {
    this.execService = execService;
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
        
        if(berkas.getObjekFile().getName().endsWith(".jpg") ||
           berkas.getObjekFile().getName().endsWith(".png") ||
           berkas.getObjekFile().getName().endsWith(".jpeg")) {
          
          berkas.setPakeThumbnail(true);
        }
      }
      
      daftarBerkas.add(berkas);
    }
    
    return daftarBerkas.toArray(new Berkas[0]);
  }
  
  public void tampilkanListBerkas() {
    Berkas[] daftarBerkas = listBerkas();

    Berkas.hapusSemuaBerkasPadaJS(ui);
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
  
  public static ArrayList<Berkas> cariBerkas(String kriteria,
          String teks, Berkas tempatCari) throws IOException {
    
    ArrayList<Berkas> dataHasil = new ArrayList<>();
    Berkas[] dataBerkas = tempatCari.listBerkas();
    
    if(kriteria.equals(Berkas.BERDASAR_NAMA)) {
      for(int i = 0; i < dataBerkas.length; i++) {
        String namaBerkas = dataBerkas[i].getObjekFile().getName();

        if(namaBerkas.toLowerCase().contains(teks.toLowerCase())) {
          dataHasil.add(dataBerkas[i]);
        }
      }
    }
    else if(kriteria.equals(Berkas.BERDASAR_TGL_DIBUAT)) {
      String tgl = teks;
      
      for(int i = 0; i < dataBerkas.length; i++) {
        BasicFileAttributes attr =
                Files.readAttributes(dataBerkas[i].getObjekFile().toPath(),
                        BasicFileAttributes.class);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String tglDibuat = df.format(attr.creationTime().toMillis());
        
        if(tglDibuat.contains(tgl)) {
          dataHasil.add(dataBerkas[i]);
        }
      }
    }
    
    return dataHasil;
  }
  
  public void tampilkanInfoBerkas() throws IOException {
    BasicFileAttributes atribut =
            Files.readAttributes(objekFile.toPath(), BasicFileAttributes.class);
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
    String namaBerkas = objekFile.getName();
    String jenisBerkas = (objekFile.isDirectory()) ? "Direktori" : "File";
    long ukuranBerkas = ukuranFile;
    int jmlKonten = (objekFile.isDirectory()) ? objekFile.listFiles().length : 0;
    String tglDibuat = df.format(atribut.creationTime().toMillis());
    String tglModif = df.format(atribut.lastModifiedTime().toMillis());
    String tglAkses = df.format(atribut.lastAccessTime().toMillis());
    
    String js = ""+
    "$('#txNamaBerkas').val('"+namaBerkas+"');"+
    "$('#jenisBerkas').text('"+jenisBerkas+"');"+
    "$('#ukuranBerkas').text('"+ukuranBerkas+" KB');"+
    "$('#jmlKonten').text('"+jmlKonten+"');"+
    "$('#tglDibuat').text('"+tglDibuat+"');"+
    "$('#tglModif').text('"+tglModif+"');"+
    "$('#tglAkses').text('"+tglAkses+"');";
    
    ui.eksekusiJavascript(js);
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
    
    File hasilRename = new File(pathNamaBaru);
    
    if(objekFile.renameTo(hasilRename)) {
      sukses = true;
      objekFile = hasilRename;
    }
    
    return sukses;
  }
  
  public void ubahNamaDanTampilkan(String namaBaru) {
    String pathNamaBaru = objekFile.getParent() + "/" + namaBaru;
    
    ubahPathAbsolutPadaJS(ui, pathNamaBaru);
    ubahNamaPadaJS(ui, namaBaru);
    
    ubahNama(pathNamaBaru);
  }
  
  public void hideBerkas() {
    if(!objekFile.getName().startsWith(".")) {
      String pathNamaBaru = objekFile.getParent() + "/." + objekFile.getName();
      String namaBaru = "." + objekFile.getName();

      ubahTersembunyiPadaJS(ui, true);
      ubahPathAbsolutPadaJS(ui, pathNamaBaru);
      ubahNamaPadaJS(ui, namaBaru);

      ubahNama(pathNamaBaru);
    }
  }
  
  public void unhideBerkas() {
    if(objekFile.getName().startsWith(".")) {
      String pathNamaBaru = objekFile.getParent() + "/" +
                            objekFile.getName().substring(1);
      String namaBaru = objekFile.getName().substring(1);

      ubahTersembunyiPadaJS(ui, false);
      ubahPathAbsolutPadaJS(ui, pathNamaBaru);
      ubahNamaPadaJS(ui, namaBaru);

      ubahNama(pathNamaBaru);
    }
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
      "berkas.setPakeThumbnail("+pakeThumbnail+");"+
      "berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);"+
      "berkas.pasangElemen($('.tempatBerkas'));";

    ui.eksekusiJavascript(js);
  }
  
  public void hapusBerkasPadaJS(WebViewUI ui) {
    String js = ""+
    "Berkas.hapusBerkasBerdasarNama('"+objekFile.getName()+"')";
    
    ui.eksekusiJavascript(js);
  }
  
  public void ubahTersembunyiPadaJS(WebViewUI ui, boolean sembunyikan) {
    String js = ""+
    "Berkas.dapatkanBerkasBerdasarNama('"+objekFile.getName()+"')"+
    ".ubahTersembunyi("+sembunyikan+");";
    
    ui.eksekusiJavascript(js);
  }
  
  public void ubahPathAbsolutPadaJS(WebViewUI ui, String path) {
    String js = ""+
    "Berkas.dapatkanBerkasBerdasarNama('"+objekFile.getName()+"')"+
    ".ubahPathAbsolut('"+path+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public void ubahNamaPadaJS(WebViewUI ui, String nama) {
    String js = ""+
    "Berkas.dapatkanBerkasBerdasarNama('"+objekFile.getName()+"')"+
    ".ubahNama('"+nama+"');";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void hapusSemuaBerkasPadaJS(WebViewUI ui) {
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
