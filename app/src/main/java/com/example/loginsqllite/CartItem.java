package com.example.loginsqllite;
// CartItem.java
import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private final ProductItem product;
    private int quantity;

    // Constructor, getters, and other methods

    // Parcelable implementation
    protected CartItem(Parcel in) {
        product = in.readParcelable(ProductItem.class.getClassLoader());
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(product, flags);
        dest.writeInt(quantity);
    }

    // Constructor
    public CartItem(ProductItem product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and setters
    public ProductItem getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }
    public int getImageResId() {
        // Implement this method to return the image resource ID of the product
        return product.getImageResId();
    }

    public double getPrice() {
        // Implement this method to return the price of the product
        return product.getPrice();
    }

}
