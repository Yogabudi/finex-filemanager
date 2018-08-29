
/////////////////////////////////////////////
//
// variabel global
//

var kontenTabTerpilih = "#berkas";

$(document).ready(function() {
  // inisialisasi komponen
  
  M.updateTextFields();
  $(".tooltipped").tooltip();
  $(".sidenav").sidenav();
  $(".tabs").tabs();
  $(".modal").modal();
  $("select").formSelect();
  $(".datepicker").datepicker();
  $(".collapsible").collapsible();
  $(".tap-target").tapTarget();

  $("#tabEdit").hide();
  $("#tabTab").hide();
  $("#loadingCircle").hide();
  $(".modal").hide();
  
  ///////////////////////////////////////////////
  //
  // atur tab ribbon
  
  $("#tabBerkas").click(function(e) {
    kontenTabTerpilih = $(this).children().attr("href");
  });
  
  $("#tabCari").click(function(e) {
    kontenTabTerpilih = $(this).children().attr("href");
  });
  
  $("#tabTab").click(function(e) {
    kontenTabTerpilih = $(this).children().attr("href");
  });
  
  $("#tabPengaturan").click(function(e) {
    kontenTabTerpilih = $(this).children().attr("href");
  });
  
  $("#tabAbout").click(function(e) {
    kontenTabTerpilih = $(this).children().attr("href");
  });
  
  ///////////////////////////////////////////////
  //
  // atur modal & sidenav
  
  jalankanTabYBY();
  
  $(".modal").modal({
    onCloseEnd: function() {
      resetPanelInfo();
      resetPanelPencarian();
      $(".modal").hide();
    }
  });
  
  $("#panelBookmark").sidenav(
    {
      onCloseEnd: function() {
        $("#ribbon").tabs("select", "berkas");
        $("#ribbon").tabs("updateTabIndicator");
      }
  });
  
  $("#menuDokumen").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeDokumen");
  });
  
  $("#menuFoto").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeFoto");
  });
  
  $("#menuMusik").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeMusik");
  });
  
  $("#menuVideo").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeVideo");
  });
  
  $("#menuUnduhan").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeUnduhan");
  });
  
  $("#menuKomputerku").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeRoot");
  });
  
  $("#menuHome").click(function(e) {
    $("#panelBookmark").sidenav("close");
    sendNSCommand("loncatKeHome");
  });
  
  $("#panelWajah").sidenav(
    {
      edge: "right",
      onCloseEnd: function() {
        console.log("tertutup");
      }
  });
  
  $("input[name='swGunakanApp']").prop("checked", false);
  
  $("input[name='swGunakanApp']").click(function() {
    $("input[name='swGunakanApp']").prop("checked", false);
    
    $(this).prop("checked", true);
  });
  
  $("#btnOkEmptyTrash").click(function(e) {
    sendNSCommand("kosongkanTrash");
  });
  
  $("#btnOkHapusBerkasPermanen").click(function(e) {    
    var berkasTerpilih = Berkas.dapatkanBanyakBerkasTerpilih();

    // simpan parameter fungsi sendNSCommand() pada array
    // index pertama adalah param pertama bernilai nama perintah
    // masukkan path absolut ke dalam parameter fungsi
    var paramFungsi = [];
    paramFungsi[0] = "hapusBerkasPermanen";
    for(var i = 0; i < berkasTerpilih.length; i++) {
      paramFungsi[i + 1] = berkasTerpilih[i].getPathAbsolut();
    }

    var fungsiKirimPerintah = window["sendNSCommand"];
    if(typeof fungsiKirimPerintah === "function") {
      fungsiKirimPerintah.apply(null, paramFungsi);
    }
  });
  
  /////////////////////////////////////////////////////
  //
  // atur ribbon 
  
  $("#txCariBerkas").keypress(function(e) {
    if(e.which === 13) {
      e.preventDefault();
      
      sendNSCommand("cariBerkas", this.value);
    }
  });
  
  $("#txCariBerkas").on("input", function(e) {
    sendNSCommand("cariBerkas", this.value);
  });
  
  $(".tooltipped").click(function() {
    $(".tooltipped").tooltip("destroy").tooltip();
  });

  $("#btnSalin").click(function () {
    M.toast({ html: "Berkas Tersalin!" });
  });
  
  $("#btnCut").click(function () {
    M.toast({ html: "Cut!" });
    sendNSCommand("cutBerkas", Berkas.dapatkanBerkasTerpilih().getPathAbsolut());
  });
  
  $("#btnTempel").click(function() {
    $("#btnOperasiBerkas").addClass("pulse");
    sendNSCommand("tempelBerkas");
  });
  
  $("#btnRefresh").click(function () {
    M.toast({ html: "Refreshed!" });
  });
  
  $("#cbTampilkanTersembunyi").click(function(e) {
    if($(this).prop("checked") === true) {
      Berkas.tampilkanBerkasTersembunyi();
    }
    else{
      Berkas.janganTampilkanBerkasTersembunyi();
    }
  });
  
  $("#btnBukaTerminal").click(function(e) {
    sendNSCommand("bukaTerminal");
  });
  
  $("#btnViewBerkas").click(function(e) {
    var berkasTerpilih = Berkas.dapatkanBanyakBerkasTerpilih();

    // simpan parameter fungsi sendNSCommand() pada array
    // index pertama adalah param pertama bernilai nama perintah
    // masukkan path absolut ke dalam parameter fungsi
    var paramFungsi = [];
    paramFungsi[0] = "unhideBerkas";
    for(var i = 0; i < berkasTerpilih.length; i++) {
      paramFungsi[i + 1] = berkasTerpilih[i].getPathAbsolut();
    }

    var fungsiKirimPerintah = window["sendNSCommand"];
    if(typeof fungsiKirimPerintah === "function") {
      fungsiKirimPerintah.apply(null, paramFungsi);
    }
  });
  
  $("#btnHideBerkas").click(function(e) {
    var berkasTerpilih = Berkas.dapatkanBanyakBerkasTerpilih();

    // simpan parameter fungsi sendNSCommand() pada array
    // index pertama adalah param pertama bernilai nama perintah
    // masukkan path absolut ke dalam parameter fungsi
    var paramFungsi = [];
    paramFungsi[0] = "hideBerkas";
    for(var i = 0; i < berkasTerpilih.length; i++) {
      paramFungsi[i + 1] = berkasTerpilih[i].getPathAbsolut();
    }

    var fungsiKirimPerintah = window["sendNSCommand"];
    if(typeof fungsiKirimPerintah === "function") {
      fungsiKirimPerintah.apply(null, paramFungsi);
    }
  });
  
  $("#btnDuplikat").click(function(e) {
    sendNSCommand("duplikatBerkas", Berkas.dapatkanBerkasTerpilih().getPathAbsolut());
  });
  
  $("#btnHapusKeTrash").click(function(e) {
    var berkasTerpilih = Berkas.dapatkanBanyakBerkasTerpilih();

    // simpan parameter fungsi sendNSCommand() pada array
    // index pertama adalah param pertama bernilai nama perintah
    // masukkan path absolut ke dalam parameter fungsi
    var paramFungsi = [];
    paramFungsi[0] = "hapusKeTrash";
    for(var i = 0; i < berkasTerpilih.length; i++) {
      paramFungsi[i + 1] = berkasTerpilih[i].getPathAbsolut();
    }

    var fungsiKirimPerintah = window["sendNSCommand"];
    if(typeof fungsiKirimPerintah === "function") {
      fungsiKirimPerintah.apply(null, paramFungsi);
    }
  });
  
  $("#btnInfoBerkas").click(function(e) {
    sendNSCommand("tampilkanInfoBerkas",
                    Berkas.dapatkanBerkasTerpilih().getPathAbsolut());
  });
  
  ///////////////////////////////////////////////////////////
  //
  // atur panel pencarian
  
  $("#tanggalDibuat").click(function() {
    $("#tanggalModif").toggle("slow");
    $("#tanggalAkses").toggle("slow");
    $("#berdasarNama").toggle("slow");
    $("#berdasarFormat").toggle("slow");

    $("#txtglDibuat").val("");
  });

  $("#tanggalModif").click(function() {
    $("#tanggalDibuat").toggle("slow");
    $("#tanggalAkses").toggle("slow");
    $("#berdasarNama").toggle("slow");
    $("#berdasarFormat").toggle("slow");

    $("#txtglModif").val("");
  });

  $("#tanggalAkses").click(function() {
    $("#tanggalDibuat").toggle("slow");
    $("#tanggalModif").toggle("slow");
    $("#berdasarNama").toggle("slow");
    $("#berdasarFormat").toggle("slow");

    $("#txtglAkses").val("");
  });

  $("#berdasarNama").click(function() {
    $("#tanggalDibuat").toggle("slow");
    $("#tanggalModif").toggle("slow");
    $("#tanggalAkses").toggle("slow");
    $("#berdasarFormat").toggle("slow");

    $("#txCariNama").val("");
  });

  $("#berdasarFormat").click(function() {
    $("#tanggalDibuat").toggle("slow");
    $("#tanggalModif").toggle("slow");
    $("#tanggalAkses").toggle("slow");
    $("#berdasarNama").toggle("slow");
  });

  $("#btnCloseTglDibuat").click(function() {
    $("#tanggalDibuat").click();
  });

  $("#btnCloseTglAkses").click(function() {
    $("#tanggalAkses").click();
  });

  $("#btnCloseTglModif").click(function() {
    $("#tanggalModif").click();
  });

  $("#btnCloseBerdasarNama").click(function() {
    $("#berdasarNama").click();
  });

  $("#btnCloseBerdasarFormat").click(function() {
    $("#berdasarFormat").click();
  });
  
  /////////////////////////////////////////////////
  //
  // atur popover
  
  $("#btnTglDibuat").webuiPopover({
    title: "Cari Berdasarkan Tanggal Dibuat",
    width: 300,
    animation: "pop",
    url: "#popTglDibuat",
    closeable: true,
    placement: "bottom",
    trigger: "manual"
  });
  
  $("#btnTglModif").webuiPopover({
    title: "Cari Berdasarkan Tanggal Diubah",
    width: 300,
    animation: "pop",
    url: "#popTglModif",
    closeable: true,
    placement: "bottom",
    trigger: "manual"
  });
  
  $("#btnTglAkses").webuiPopover({
    title: "Cari Berdasarkan Tanggal Akses",
    width: 300,
    animation: "pop",
    url: "#popTglAkses",
    closeable: true,
    placement: "bottom",
    trigger: "manual"
  });
  
  $("#btnOperasiBerkas").webuiPopover({
    title: "Operasi Berkas",
    animation: "pop",
    url: "#popOperasiBerkas",
    width: "500",
    height: "380",
    placement: "bottom-left",
    closeable: true,
    onShow: function(element) {
      $("#btnOperasiBerkas").removeClass("pulse");
    }
  });
    
  $("#btnEditPath").webuiPopover({
    title: "Path Berkas (Tekan Enter untuk menuju path)",
    animation: "pop",
    url: "#popPath",
    width: "500",
    placement: "bottom-right",
    onShow: function(element) {
      $("#txPath").select();
    }
  });
  
  $("#btnUbahNama").webuiPopover({
    title: "Ubah Nama Berkas",
    animation: "pop",
    url: "#popUbahNama",
    closeable: true,
    width: "300",
    placement: "bottom-right",
    onShow: function(element) {
      var namaBerkas = Berkas.dapatkanBerkasTerpilih().getNama();
      $("#txNamaBaru").val(namaBerkas);
      $("#txNamaBaru").select();
    }
  });
  
  $("#btnBuatFolder").webuiPopover({
    title: "Buat Folder Baru",
    animation: "pop",
    url: "#popBuatFolderBaru",
    closeable: true,
    width: "300",
    placement: "bottom-right",
    onShow: function(element) {
      $("#txNamaFolder").select();
    }
  });
  
  $("#btnBuatFile").webuiPopover({
    title: "Buat File Baru",
    animation: "pop",
    url: "#popBuatFileBaru",
    closeable: true,
    width: "300",
    placement: "bottom-right",
    onShow: function(element) {
      $("#txNamaFile").select();
    }
  });
  
  $("#btnLokasiGambarKustom").webuiPopover({
    title: "Folder Gambar Lain",
    animation: "pop",
    url: "#popFolderGambarLain",
    closeable: true,
    width: "400",
    height: "350",
    placement: "bottom-right",
    onHide: function(element) {
      tutupAccordion("#accordFolderGambarLain");
    }
  });
  
  $("#btnAturPintasan").webuiPopover({
    title: "Manajemen Pintasan",
    animation: "pop",
    url: "#popAturPintasan",
    closeable: true,
    width: "400",
    height: "350",
    placement: "bottom-right",
    onHide: function(element) {
      tutupAccordion("#accordPintasan");
    }
  });
  
  $("#btnOkBuatFolder").click(function(e) {
    var namaFolder = $("#txNamaFolder").val();
    var tersembunyi = $("#swHideFolderBaru").prop("checked");
    
    sendNSCommand("buatFolderBaru", namaFolder, tersembunyi);
    $("#btnBuatFolder").webuiPopover("hide");
    $("#txNamaFolder").val("");
  });
  
  $("#btnOkBuatFile").click(function(e) {
    var namaFile = $("#txNamaFile").val();
    var tersembunyi = $("#cbSembunyikanFileBaru").prop("checked");
    
    sendNSCommand("buatFileBaru", namaFile, tersembunyi);
    $("#btnBuatFile").webuiPopover("hide");
    $("#txNamaFile").val("");
  });
  
  $("#btnOkUbahNama").click(function(e) {
    $("#btnUbahNama").webuiPopover("hide");
    
    sendNSCommand("ubahNama", Berkas.dapatkanBerkasTerpilih().getPathAbsolut(),
                  $("#txNamaBaru").val());
  });
  
  $("#txPath").keypress(function(e) {
    if(e.which === 13) {
      var path = $("#txPath").val();
      sendNSCommand("keyEnterDitekan", "#txPath", path);
    }
    
    e.stopPropagation();
  });
  
  $("#txNamaBaru").keypress(function(e) {
    if(e.which === 13) {
      $("#btnOkUbahNama").click();
    }
    
    e.stopPropagation();
  });
  
  $("#txNamaFolder").keypress(function(e) {
    if(e.which === 13) {
      $("#btnOkBuatFolder").click();
    }
    
    e.stopPropagation();
  });
  
  $("#txNamaFile").keypress(function(e) {
    if(e.which === 13) {
      $("#btnOkBuatFile").click();
    }
    
    e.stopPropagation();
  });
  
