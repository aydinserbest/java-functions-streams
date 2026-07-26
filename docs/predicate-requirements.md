# Predicate Alıştırmaları — Requirement'lar

Bu alıştırmaların amacı `Predicate<T>`, `test`, `Stream.filter`, `and`, `or` ve
`negate` kullanımlarını gerçek hayata benzeyen küçük senaryolarla çalışmaktır.

## 1. Kullanıcı adı doğrulama

Bir kullanıcı adının kurallara uygun olup olmadığını kontrol eden bir
`Predicate<String>` tanımla.

Kurallar:

- `null` olmamalı.
- Boş veya yalnızca boşluklardan oluşmamalı.
- En az 5 karakter içermeli.

Şunlarla test et:

```text
"john"   -> false
"mehmet" -> true
""       -> false
null     -> false
```

Buradaki "geçerli", kullanıcı adının listede bulunması değil, biçimsel kurallara
uygun olması anlamındadır.

### Business açısından burada ne yapılıyor?

Bir kayıt ekranından gelen kullanıcı adı sisteme kaydedilmeden önce biçimsel iş
kurallarından geçiriliyor. Predicate, kullanıcı adını değiştirmez veya
kaydetmez; yalnızca kayıt akışının devam edip edemeyeceğine ilişkin `true` ya da
`false` kararı üretir.

Gerçek bir kayıt ekranı düşün:

```text
Kullanıcı adı: [ mehmet ]
               [ Kayıt Ol ]
```

Kullanıcı butona bastığında değer doğrudan kaydedilmez. Sistemin koyduğu
kurallar şunlardır:

- Kullanıcı adı girilmiş olmalı.
- Yalnızca boşluklardan oluşmamalı.
- En az 5 karakter olmalı.

Predicate `true` verirse biçim kontrolü geçilir; `false` verirse kayıt akışı
durdurulup kullanıcıya hata gösterilebilir.

## 2. Farklı sayı koşullarını aynı metotta çalıştırma

Şu yapıda genel bir metot oluştur:

```java
boolean checkNumber(int number, Predicate<Integer> condition)
```

Aynı metoda farklı Predicate'ler göndererek bir sayının:

- Pozitif olup olmadığını
- Çift olup olmadığını
- 10 ile 100 arasında olup olmadığını
- 3'e tam bölünüp bölünmediğini

kontrol et. `checkNumber` metodu koşulun ayrıntısını bilmemeli; yalnızca
dışarıdan gelen Predicate'i çalıştırmalı.

Gerçek bir sipariş ekranında aynı adet için farklı sorular sorulabilir:

- Adet pozitif mi? Negatif veya sıfır sipariş engellensin.
- Adet çift mi? İkili paketleme kuralına uyuyor mu?
- 10–100 arasında mı? Sipariş sınırlarına uyuyor mu?
- 3'e tam bölünüyor mu? Üçlü kolilerde artan ürün kalıyor mu?

`checkNumber()` bu kuralları sabitlemez; gereken Predicate metoda gönderilir.

### Business açısından burada ne yapılıyor?

Ödeme tutarı, sipariş adedi veya paket büyüklüğü gibi bir sayıya farklı iş
kuralları uygulanabiliyor. Genel kontrol mekanizması değişmeden pozitiflik,
çiftlik, izin verilen aralık veya paket sayısına tam bölünebilme gibi karar
davranışları Predicate olarak dışarıdan veriliyor.

## 3. Aktif kullanıcıları filtreleme

Şu modeli oluştur:

```java
User(String username, boolean active)
```

En az beş kullanıcıdan oluşan bir liste hazırla. Yalnızca aktif kullanıcıları
döndüren bir metot geliştir:

```java
List<User> getActiveUsers(List<User> users)
```

Filtreleme işleminde `Predicate<User>` ve `stream().filter(...)` kullan.

### Business açısından burada ne yapılıyor?

