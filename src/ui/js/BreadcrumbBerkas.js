
class BreadcrumbBerkas extends Berkas {
  
  constructor(nama) {
    super("folder", nama);
  }
  
  tandai() {
    var id = this.nama.split(" ").join("");
    $("#bc_" + id + " .yby-breadcrumb").addClass("yby-breadcrumb-aktif");
  }
  
  hilangkanTanda() {
    var id = this.nama.split(" ").join("");
    $("#bc_" + id + " .yby-breadcrumb").removeClass("yby-breadcrumb-aktif");
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
  
  pasangElemen(elemenTempat) {
    var id = this.nama.split(" ").join("");
    
    var bc =
      "<div id='bc_"+id+"' "+
        "class='col breadFolder' path-absolut='"+this.pathAbsolut+"'>"+
        "<div class='card hoverable yby-breadcrumb'>"+
          "<div class='yby-breadcrumb-elemen center-align'>"+
            this.nama +
          "</div>"+
        "</div>"+
      "</div>";
      
    elemenTempat.append(bc);
  }
};

// testing
$(document).ready(function() {
  var bc = new BreadcrumbBerkas("ini bukan folder");
  bc.pathAbsolut = "/home/folder" + bc.nama;
  bc.pasangElemen($("#panelBreadcrumb"));
  
  var bc2 = new BreadcrumbBerkas("ini bc 2");
  bc2.pathAbsolut = "/home/folder" + bc2.nama;
  bc2.pasangElemen($("#panelBreadcrumb"));
  
  bc2.tandai();
});