
class Jembatan {
  
  constructor() {
  }
  
  static tampilkanBerkas(folder) {
    sendNSCommand("tampilkanBerkas", folder);
  }
  
  static kirimInfoBerkas(berkas) {
    sendNSCommand("kirimInfoBerkas", berkas.ubahKeFormatParam());
  }
}