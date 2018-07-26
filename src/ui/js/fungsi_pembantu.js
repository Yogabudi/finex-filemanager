
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