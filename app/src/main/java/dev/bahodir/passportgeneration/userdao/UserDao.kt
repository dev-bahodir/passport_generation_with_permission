package dev.bahodir.passportgeneration.userdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.bahodir.passportgeneration.user.User

@Dao
interface UserDao {

    @Insert fun addUser(user: User)

    @Query("SELECT * FROM USER") fun getUser(): MutableList<User>
}