package dev.bahodir.passportgeneration.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "surname")
    var surname: String? = null,
    @ColumnInfo(name = "middle_name")
    var middle_name: String? = null,
    @ColumnInfo(name = "region")
    var region: String? = null,
    @ColumnInfo(name = "district")
    var district: String? = null,
    @ColumnInfo(name = "home_address")
    var home_address: String? = null,
    @ColumnInfo(name = "receipt_passport")
    var receipt_passport: String? = null,
    @ColumnInfo(name = "passport_term")
    var passport_term: String? = null,
    @ColumnInfo(name = "serial_number")
    var serial_number: String? = null,
    @ColumnInfo(name = "gender")
    var gender: String? = null,
    @ColumnInfo(name = "photo_path")
    var photo_path: String? = null
) : Serializable