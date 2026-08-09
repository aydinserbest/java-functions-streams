# Koleksiyonu Dönüştürmek (Transforming a Collection)

Bu paket, `map()` ile bir koleksiyondaki HER elemandan yeni bir değer
üretip yeni bir Stream/liste oluşturmayı işliyor. Temel fikir: `map()`
elemanın kendisini DEĞİŞTİRMEZ, ondan YENİ bir değer üretir (kaynak liste
her zaman aynı kalır).

## Bu pakette

- **`Transform`** — `map(String::toUpperCase)` ile temel dönüşüm; aynı işi
  klasik döngüyle ve `forEach()` içinde dış listeye ekleyerek yapmanın
  neden "BAD IDEA" olduğu; `map(String::length)` ile girdi/çıktı tipinin
  nasıl değişebildiği (`String -> Integer`).
- **`TransformStreamExample`** — `map()`/`filter()`'ın gerçekte bir `List`
  değil, henüz çalıştırılmamış bir `Stream` döndürdüğünü (lazy pipeline)
  gösteren küçük örnek.
- **`transform-explanation.md`** — bu ikisini çok daha ayrıntılı ele alan,
  soru-cevap tarzında yazılmış uzun bir not (map vs transform, projection
  kavramı, lazy evaluation).

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — her elemandan yeni bir
değer üretip yeni bir liste oluşturmanız gereken 10 iş senaryosu; ayrıca
"BAD IDEA" kalıbını fark etme ve lazy evaluation'ı fark ettiren iki tuzak
soru içeriyor.
