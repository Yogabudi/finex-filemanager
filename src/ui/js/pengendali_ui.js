
function inisialisasiKomponen() {
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
  
  jalankanTabYBY();
  jalankanBreadcrumb();
  
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
        resetPanelWajah();
        console.log("tertutup");
      }
  });
    
  $("input[name='swGunakanApp']").prop("checked", false);
}

function aturSeleksiBerkas() {
  new DragSelect({
    area: document.getElementById("konten"),
    selectables: document.getElementsByClassName("berkas"),
    callback: function(elements) {
      if(elements.length > 1) {
        $("#tabEdit").show();
        $("#ribbon").tabs("select", "edit");
        $("#ribbon").tabs("updateTabIndicator");

        $("#btnUbahNama").hide();
        $("#btnDuplikat").hide();
        $("#btnInfoBerkas").hide();
      }
      else {
        if(!$(document.activeElement).hasClass("berkas")) {
          $("#ribbon").tabs("select", "berkas");
          $("#ribbon").tabs("updateTabIndicator");
          $("#tabEdit").hide();
        }
        else {
          $("#tabEdit").show();
          $("#ribbon").tabs("select", "edit");
          $("#ribbon").tabs("updateTabIndicator");

          $("#btnUbahNama").show();
          $("#btnDuplikat").show();
          $("#btnInfoBerkas").show();
        }
      }
    },
    
    onElementSelect: function(element) {
      element.focus();
    }
  });
  
//  $("#konten").click(function() {
//    hilangkanTandaBerkas();
//    
//    if($(document.activeElement).hasClass("berkas")) {
//      tandaiBerkas(document.activeElement);
//      
//      $("#tabEdit").show();
//      $("#ribbon").tabs("select", "edit");
//      $("#ribbon").tabs("updateTabIndicator");
//      
//      $("#btnUbahNama").show();
//      $("#btnDuplikat").show();
//      $("#btnInfoBerkas").show();
//    }
//    else {
//      $("#ribbon").tabs("select", "berkas");
//      $("#ribbon").tabs("updateTabIndicator");
//      $("#tabEdit").hide();
//    }
//  });
}

function aturContextMenu() {
  $(document).bind("contextmenu", function(e) {
    return false;
  });
  
  var menuFolder = [
    ["<i class='material-icons left'>&#xe14e;</i> Cut", ""],
    ["<i class='material-icons left'>&#xe14d;</i> Salin", ""],
    ["<i class='material-icons left'>&#xe14f;</i> Paste Kedalam Folder", ""],
    ["<i class='material-icons left'>&#xe14f;</i> Paste Disini", ""],
    ["<i class='material-icons left'>&#xe88e;</i> Info Berkas", ""],
    ["<i class='material-icons left'>&#xe3bb;</i> Duplikat", ""],
    ["<i class='material-icons left'>&#xe872;</i> Hapus Ke Trash", ""],
    ["<i class='material-icons left'>&#xe872;</i> Hapus", ""]
  ];
  
  var menuTempatBerkas = [
    ["<i class='material-icons left'>&#xe2cc;</i> Buat Folder Baru", ""],
    ["<i class='material-icons left'>&#xe24d;</i> Buat File Baru", ""],
    ["<i class='material-icons left'>&#xe14f;</i> Paste", ""],
    ["<i class='material-icons left'>&#xe86f;</i> Buka Terminal Disini", ""],
    ["<i class='material-icons left'>&#xe872;</i> Kosongkan Trash", ""],
    ["<i class='material-icons left'>&#xe8b6;</i> Cari Berkas", ""],
    ["<i class='material-icons left'>&#xe420;</i> Koleksi Wajah", ""],
    ["<i class='material-icons left'>&#xe8a4;</i> Lihat Operasi Berkas", ""]
  ];
  
  var menuBreadcrumb = [
    ["<i class='material-icons left'>&#xe5c8;</i> Maju", ""],
    ["<i class='material-icons left'>&#xe5c4;</i> Mundur", ""]
  ];
  
  class2context("berkas", "", menuFolder);
  class2context("tempatBerkas", "", menuTempatBerkas);
  class2context("breadcrumbBerkas", "", menuBreadcrumb);
}

function aturPopover() {
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
    title: "Path Berkas",
    animation: "pop",
    url: "#popPath",
    width: "700"
  });
  
  $("#btnUbahNama").webuiPopover({
    title: "Ubah Nama Berkas",
    animation: "pop",
    url: "#popUbahNama",
    closeable: true,
    width: "300",
    onShow: function(element) {
      var namaBerkas = dapatkanNamaBerkas(dapatkanBerkasTerpilih());
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
}

function aturPanelCari() {
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
}

$(document).ready(function() {
  inisialisasiKomponen();
  
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
  
  $("input[name='swGunakanApp']").click(function() {
    $("input[name='swGunakanApp']").prop("checked", false);
    
    $(this).prop("checked", true);
  });
  
  aturPopover()
  aturPanelCari();
  aturSeleksiBerkas();
  aturContextMenu();
});