package org.practice.fpij.usingcollections.consumerforeach;

public class Stock {
    private String productName;
    private int quantity;
    public Stock(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
