package com.kavi.droid.exchange.services.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.kavi.droid.exchange.utils.CommonUtils;
import com.squareup.picasso.Picasso;

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
    private String selectedImageUrl;
    private ImageView selectedImageView;
    private CommonUtils commonUtils = new CommonUtils();

    public ImageLoadingManager(Context context) {
        this.context = context;
    }

    /**
     * Get the image url and ImageView to manager
     * Call the async task to load image from url and set it to given ImageView
     * @param imageUrl Given image url
     * @param imageView Given ImageView object
     * @param isRoundImage is image must show as round or not
     */
    public void loadImageToImageView (String imageUrl, ImageView imageView, boolean isRoundImage) {
        this.selectedImageUrl = imageUrl;
        this.selectedImageView = imageView;

        if (selectedImageUrl != null && selectedImageView != null && commonUtils.isOnline(context)) {
            if (isRoundImage) {
                Picasso.with(context)
                        .load(selectedImageUrl)
                        .transform(new CircleTransform())
                        .into(selectedImageView);
            } else {
                Picasso.with(context)
                        .load(selectedImageUrl)
                        .transform(new RoundedCornersTransform())
                        .into(selectedImageView);
            }

        }
    }
}
