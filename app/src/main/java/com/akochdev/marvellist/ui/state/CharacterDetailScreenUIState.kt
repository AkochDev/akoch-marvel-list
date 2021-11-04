package com.akochdev.marvellist.ui.state

import com.akochdev.domain.model.CharacterDetailModel

data class CharacterDetailScreenUIState(
    val isLoading: Boolean = false,
    val isEmptyState: Boolean = false,
    val isErrorState: Boolean = false,
    val characterData: CharacterDetailModel? = null,
    val errorMessage: String? = null
)
