package com.truongkhanh.noteapp.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.regex.Matcher
import java.util.regex.Pattern

inline val String?.urlEncoded: String
    get() = if (Charset.isSupported("UTF-8")) {
        URLEncoder.encode(this ?: "", "UTF-8")
    } else {
        // If UTF-8 is not supported, use the default charset.
        @Suppress("deprecation")
        URLEncoder.encode(this ?: "")
    }

inline val String?.urlDecoded: String
    get() = if (Charset.isSupported("UTF-8")) {
        URLDecoder.decode(this ?: "", "UTF-8")
    } else {
        @Suppress("deprecation")
        URLDecoder.decode(this ?: "")
    }

fun String.removeUTFCharacters(): String {
    val p = Pattern.compile("\\\\u(\\p{XDigit}{4})")
    val m = p.matcher(this)
    val buf = StringBuffer(this.length)
    while (m.find()) {
        val ch = Integer.parseInt(m.group(1), 16).toChar().toString()
        m.appendReplacement(buf, Matcher.quoteReplacement(ch))
    }
    m.appendTail(buf)
    return buf.toString()
}