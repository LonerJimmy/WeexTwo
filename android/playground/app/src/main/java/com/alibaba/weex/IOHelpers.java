package com.alibaba.weex;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class IOHelpers {

    public static void copyStream(@NonNull InputStream in, @NonNull OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static File saveStream(@NonNull InputStream in, @NonNull File path) throws IOException {
        if (!path.exists() && !path.getParentFile().mkdirs() && !path.createNewFile()) {
            throw new IOException("Failed to create file " + path.getAbsolutePath());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        copyStream(in, out);

        in.close();
        out.close();

        byte[] response = out.toByteArray();

        FileOutputStream fos = new FileOutputStream(path);

        fos.write(response);
        fos.close();

        return path;
    }

    public static void copyFiles(@NonNull File source, @NonNull File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists() && !target.mkdirs()) {
                throw new IOException("Failed to create directory " + target.getAbsolutePath());
            }

            String[] children = source.list();

            for (String child : children) {
                copyFiles(new File(source, child), new File(target, child));
            }
        } else {
            // Make sure the directory we plan to store the recording in exists
            File directory = target.getParentFile();

            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Failed to create directory " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(source);

            try {
                OutputStream out = new FileOutputStream(target);

                try {
                    copyStream(in, out);
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        }
    }

    public static String getStringFromStream(@NonNull InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } finally {
            reader.close();
        }
    }

    public static String getStringFromFile(@NonNull File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        try {
            return IOHelpers.getStringFromStream(fis);
        } finally {
            fis.close();
        }
    }

    public static boolean deleteDirectory(@NonNull File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return path.delete();
    }


    public static void saveBitmap(File outFile, Bitmap bitmap) {
        if (outFile.exists()) {
            outFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCacheFilePath(Context context, String folderName, String fileName) {
        File file = getImageCacheFile(context, folderName, fileName);
        if (file == null) {
            return "";
        } else {
            try {
                return file.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    public static String getImageCacheFilePath(Context context, String fileName) {
        return getCacheFilePath(context, "images", fileName);
    }

    public static File getImageCacheFile(Context context, String fileName) {
        return getImageCacheFile(context, "images", fileName);
    }

    public static File getImageCacheFile(Context context, String folderName, String fileName) {
        File imagePath = new File(context.getCacheDir(), folderName);
        return !imagePath.exists() && !imagePath.mkdirs() ? null : new File(imagePath, fileName);
    }


}
