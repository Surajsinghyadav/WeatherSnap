package com.suraj.weathersnap.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CityResultsDao {

    @Query("SELECT * FROM CityResultEntity WHERE name LIKE :query || '%' LIMIT 15 ")
    fun localSearch(query: String) : List<CityResultEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveCityResults(cityResultsEntity: List<CityResultEntity>)

}