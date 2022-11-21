package com.zjl.base.utils.ext

import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.ByteString.Companion.toByteString
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * 返回文件的MD5值
 * @param base64 是否将md5值进行base64编码, 否则将返回hex编码
 */
fun File.md5(base64: Boolean = false): String? {
    try {
        val fileInputStream = FileInputStream(this)
        val digestInputStream = DigestInputStream(fileInputStream, MessageDigest.getInstance("MD5"))
        val buffer = ByteArray(1024 * 256)
        digestInputStream.use {
            while (true) if (digestInputStream.read(buffer) <= 0) break
        }
        val md5 = digestInputStream.messageDigest.digest()
        return if (base64) {
            md5.toByteString().base64()
        } else {
            md5.toByteString().hex()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

/**
 * 返回文件的MediaType值, 如果不存在返回null
 */
fun File.mediaType(): MediaType? {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(absolutePath)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)?.toMediaTypeOrNull()
}