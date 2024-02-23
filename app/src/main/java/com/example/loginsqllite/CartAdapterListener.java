package com.example.loginsqllite;

import java.util.List;

public interface CartAdapterListener {
    void onCartUpdated(List<CartItem> updatedCartItems);
}

