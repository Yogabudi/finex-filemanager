
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
    placement: "bottom-left",
    onShow: function(element) {
      $("#btnOperasiBerkas").removeClass("pulse");
    }
  });
    
  $("#btnEditPath").webuiPopover({
    title: "Path Berkas (Tekan Enter untuk menuju path)",
    animation: "pop",
    url: "#popPath",
    width: "500",
    placement: "bottom-right"
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
    placement: "bottom-right"
  });
  
  $("#btnBuatFile").webuiPopover({
    title: "Buat File Baru",
    animation: "pop",
    url: "#popBuatFileBaru",
    closeable: true,
    width: "300",
    placement: "bottom-right"
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
  
  var dataTempatBerkas = [
    new ObjekMenu("Buat Folder Baru", "assets/Icons/24/new-add-folder.png", "").buatMenu(),
    new ObjekMenu("Buat File Baru", "assets/Icons/24/new-document.png", "").buatMenu(),
    new ObjekMenu("Paste", "assets/Icons/24/clipboard-paste-button.png", "").buatMenu(),
    new ObjekMenu("Buka Terminal Disini", "assets/Icons/24/icon.png", "").buatMenu(),
    new ObjekMenu("Kosongkan Trash", "assets/Icons/24/garbage.png", "").buatMenu(),
    new ObjekMenu("Cari Berkas", "assets/Icons/24/loupe.png", "").buatMenu(),
    new ObjekMenu("Koleksi Wajah", "assets/Icons/24/smile.png", "").buatMenu(),
    new ObjekMenu("Lihat Operasi Berkas", "assets/Icons/24/operation.png", "").buatMenu()
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
  
  var tinggiWindow = $(window).height();
  var lebarWindow = $(window).width();
  $("#konten").height(tinggiWindow - 190);
  $("#konten").width(lebarWindow - 7);
  
  $(window).resize(function() {
    var tinggiWindow = $(window).height();
    var lebarWindow = $(window).width();
    $("#konten").height(tinggiWindow - 190);
    $("#konten").width(lebarWindow - 7);
  });
    
  //////////////////////////////////////////////////////////
  //
  // kirim pesan ke Java
  
//  Jembatan.tampilkanRoot();
  
  for(var i = 1; i <= 5; i++) {
    var berkas = new Berkas();
    berkas.setNama("Ini Folder " + i);
    berkas.setPathAbsolut("/home/" + berkas.getNama());
    berkas.setJenis("folder");
    berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);
    berkas.pasangElemen($(".tempatBerkas"));
  }
  
  for(var i = 1; i <= 3; i++) {
    var berkas = new Berkas();
    berkas.setNama("File " + i);
    berkas.setPathAbsolut("/home/" + berkas.getNama());
    berkas.setJenis("file");
    berkas.setIcon("assets/Icons/64/053-document-7.png");
    berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);
    berkas.pasangElemen($(".tempatBerkas"));
  }
  
  Berkas.pasangEventSeleksi(Berkas.eventSaatTerpilihBanyak, Berkas.eventSaatTerpilihSatu,
                      Berkas.eventSaatTidakTerpilih);
});