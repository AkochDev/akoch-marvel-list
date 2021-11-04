package com.akochdev.marvellist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akochdev.domain.model.Result
import com.akochdev.domain.model.Result.Success
import com.akochdev.domain.usecase.CharacterListUseCase
import com.akochdev.marvellist.ui.state.CharacterListScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NO_MESSAGE = ""

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val useCase: CharacterListUseCase
) : ViewModel() {

    private var listScrollPosition = 0
    private var currentPage = 1
    private var isLoadingNextPage = false

    private val _characterList: MutableLiveData<CharacterListScreenUIState> =
        MutableLiveData(CharacterListScreenUIState(isLoading = true))
    val characterList: LiveData<CharacterListScreenUIState> = _characterList

    init {
        fetchCharacterList()
    }

    fun fetchCharacterList() {
        viewModelScope.launch {
            val result = useCase.getMarvelCharacters(PAGE_SIZE)
            _characterList.postValue(
                when {
                    result is Success && result.value != null -> {
                        CharacterListScreenUIState(
                            characterData = result.value,
                            currentPage = currentPage
                        )
                    }
                    result is Success && result.value == null -> {
                        CharacterListScreenUIState(isEmptyState = true)
                    }
                    else -> {
                        val failure = result as? Result.Failure
                        CharacterListScreenUIState(
                            isErrorState = true,
                            errorMessage = "${failure?.msg ?: NO_MESSAGE} ${failure?.cause?.localizedMessage ?: NO_MESSAGE}"
                        )
                    }
                }
            )
        }
    }

    fun loadMore() {
        if ((listScrollPosition + 1) >= (currentPage * PAGE_SIZE)) {
            isLoadingNextPage = true
            currentPage++

            val oldData = _characterList.value
            _characterList.postValue(
                CharacterListScreenUIState(
                    isLoading = true,
                    currentPage = currentPage,
                    characterData = oldData?.characterData
                )
            )

            viewModelScope.launch {
                val result = useCase.getMarvelCharacters(
                    limit = PAGE_SIZE,
                    offset = (currentPage - 1) * PAGE_SIZE
                )
                if (result is Success && result.value != null) { // we don't really care if this call fails or brings no data
                    _characterList.postValue(
                        CharacterListScreenUIState(
                            isLoading = false,
                            currentPage = currentPage,
                            characterData = oldData?.characterData?.plus(result.value ?: listOf())
                        )
                    )
                }
            }
        }
    }

    fun onChangeScrollPosition(position: Int) {
        listScrollPosition = position
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}
