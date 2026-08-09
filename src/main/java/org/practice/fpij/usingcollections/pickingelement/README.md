# Tek Bir Elemanı Seçmek (Picking an Element)

Bu paket, koleksiyondan koşula uyan İLK elemanı GÜVENLE seçmeyi işliyor.
"Güvenle" derken kastedilen: eleman bulunamayabilir, ve bu ihtimali
`null` ile değil, Java'nın "sonuç olmayabilir" kabını (`Optional`) kullanarak
yönetmek.

## Bu pakette

- **`FilterAndCollectElements`** — hazırlık adımı: `filter()` + `collect()`
  ile koşula uyan BİRDEN FAZLA elemanı seçme (asıl konunun bir öncesi).
- **`PickElementImperative`** — klasik, kokulu yaklaşım: `null` ile
  başlatılan bir değişken, elle döngü + `break`, sonradan `if/else`.
- **`PickElementElegant`** — `filter().findFirst()` ile tek satırda ilk
  eşleşeni bulma; sonucu `Optional<T>` olarak yönetme (`orElse`,
  `isPresent()+get()`, `orElseThrow()`, `ifPresent()`).

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — "sonuç bulunamazsa ne
olmalı?" sorusuna göre `orElse` / `ifPresent` / `ifPresentOrElse` /
`orElseThrow` arasında doğru seçimi yapmanız gereken 10 iş senaryosu.
