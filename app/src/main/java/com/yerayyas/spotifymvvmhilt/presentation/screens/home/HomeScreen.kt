package com.yerayyas.spotifymvvmhilt.presentation.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yerayyas.spotifymvvmhilt.presentation.components.ArtistItem
import com.yerayyas.spotifymvvmhilt.presentation.components.PlayerComponent
import com.yerayyas.spotifymvvmhilt.presentation.dialogs.DialogUpdate
import com.yerayyas.spotifymvvmhilt.presentation.model.Artist
import com.yerayyas.spotifymvvmhilt.ui.theme.Black

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val artists = viewModel.artist.collectAsState()
    val player by viewModel.player.collectAsState()
    val blockVersion by viewModel.blockVersion.collectAsState()

    if (blockVersion) {
        val context = LocalContext.current
        DialogUpdate(context)
    }

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
                ArtistItem(
                    artist = it,
                    onItemSelected = {viewModel.addPlayer(it)}
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (player != null) {
            player?.let {
                PlayerComponent(
                    player = it,
                    onPlaySelected = { viewModel.onPlaySelected() },
                    onCancelSelected = { viewModel.onCancelSelected() }

                )
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
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



fun navigateToPlayStore(context: Context) {
    val appPackage = context.packageName
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")))
    } catch (e: Exception) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
            )
        )
    }
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
