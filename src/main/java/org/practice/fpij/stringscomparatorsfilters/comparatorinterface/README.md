# Comparator Arayüzünü Uygulamak (Implementing the Comparator Interface)

Bu paket, `Comparator` arayüzünün nasıl bir functional interface'e dönüştüğünü;
`List.sort()` yerine neden `stream().sorted()` tercih edildiğini; bir
Comparator'ı method reference ile ifade etmenin ne zaman mümkün olduğunu
(hedef/argüman parametre kalıbı); tekrar eden karşılaştırma mantığını
`reversed()` ile nasıl ortadan kaldırdığımızı; ve `min()`/`max()` ile bir
koleksiyondan tek bir "en küçük/en büyük" elemanı güvenle (Optional ile)
seçmeyi işliyor.

## Bu pakette

- **`Person`** — örneklerde kullanılan paylaşılan JavaBean. `ageDifference`
  metodu, bilinçli olarak `Comparator`'ın `compare` sözleşmesine (negatif /
  sıfır / pozitif) uyacak şekilde tasarlandı.
- **`SortByAgeAscending`** — `stream().sorted(lambda)` ile artan yaşa göre
  sıralama; `List.sort()`'un neden tercih edilmediği (orijinal listeyi
  mutasyona uğratması); ardından aynı lambda'yı `Person::ageDifference`
  method reference'ına indirgeme.
- **`SortByAgeDescendingDuplication`** — "problem": azalan sıralama için
  parametreleri elle ters çevrilmiş yeni bir lambda yazmak hem method
  reference kullanımını imkansız kılıyor hem de DRY ihlaline yol açıyor.
- **`SortByAgeReusingComparator`** — "çözüm": karşılaştırma mantığını adlı
  bir `Comparator` değişkeninde tanımlayıp `reversed()` (bir default metot,
  higher-order fonksiyon) ile tekrarsız şekilde tersini üretme.
- **`SortByNameAndFindMinMax`** — aynı `sorted()` iskeletini isme göre
  (String'in `compareTo`'su) yeniden kullanma; `min()`/`max()` ile listeyi
  hiç sıralamadan doğrudan en genç/en yaşlı kişiyi bulma ve `Optional` ile
  güvenle yazdırma.

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — bir listeyi Comparator
ile sıralamanız, orijinaline dokunmadan yeniden kullanmanız veya tek bir
uç elemanı (en küçük/en büyük) bulmanız gereken 10 iş senaryosu.
