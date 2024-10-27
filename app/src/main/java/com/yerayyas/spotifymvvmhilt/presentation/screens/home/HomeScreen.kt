package com.yerayyas.spotifymvvmhilt.presentation.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yerayyas.spotifymvvmhilt.presentation.components.ArtistsList
import com.yerayyas.spotifymvvmhilt.presentation.components.HeaderTitle
import com.yerayyas.spotifymvvmhilt.presentation.components.PlayerComponent
import com.yerayyas.spotifymvvmhilt.presentation.components.ShowToast
import com.yerayyas.spotifymvvmhilt.presentation.connectivity.ConnectivityViewModel
import com.yerayyas.spotifymvvmhilt.presentation.connectivity.ManageNetworkListening
import com.yerayyas.spotifymvvmhilt.presentation.dialogs.DialogUpdate
import com.yerayyas.spotifymvvmhilt.ui.theme.Black
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun HomeScreen(
    @ApplicationContext context: Context,
    viewModel: HomeViewModel = hiltViewModel(),
    connectivityViewModel: ConnectivityViewModel = hiltViewModel()
) {
    val artists = viewModel.artist.collectAsState()
    val player by viewModel.player.collectAsState()
    val blockVersion by viewModel.blockVersion.collectAsState()

    if (blockVersion) {
        DialogUpdate(context)
    }

    ManageNetworkListening(connectivityViewModel)

    ShowToast(context, connectivityViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        HeaderTitle()
        ArtistsList(artists.value) { artist ->
            viewModel.addPlayer(artist)
        }
        Spacer(modifier = Modifier.weight(1f))
        player?.let { PlayerComponent(it, viewModel::onPlaySelected, viewModel::onCancelSelected) }
        Spacer(modifier = Modifier.height(200.dp))
    }
}

fun navigateToPlayStore(context: Context) {
    val appPackage = context.packageName
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackage")
            )
        )
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
