
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

function resetPanelPencarian() {
  tutupAccordion("#accordBerdasarkan")
  
  $("#berdasarFormat").show();
  $("#berdasarNama").show();
  $("#tanggalAkses").show();
  $("#tanggalDibuat").show();
  $("#tanggalModif").show();
}