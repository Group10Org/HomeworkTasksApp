package com.example.hwtasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

class ShortsRepository(private val apiKey: String) {

    suspend fun loadShortsForClasses(classNames: List<String>): List<ShortItem> = withContext(Dispatchers.IO) {
        val result = mutableListOf<ShortItem>()

        for (className in classNames) {
            val query = URLEncoder.encode("$className shorts", "UTF-8")
            val url =
                "https://www.googleapis.com/youtube/v3/search" +
                        "?part=snippet" +
                        "&type=video" +
                        "&videoDuration=short" +
                        "&maxResults=5" + // limit per class
                        "&q=$query" +
                        "&key=$apiKey"

            try {
                val response = URL(url).readText()
                val json = JSONObject(response)
                val items = json.getJSONArray("items")

                for (i in 0 until items.length()) {
                    val obj = items.getJSONObject(i).getJSONObject("snippet")
                    val videoId = items.getJSONObject(i).getJSONObject("id").getString("videoId")
                    val thumb = obj.getJSONObject("thumbnails").getJSONObject("high").getString("url")

                    result.add(
                        ShortItem(
                            videoId = videoId,
                            title = obj.getString("title"),
                            thumbnailUrl = thumb,
                            channelName = obj.getString("channelTitle")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        result
    }
}
