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
    private boolean isRoundImage;
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
        this.isRoundImage = isRoundImage;

        if (selectedImageUrl != null && selectedImageView != null && commonUtils.isOnline(context)) {
            new LoadImageTask().execute();
        }
    }

    /**
     * AsyncTask loadImageTask
     * This load the image recipe from given url in background
     */
    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bmp = null;
            try {
                URL url = new URL(selectedImageUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (isRoundImage) {
                selectedImageView.setImageBitmap(getRoundedShape(bitmap));
            } else {
                selectedImageView.setImageBitmap(bitmap);
            }
        }

        /**
         * Convert bitmap into round shape
         * @param scaleBitmapImage
         * @return Bitmap object
         */
        public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
            int targetWidth = 125;
            int targetHeight = 125;
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);

            canvas.clipPath(path);
            Bitmap sourceBitmap = scaleBitmapImage;
            if (sourceBitmap != null) {
                canvas.drawBitmap(sourceBitmap,
                        new Rect(0, 0, sourceBitmap.getWidth(),
                                sourceBitmap.getHeight()),
                        new Rect(0, 0, targetWidth, targetHeight), null);
            }
            return targetBitmap;
        }
    }
}
