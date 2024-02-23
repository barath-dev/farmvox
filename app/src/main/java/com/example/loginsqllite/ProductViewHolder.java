package com.example.loginsqllite;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView productImageView;
    public TextView productNameTextView;
    public TextView productPriceTextView;
    public TextView productQuantityTextView;
    public Button addButton;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImageView = itemView.findViewById(R.id.productImageView);
        productNameTextView = itemView.findViewById(R.id.productNameTextView);
        productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
        productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);
        addButton = itemView.findViewById(R.id.addButton);
    }
}
