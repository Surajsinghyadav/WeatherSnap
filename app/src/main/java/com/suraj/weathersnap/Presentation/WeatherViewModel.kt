package com.suraj.weathersnap.Presentation

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import com.suraj.weathersnap.data.local.AppDatabase
import com.suraj.weathersnap.data.local.CityResult
import com.suraj.weathersnap.data.local.PhotoProperties
import com.suraj.weathersnap.data.local.ReportsEntity
import com.suraj.weathersnap.data.local.SavedReport
import com.suraj.weathersnap.data.local.WeatherData
import com.suraj.weathersnap.data.mapper.toEntity
import com.suraj.weathersnap.data.repository.WeatherRepository
import com.suraj.weathersnap.toWeatherCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {


    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Ideal)
    val searchUiState = _searchUiState.asStateFlow()
    private val _weatherCardUiState = MutableStateFlow<WeatherCardUiState>(WeatherCardUiState.Ideal)

    val weatherCardUiState = _weatherCardUiState.asStateFlow()



    private val appDatabase = AppDatabase.getDatabase(application)
    private val reportsDao =appDatabase.reportsDao()


        private val weatherRepository = WeatherRepository()
    private val _cityQuery = MutableStateFlow("")
    val cityQuery: StateFlow<String> = _cityQuery.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

//    private val _weather = MutableStateFlow<WeatherData?>(WeatherData())
//    val weather: StateFlow<WeatherData?> = _weather.asStateFlow()

    private val _photoProperties = MutableStateFlow<PhotoProperties?>(null)
    val PhotoProperties = _photoProperties.asStateFlow()

    fun updatePhotoProperties(imagePath: String, originalKb:Int, compressedKb: Int ){
        _photoProperties.value = PhotoProperties(
            imagePath,
            originalKb,
            compressedKb
        )
    }

    fun clearReportState(){
        _photoProperties.value = null
        _fieldNotes.value = ""
    }



    val savedReports: StateFlow<List<ReportsEntity>> = reportsDao.getAllReports()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val totalReportCount: StateFlow<Int> = reportsDao.getTotalReportCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private val _fieldNotes = MutableStateFlow("")
    val fieldNotes: StateFlow<String> = _fieldNotes.asStateFlow()


    fun onCityQueryChange(query: String) {
        viewModelScope.launch {
            _cityQuery.value = query
            if (query.length > 2) {
                _searchUiState.value = SearchUiState.Loading
                try {
                    val result = weatherRepository.searchCities(query)
                    if (result.isEmpty()) {
                        _searchUiState.value = SearchUiState.Error("No match found for $query")
                    } else {
                        _searchUiState.value = SearchUiState.Success(
                            cityResult = result
                        )
                    }

                } catch (e: Exception) {
                    _searchUiState.value = SearchUiState.Error("Error")
//                    emptyList()
                }
            }

        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
        _cityQuery.value = ""
        _searchUiState.value = SearchUiState.Ideal
    }


    fun onSearch(lat: Double, long: Double, city: String, country: String) {
        if (_cityQuery.value.isNotBlank()) {
            viewModelScope.launch {
                _weatherCardUiState.value = WeatherCardUiState.Loading

                try {
                    val result = weatherRepository.getWeather(lat,long)
                    _weatherCardUiState.value = WeatherCardUiState.Success(
                        WeatherData(
                            city = city,
                            country = country,
                            condition = result.current.weatherCode.toWeatherCondition(),
                            temperature = result.current.temperature.toInt(),
                            humidity =  result.current.humidity,
                            windSpeed = result.current.windSpeed,
                            pressure = result.current.pressure.toInt()
                        )
                    )
                    clearSuggestions()

                }catch (e: Exception){
                    val error = e.message ?: "Something Went Wrong"
                    _weatherCardUiState.value = WeatherCardUiState.Error(error)
                }

            }

        }
    }

    fun onFieldNotesChange(notes: String) {
        _fieldNotes.value = notes
    }

    fun saveReport(report: SavedReport){
        viewModelScope.launch {
            reportsDao.saveReport(report.toEntity())
        }
    }

}

sealed class WeatherCardUiState{
    object Ideal: WeatherCardUiState()
    object Loading : WeatherCardUiState()
    class Error(val error: String) : WeatherCardUiState()
    class Success(val weatherData: WeatherData) : WeatherCardUiState()
}


sealed class SearchUiState {
    object Ideal : SearchUiState()
    object Loading : SearchUiState()
    data class Success(
        val cityResult: List<CityResult>
        ) : SearchUiState()
    data class Error(val e: String) : SearchUiState()

}


