package com.suidadabian.lixiaofeng.pilipili.download;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.PiliPiliApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class DownloadManager {
    private static final long TIMEOUT_MILLISECONDS = 60000L;
    private static final Object DOWNLOAD_VALUE = new Object();
    private static DownloadManager instance;
    private ExecutorService threadPool;
    private ConcurrentHashMap<String, Object> downloadingPictureMap;
    private Handler handler;

    private DownloadManager() {
        threadPool = Executors.newSingleThreadExecutor();
        downloadingPictureMap = new ConcurrentHashMap<>();
        handler = new Handler(Looper.getMainLooper());
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }

        return instance;
    }

    public void downloadPicture(String pictureUrl, Callback callback) {
        WeakReference<Callback> callbackRef = new WeakReference<>(callback);

        if (pictureUrl == null || pictureUrl.isEmpty()) {
            return;
        }

        String name = pictureUrl.substring(pictureUrl.lastIndexOf('/') + 1);

        if (downloadingPictureMap.containsKey(pictureUrl)) {
            Toast.makeText(PiliPiliApplication.getContext(), "正在下载：" + name, Toast.LENGTH_SHORT).show();
            return;
        }

        if (hasExternalStoragePrivatePicture(pictureUrl)) {
            Toast.makeText(PiliPiliApplication.getContext(), "已下载：" + name, Toast.LENGTH_SHORT).show();
            return;
        }

        threadPool.execute(() -> {
            FutureTarget<File> futureTarget = GlideApp.with(PiliPiliApplication.getContext())
                    .downloadOnly()
                    .load(pictureUrl)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                downloadingPictureMap.put(pictureUrl, DOWNLOAD_VALUE);
                File file = futureTarget.get(TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
                File realFile = new File(PiliPiliApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), name);
                bis = new BufferedInputStream(new FileInputStream(file));
                bos = new BufferedOutputStream(new FileOutputStream(realFile));
                byte[] buffer = new byte[2048];
                while (bis.read(buffer) != -1) {
                    bos.write(buffer);
                }
                if (callbackRef.get() != null) {
                    handler.post(() -> callbackRef.get().onSuccess(pictureUrl, realFile));
                }
                MediaScannerConnection.scanFile(PiliPiliApplication.getContext(), new String[]{file.toString()}, null, null);
                handler.post(() -> Toast.makeText(PiliPiliApplication.getContext(), "下载成功：" + name, Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                if (callbackRef.get() != null) {
                    callbackRef.get().onFail(pictureUrl, e);
                }
                handler.post(() -> Toast.makeText(PiliPiliApplication.getContext(), "下载失败：" + name, Toast.LENGTH_SHORT).show());
            } finally {
                downloadingPictureMap.remove(pictureUrl);
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean hasExternalStoragePrivatePicture(String pictureUrl) {
        File path = PiliPiliApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, pictureUrl);
            return file.exists();
        }

        return false;
    }

    public File getDownloadPath() {
        return PiliPiliApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public interface Callback {
        void onSuccess(String pictureUrl, File file);

        void onFail(String pictureUrl, Exception e);
    }
}
