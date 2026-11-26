package com.liskovsoft.youtubeapi.comments

import com.google.gson.Gson
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper
import java.io.ByteArrayOutputStream
import java.net.URLEncoder

internal object CommentsApiParams {
    private const val COMMENT_ACTION_TEMPLATE: String = "\"actions\":[\"%s\"]"

    fun getCommentsQuery(commentsKey: String): String {
        val chatData = String.format("\"continuation\":\"%s\"", commentsKey)
        return PostDataHelper.createQueryTV(chatData)
    }

    fun getActionQuery(actionKey: String): String {
        return PostDataHelper.createQueryTV(String.format(COMMENT_ACTION_TEMPLATE, actionKey))
    }
    private const val CREATE_COMMENT_TEMPLATE: String = "\"createCommentParams\":\"%s\",\"commentText\":%s"

    /**
     * Query for creating a top-level comment.
     * Authenticated request (rides the global auth headers).
     */
    fun getCreateCommentQuery(videoId: String, commentText: String): String {
        val data =
            String.format(CREATE_COMMENT_TEMPLATE, encodeCreateCommentParams(videoId), Gson().toJson(commentText))
        return PostDataHelper.createQueryTV(data)
    }

    /**
     * Protobuf-encoded params for the create_comment endpoint:
     * { 1: videoId (string), 5: { index: 0 } (empty message), 10: 7 (varint) }.
     * Base64 (padded, standard alphabet), then URL-encoded.
     */
    internal fun encodeCreateCommentParamsForTest(videoId: String): String = encodeCreateCommentParams(videoId)

    private fun encodeCreateCommentParams(videoId: String): String {
        val stream = ByteArrayOutputStream()

        // field 2 (string): videoId
        val videoIdBytes = videoId.toByteArray()
        stream.write(0x12)
        writeVarint(stream, videoIdBytes.size)
        stream.write(videoIdBytes)

        // field 5 (message): empty (index defaults to 0)
        stream.write(0x2A)
        stream.write(0)

        // field 10 (varint): 7
        stream.write(0x50)
        stream.write(7)

        return URLEncoder.encode(encodeBase64(stream.toByteArray()), "UTF-8")
    }

    private fun writeVarint(stream: ByteArrayOutputStream, value: Int) {
        var v = value
        while (true) {
            if (v and 0x7F.inv() == 0) {
                stream.write(v)
                return
            }
            stream.write((v and 0x7F) or 0x80)
            v = v ushr 7
        }
    }

    private fun encodeBase64(bytes: ByteArray): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val out = StringBuilder((bytes.size + 2) / 3 * 4)
        var i = 0
        while (i < bytes.size) {
            val has1 = i + 1 < bytes.size
            val has2 = i + 2 < bytes.size
            val b0 = bytes[i].toInt() and 0xFF
            val b1 = if (has1) bytes[i + 1].toInt() and 0xFF else 0
            val b2 = if (has2) bytes[i + 2].toInt() and 0xFF else 0
            out.append(alphabet[b0 shr 2])
            out.append(alphabet[(b0 and 0x03) shl 4 or (b1 shr 4)])
            out.append(if (has1) alphabet[(b1 and 0x0F) shl 2 or (b2 shr 6)] else '=')
            out.append(if (has2) alphabet[b2 and 0x3F] else '=')
            i += 3
        }
        return out.toString()
    }
}
