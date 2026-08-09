# Stream'de continue/break Karşılıkları

Bu paket, geleneksel `for` döngüsündeki `continue` (bir/birkaç elemanı
atla) ve `break` (belirli bir noktada dur) davranışlarının Stream
dünyasındaki karşılıklarını işliyor. Dört fonksiyon, "kapı" benzetmesiyle
özetlenebilir:

```text
skip(n)         : başta kapalı kapı, n eleman geçince açılır
dropWhile(pred) : başta kapalı kapı, pred yanlış olan ilk elemanda açılır
limit(n)        : başta açık kapı, n eleman geçince sonsuza dek kapanır
takeWhile(pred) : başta açık kapı, pred yanlış olan ilk elemanda sonsuza dek kapanır
```

## Alt paketler

- **`skippingvalues`** — `continue`'nun karşılığı: baştan eleman atlama.
  `SkipElements`/`SkipValues`/`SkipAndFilter` (`skip()` temelleri),
  `DropWhileDemo` (`dropWhile()` ve `filter()` farkı — "turnike" benzetmesi),
  `TraditionalAndFunctional` (klasik döngü ile `skip()` karşılaştırması),
  `ContinueStatementEquivalent` (if/continue'nun `skip()`/`dropWhile()` ile
  birebir karşılığı).
- **`terminatingIterations`** — `break`'in karşılığı: `LimitElements`,
  `limit()` ve `takeWhile()`'ı, `limit(3)` ve
  `takeWhile(name -> name.length() > 4)` örnekleriyle işliyor.

## Alıştırmalar (`exercises/`)

- **`questions/practice-questions.md`** ve **`practice-answers.md`** — bu
  dört fonksiyonu (`skip`/`dropWhile`/`limit`/`takeWhile`) gerektiren 10 iş
  senaryosu.
- **`Q01`...`Q10`** — o sorulara karşılık gelen boş, `TODO`'lu sınıflar.
- **`helperClasses/`** — çözümler için gereken yan konular:
  `MapVsMapToObj` (int'ten nesneye geçiş), `RandomBasics` (rastgele veri
  üretme), `LocalDateBasics` (tarih işlemleri), `ComparatorSortingBasics`
  (`Comparator.comparing` ile sıralama).
