package com.countjoy.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.core.locale.LocaleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagePickerViewModel @Inject constructor(
    private val localeManager: LocaleManager
) : ViewModel() {
    
    private val _supportedLocales = MutableStateFlow<List<LocaleManager.LocaleInfo>>(emptyList())
    val supportedLocales: StateFlow<List<LocaleManager.LocaleInfo>> = _supportedLocales.asStateFlow()
    
    private val _currentLocale = MutableStateFlow<String>("")
    val currentLocale: StateFlow<String> = _currentLocale.asStateFlow()
    
    init {
        loadSupportedLocales()
    }
    
    private fun loadSupportedLocales() {
        viewModelScope.launch {
            _supportedLocales.value = localeManager.getSupportedLocales()
            _currentLocale.value = localeManager.getCurrentLocale().language
        }
    }
    
    fun changeLanguage(languageCode: String) {
        viewModelScope.launch {
            localeManager.setAppLocale(languageCode)
            _currentLocale.value = languageCode
            // Reload the list to update the selected state
            loadSupportedLocales()
        }
    }
}