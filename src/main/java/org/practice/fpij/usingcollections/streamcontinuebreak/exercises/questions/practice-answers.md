# Alıştırma Cevapları: Stream'de Erken Atlama / Erken Durdurma

`practice-questions.md` içindeki 10 sorunun çözüm yaklaşımı ve
her birinde neden o Stream fonksiyonunun seçildiği aşağıda anlatılıyor.
Örnek kod parçacıkları `skippingvalues` ve `terminatingIterations`
klasörlerindeki demo sınıflarla aynı mantığı kullanıyor.

Karar vermeden önce kendinize şu iki soruyu sorun:
1. Atlanacak/alınacak miktar **sabit bir sayı mı**, yoksa **bir şarta mı** bağlı?
2. İşlem listenin **başından** mı atlanıyor/alınıyor, yoksa **sona kadar mı** sürüyor?

Bu iki soru cevabı, dört fonksiyondan hangisine ihtiyacınız olduğunu
doğrudan belirler:

| | Sabit sayı | Şart |
|---|---|---|
| **Baştan atla** | `skip(n)` | `dropWhile(pred)` |
| **Baştan al, sonra dur** | `limit(n)` | `takeWhile(pred)` |

---

### 1. Sayfalama (Pagination)

**İhtiyaç:** 3. sayfa, sayfa başına 10 ürün → ilk 20 ürünü atla, sonraki
10 tanesini al.

**Çözüm:** `skip(20).limit(10)`

**Neden:** Atlanacak miktar (20) da alınacak miktar (10) da **sabit
sayılar**, herhangi bir şarta bağlı değil. Bu yüzden `dropWhile`/
`takeWhile` gereksiz — onlar veriye bakıp karar vermeyi gerektirir,
burada ise doğrudan "kaçıncı elemandan kaçıncı elemana kadar"
biliniyor. `skip` ve `limit` art arda kullanılınca klasik "offset +
page size" sayfalama mantığını verir.

```java
products.stream()
        .skip(20)
        .limit(10)
        .forEach(...);
```

---

### 2. Deneme Süresi Biten Abonelikler

**İhtiyaç:** Baştaki tutarı 0 olan (deneme süresi) kayıtları atla,
gerçek ücretlendirmenin başladığı ilk kayıttan itibaren hepsini al.

**Çözüm:** `dropWhile(invoice -> invoice.getAmount() == 0)`

**Neden:** Kaç tane deneme kaydı olduğu **önceden bilinmiyor** —
kullanıcıya göre değişir. Bu yüzden sabit sayı ile çalışan `skip(n)`
işe yaramaz; atlama kararı her elemanın **kendi değerine** bakılarak
verilmeli. `dropWhile` tam olarak bunu yapar: şart (`amount == 0`)
doğru olduğu sürece atar, yanlış olan ilk elemanı gördüğü an atmayı
bırakır ve kalan her şeyi (bir daha şartı kontrol etmeden) alır.

---

### 3. En Yüksek Harcama Yapan Müşteriler

**İhtiyaç:** Harcamaya göre büyükten küçüğe sıralı listeden sadece ilk
5 müşteri.

**Çözüm:** `limit(5)`

**Neden:** Liste zaten sıralı olduğu için "en yüksek 5" ihtiyacı,
akışta ilerlerken bir şartı kontrol etmeyi değil, sadece **sayıyı**
sınırlamayı gerektiriyor. `takeWhile` burada yanlış olurdu çünkü bir
şart öne sürmüyoruz ("harcama X'ten büyük olduğu sürece al" gibi bir
kural yok) — sadece "ilk 5 tanesini al" diyoruz. Sıralamanın zaten
doğru sırada geldiğini varsaymak `limit`'i doğru araç yapıyor.

---

### 4. Sıcaklık Sensöründe Anomali Öncesi Veriler

**İhtiyaç:** Ölçümleri, sıcaklık güvenli eşiği aşana kadar işle; aştığı
anda dur.

**Çözüm:** `takeWhile(reading -> reading.getTemperature() <= 4)`

**Neden:** Durma noktası bir **sayı değil, bir koşul** (eşiğin
aşılması) ve biz "koşul doğru olduğu sürece devam et, koşul bozulunca
hemen dur" istiyoruz — klasik `break` senaryosu. `limit` burada işe
yaramaz çünkü kaç ölçümden sonra eşiğin aşılacağını önceden
bilmiyoruz. `takeWhile`, şartı ilk sağlamayan elemanda akışı anında
sonlandırır (kapı sonsuza dek kapanır), tıpkı `break` gibi.

---

### 5. Başarısız Giriş Denemelerinde Hoşgörü Hakkı

**İhtiyaç:** İlk 2 başarısız denemeyi göz ardı et, 3. denemeden
itibaren şüpheli olay olarak logla.

**Çözüm:** `skip(2)`

**Neden:** Hoşgörü hakkı **sabit bir sayı** (2 deneme) ile tanımlı,
denemelerin içeriğine (örneğin hangi şifre girildiğine) bağlı bir şart
yok. `dropWhile` kullanmak yanlış olurdu çünkü onun için bir "doğru/
yanlış" şartı gerekir; burada koşul yok, sadece "ilk N tanesini
say ve geç" var.

