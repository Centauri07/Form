package me.centauri07.form.field.input.reader

class ByteReader: InputReader<Byte>(Byte::class.java) {
    override val converter: (String) -> Byte = String::toByte
}

class ShortReader: InputReader<Short>(Short::class.java) {
    override val converter: (String) -> Short = String::toShort
}

class IntReader: InputReader<Int>(Int::class.java) {
    override val converter: (String) -> Int = String::toInt
}

class LongReader: InputReader<Long>(Long::class.java) {
    override val converter: (String) -> Long = String::toLong
}

class FloatReader: InputReader<Float>(Float::class.java) {
    override val converter: (String) -> Float = String::toFloat
}

class DoubleReader: InputReader<Double>(Double::class.java) {
    override val converter: (String) -> Double = String::toDouble
}

class BooleanReader: InputReader<Boolean>(Boolean::class.java) {
    override val converter: (String) -> Boolean = String::toBoolean
}

class CharReader: InputReader<Char>(Char::class.java) {
    override val converter: (String) -> Char? = {
        if (it.length > 1) null
        else it[0]
    }
}