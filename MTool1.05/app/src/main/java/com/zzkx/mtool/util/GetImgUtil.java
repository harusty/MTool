package com.zzkx.mtool.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zzkx.mtool.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2016/4/15.
 */
public class GetImgUtil {
    public static final int REQUEST_CODE_PICK_IMAGE = 10;
    public static final int REQUEST_CODE_CAPTURE_CAMERA = 11;
    public static final int CROP_FINISH = 4;
    public static int MIN_SIZE = 300;
    public static File CATCH_IMAGE_DIR = new File(Environment.getExternalStorageDirectory(),
            "cam_cache_img.jpg");
    //    public static File CATCH_IMAGE_DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            "cam_cache_img.jpg");
    public static File CROP_IMG_DIR = new File(Environment.getExternalStorageDirectory(), "crop.png");

    public static void getImageFromCamera(Fragment frag) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(CATCH_IMAGE_DIR));
            frag.startActivityForResult(intent2, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            Toast.makeText(MyApplication.getContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    public static void getImageFromCamera(Activity act,File file) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            try {
                file.getParentFile().mkdirs();
            }catch (Exception e){
                e.printStackTrace();
            }
            act.startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)),
                    REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            Toast.makeText(MyApplication.getContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    public static void getImageFromAlbum(Fragment frag) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        frag.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public static void getImageFromAlbum(Activity act) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        act.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public static void sdScan(File mPhotoFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(mPhotoFile);
            mediaScanIntent.setData(contentUri);
            MyApplication.getContext().sendBroadcast(mediaScanIntent);
        } else {
            MyApplication.getContext().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public static void cropPhoto(Fragment frag, Uri uri, int aspectX, int aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX*1000);
        intent.putExtra("aspectY", aspectY*1000-1);
        // outputX outputY 是裁剪图片宽高
        if (aspectX != 0 && aspectY != 0) {
            intent.putExtra("outputX", 400 * aspectX);
            intent.putExtra("outputY", 400 * aspectY);
        } else {
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 400);
        }
        intent.putExtra("return-data", false);

        Uri cropUri = Uri.fromFile(CROP_IMG_DIR);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        frag.startActivityForResult(intent, CROP_FINISH);
    }

    public static void cropPhoto(Activity act, Uri uri, int aspectX, int aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX*1000);
        intent.putExtra("aspectY", aspectY*1000-1);
        // outputX outputY 是裁剪图片宽高
        if (aspectX != 0 && aspectY != 0) {
            intent.putExtra("outputX", 400 * aspectX);
            intent.putExtra("outputY", 400 * aspectY);
        } else {
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 400);
        }
        intent.putExtra("return-data", false);

        Uri cropUri = Uri.fromFile(CROP_IMG_DIR);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        act.startActivityForResult(intent, CROP_FINISH);
    }

    public static Bitmap parseUri(Uri uri) {
        Bitmap bitmap = null;
        ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void compressImage(File file) {
        try {
            int degree = readPictureDegree(file.getAbsolutePath());//获取照片保存方向
            //先描边
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);

            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            float hh = 1920f;
            float ww = 1080f;
            int be = 1;
            if (w > h && w > ww) {
                be = (int) (w / ww);
            } else if (w < h && h > hh) {
                be = (int) (h / hh);
            }
            if (be <= 0)
                be = 1;
            newOpts.inSampleSize = be;//设置缩放比例
            newOpts.inJustDecodeBounds = false;
            Bitmap result = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);

//            FileOutputStream fileOutputStream = new FileOutputStream(GetImgUtil.CATCH_IMAGE_DIR);
            result = adjustPhotoRotation(result, degree);
            if (result != null) {
                QualityCompress(result, CATCH_IMAGE_DIR, MIN_SIZE);
            }
//                result.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void QualityCompress(Bitmap image, File file, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {

        }
        return null;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            ToastUtils.showToast("ido");
            e.printStackTrace();
        }
        return degree;
    }

    public static void deleteImgCach() {
        if (CROP_IMG_DIR.exists()) {
            CROP_IMG_DIR.delete();
            sdScan(CROP_IMG_DIR);
        }
        if (CATCH_IMAGE_DIR.exists()) {
            CATCH_IMAGE_DIR.delete();
            sdScan(CATCH_IMAGE_DIR);
        }
    }

    public static Bitmap getBitmap(String path) throws IOException {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
    public static Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error searchMessage for IOException or IllegalStateException
            getRequest.abort();
//            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e.toString());
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
}
