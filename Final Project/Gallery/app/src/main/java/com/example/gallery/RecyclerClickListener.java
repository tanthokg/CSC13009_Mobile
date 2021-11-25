package com.example.gallery;

import android.view.View;

public interface RecyclerClickListener {

    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
