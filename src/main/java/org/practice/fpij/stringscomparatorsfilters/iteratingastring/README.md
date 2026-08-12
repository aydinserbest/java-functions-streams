# Bir String'i Karakter Karakter Gezmek (Iterating a String)

Bu paket, `String`in `chars()` metoduyla bir metni tek tek karakterlerine
ayırıp Stream API ile (`filter`, `map`/`mapToObj`, `forEach`) işlemeyi ve bu
sırada method reference'ların üç farklı türünü (bir instance üzerinden
static olmayan çağrı, bir sınıfın static metodu, ve bunların yol açtığı
belirsizlik tuzağını) ayırt etmeyi işliyor.

## Bu pakette

- **`IterateStringNumericOutput`** — `chars()`'ın aslında bir
  `Stream<Character>` DEĞİL, `IntStream` döndürdüğünü gösterir (çıktıda
  harf yerine sayı görme sürprizi); lambda'dan `System.out::println`
  method reference'ına geçiş.
- **`IterateStringAsCharacters`** — kendi yazdığımız `printChar` yardımcı
  metoduyla (static method reference) ya da `mapToObj` ile int'leri
  gerçek karakterlere çevirme.
- **`IterateStringFilterDigits`** — `filter(Character::isDigit)` ile
  sadece rakamları süzme; method reference'ların instance/static ayrımını
  ve "aynı imzada hem instance hem static metot varsa" ortaya çıkan
  DERLEME HATASI tuzağını (örn. `Double::toString`) açıklar.

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — bir metnin
karakterlerini tek tek işlemeniz gereken (rakam sayma, harf süzme, boşluk
temizleme gibi) 10 iş senaryosu.
