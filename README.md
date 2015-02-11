# GoogleMapProject
Araçlarınızın çeşitli mesafelerde ne kadar yakıt tüketeceğini  bilmek isteyenlerin kullanabileceği bir programdır. Kullanıcıların sadece gitmek istediği konumları uygulamadan seçmesi yeterlidir. Ayrıca program bu verilerinizi kaydedebilir ve bu verilere daha sonra bakabilirsiniz. Diğer bir özelliği  Konum takip özelliğini seçtiğinizde ve kayıt etmiş olduğunuz verilere  girdiğinizde sizlerin bulunduğunuz konumu gösterir ve hedef noktasına olan uzaklığı ve varış sürenizi tahmini olarak sizlere bildirmektedir.
 Kısaca özetiyle : 
     *Seçilen konumlar arası mesafe                                                                         
     *Tahmini varış süresi                                                                                                                                                        
     *Aracınızın Km başına yakıt tutarını hesaba katarak toplam harcanacak yakıt fiyatı          
     *Seçmiş olduğunuz koordinat kayıt edilebilmesi 
    *Seçilen koordinatların en kısa yol güzergahı 
    *Konum takip özelliği seçili olduğunda Navigasyon özelliği

                Kullanılan Teknolojiler
    *Sqlite "Kullanıcıların Konum ve gerekli hesaplama bilgilerini kaydetmek için "                                            
    *Google Map "Kullanıcıların Seçtikleri konumları belirleme ve Bulundukları konumları takip etmek için"                      
    *RestFull Client  "Google Map'in sunmuş olduğu RestFull Servisten veri cekebilmek için kullanılmiştır"                      
    *Android AsyncTask  "Program çalıştığı süreçte arka planda Server ve Client iletişimini sağlamak ve Google Map'te gerekli
    görünüm işlemlerini yapılabilmesi için kullanılmıştır. " 