Sistemde kaydı bulunan herkes aktif işlemlere katılamayabilir. Bildirim
gönderme, giriş izni veya görev atama gibi süreçler öncesinde yalnızca aktif
kullanıcılar seçiliyor. Predicate her kullanıcı için "aktif mi?" kararı verir;
`filter()` ise bu kararı bütün liste üzerinde çalıştırır.

Örneğin yönetim panelinde "Aktif kullanıcılara bildirim gönder" işlemi olsun.
Sistemin kuralları şunlardır:

- `active=true` olan hesaplar sonuç listesinde tutulmalı.
- `active=false` olan hesaplar bildirime dahil edilmemeli.
- Pasif hesap silinmemeli; yalnızca bu iş akışının dışında bırakılmalı.

## 4. Stokta bulunan pahalı ürünler

Şu modeli oluştur:

```java
Product(String name, double price, int stock)
```

Ayrı Predicate'ler tanımla:

- Fiyatı 100 eurodan yüksek ürünler
- Stok miktarı sıfırdan büyük ürünler

İki koşulu `and()` ile birleştirerek hem pahalı hem de stokta bulunan ürünleri
filtrele.

Örnek veri ve beklenen sonuç:

```text
Laptop:   1200 euro, stok 5   -> sonuçta olmalı
Mouse:      40 euro, stok 20  -> sonuçta olmamalı
Monitor:   300 euro, stok 0   -> sonuçta olmamalı
Keyboard:  150 euro, stok 3   -> sonuçta olmalı
```

### Business açısından burada ne yapılıyor?

Bir mağaza yüksek fiyatlı ve gerçekten satılabilir ürünleri ayrı bir raporda
veya vitrinde göstermek istiyor. Fiyat ve stok iki bağımsız iş kuralıdır; ürünün
sonuçta kalması için her ikisini de karşılaması gerektiğinden Predicate'ler
`and()` ile birleştirilir.

Bir mağaza yöneticisi "Stokta bulunan ve fiyatı 100 euroyu aşan ürünleri getir"
diyebilir. Sistemin iki ayrı kuralı vardır:

- Ürün pahalı kabul edilmek için `price > 100` koşulunu sağlamalı.
- Satılabilir olmak için `stock > 0` koşulunu sağlamalı.

Monitor pahalı olsa da stokta olmadığı, Mouse stokta olsa da fiyat sınırını
geçmediği için sonuçtan çıkarılır.

## 5. Siparişleri duruma göre dinamik filtreleme

Şu modeli oluştur:

```java
Order(long id, String status, double totalAmount)
```

Durumlar `NEW`, `PAID`, `SHIPPED` ve `CANCELLED` olabilir. Sipariş listesiyle
birlikte dışarıdan koşul alan genel bir metot oluştur:

```java
List<Order> filterOrders(List<Order> orders, Predicate<Order> condition)
```

Bu tek metodu kullanarak:

- Ödenmiş siparişleri
- İptal edilmiş siparişleri
- Tutarı 500 eurodan yüksek siparişleri
- Kargolanmış ve tutarı 100 eurodan yüksek siparişleri

bul.

Gerçek bir sipariş ekranında kullanıcı durum ve tutar filtresi seçebilir. Finans
ekibi `PAID`, destek ekibi `CANCELLED`, operasyon ekibi ise hem `SHIPPED` hem de
tutarı 100 eurodan yüksek siparişleri isteyebilir. Aynı `filterOrders()` metodu
korunur; ekrandaki seçime göre ona gönderilen Predicate değişir.

### Business açısından burada ne yapılıyor?

Operasyon, finans ve müşteri hizmetleri aynı sipariş havuzuna farklı sorular
sorar: "Hangileri ödendi? ", "Hangileri iptal edildi? " veya "Hangi yüksek
tutarlı siparişler kargolandı? " Genel arama metodu sabit kalır; departmanın
ihtiyacı olan seçim kuralı Predicate olarak gönderilir.

## 6. E-posta adresi doğrulama

