package com.akochdev.marvellist.ui.state

import com.akochdev.domain.model.CharacterListModel

data class CharacterListScreenUIState(
    val isLoading: Boolean = false,
    val isEmptyState: Boolean = false,
    val isErrorState: Boolean = false,
    val isErrorLoadingMore: Boolean = false,
    val currentPage: Int = 0,
    val characterData: List<CharacterListModel>? = null,
    val errorMessage: String? = null
)
