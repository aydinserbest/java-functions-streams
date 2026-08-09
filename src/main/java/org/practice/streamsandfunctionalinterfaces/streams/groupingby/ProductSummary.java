package org.practice.streamsandfunctionalinterfaces.streams.groupingby;

/*
 * ProductSummary bir ekran/rapor DTO'sudur.
 *
 * Domain Product'ın bütün alanlarını taşımak yerine, kullanıcıya gösterilecek
 * iki hazır değeri taşır:
 *
 * displayName    -> gösterilecek ürün adı
 * formattedPrice -> kullanıcıya gösterilecek fiyat metni
 *
 * record, yalnızca veri taşıyan küçük DTO'lar için constructor, accessor,
 * equals(), hashCode() ve okunabilir toString() metotlarını otomatik üretir.
 */
public record ProductSummary(
        String displayName,
        String formattedPrice
) {
}
