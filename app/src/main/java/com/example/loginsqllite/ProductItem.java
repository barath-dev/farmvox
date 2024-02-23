package com.example.loginsqllite;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class ProductItem implements Parcelable {
    private int imageResId;
    private String productName;
    private double price;
    private String quantity;

    public ProductItem(int imageResId, String productName, double price, String quantity) {
        this.imageResId = imageResId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    protected ProductItem(Parcel in) {
        imageResId = in.readInt();
        productName = in.readString();
        price = in.readDouble();
        quantity = in.readString();
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    public int getImageResId() {
        return imageResId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageResId);
        dest.writeString(productName);
        dest.writeDouble(price);
        dest.writeString(quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductItem that = (ProductItem) obj;
        return Double.compare(that.price, price) == 0 &&
                imageResId == that.imageResId &&
                productName.equals(that.productName) &&
                quantity.equals(that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageResId, productName, price, quantity);
    }
}
