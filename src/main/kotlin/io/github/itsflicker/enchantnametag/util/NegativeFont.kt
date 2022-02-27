package io.github.itsflicker.enchantnametag.util

/**
 * @author wlys
 * @since 2022/2/26 18:47
 */
enum class NegativeFont(val char: Char, val space: Int, val height: Int) {

    NEG_1('', -1, -3),
    NEG_2('', -2, -4),
    NEG_3('', -3, -5),
    NEG_4('', -4, -6),
    NEG_5('', -5, -7),
    NEG_6('', -6, -8),
    NEG_7('', -7, -9),
    NEG_8('', -8, -10),
    NEG_16('', -16, -18),
    NEG_32('', -32, -34),
    NEG_64('', -64, -66),
    NEG_128('', -128, -130);

    companion object {

        fun getShortestSequenceForLength(length: Int): String {
            val stringBuilder = StringBuilder()
            var i = length

            while (i - 128 > 0) {
                stringBuilder.append(NEG_128.char)
                i -= 128
            }
            while (i - 64 > 0) {
                stringBuilder.append(NEG_64.char)
                i -= 64
            }
            while (i - 32 > 0) {
                stringBuilder.append(NEG_32.char)
                i -= 32
            }
            while (i - 16 > 0) {
                stringBuilder.append(NEG_16.char)
                i -= 16
            }
            while (i - 8 > 0) {
                stringBuilder.append(NEG_8.char)
                i -= 8
            }
            while (i - 7 > 0) {
                stringBuilder.append(NEG_7.char)
                i -= 7
            }
            while (i - 6 > 0) {
                stringBuilder.append(NEG_6.char)
                i -= 6
            }
            while (i - 5 > 0) {
                stringBuilder.append(NEG_5.char)
                i -= 5
            }
            while (i - 4 > 0) {
                stringBuilder.append(NEG_4.char)
                i -= 4
            }
            while (i - 3 > 0) {
                stringBuilder.append(NEG_3.char)
                i -= 3
            }
            while (i - 2 > 0) {
                stringBuilder.append(NEG_2.char)
                i -= 2
            }
            while (i - 1 > 0) {
                stringBuilder.append(NEG_1.char)
                i--
            }
            return stringBuilder.toString()
        }
    }
}