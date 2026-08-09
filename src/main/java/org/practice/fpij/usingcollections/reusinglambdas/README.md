# Lambda İfadelerini Yeniden Kullanma (Reusing Lambda Expressions)

Bu paket kısa bir uyarıyla başlıyor: lambda'lar o kadar kısa yazılıyor ki,
aynı kontrolü fark etmeden birden fazla yere KOPYALA-YAPIŞTIR yapmak çok
kolay. Çözüm basit: aynı lambda'yı BİR KERE, açık tipli bir değişkende
(`Predicate<T>` gibi) tanımlayıp, ihtiyaç duyulan her yerde AYNI değişkeni
kullanmak.

## Bu pakette

- **`DuplicateLambdaProblem`** — `friends`, `editors`, `comrades` gibi üç
  farklı listede AYNI lambda'nın (`name -> name.startsWith("N")`) üç kez
  tekrar edilmesi ve bunun neden bir bakım sorunu olduğu.
- **`ReusableLambdaSolution`** — aynı lambda'yı `final Predicate<String>
  startsWithN = ...` şeklinde bir değişkende toplayıp üç `filter()`
  çağrısında da paylaşma; `Predicate`'in `filter()`'a özel olmadığını,
  `anyMatch()` gibi başka yerlerde de kullanılabildiğini gösteren bonus
  örnek.

## Alıştırmalar

`exercises/questions.md` ve `exercises/answers.md` — aynı (SABİT, parametre
almayan) kontrolün birden fazla listede tekrar tekrar yazılmaması gereken
10 iş senaryosu.

> Not: Kontrolün kendisi de dışarıdan bir değere göre değişiyorsa (örn.
> "hangi şehir?"), bu artık bu paketin değil, `lexicalscopingandclosures`
> paketinin konusudur.
