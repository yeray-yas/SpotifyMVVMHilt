package com.yerayyas.spotifymvvmhilt.presentation.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.yerayyas.spotifymvvmhilt.presentation.model.Artist

@Composable
fun ArtistsList(artists: List<Artist>, onItemSelected: (Artist) -> Unit) {
    LazyRow {
        items(artists) { artist ->
            ArtistItem(artist = artist, onItemSelected = onItemSelected)
        }
    }
}