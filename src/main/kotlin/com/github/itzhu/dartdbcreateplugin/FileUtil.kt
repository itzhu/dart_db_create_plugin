package com.github.itzhu.dartdbcreateplugin

import java.io.*
import java.lang.StringBuilder

object FileUtil {

    /**
     * 单个文件复制
     */
    @JvmStatic
    fun read(inputStream: InputStream): String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            var str: String?
            while (true) {
                str = reader.readLine()
                if (str == null) break
                sb.append(str).append(System.lineSeparator())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
                reader.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }


    /**
     * 单个文件复制
     */
    @JvmStatic
    fun copyFile(inputStream: InputStream, outFile: File) {
        val bis = BufferedInputStream(inputStream)
        val bos = BufferedOutputStream(FileOutputStream(outFile))
        val buf = ByteArray(1024)
        try {
            var len = 0
            while (true) {
                len = bis.read(buf)
                if (len == -1) break
                bos.write(buf, 0, len)
            }
            bos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bos.close()
                bis.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


}