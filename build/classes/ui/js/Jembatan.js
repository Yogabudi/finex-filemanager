
class Jembatan {
  
  constructor() {
  }
  
  static tampilkanRoot() {
    sendNSCommand("tampilkanRoot");
  }
  
  static tampilkanBerkas(folder) {
    sendNSCommand("tampilkanBerkas", folder);
  }
  
  static kirimInfoBerkas(berkas) {
    sendNSCommand("kirimInfoBerkas", berkas.ubahKeFormatParam());
  }
}