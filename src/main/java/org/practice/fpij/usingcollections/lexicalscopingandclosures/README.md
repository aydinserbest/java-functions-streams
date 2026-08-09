# Lexical Scoping ve Closure'lar

Bu paket şu soruyu işliyor: aynı kontrolü (örn. "isim N ile mi başlıyor?")
farklı bir değerle (N yerine B, ya da başka bir harf) tekrar tekrar
yazmadan nasıl üretiriz? Cevap, bir DEĞER alıp o değeri HATIRLAYAN (kapatan
— closure) bir davranış (`Predicate`/`Function`) döndüren fonksiyonlar
yazmak.

```text
static Predicate<T> factory(X parametre) {
    return eleman -> ... parametre ...;
}
```

`factory("N")` ve `factory("B")` aynı KODU paylaşır, sadece kapattıkları
değer farklıdır.

## Bu pakette

- **`DuplicatePredicateProblem`** — `reusinglambdas`'ta çözdüğümüz tekrarın,
  ikinci bir harf eklenince (N ve B) nasıl geri geldiğini gösteriyor.
- **`StartsWithHigherOrderFunction`** — harfi parametre alıp bir `Predicate`
  üreten static bir metotla çözüm; lexical scoping ve closure kavramları,
  `final`/effectively-final kısıtı burada anlatılıyor.
- **`CurriedFunctionRefactor`** — aynı fikri static metot yerine bir
  `Function<String, Predicate<String>>` DEĞİŞKENİNE taşıyıp (curry), üç
  farklı yazım (verbose → kısa → tip çıkarımlı) ile gösteriyor.
- **`PredicateVsFunction`** — `Predicate<T>` (karar) ile `Function<T,R>`
  (dönüşüm) arasındaki farkı ve ikisinin nasıl iç içe geçebildiğini
  (`Function<String, Predicate<String>>`) özetliyor.

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — hep aynı kontrolü
farklı bir parametreyle (şehir, eşik, oran, yıl...) üretmeniz gereken 10 iş
senaryosu.
