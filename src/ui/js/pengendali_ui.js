
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
  $("#loadingCircle").hide();
  $(".modal").hide();
  
  //////////////////////////////////////////////////////
  //
  // atur seleksi berkas
  
  
  
  ///////////////////////////////////////////////
  //
  // atur modal & sidenav
  
  jalankanTabYBY();
  BreadcrumbBerkas.jalankanBreadcrumb();
  
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
  
  /////////////////////////////////////////////////////
  //
  // atur tombol ribbon 
  
  $(".tooltipped").click(function() {
    $(".tooltipped").tooltip("destroy").tooltip();
  });

  $("#btnSalin").click(function () {
    M.toast({ html: "Berkas Tersalin!" });
  });
  
  $("#btnCut").click(function () {
    M.toast({ html: "Cut!" });
  });
  
  $("#btnRefresh").click(function () {
    M.toast({ html: "Refreshed!" });
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
  
  $("#btnOperasiBerkas").webuiPopover({
    title: "Operasi Berkas",
    animation: "pop",
    url: "#popOperasiBerkas",
    width: "500",
    height: "380",
    onShow: function(element) {
      $("#btnOperasiBerkas").removeClass("pulse");
    }
  });
    
  $("#btnEditPath").webuiPopover({
    title: "Path Berkas (Tekan Enter untuk menuju path)",
    animation: "pop",
    url: "#popPath",
    width: "500",
  });
  
  $("#btnUbahNama").webuiPopover({
    title: "Ubah Nama Berkas",
    animation: "pop",
    url: "#popUbahNama",
    closeable: true,
    width: "300",
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
    width: "300"
  });
  
  $("#btnBuatFile").webuiPopover({
    title: "Buat File Baru",
    animation: "pop",
    url: "#popBuatFileBaru",
    closeable: true,
    width: "300"
  });
  
  $("#btnLokasiGambarKustom").webuiPopover({
    title: "Folder Gambar Lain",
    animation: "pop",
    url: "#popFolderGambarLain",
    closeable: true,
    width: "400",
    height: "350",
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
    onHide: function(element) {
      tutupAccordion("#accordPintasan");
    }
  });
  
  $("#btnOkUbahNama").click(function () {
    $("#btnUbahNama").webuiPopover("hide");
  });
  
  $(document).scroll(function() {
    $("#btnOperasiBerkas").webuiPopover("hide");
    $("#btnBuatFolder").webuiPopover("hide");
    $("#btnEditPath").webuiPopover("hide");
    $("#btnUbahNama").webuiPopover("hide");
    $("#btnBuatFile").webuiPopover("hide");
    $("#btnLokasiGambarKustom").webuiPopover("hide");
    $("#btnAturPintasan").webuiPopover("hide");
  });
  
  $("#pesanTidakAdaFolderLain").hide();
  $("#pesanTidakAdaOp").hide();
  $("#pesanTidakAdaPintasan").hide();
  
  ////////////////////////////////////////////////////////////
  //
  // atur contextmenu
  
  $(document).bind("contextmenu", function(e) {
    return false;
  });
  
  var menuFolder = [
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/scissors.png' />\n\
        <span>Cut</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/papers.png' />\n\
        <span>Salin</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/clipboard-paste-button.png' />\n\
        <span>Paste kedalam folder</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/clipboard-paste-button.png' />\n\
        <span>Paste disini</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/info.png' />\n\
        <span>Info Berkas</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/duplicate-file.png' />\n\
        <span>Duplikat</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/garbage.png' />\n\
        <span>Hapus ke trash</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/delete.png' />\n\
        <span>Hapus</span>\n\
      </div>", ""]
  ];
  
  var menuTempatBerkas = [
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/new-add-folder.png' />\n\
        <span>Buat Folder Baru</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/new-document.png' />\n\
        <span>Buat File Baru</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/clipboard-paste-button.png' />\n\
        <span>Paste</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/icon.png' />\n\
        <span>Buka Terminal Disini</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/garbage.png' />\n\
        <span>Kosongkan Trash</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/loupe.png' />\n\
        <span>Cari Berkas</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/smile.png' />\n\
        <span>Koleksi Wajah</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/operation.png' />\n\
        <span>Lihat Operasi Berkas</span>\n\
      </div>", ""]
  ];
  
  var menuBreadcrumb = [
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/right-arrow.png' />\n\
        <span>Maju</span>\n\
      </div>", ""],
    ["<div class='icon-contextmenu'>\n\
        <img src='assets/Icons/24/left-arrow.png' />\n\
        <span>Mundur</span>\n\
      </div>", ""]
  ];
  
  class2context("berkas", "", menuFolder);
  class2context("tempatBerkas", "", menuTempatBerkas);
  class2context("breadcrumbBerkas", "", menuBreadcrumb);
  
  //////////////////////////////////////////////////////////
  
});