
function warnaiBreadcrumb() {
  $(".yby-breadcrumb").addClass("grey");
  $(".yby-breadcrumb").addClass("lighten-2");
}

function jalankanBreadcrumb() {
  warnaiBreadcrumb();
  
  $(".yby-breadcrumb-aktif").removeClass("lighten-2");
  $(".yby-breadcrumb-aktif").addClass("white");
  
  $(".yby-breadcrumb").click(function() {
    $(".yby-breadcrumb").removeClass("white");
    $(".yby-breadcrumb").addClass("lighten-2");
    
    $(this).removeClass("lighten-2");
    $(this).addClass("white");
  });
}

function warnaiTabYBY() {
  $(".yby-tab").addClass("grey");
  $(".yby-tab").addClass("lighten-2");
  
  $(".yby-konten-tab").addClass("grey");
  $(".yby-konten-tab").addClass("lighten-4");
}

function bukaTabYBY(elemenTab, idKontenTab) {
  $(".yby-container-konten-tab").hide();
  $(".yby-tab").removeClass("lighten-4");
  $(".yby-tab").addClass("lighten-2");

  $("#" + idKontenTab).show();
  $(elemenTab).removeClass("lighten-2");
  $(elemenTab).addClass("lighten-4");
}

function jalankanTabYBY() {
  warnaiTabYBY();
  
  var targetKonten = $(".yby-tab-aktif").attr("data-target");
  bukaTabYBY(".yby-tab-aktif", targetKonten);
  
  $(".yby-tab").click(function() {
    var idKontenTarget = $(this).attr("data-target");
    bukaTabYBY(this, idKontenTarget);
  });
}

function tutupAccordion(selector) {
  var jumlahAnak = $(selector).children().length;
  for(var i = 0; i < jumlahAnak; i++) {
    $(selector).collapsible("close", i);
  }
}

function resetPanelInfo() {
  var targetKonten = $(".yby-tab-aktif").attr("data-target");
  bukaTabYBY(".yby-tab-aktif", targetKonten);
  
  tutupAccordion("#accordApp");
}

function resetPanelWajah() {
  tutupAccordion("#accordWajah");
}

function resetPanelPencarian() {
  tutupAccordion("#accordBerdasarkan")
  
  $("#berdasarFormat").show();
  $("#berdasarNama").show();
  $("#tanggalAkses").show();
  $("#tanggalDibuat").show();
  $("#tanggalModif").show();
}

function dapatkanNamaBerkas(elemenBerkas) {
  var containerKontenButton = $(elemenBerkas).children()[0];
  var containerLabel = $(containerKontenButton).children()[1];
  var label = $.trim($(containerLabel).text());
  
  return label;
}

function dapatkanBerkasTerpilih() {
  var elemen = $(".tempatBerkas").find(".ds-selected");
  
  return elemen;
}

function tandaiBerkas(elemenBerkas) {
  $(elemenBerkas).addClass("ds-selected");
}

function hilangkanTandaBerkas() {
  $(".tempatBerkas").children().removeClass("ds-selected");
}

function buatTombolBerkas(id, namaBerkas, icon, jq_elemenTempatnya) {
  var htmlButton = "<button id='btnFile_" + id + "' " +
                    "class='button button-3d button-box button-jumbo berkas'>" +
                    "<span class='row'>" +
                    "<span class='center col s12'>" +
                    "<img src='" + icon + "'/>" +
                    "</span>" +
                    "<span class='center col s12'>" +
                    namaBerkas +
                    "</span>"
                    "</span>"
                    "</button>";
                    
  $(jq_elemenTempatnya).append(htmlButton);
}

// method ini untuk tab file system
function buatTabBaru(id, labelTab) {
  $("#kumpulanTabFileSystem li").children().removeClass("active");
  
  var htmlTab = "<li id='tabFile_" + id + "' class='tab'>" +
              "<a href='#konten_" + id + "' class='active' " +
               "style='text-transform: none;'>" +
               labelTab +
               "</a>" +
               "</li>";
       
  $("#kumpulanTabFileSystem").append(htmlTab);
}

function buatKontenKosong(idTab) {
  var htmlDiv = "<div id='konten_" + idTab + "' class='tempatBerkas'></div>";
  $("#panelFile").append(htmlDiv);
}

function pilihTab(idTab) {
//  var instanceTab = M.Tabs.getInstance(
//          document.getElementById("kumpulanTabFileSystem"));
//  instanceTab.select("tabFile_" + idTab);
//  instanceTab.updateTabIndicator();

  var id = "konten_" + idTab;

  $("#kumpulanTabFileSystem").tabs("select", id);
  $("#kumpulanTabFileSystem").tabs("updateTabIndicator");
}