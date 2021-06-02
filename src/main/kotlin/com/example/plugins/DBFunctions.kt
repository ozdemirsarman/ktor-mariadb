package com.example.plugins.DBfunctions

import com.example.plugins.*
import org.jetbrains.exposed.sql.*
import com.google.gson.Gson
import io.ktor.routing.*
import kotlinx.coroutines.handleCoroutineException
import org.jetbrains.exposed.sql.transactions.transaction


object User : Table("users") {
    val id = integer("id")
    val username = varchar("username", length = 50)
    val password = varchar("password",length=50)
    val active = integer("active")
}
data class Users(val id: Int, val username: String, val password: String)

public fun initDB() {
    val url = "jdbc:mysql://root:testtester@localhost:3306/test?useUnicode=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    Database.connect(url, driver)
}


/*

val id= User.insert {

    it[username] = "tiesto"
    it[password] = "tampabay"
    it[active] = 1
}

*/

fun adduser(): String{

    var islemonayi="Veri eklendi."

    transaction {
        val id = User.insert {

            it[username] = "tiestonty"
            it[password] = "tampabay"
            it[active] = 1
        }
    }
 return islemonayi
}

fun createuser(name: String?): String {


var responsetransaction= "User created."
    transaction {
        val id = User.insert {

           it[username] = name.toString()
            it[password] = "tampabay"
            it[active] = 1
        }
    }
return responsetransaction
}


fun createuser2(username1: String?, password1: String?): String {


    var responsetransaction1= "User created."
    transaction {
        val id = User.insert {

            it[username] = username1.toString()
            it[password] = password1.toString()
            it[active] = 1
        }
    }
    return responsetransaction1
}

fun getTopuserData(): String {
    var json: String = ""
    transaction {
        val res = User.selectAll().orderBy(User.id, false).limit(10)
        val c = ArrayList<Users>()
        for (f in res) {
            c.add(Users(id = f[User.id], username = f[User.username], password = f[User.password]))
        }
        json = Gson().toJson(c);
    }
    return json
}

fun getlastuser(): String {
    var json: String = ""

    transaction {
        val res = User.selectAll().orderBy(User.id, isAsc = false).limit(1)
        val c = ArrayList<Users>()
        for (f in res) {
            c.add(Users(id = f[User.id], username = f[User.username], password = f[User.password]))
        }
        json = Gson().toJson(c);
    }
    return json
}


fun usercount(): String {
   var toplamkullanicisayisi: String = ""

    transaction {
        val toplamkullanici = User.selectAll().count()
      toplamkullanicisayisi = toplamkullanici.toString()
    }
    return toplamkullanicisayisi
}

