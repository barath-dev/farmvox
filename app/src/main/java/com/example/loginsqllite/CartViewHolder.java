package com.example.loginsqllite;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public TextView cartProductName;
    public TextView cartProductQuantity;
    public ImageView cartProductImage;
    public TextView cartProductPrice;
    public Button plusButton;
    public Button minusButton;
    public Button removeButton;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartProductName = itemView.findViewById(R.id.cartProductName);
        cartProductQuantity = itemView.findViewById(R.id.cartProductQuantity);
        cartProductImage = itemView.findViewById(R.id.cartProductImage);
        cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
        plusButton = itemView.findViewById(R.id.plusButton);
        minusButton = itemView.findViewById(R.id.minusButton);
        removeButton = itemView.findViewById(R.id.removeButton);
    }
}
