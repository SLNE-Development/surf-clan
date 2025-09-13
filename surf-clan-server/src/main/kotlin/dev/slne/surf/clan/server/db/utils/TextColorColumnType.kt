package dev.slne.surf.clan.server.db.utils

import net.kyori.adventure.text.format.TextColor
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.currentDialect
import java.nio.ByteBuffer

class TextColorColumnType : ColumnType<TextColor>() {
    override fun sqlType() = currentDialect.dataTypeProvider.integerType()

    override fun valueFromDB(value: Any): TextColor = when (value) {
        is Int -> TextColor.color(value)
        is String -> TextColor.color(value.toInt())
        is ByteArray -> TextColor.color(value.decodeToString().toInt())
        is ByteBuffer -> TextColor.color(value.array().decodeToString().toInt())
        else -> error("Unexpected value of type TextColor: $value of ${value::class.qualifiedName}")
    }

    override fun notNullValueToDB(value: TextColor): Any = value.value()
}

fun Table.textColor(name: String): Column<TextColor> = registerColumn(name, TextColorColumnType())