Bir `Predicate<String>` kullanarak basit bir e-posta kontrolü oluştur.

Kurallar:

- Değer `null` olmamalı.
- Boş olmamalı.
- `@` karakteri içermeli.
- `@` karakterinden sonra en az bir `.` bulunmalı.
- Boşluk içermemeli.

Şunlarla test et:

```text
john@example.com
johnexample.com
john@example
john @example.com
null
```

Amaç eksiksiz bir internet standardı uygulamak değil, birden fazla koşulu bir
Predicate içinde veya küçük Predicate'leri birleştirerek kullanmaktır.

Gerçek bir üyelik formunda kullanıcı "Hesap Oluştur" dediğinde e-posta doğrudan
kabul edilmez. Sistemin temel kuralları şunlardır:

- Alan boş veya `null` olmamalı.
- Adreste boşluk bulunmamalı.
- `@` işareti bulunmalı.
- `@` sonrasındaki bölümde bir nokta bulunmalı.

Predicate `false` verirse kullanıcıdan alanı düzeltmesi istenir. Bu kontrol
adresin gerçekten var olduğunu değil, yalnızca temel biçimini sınar.

### Business açısından burada ne yapılıyor?

Kayıt veya iletişim formundan gelen e-posta, sisteme kabul edilmeden önce temel
biçim kurallarından geçiriliyor. Predicate e-posta göndermiyor ve adresi
düzeltmiyor; yalnızca değerin sonraki işleme alınabilecek görünümde olup
olmadığına karar veriyor.

## 7. İş başvurusuna uygun adayları bulma

Şu modeli oluştur:

```java
Candidate(
    String name,
    int yearsOfExperience,
    boolean knowsJava,
    boolean availableForFullTime
)
```

Ayrı Predicate'ler oluştur:

- En az 2 yıl deneyimli
- Java bilen
- Tam zamanlı çalışmaya uygun

Üç koşulu `and()` kullanarak birleştir ve uygun adayları filtrele. Ayrıca Java
bilmeyen adayları, Java bilme Predicate'inin `negate()` metodunu kullanarak ayrı
bir listede göster.

Gerçek bir Java Developer ilanında sistem şu şartları uygulayabilir:

- Adayın en az 2 yıl deneyimi olmalı.
- Aday Java bilmeli.
- Aday tam zamanlı çalışabilmeli.

Üç Predicate de `true` olmadan aday uygun listesine girmez. `negate()` ise aynı
Java bilgisi kuralını tekrar yazmadan Java bilmeyen adayları bulur.

### Business açısından burada ne yapılıyor?

İnsan kaynakları, açık pozisyonun zorunlu kriterlerini aday listesine uygular.
Deneyim, Java bilgisi ve çalışma uygunluğu ayrı değerlendirme kurallarıdır;
uygun adayın üçünden de geçmesi gerekir. `negate()` ise mevcut "Java biliyor"
kuralını tekrar yazmadan ters aday grubunu bulmayı sağlar.

## 8. Müşteri arama filtresi

Şu modeli oluştur:

```java
Customer(String name, String country, int age, boolean premium)
```

Genel bir arama metodu oluştur:

```java
List<Customer> search(
    List<Customer> customers,
    Predicate<Customer> criteria
)
```

Metodu değiştirmeden şu aramaları yap:

- Hollanda'da yaşayan müşteriler
- 18 yaşından büyük müşteriler
- Premium müşteriler
- Hollanda'da yaşayan ve premium müşteriler
- Premium veya 50 yaşından büyük müşteriler

Son iki aramada `and()` ve `or()` kullan.

Gerçek bir CRM ekranında ülke, yaş ve Premium üyelik alanları bulunabilir.
Kullanıcı tek bir alanı veya birkaç alanı birlikte seçer. `and()` bütün seçili
şartların sağlanmasını, `or()` alternatif segmentlerden en az birine girilmesini
ifade eder. Seçimler değişir, genel `search()` metodu değişmez.

### Business açısından burada ne yapılıyor?

