package com.yerayyas.spotifymvvmhilt.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yerayyas.spotifymvvmhilt.presentation.components.ArtistItem
import com.yerayyas.spotifymvvmhilt.presentation.model.Artist
import com.yerayyas.spotifymvvmhilt.ui.theme.Black
import com.yerayyas.spotifymvvmhilt.ui.theme.Purple40


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val artists = viewModel.artist.collectAsState()
    val player by viewModel.player.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Text(
            text = "Popular Artists",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )



        LazyRow {
            items(artists.value) {
                ArtistItem(it) {}
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        if (player != null) {
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(Purple40)
            ) {
                Text(text = player?.artist?.name.orEmpty())
            }
        }
    }
}


@Preview
@Composable
private fun ArtistItemPrev() {
    val artist = Artist(
        name = "Pepe",
        "El mejor",
        "https://images.dog.ceo//breeds//retriever-curly//n02099429_121.jpg"
    )
    ArtistItem(artist) {}

}


/*
fun createArtist(db: FirebaseFirestore) {
    val random = (0..10000).random()
    val artist = Artist(name = "Yeray $random", numberOfSongs = random)
    db.collection("artists").add(artist)
        .addOnSuccessListener {
            Log.i("OULLEA", "SUCCESS")
        }
        .addOnFailureListener {
            Log.i("OULLEA", "FAILED")
        }
        .addOnCompleteListener {
            Log.i("OULLEA", "COMPLETED")
        }
}*/
