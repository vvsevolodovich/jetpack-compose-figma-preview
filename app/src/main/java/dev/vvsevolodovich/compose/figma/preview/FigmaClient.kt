package dev.vvsevolodovich.compose.figma.preview

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FigmaClient(val fileId: String, val accessToken: String) {

    val client = OkHttpClient();
    val baseUrl = "api.figma.com";

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

    fun getImage(key: String, callback: (String?) -> Unit) {
        val request = Request.Builder()
            .url("https://$baseUrl/v1/images/$fileId?ids=$key&scale=3")
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
}