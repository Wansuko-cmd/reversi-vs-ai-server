package table

import org.jetbrains.exposed.sql.Table

object PlayerModel : Table("players") {
    val id = varchar("id", 36)
    val isAi = bool("is_ai")
    val playerStatus = varchar("room_id", 36).nullable()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
