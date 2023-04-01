package com.example.githubusers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteusers")
class FavoriteUserEntity(
    @PrimaryKey(autoGenerate = false)
    @field:ColumnInfo(name = "login")
    var login: String = "",
    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,
)
