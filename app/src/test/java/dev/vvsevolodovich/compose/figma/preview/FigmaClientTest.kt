package dev.vvsevolodovich.compose.figma.preview

import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FigmaClientTest {
    @Test
    fun shouldParseImageUrlCorrectly() {
        val figmaClient = FigmaClient("d51xtGzfqgz1LXyZPDnP1E","162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d")
        val latch = CountDownLatch(1)
        val expected =
            "https://s3-us-west-2.amazonaws.com/figma-alpha-api/img/fe7b/508c/e06bc9ae2aa905af08dee256a7ca2ae4"

        var targetUrl: String? = null
        figmaClient.getImage("1:405") {
            targetUrl = it
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)
        assertTrue("Url is incorrect", expected == targetUrl)
    }

    @Test
    fun shouldHandleFigmaError() {
        val figmaClient = FigmaClient("d51xtGzfqgz1LXyZPDnP1E","162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d")
        val latch = CountDownLatch(1)

        var targetUrl: String? = "123"
        figmaClient.getImage("1:0") {
            targetUrl = it
            latch.countDown()
        }
        latch.await(60, TimeUnit.SECONDS)
        assertNull(targetUrl)
    }
}