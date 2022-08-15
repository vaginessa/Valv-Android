package se.arctosoft.vault.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import se.arctosoft.vault.R;

public class GalleryFullscreenViewHolder extends RecyclerView.ViewHolder {
    public final SubsamplingScaleImageView imageView;
    public final TextView txtName;

    public GalleryFullscreenViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        txtName = itemView.findViewById(R.id.txtName);
    }
}