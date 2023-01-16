package com.pranay.newsapp.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromSource(source: com.pranay.newsapp.model.Source): String {
        return  source.name
    }

    @TypeConverter
    fun toSource(name: String): com.pranay.newsapp.model.Source {
        return com.pranay.newsapp.model.Source(name, name)
    }

}