//  $(document).scroll(function() {
//    $("#btnOperasiBerkas").webuiPopover("hide");
//    $("#btnBuatFolder").webuiPopover("hide");
//    $("#btnEditPath").webuiPopover("hide");
//    $("#btnUbahNama").webuiPopover("hide");
//    $("#btnBuatFile").webuiPopover("hide");
//    $("#btnLokasiGambarKustom").webuiPopover("hide");
//    $("#btnAturPintasan").webuiPopover("hide");
//    $("#btnTglDibuat").webuiPopover("hide");
//  });
  
  $("#pesanTidakAdaFolderLain").hide();
  $("#pesanTidakAdaOp").show();
  $("#pesanTidakAdaPintasan").hide();
  
  $("#btnTglDibuat").click(function(e) {
    e.stopPropagation();
    $(this).webuiPopover("show");
  });
  
  $("#btnTglModif").click(function(e) {
    e.stopPropagation();
    $(this).webuiPopover("show");
  });
  
  $("#btnTglAkses").click(function(e) {
    e.stopPropagation();
    $(this).webuiPopover("show");
  });
  
  $("#konten").click(function(e) {
    $("#btnTglAkses").webuiPopover("hide");
    $("#btnTglModif").webuiPopover("hide");
    $("#btnTglDibuat").webuiPopover("hide");
  });
  
  $("#txTglDibuat").datepicker({
    container: $("html"),
    format: "dd/mm/yyyy",
    i18n: {
      cancel: "batalkan",
      done: "cari berkas"
    },
    onClose: function() {
      $("#btnTglDibuat").webuiPopover("hide");
    }
  });
  
  $("#txTglModif").datepicker({
    container: $("html"),
    format: "dd/mm/yyyy",
    i18n: {
      cancel: "batalkan",
      done: "cari berkas"
    },
    onClose: function() {
      $("#btnTglModif").webuiPopover("hide");
    }
  });
  
  $("#txTglAkses").datepicker({
    container: $("html"),
    format: "dd/mm/yyyy",
    i18n: {
      cancel: "batalkan",
      done: "cari berkas"
    },
    onClose: function() {
      $("#btnTglAkses").webuiPopover("hide");
    }
  });
  
  $("#txTglDibuat").change(function(e) {
    sendNSCommand("cariBerkasBerdasarTglDibuat", this.value);
  });
  
  ////////////////////////////////////////////////////////////
  //
  // atur contextmenu
  
  $(document).bind("contextmenu", function(e) {
    return false;
  });
  
  var dataTempatBerkas = [
    new ObjekMenu("Buat Folder Baru", "assets/Icons/24/new-add-folder.png", "menu_buatFolderBaru()").buatMenu(),
    new ObjekMenu("Buat File Baru", "assets/Icons/24/new-document.png", "menu_buatFileBaru()").buatMenu(),
    new ObjekMenu("Paste", "assets/Icons/24/clipboard-paste-button.png", "menu_paste()").buatMenu(),
    new ObjekMenu("Buka Terminal Disini", "assets/Icons/24/icon.png", "menu_bukaTerminal()").buatMenu(),
    new ObjekMenu("Kosongkan Trash", "assets/Icons/24/garbage.png", "menu_kosongkanTrash()").buatMenu(),
    new ObjekMenu("Cari Berkas", "assets/Icons/24/loupe.png", "menu_cariBerkas()").buatMenu(),
    new ObjekMenu("Koleksi Wajah", "assets/Icons/24/smile.png", "menu_koleksiWajah()").buatMenu(),
    new ObjekMenu("Lihat Operasi Berkas", "assets/Icons/24/operation.png", "menu_lihatOpBerkas()").buatMenu()
  ];

  var dataMenuBc = [
    new ObjekMenu("Maju", "assets/Icons/24/right-arrow.png", "").buatMenu(),
    new ObjekMenu("Mundur", "assets/Icons/24/left-arrow.png", "").buatMenu()
  ];

  var menuBc = new ContextMenu().tambahkanSemuaMenu(dataMenuBc).pasang("breadcrumbBerkas");
  var menuTempatBerkas = new ContextMenu().tambahkanSemuaMenu(dataTempatBerkas).pasang("konten");
  
  //////////////////////////////////////////////////////////
  //
  // atur ukuran konten
  //
  
  $("#konten").height($(window).height() - 190);
  $("#konten").width($(window).width() - 7);
  
  $(window).resize(function() {
    $("#konten").height($(window).height() - 190);
    $("#konten").width($(window).width() - 7);
  });
  
  //////////////////////////////////////////////////////////
  //
  // atur tombol nav depan dan belakang
  //
  
  $("#btnKebelakang").click(function () {
    sendNSCommand("tampilkanBerkasSebelumnya");
  });
  
  $("#btnKedepan").click(function() {
    sendNSCommand("tampilkanBerkasKedepan");
  });
  
  //////////////////////////////////////////////////////////
  //
  // testing buat folder
    
