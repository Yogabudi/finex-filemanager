
class PanelOperasiBerkas {
  
  constructor(idPanel, jenisOp) {
    var jenisOperasi = jenisOp;
    var pathAktif = "";
    var pathTujuan = "";
    
    var id = "op_" + idPanel;
    
    /////////////////////////////////////////////
    //
    // getter dan setter
    //
    
    this.setJenisOperasi = function(jenisOp) {
      jenisOperasi = jenisOp;
      
      return this;
    };
    
    this.getJenisOperasi = function() {
      return jenisOperasi;
    };
    
    this.setPathAktif = function(p) {
      pathAktif = p;
      
      return this;
    };
    
    this.getPathAktif = function() {
      return pathAktif;
    };
    
    this.setPathTujuan = function(p) {
      pathTujuan = p;
      
      return this;
    };
    
    this.getPathTujuan = function() {
      return pathTujuan;
    };
    
    /////////////////////////////////////////////
    
    this.ubahPathAktif = function(p) {
      pathAktif = p;
      
      $("div[id-operasi='"+id+"']")
                .find(".berkas-terpilih")
                .text(pathAktif);
        
      return this;
    };
    
    this.ubahPathTujuan = function(p) {
      pathTujuan = p;
      
      $("div[id-operasi='"+id+"']")
                .find(".berkas-terpilih")
                .text(pathAktif);
        
      return this;
    };
    
    this.pasangElemen = function(elemenTempat) {
      $("#pesanTidakAdaOp").hide();
      
      if(jenisOperasi === "penyalinan") {
        var elemen = ""+
        "<div id-operasi='"+id+"' jenis-op='penyalinan' class='col s12 panel-op'>"+
          "<div class='card'>"+
            "<div class='card-content'>"+
              "<div class='row'>"+
                "<div class='col s12'>"+
                  "<span class='card-title'>Menyalin berkas...</span>"+
                "</div>"+
                "<div class='col s12 center-align'>"+
                  "<img class='iconSalin_File1'"+
                       "src='assets/Icons/64/053-document-7.png'/>"+
                  "<img src='assets/Icons/64/053-document-7.png'"+
                       "class='iconSalin_File2 animated infinite slideOutRight' />"+
                  "<img class='iconSalin_TempatBerkas'"+
                       "src='assets/Icons/64/194-document-1.png' />"+
                "</div>"+
              "</div>"+
            "</div>"+
            "<div class='card-action'>"+
              "<b>Berkas : </b>"+
                "<span class='berkas-terpilih'>"+
                pathAktif+
                "</span>"+
            "</div>"+
            "<div class='card-action'>"+
              "<b>Lokasi Tujuan : </b>"+
              "<span class='berkas-tujuan'>"+pathTujuan+"</span>"+
            "</div>"+
          "</div>"+
        "</div>";
        
        elemenTempat.append(elemen);
      }
      else if(jenisOperasi === "pemindahan") {
        var elemen = ""+
        "<div id-operasi='"+id+"' jenis-op='pemindahan' class='col s12 panel-op'>"+
          "<div class='card'>"+
            "<div class='card-content'>"+
              "<div class='row'>"+
                "<div class='col s12'>"+
                  "<span class='card-title'>Memindahkan berkas...</span>"+
                "</div>"+
                "<div class='col s12 right-align'>"+
                  "<img class='iconPindahkan_TempatBerkas'"+
                       "src='assets/Icons/64/194-document-1.png' />"+
                  "<img class='iconPindahkan_File animated infinite bounceInLeft'"+
                       "src='assets/Icons/64/053-document-7.png'/>"+
                "</div>"+
              "</div>"+
            "</div>"+
            "<div class='card-action'>"+
              "<b>Berkas : </b>"+
              "<span class='berkas-terpilih'>"+
              pathAktif+
              "</span>"+
            "</div>"+
            "<div class='card-action'>"+
              "<b>Lokasi Tujuan : </b>"+
              "<span class='berkas-tujuan'>"+pathTujuan+"</span>"+
            "</div>"+
          "</div>"+
        "</div>";

        elemenTempat.append(elemen);
      }
      
      return this;
    };
  }
  
  static dapatkanPanel(indexId) {
    var jenisOp = $("div[id-operasi='op_"+indexId+"']").attr("jenis-op");
    var pathAktif = $.trim($("div[id-operasi='op_"+indexId+"']")
                            .find(".berkas-terpilih")
                            .text());
    var pathTujuan = $.trim($("div[id-operasi='op_"+indexId+"']")
                            .find(".berkas-tujuan")
                            .text());
                    
    var panel = new PanelOperasiBerkas(jenisOp);
    panel.setPathAktif(pathAktif);
    panel.setPathTujuan(pathTujuan);
    
    return panel;
  }
  
  static hapusPanel(indexId) {
    if($("div[id-operasi='op_"+indexId+"']").length) {
      $("div[id-operasi='op_"+indexId+"']").remove();
    }
    
    $("#pesanTidakAdaOp").show();
  }
}