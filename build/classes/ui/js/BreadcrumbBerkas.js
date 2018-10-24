
class BreadcrumbBerkas extends Berkas {
  
  constructor(nama, pathAbsolut) {
    super();
    this.setNama(nama);
    this.setPathAbsolut(pathAbsolut);
    this.setJenis("folder");
    
    this.tandai = function() {
      $("div[id-bc='"+this.getNama()+"'] .yby-breadcrumb")
              .addClass("yby-breadcrumb-aktif");
      $(".yby-breadcrumb-aktif").removeClass("grey");
      $(".yby-breadcrumb-aktif").removeClass("lighten-2");
    };
    
    this.hilangkanTanda = function() {
      $(".yby-breadcrumb-aktif").addClass("grey");
      $(".yby-breadcrumb-aktif").addClass("lighten-2");
      $("div[id-bc='"+this.getNama()+"'] .yby-breadcrumb")
              .removeClass("yby-breadcrumb-aktif");
    };
    
    this.eventSaatKlik = function() {
      var bc = BreadcrumbBerkas.dapatkanBreadcrumbTerpilih();
      var pathAbsolut = bc.getPathAbsolut();
      var labelBc = bc.getNama();
      
      sendNSCommand("tampilkanListBerkasDariBreadcrumb", pathAbsolut, labelBc);
    };
    
    this.pasangElemen = function(elemenTempat) {    
      var bc =
        "<div id-bc='"+this.getNama()+"' "+
          "class='col breadFolder' path-absolut='"+this.getPathAbsolut()+"'>"+
          "<div class='card hoverable yby-breadcrumb grey lighten-2'>"+
            "<div class='yby-breadcrumb-elemen center-align'>"+
              this.getNama() +
            "</div>"+
          "</div>"+
        "</div>";

      elemenTempat.append(bc);
      
      $(".yby-breadcrumb").off("click");
      $(".yby-breadcrumb").on("click", function() {
        $(".yby-breadcrumb").removeClass("yby-breadcrumb-aktif");
        $(".yby-breadcrumb").addClass("grey");
        $(".yby-breadcrumb").addClass("lighten-2");

        $(this).removeClass("grey");
        $(this).removeClass("lighten-2");
        $(this).addClass("yby-breadcrumb-aktif");
      });
      
      $("div[id-bc='"+this.getNama()+"']").on("click", this.eventSaatKlik);
    };
  }
  
  static tandai(label) {
    $("div[id-bc='"+label+"'] .yby-breadcrumb").addClass("yby-breadcrumb-aktif");
    $(".yby-breadcrumb-aktif").removeClass("grey");
    $(".yby-breadcrumb-aktif").removeClass("lighten-2");
  }
  
  static hilangkanTanda(label) {
    $(".yby-breadcrumb-aktif").addClass("grey");
    $(".yby-breadcrumb-aktif").addClass("lighten-2");
    $("div[id-bc='"+label+"'] .yby-breadcrumb").removeClass("yby-breadcrumb-aktif");
  }
  
  static dapatkanBreadcrumbTerpilih() {
    var label = $.trim($(".yby-breadcrumb-aktif div").text());
    var path = $(".yby-breadcrumb-aktif").parent().attr("path-absolut");
    
    return new BreadcrumbBerkas(label, path);
  }
  
  static hapusSemua() {
    if($(".breadFolder").length) {
      $(".breadFolder").remove();
    }
  }
};

// testing
$(document).ready(function() {
//  var bc = new BreadcrumbBerkas("ini bukan folder");
//  bc.setPathAbsolut("/home/folder" + bc.getNama());
//  bc.pasangElemen($("#panelBreadcrumb"));
//  
//  var bc2 = new BreadcrumbBerkas("ini bc 2");
//  bc2.setPathAbsolut("/home/folder" + bc2.getNama());
//  bc2.pasangElemen($("#panelBreadcrumb"));
//  
//  bc2.tandai();
});