//  for(var i = 1; i <= 10; i++) {
//    if(i === 4) {
//      var berkas = new Berkas();
//      berkas.setNama("Ini folder yang sangat panjang panjang panjang sekali");
//      berkas.setPathAbsolut("/home/" + berkas.getNama());
//      berkas.setJenis("folder");
//      berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);
//      berkas.pasangElemen($(".tempatBerkas"));
//    }
//    else {
//      var berkas = new Berkas();
//      berkas.setNama("Ini Folder " + i);
//      berkas.setPathAbsolut("/home/" + berkas.getNama());
//      berkas.setJenis("folder");
//      berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);
//      berkas.pasangElemen($(".tempatBerkas"));
//    }
//  }
  

  for(var i = 1; i <= 3; i++) {
    var berkas = new Berkas();
    berkas.setNama("File " + i);
    berkas.setPathAbsolut("/home/" + berkas.getNama());
    berkas.setJenis("file");
    berkas.setIcon("assets/Icons/64/053-document-7.png");
    berkas.setTersembunyi(false);
    berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);
    berkas.pasangElemen($(".tempatBerkas"));
  }
  
  var berkas = new Berkas();
  berkas.setNama("File Thumbnail");
  berkas.setPathAbsolut("/home/ini_laptop/Pictures/batu.jpg");
  berkas.setJenis("file");
  berkas.setIcon("assets/Icons/64/053-document-7.png");
  berkas.setTersembunyi(false);
  berkas.setPakeThumbnail(true);
  berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);
  berkas.pasangElemen($(".tempatBerkas"));
