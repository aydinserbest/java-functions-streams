# Elemanları Birleştirme (Joining Elements)

Bu paket, bir koleksiyondaki elemanları aralarına bir ayraç (virgül, tire,
`/` gibi) koyarak TEK bir metinde birleştirmeyi işliyor. Basit görünen bu
iş, elle (klasik döngüyle) yapılınca şaşırtıcı derecede can sıkıcı hâle
geliyor (sondaki fazladan virgül, boş liste kontrolü); modern JDK araçları
bunu tek satıra indiriyor.

## Bu pakette

- **`PrintListImperative`** — klasik `for`/enhanced `for` ile birleştirme
  denemesi ve ortaya çıkan "fazladan virgül" sorunu.
- **`StringJoinDemo`** — `String.join(ayraç, liste)` ile tek satırlık çözüm.
- **`CollectJoiningDemo`** — önce `map()` ile dönüştürüp sonra
  `Collectors.joining(...)` ile birleştirme; `joining()`'in
  ayraç/prefix/suffix çeşitleri.
- **`StringJoinerDemo`** — perde arkasındaki `StringJoiner` sınıfını
  doğrudan kullanarak prefix/suffix, boş durum mesajı (`setEmptyValue`) ve
  ayrı biriktirilmiş sonuçları birleştirme (`merge`).

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — hangi aracın (
`String.join` / `map().collect(joining())` / `StringJoiner`) hangi
senaryoya uyduğunu ayırt etmeye yönelik 10 iş senaryosu.
