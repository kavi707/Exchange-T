package com.kavi.droid.exchange.services.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kavi on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ImageLoadingManager {

    private Context context;
    private CommonUtils commonUtils = new CommonUtils();
    private ImageView selectedImageView = null;

    public ImageLoadingManager(Context context) {
        this.context = context;
    }

    /**
     * Render the image from url and load it to given image view
     * @param imageUrl Given image url
     * @param imageView Given ImageView object
     */
    public void loadImageToImageView (String imageUrl, ImageView imageView) {
        if (imageUrl != null && imageView != null && commonUtils.isOnline(context)) {
            this.selectedImageView = imageView;
            Picasso.with(context)
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    /**
     * Render the image from url and load it to given image view as round image
     * @param imageUrl Given image url
     * @param imageView Given ImageView object
     */
    public void loadRoundImageToImageView (String imageUrl, ImageView imageView) {
        if (imageUrl != null && imageView != null && commonUtils.isOnline(context)) {
            this.selectedImageView = imageView;
            Picasso.with(context)
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .into(imageView);
        }
    }

    /**
     * Render the image from url and load it to given image view with round corners
     * @param imageUrl Given image url
     * @param imageView Given ImageView object
     */
    public void loadRoundCornerImageToImageView (String imageUrl, ImageView imageView) {
        if (imageUrl != null && imageView != null && commonUtils.isOnline(context)) {
            this.selectedImageView = imageView;
            Picasso.with(context)
                    .load(imageUrl)
                    .into(roundCornerImageTarget);
        }
    }

    private Target roundCornerImageTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Bitmap is loaded, use image here
            selectedImageView.setImageBitmap(getRoundedCornerBitmap(bitmap));
            Picasso.with(context).cancelRequest(this);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            // On bitmap loading failed
            Picasso.with(context).cancelRequest(this);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
