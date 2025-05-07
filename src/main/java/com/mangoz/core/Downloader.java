package com.mangoz.core;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Downloader {
    protected final OkHttpClient httpClient;
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    public Downloader() {
        this.httpClient = new OkHttpClient();
    }

    /**
     * Fetches the direct video URL from a reel link.
     * 
     * @param reelUrl The URL of the reel (e.g., TikTok/Instagram/FB link).
     * @return Direct MP4 URL or null if not found.
     */
    public abstract String extractVideoUrl(String reelUrl) throws IOException;

    /**
     * Downloads a video from a direct URL and saves it to disk.
     * 
     * @param videoUrl   Direct MP4 URL.
     * @param outputPath File path to save (e.g., "/videos/reel.mp4").
     */
    public void downloadVideo(String videoUrl, String outputPath) throws IOException {
        Path path = Paths.get(outputPath);
        if (path.getParent() != null) {
            path.getParent().toFile().mkdirs();
        }

        Request request = new Request.Builder()
                .url(videoUrl)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = httpClient.newCall(request).execute();
                BufferedInputStream in = new BufferedInputStream(response.body().byteStream());
                FileOutputStream out = new FileOutputStream(outputPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Public method to download a reel from its shareable URL.
     * 
     * @param reelUrl    Platform-specific reel URL.
     * @param outputPath Save location (e.g., "downloads/tiktok_video.mp4").
     */
    public void download(String reelUrl, String outputPath) throws IOException {
        String videoUrl = extractVideoUrl(reelUrl);
        if (videoUrl == null) {
            throw new IOException("Failed to extract video URL from: " + reelUrl);
        }
        downloadVideo(videoUrl, outputPath);
    }
}