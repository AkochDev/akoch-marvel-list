package com.akochdev.marvellist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import com.akochdev.domain.model.Result
import com.akochdev.domain.model.Result.Success
import com.akochdev.domain.usecase.CharacterDetailUseCase
import com.akochdev.marvellist.ui.state.CharacterDetailScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val useCase: CharacterDetailUseCase
) : ViewModel() {

    private val _characterDetail: MutableLiveData<CharacterDetailScreenUIState> =
        MutableLiveData(CharacterDetailScreenUIState(isLoading = true))
    val characterDetail: LiveData<CharacterDetailScreenUIState> = _characterDetail

    @InternalCoroutinesApi
    fun fetchCharacterDetails(characterId: String?) {
        if (characterId == null) {
            _characterDetail.postValue(CharacterDetailScreenUIState(isEmptyState = true))
            return
        }
        viewModelScope.launch {
            useCase.getMarvelCharacterDetails(characterId)
                .collect { result ->
                    _characterDetail.postValue(
                        when {
                            result is Success && result.value != null -> CharacterDetailScreenUIState(
                                characterData = result.value
                            )
                            result is Success && result.value == null -> CharacterDetailScreenUIState(
                                isEmptyState = true
                            )
                            else -> {
                                val failure = result as? Result.Failure
                                CharacterDetailScreenUIState(
                                    isErrorState = true,
                                    errorMessage = "${failure?.msg ?: ""} ${failure?.cause?.localizedMessage ?: ""}"
                                )
                            }
                        }
                    )
                }
        }
    }
}
