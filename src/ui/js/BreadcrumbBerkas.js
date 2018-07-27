
class BreadcrumbBerkas extends Berkas {
  
  constructor(nama) {
    super();
    this.setNama(nama);
    this.setJenis("folder");
    
    this.tandai = function() {
      var id = this.getNama().split(" ").join("");
      $("#bc_" + id + " .yby-breadcrumb").addClass("yby-breadcrumb-aktif");
    };
    
    this.hilangkanTanda = function() {
      var id = this.getNama().split(" ").join("");
      $("#bc_" + id + " .yby-breadcrumb").removeClass("yby-breadcrumb-aktif");
    };
    
    this.pasangElemen = function(elemenTempat) {
      var id = this.getNama().split(" ").join("");
    
      var bc =
        "<div id='bc_"+id+"' "+
          "class='col breadFolder' path-absolut='"+this.getPathAbsolut()+"'>"+
          "<div class='card hoverable yby-breadcrumb'>"+
            "<div class='yby-breadcrumb-elemen center-align'>"+
              this.getNama() +
            "</div>"+
          "</div>"+
        "</div>";

      elemenTempat.append(bc);
    };
  }
  
  static warnaiBreadcrumb() {
    $(".yby-breadcrumb").addClass("grey");
    $(".yby-breadcrumb").addClass("lighten-2");
  }

  static jalankanBreadcrumb() {
    BreadcrumbBerkas.warnaiBreadcrumb();

    $(".yby-breadcrumb-aktif").removeClass("lighten-2");
    $(".yby-breadcrumb-aktif").addClass("white");

    $(".yby-breadcrumb").click(function() {
      $(".yby-breadcrumb").removeClass("white");
      $(".yby-breadcrumb").addClass("lighten-2");

      $(this).removeClass("lighten-2");
      $(this).addClass("white");
    });
  }
};

// testing
$(document).ready(function() {
  var bc = new BreadcrumbBerkas("ini bukan folder");
  bc.setPathAbsolut("/home/folder" + bc.getNama());
  bc.pasangElemen($("#panelBreadcrumb"));
  
  var bc2 = new BreadcrumbBerkas("ini bc 2");
  bc2.setPathAbsolut("/home/folder" + bc2.getNama());
  bc2.pasangElemen($("#panelBreadcrumb"));
  
  bc2.tandai();
});