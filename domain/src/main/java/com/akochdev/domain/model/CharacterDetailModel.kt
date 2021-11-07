package com.akochdev.domain.model

data class CharacterDetailModel(
    val id: String,
    val name: String,
    val description: String,
    val pictureUrl: String,
    val comics: List<String>,
    val series: List<String>,
    val stories: List<String>,
    val detailUrl: String?,
    val comicLinkUrl: String?,
    val wikiLinkUrl: String?,
)
