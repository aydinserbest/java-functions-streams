# Stream `collect()` Alıştırmaları — Requirement'lar

`collect()` elemanları bir `Collector` tarifine göre liste, küme, harita veya
özet metin gibi sonuçlarda biriktiren terminal operation'dır.

## 1. Aktif ürünleri değiştirilebilir listeye toplama

`Product(String name, boolean active)` listesinden aktif ürünleri
`Collectors.toCollection(ArrayList::new)` ile topla ve sonuca yeni ürün
eklenebildiğini göster.

### Business açısından burada ne yapılıyor?

Katalog editörü filtrelenmiş çalışma listesini sonradan elle genişletecektir. Bu
yüzden yalnızca sonuçları toplamak değil, özellikle değiştirilebilir bir
`ArrayList` istemek business ihtiyacıdır.

## 2. Kampanya şehirlerini benzersiz kümeye alma

Müşterilerin şehirlerini `Collectors.toSet()` ile topla. Aynı şehirden birçok
müşteri olsa da sonuçta şehir bir kez bulunmalıdır.

### Business açısından burada ne yapılıyor?

Pazarlama raporu müşteri sayısını değil kampanyanın ulaştığı farklı şehirleri
gösterir. Tekrar eden şehirler bu rapora yeni bilgi katmaz.

## 3. Çalışanları departmana göre gruplama

`Employee(String name, String department)` listesini
`Collectors.groupingBy(Employee::department)` ile `Map<String,List<Employee>>`
sonucuna dönüştür.

### Business açısından burada ne yapılıyor?

İnsan kaynakları ekranı çalışanları departman başlıkları altında gösterir. Her
departman anahtar, o departmandaki çalışanlar değer listesi olur.

## 4. Siparişleri duruma göre sayma

`Order(long id, String status)` listesini durum başına adet verecek şekilde
`groupingBy` ve `counting` ile `Map<String,Long>` içine topla.

### Business açısından burada ne yapılıyor?

Operasyon paneli siparişlerin tek tek detayından önce `NEW: 12`, `SHIPPED: 40`
gibi özet kutuları gösterir. Gruplama ve alt collector birlikte çalışır.

## 5. Ürünleri SKU ile haritalama

`Product(String sku, String name)` listesini `Map<String,Product>` yap.
`Collectors.toMap()` kullan ve tekrar eden SKU bulunmadığını varsay.

### Business açısından burada ne yapılıyor?

Sipariş servisi ürünü listeyi baştan sona aramak yerine SKU anahtarıyla doğrudan
bulmak ister. Collect işlemi liste görünümünü lookup haritasına dönüştürür.

## 6. Tekrarlı kullanıcı adlarında birleştirme kuralı

`User(String username, LocalDateTime lastLogin)` listesini kullanıcı adına göre
haritaya topla. Aynı kullanıcı adı iki kez gelirse daha yeni giriş tarihli kaydı
tutan merge function yaz.

### Business açısından burada ne yapılıyor?

Birden fazla kaynaktan gelen kullanıcı verileri çakışabilir. Sistem sessizce
hata vermek yerine açık bir business kuralıyla en güncel kaydı korur.

## 7. Fatura numaralarını okunabilir metinde birleştirme

Fatura numaralarını `Collectors.joining(", ", "[", "]")` ile `[INV-1001,
INV-1002]` biçiminde birleştir.

### Business açısından burada ne yapılıyor?

Rapor başlığı veya e-posta, ayrı String kayıtları yerine kullanıcıya okunabilir
tek bir referans listesi ister. Prefix, delimiter ve suffix çıktı formatını
belirler.

## 8. Öğrencileri geçti/kaldı olarak bölme

`Student(String name, int grade)` listesini notu en az 60 olanlar ve olmayanlar
şeklinde `Collectors.partitioningBy()` ile böl.

### Business açısından burada ne yapılıyor?

Sınav servisi aynı öğrenci grubundan iki rapor üretir. Sonuç haritasındaki
`true` geçenleri, `false` kalanları temsil eder.

## 9. Departman başına toplam maaş

`Employee(String name, String department, double salary)` listesini departmana
göre grupla ve `Collectors.summingDouble(Employee::salary)` ile her departmanın
toplam maaşını hesapla.

### Business açısından burada ne yapılıyor?

Bütçe ekranı çalışan listesinden departman maliyetlerine geçer. Alt collector,
her grup içindeki maaşları tek parasal değerde toplar.

## 10. Siparişleri sıralı ve benzersiz ID kümesine toplama

Sipariş ID'lerini `Collectors.toCollection(TreeSet::new)` ile topla. Girdide
tekrar eden ve karışık sırada ID'ler bulunsun.

### Business açısından burada ne yapılıyor?

Denetim raporu aynı sipariş numarasını tekrar göstermemeli ve numaraları doğal
sırada sunmalıdır. İstenen sonuç yapısı açıkça `TreeSet` olarak seçilir.
