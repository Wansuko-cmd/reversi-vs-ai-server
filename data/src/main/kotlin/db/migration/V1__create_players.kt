package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import table.PlayerModel

@Suppress("ClassName", "unused")
class V1__create_players : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction { SchemaUtils.create(PlayerModel) }
    }
}
