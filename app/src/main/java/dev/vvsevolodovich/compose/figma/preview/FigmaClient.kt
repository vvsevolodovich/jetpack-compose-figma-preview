package dev.vvsevolodovich.compose.figma.preview

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.time.Duration
import java.util.concurrent.TimeUnit

class FigmaClient(val fileId: String, val accessToken: String) {

    val client = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS).build();
    val baseUrl = "api.figma.com/v1";


    /*Future<String> getImages(String key, FigmaQuery query) async =>
    await _getFigma('/images/$key', query);
*/
/*    Future<String> _getFigma(String path, [FigmaQuery query]) async {
        final uri = Uri.https(base, '$apiVersion$path', query?.figmaQuery);
        final response = await _sendRequest('GET', uri, _authHeaders);
        if (response.statusCode >= 200 && response.statusCode < 300) {
            return response.body;
        } else {
            throw FigmaError(code: response.statusCode, message: response.body);
        }
    }*/

    fun searchComponents(fileKey: String, callback: (List<FigmaComponent>?) -> Unit) {
        val request = Request.Builder()
            .url("https://$baseUrl/files/$fileKey/components")
            .header("X-Figma-Token", accessToken)
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(arrayListOf())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val jsonString = response.body()!!.string()
                    val parsedComponents = parseComponents(jsonString)
                    callback(parsedComponents);
                }
            }
        })
    }

    fun getImage(key: String, callback: (String?) -> Unit) {
        val request = Request.Builder()
            .url("https://$baseUrl/images/$fileId?ids=$key&scale=3")
            .header("X-Figma-Token", accessToken)
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback("ERROR")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val jsonString = response.body()!!.string()
                    print(jsonString)
                    callback(parseImageUrl(key, jsonString))
                }
            }
        })
    }

    fun parseImageUrl(id: String, jsonString: String): String? {
        val jObject = JSONObject(jsonString)
        val hasError = !jObject.isNull("err")
        return if (hasError) {
            val error = jObject.getString("err")
            print(error)
            null
        } else {
            val images = jObject.getJSONObject("images")
            if (images.isNull(id)) {
                null
            } else {
                val imageUrl = images.getString(id)
                imageUrl
            }
        }
    }

    data class FigmaComponent(
        val key: String,
        val nodeId: String,
        val thumbnailUrl: String,
        val name: String,
        val description: String
    )

    fun parseComponents(jsonString: String): List<FigmaComponent> {
        val jObject = JSONObject(jsonString)
        val hasError = jObject.getInt("status") != 200
        val cmpnts: MutableList<FigmaComponent> = arrayListOf();
        Log.d("PARSE", jsonString)
        if (hasError) {
            val error = jObject.getString("err")
            print(error)
        } else {
            val meta = jObject.getJSONObject("meta")
            if (!meta.isNull("components")) {
                val components = meta.getJSONArray("components")
                (0 until components.length()).forEach {
                    val c = components.getJSONObject(it)
                    cmpnts.add(FigmaComponent(
                        c.getString("key"),
                        c.getString("node_id"),
                        c.getString("thumbnail_url"),
                        c.getString("name"),
                        c.getString("description"))
                    )
                }
                Log.d("PARSE", "Size of components is " + cmpnts.size);
            }
        }
        return cmpnts;
    }
}