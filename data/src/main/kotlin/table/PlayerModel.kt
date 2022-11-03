package table

import org.jetbrains.exposed.sql.Table

object PlayerModel : Table("users") {
    val id = varchar("id", 36)
    val isAi = bool("is_ai")
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
