package com.suraj.weathersnap.data.mapper

import com.suraj.weathersnap.data.local.CityResult
import com.suraj.weathersnap.data.local.CityResultEntity
import com.suraj.weathersnap.data.local.ReportsEntity
import com.suraj.weathersnap.data.local.SavedReport

fun SavedReport.toEntity() : ReportsEntity{
return ReportsEntity(
    id = this.id,
    city = this.city,
    country = this.country,
    condition = this.condition,
    temperature = this.temperature,
    date = this.date,
    originalSizeKb = this.originalSizeKb,
    compressedSizeKb = this.compressedSizeKb,
    reportNotes = this.reportNotes,
    imagePath = this.imagePath
)
}


fun ReportsEntity.toExternalModel() : SavedReport{
    return SavedReport(
        id = this.id,
        city = this.city,
        country = this.country,
        condition = this.condition,
        temperature = this.temperature,
        date = this.date,
        originalSizeKb = this.originalSizeKb,
        compressedSizeKb = this.compressedSizeKb,
        reportNotes = this.reportNotes,
        imagePath = this.imagePath
    )
}


fun CityResultEntity.toExternalModel() : CityResult{
    return  CityResult(
        id = this.id,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        country = this.country,
    )
}

fun CityResult.toEntity() : CityResultEntity{
    return CityResultEntity(
        id = this.id,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        country = this.country,
    )
}
