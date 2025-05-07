package com.mangoz.platforms;

import com.mangoz.core.Downloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class tiktok extends Downloader {
    @Override
    public String extractVideoUrl(String tiktokUrl) throws IOException {
        Document doc = Jsoup.connect(tiktokUrl)
                .userAgent(USER_AGENT)
                .get();
        return doc.select("meta[property=og:video]").attr("content");
    }
}