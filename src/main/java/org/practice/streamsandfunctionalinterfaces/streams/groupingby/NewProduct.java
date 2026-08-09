package org.practice.streamsandfunctionalinterfaces.streams.groupingby;

public class NewProduct {
    private String name;
    private int price;
    public NewProduct(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    /*
     * toString(), nesneyi konsolda ve debugger'da okunabilir görmek için kullanılır.
     * Burada yalnızca incelemek istediğimiz name ve price alanlarını gösteriyoruz.
     * Gerçek kullanıcı ekranı doğrudan toString() çıktısına bağlanmamalıdır.
     * Ekranda gösterilecek alanlar DTO veya açık formatlama ile ayrıca hazırlanmalıdır.
     */
    @Override
    public String toString() {
        return "NewProduct{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
