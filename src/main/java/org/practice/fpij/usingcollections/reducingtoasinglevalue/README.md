# Koleksiyonu Tek Bir Değere İndirgemek (Reducing to a Single Value)

Bu paket, önceki paketlerden farklı bir şey yapıyor: elemanları birbirinden
BAĞIMSIZ işlemek yerine, elemanları BİRBİRİYLE KARŞILAŞTIRIP bir sonucu
eleman eleman BİRİKTİRİYOR (toplam, en büyük, en küçük...). Bu, popüler
"MapReduce" deseninin `Stream` üzerindeki karşılığı.

## Bu pakette

- **`SumOfLengths`** — `mapToInt(...).sum()` ile basit toplama;
  `sum()`'ın aslında `reduce()`'ın özel bir hâli olduğu.
- **`ReduceToLongestName`** — genel amaçlı `reduce((a, b) -> ...)`: elemanları
  ikişer ikişer karşılaştırıp sonucu bir sonraki karşılaştırmaya taşıma,
  boş/tek elemanlı liste durumları, neden `Optional` döndüğü.
- **`MaxWithComparator`** — aynı işi hazır `max(Comparator...)`/
  `min(Comparator...)` ile daha kısa ve niyeti daha açık şekilde yapma.
- **`ReduceWithIdentity`** — bir taban (varsayılan) değer verilen
  `reduce(taban, ...)` overload'u; artık `Optional` DÖNMEMESİ.

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — toplama, en
büyük/küçüğü bulma ve "eşitlikte ilk kazanır" gibi kesin kurallar
gerektiren 10 iş senaryosu.