---

### 6. Toplu Baskı Kuyruğunda Yazıcı Kapasitesi

**İhtiyaç:** 50 sayfalık kuyruktan yazıcı kapasitesi olan ilk 15
dosyayı al.

**Çözüm:** `limit(15)`

**Neden:** Yazıcı kapasitesi sabit bir sayı (15); dosyaların içeriğine
göre bir seçim yapılmıyor, sırayla ilk 15 tanesi isteniyor. Bu yüzden
`takeWhile` gibi şart bazlı bir yapıya gerek yok — doğrudan `limit`
yeterli.

---

### 7. Kredi Skoru Düşük Geçmiş Başvuruları Atlama

**İhtiyaç:** Listenin başındaki, skoru barajın altında kalan eski
başvuruları atla; skoru barajı geçen ilk başvurudan itibaren hepsini
yeniden değerlendir.

**Çözüm:** `dropWhile(app -> app.getScore() < threshold)`

**Neden:** Kaç tane düşük skorlu eski başvuru olduğu belli değil —
bu bilgi **verinin kendisinde saklı**, dışarıdan bilinen bir sayı
değil. Üstelik dikkat: barajı geçtikten sonra tekrar barajın altına
düşen bir başvuru olsa bile (skor dalgalı olabilir), `dropWhile` bir
kez "kapıyı açtıktan" sonra bir daha kapatmaz — tıpkı `DropWhileDemo`
içindeki Scott örneğinde olduğu gibi. Sorunun "skoru barajı geçen ilk
başvurudan itibaren **tüm** başvuruları al" demesi de bu davranışla
birebir örtüşüyor.

---

### 8. Alışveriş Sepetinde Bütçe Sınırı

**İhtiyaç:** Ürünleri ekleme sırasına göre gez, toplam tutar 500 TL'yi
aşana kadar sepete al, aştığı anda dur.

**Çözüm:** `takeWhile` + akışın dışında tutulan bir "çalışan toplam"

**Neden:** Durma noktası yine bir **koşul** (toplam bütçeyi aşma) ama
bu sefer koşul, her elemanın kendi değerine değil, o ana kadarki
**birikmiş toplama** bağlı. Bu yüzden predicate'in bir dış değişkende
(örneğin `AtomicInteger` veya tek elemanlı bir dizi) çalışan toplamı
tutması gerekir:

```java
AtomicInteger runningTotal = new AtomicInteger(0);
cart.getItemsInOrder().stream()
        .takeWhile(item -> runningTotal.addAndGet(item.getPrice()) <= 500)
        .forEach(approvedCart::add);
```

**Dikkat:** Bu, Stream'lerde genelde kaçınılması gereken bir "yan
etkili predicate" kullanımıdır (paralel stream'lerde sıra garantisi
olmadığı için toplam yanlış hesaplanabilir). Sıralı (sequential) bir
stream üzerinde çalıştığından emin olun; aksi halde `reduce` benzeri
daha güvenli bir yaklaşım tercih edilmeli.

---

### 9. Vardiya Raporunda İlk Yoğunluk Anına Kadarki Sessiz Dönem

**İhtiyaç:** Çağrı sayısı eşiğin altında kaldığı ilk "sessiz"
dakikaları rapordan çıkar, yoğunluğun başladığı ilk dakikadan itibaren
her şeyi analiz et.

**Çözüm:** `dropWhile(minute -> minute.getCallCount() < threshold)`

**Neden:** Isınma süresinin kaç dakika sürdüğü sabit değil, günden
güne değişebilir — yani **veriye bakarak** karar vermek gerekiyor.
Ayrıca "yoğunluk başladıktan sonra tekrar düşse bile rapor devam
etsin" isteniyor (raporun kopuk kopuk olmaması için), bu da tam
olarak `dropWhile`'ın "kapı bir kez açılınca bir daha kapanmaz"
davranışıyla eşleşiyor.

---

### 10. Sınırlı Stoklu Kampanyada İlk Katılanlar

**İhtiyaç:** Kayıt zamanına göre sıralı listeden ilk 100 kişiyi al.

**Çözüm:** `limit(100)`

**Neden:** Kontenjan sabit bir sayı (100); kimin kampanyaya uygun
olduğuna dair bir içerik şartı yok, sadece sıradaki ilk 100 kişi
isteniyor. `takeWhile` burada anlamsız olurdu çünkü kontrol edilecek
bir koşul (örneğin "kayıt tarihi X'ten önce olduğu sürece al") tarif
edilmiyor — doğrudan sayısal bir sınır var.

---

## Genel özet

- **Sayı biliniyorsa** → `skip(n)` / `limit(n)`
- **Sayı bilinmiyor, veri kendi içinde bir dönüm noktası taşıyorsa**
  → `dropWhile(pred)` / `takeWhile(pred)`
- **Baştan atlanacaksa** → `skip` / `dropWhile`
- **Baştan alınıp sonra durulacaksa** → `limit` / `takeWhile`
- Gerekirse bunlar **birlikte** de kullanılabilir (bkz. Soru 1'deki
  `skip(20).limit(10)`), tıpkı geleneksel kodda hem `continue` hem
  `break` içeren bir döngü yazabileceğiniz gibi.