//  
//  var opSalin = new PanelOperasiBerkas("penyalinan")
//          .setPathAktif("/i/a/a")
//          .setPathTujuan("/a/w/d")
//          .pasangElemen($("#rowOperasiBerkas"));
//  
//  var opSalin2 = new PanelOperasiBerkas("penyalinan")
//          .setPathAktif("/c/s/b")
//          .setPathTujuan("/s/f/w")
//          .pasangElemen($("#rowOperasiBerkas"));
});

//var param = ["tesArray", "hello", "world", "!!!!"];
//var fungsi = window["sendNSCommand"];
//if(typeof fungsi === "function") {
//  fungsi.apply(null, param);
//}

////////////////////////////////////////////
//
// handler untuk context menu
//

function menu_buatFolderBaru() {
  setTimeout(function () {
    $("#btnBuatFolder").webuiPopover("show");
  }, 150);
}

function menu_buatFileBaru() {
  setTimeout(function () {
    $("#btnBuatFile").webuiPopover("show");
  }, 150);
}

function menu_paste() {
  
}

function menu_bukaTerminal() {
  setTimeout(function() {
    sendNSCommand("bukaTerminal");
    
  }, 150);
}

function menu_kosongkanTrash() {
  setTimeout(function() {
    $("#panelConfirmEmptyTrash").modal("open");
    
  }, 150);
}

function menu_cariBerkas() {
  setTimeout(function() {
    $("#panelPencarianBerkas").modal("open");
    
  }, 150);
}

function menu_koleksiWajah() {
  setTimeout(function() {
    $("#panelWajah").sidenav("open");
    
  }, 150);
}

function menu_lihatOpBerkas() {
  setTimeout(function() {
    $("#btnOperasiBerkas").webuiPopover("show");
    
  }, 150);
}

function menu_infoBerkas() {
  setTimeout(function() {
    $("#btnInfoBerkas").click();
    
  }, 150);
}

function menu_hapusKeTrash() {
  setTimeout(function() {
    $("#btnHapusKeTrash").click();
    
  }, 150);
}

function menu_hapusPermanen() {
  setTimeout(function() {
    $("#btnHapus").click();
    
  }, 150);
}

function menu_duplikat() {
  setTimeout(function() {
    $("#btnDuplikat").click();
    
  }, 150);
}