CRM ekranındaki ülke, yaş ve üyelik türü gibi arama alanları farklı müşteri
segmentleri oluşturur. Arama metodu her kombinasyon için yeniden yazılmaz;
kullanıcının seçtiği kriterler Predicate olarak birleştirilir ve aynı müşteri
listesine uygulanır.

## 9. Süresi geçmiş ve ödenmemiş faturalar

Şu modeli oluştur:

```java
Invoice(long id, LocalDate dueDate, boolean paid, double amount)
```

Ayrı `isOverdue` ve `isUnpaid` Predicate'leri oluştur. Son ödeme tarihi bugünden
önce olan ve henüz ödenmemiş faturaları `and()` ile bul.

Ek olarak süresi geçmiş, ödenmemiş ve tutarı 1000 eurodan yüksek faturaları ayrı
bir listede göster. Tarih kontrolünde `LocalDate.now()` kullanabilirsin.

Gerçek bir muhasebe ekranında "Ödeme hatırlatması gereken faturaları göster"
işlemi olsun. Sistemin kuralları şunlardır:

- Son ödeme tarihi bugünden önce olmalı.
- Fatura halen ödenmemiş olmalı.
- Öncelikli liste için tutar ayrıca 1000 euroyu aşmalı.

Ödenmiş eski bir fatura veya vadesi gelmemiş ödenmemiş bir fatura gecikmiş ödeme
listesine alınmaz.

### Business açısından burada ne yapılıyor?

Finans ekibi ödeme hatırlatması veya tahsilat süreci başlatılacak faturaları
belirler. Bir faturanın yalnızca eski tarihli olması yeterli değildir; halen
ödenmemiş olması da gerekir. Yüksek tutar Predicate'i eklenerek öncelikli
tahsilat listesi ayrıca üretilebilir.

## 10. Dinamik ürün arama sistemi

Şu modeli oluştur:

```java
Product(
    String name,
    String category,
    double price,
    double rating,
    boolean inStock
)
```

Genel filtreleme metodu oluştur:

```java
List<Product> filterProducts(
    List<Product> products,
    Predicate<Product> criteria
)
```

Aşağıdaki Predicate'leri ayrı ayrı tanımla:

- Belirli bir kategoriye ait ürün
- Belirlenen minimum fiyattan pahalı veya bu fiyata eşit ürün
- Belirlenen maksimum fiyattan ucuz veya bu fiyata eşit ürün
- Belirlenen minimum puana sahip ürün
- Stokta bulunan ürün

Kategorisi `Electronics`, fiyatı 100–1000 euro arasında, puanı en az 4 ve stokta
olan ürünleri küçük Predicate'leri `and()` ile birleştirerek getir.

Ek çalışma: Arama alanlarının opsiyonel olduğunu varsay. Örneğin kategori veya
maksimum fiyat verilmemişse o koşul uygulanmasın; yalnızca girilen filtreler
birleştirilsin.

Gerçek bir e-ticaret filtre panelinde kategori, alt/üst fiyat, minimum puan ve
"stokta" seçeneği bulunur. Sistemin kuralları şunlardır:

- Kategori seçildiyse ürün o kategoriye ait olmalı.
- Fiyat sınırları girildiyse ürün aralıkta kalmalı.
- Minimum puan seçildiyse rating sınırı geçilmeli.
- Stok seçeneği işaretlendiyse yalnızca mevcut ürünler kalmalı.

Yalnızca doldurulan alanların Predicate'leri birleştirilir; boş filtreler
ürünleri gereksiz yere elemez.

### Business açısından burada ne yapılıyor?

E-ticaret kullanıcısı kategori, fiyat aralığı, puan ve stok durumunu seçerek
ürün arar. Her seçim bağımsız bir Predicate oluşturur; yalnızca doldurulan
alanlar birleştirilir. Böylece her filtre kombinasyonu için ayrı metot yazmadan
dinamik bir ürün arama davranışı elde edilir.
