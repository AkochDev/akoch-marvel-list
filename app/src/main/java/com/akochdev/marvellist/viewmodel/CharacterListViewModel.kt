package com.akochdev.marvellist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akochdev.domain.model.Result
import com.akochdev.domain.model.Result.Success
import com.akochdev.marvellist.ui.state.CharacterListScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.akochdev.domain.usecase.CharacterListUseCase
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val useCase: CharacterListUseCase
) : ViewModel() {

    private val _characterList: MutableLiveData<CharacterListScreenUIState> =
        MutableLiveData(CharacterListScreenUIState(isLoading = true))

    fun fetchCharacterList(): LiveData<CharacterListScreenUIState> {
        viewModelScope.launch {
            val result = useCase.getMarvelCharacters(100, 0) // TODO add pagination
            _characterList.postValue(
                when {
                    result is Success && result.value != null -> {
                        CharacterListScreenUIState(characterData = result.value)
                    }
                    result is Success && result.value == null -> {
                        CharacterListScreenUIState(isEmptyState = true)
                    }
                    else -> {
                        val failure = result as? Result.Failure
                        CharacterListScreenUIState(
                            isErrorState = true,
                            errorMessage = "${failure?.msg ?: ""} ${failure?.cause?.localizedMessage ?: ""}"  // TODO check if we need to use different ui per error type
                        )
                    }
                }
            )
        }
        return _characterList
    }
}
