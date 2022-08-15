package se.arctosoft.vault.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.lang.ref.WeakReference;
import java.util.List;

import se.arctosoft.vault.R;
import se.arctosoft.vault.adapters.viewholders.GalleryFullscreenViewHolder;
import se.arctosoft.vault.data.GalleryFile;
import se.arctosoft.vault.encryption.Encryption;
import se.arctosoft.vault.utils.Settings;
import se.arctosoft.vault.utils.Toaster;

public class GalleryFullscreenAdapter extends RecyclerView.Adapter<GalleryFullscreenViewHolder> {
    private static final String TAG = "GalleryFullscreenAdapter";
    private final WeakReference<FragmentActivity> weakReference;
    private final List<GalleryFile> galleryFiles;

    public GalleryFullscreenAdapter(FragmentActivity context, @NonNull List<GalleryFile> galleryFiles) {
        this.weakReference = new WeakReference<>(context);
        this.galleryFiles = galleryFiles;
    }

    @NonNull
    @Override
    public GalleryFullscreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_gallery_fullscreen, parent, false);
        return new GalleryFullscreenViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryFullscreenViewHolder holder, int position) {
        FragmentActivity context = weakReference.get();
        GalleryFile galleryFile = galleryFiles.get(position);
        new Thread(() -> Encryption.decryptToCache(context, galleryFile.getUri(), Settings.getInstance(context).getTempPassword(), new Encryption.IOnUriResult() {
            @Override
            public void onUriResult(Uri outputUri) {
                context.runOnUiThread(() -> holder.imageView.setImage(ImageSource.uri(outputUri)));
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toaster.getInstance(context).showLong("Failed to decrypt " + galleryFile.getName() + ": " + e.getMessage());
            }
        })).start();
        holder.txtName.setText(galleryFile.getName());
        holder.imageView.setOnClickListener(v -> context.onBackPressed());
        holder.imageView.setMinimumDpi(40);
    }

    @Override
    public void onViewRecycled(@NonNull GalleryFullscreenViewHolder holder) {
        holder.imageView.recycle();
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return galleryFiles.size();
    }

}
