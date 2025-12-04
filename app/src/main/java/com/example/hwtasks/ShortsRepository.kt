package com.example.hwtasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class ShortsRepository(private val apiKey: String) {

    private var nextPageToken: String? = null

    suspend fun loadShorts(): List<ShortItem> = withContext(Dispatchers.IO) {
        val baseUrl =
            "https://www.googleapis.com/youtube/v3/search" +
                    "?part=snippet" +
                    "&type=video" +
                    "&videoDuration=short" +
                    "&maxResults=20" +
                    "&q=shorts" +
                    "&key=$apiKey" +
                    (nextPageToken?.let { "&pageToken=$it" } ?: "")

        val response = URL(baseUrl).readText()
        val json = JSONObject(response)

        nextPageToken = json.optString("nextPageToken", null)

        val items = json.getJSONArray("items")
        val result = mutableListOf<ShortItem>()

        for (i in 0 until items.length()) {
            val obj = items.getJSONObject(i).getJSONObject("snippet")
            val videoId =
                items.getJSONObject(i)
                    .getJSONObject("id")
                    .getString("videoId")

            val thumb =
                obj.getJSONObject("thumbnails")
                    .getJSONObject("high")
                    .getString("url")

            result.add(
                ShortItem(
                    videoId = videoId,
                    title = obj.getString("title"),
                    thumbnailUrl = thumb,
                    channelName = obj.getString("channelTitle")
                )
            )
        }

        return@withContext result
    }
}