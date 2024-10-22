package com.yerayyas.spotifymvvmhilt.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.yerayyas.spotifymvvmhilt.domain.usecases.CanAccessToAppUseCase
import com.yerayyas.spotifymvvmhilt.presentation.model.Artist
import com.yerayyas.spotifymvvmhilt.presentation.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val canAccessToAppUseCase: CanAccessToAppUseCase,
    private val realtimeDatabase: FirebaseDatabase,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _artist = MutableStateFlow<List<Artist>>(emptyList())
    val artist: StateFlow<List<Artist>> = _artist

    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player

    private val _blockVersion = MutableStateFlow(false)
    val blockVersion: StateFlow<Boolean> = _blockVersion

    init {
        viewModelScope.launch {
            checkUserVersion()
            getArtists()
            getPlayer()
        }
    }

    private suspend fun checkUserVersion() {
        _blockVersion.value = !canAccessToAppUseCase()
    }

    private suspend fun getArtists() {
        _artist.value = runCatching { getAllArtists() }.getOrElse {
            Log.e("HomeViewModel", "Error fetching artists", it)
            emptyList()
        }
    }

    private suspend fun getAllArtists(): List<Artist> {
        return db.collection("artists")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(Artist::class.java) }
    }

    private fun getPlayer() {
        viewModelScope.launch {
            collectPlayer().collect { snapshot ->
                val player = snapshot.getValue(Player::class.java)
                _player.value = player
                Log.i("HomeViewModel", "Player: $player")
            }
        }
    }

    private fun collectPlayer(): Flow<DataSnapshot> = callbackFlow {
        val playerRef = realtimeDatabase.reference.child("player") // Defining the reference

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeViewModel", "Error: ${error.message}")
                close(error.toException())
            }
        }

        playerRef.addValueEventListener(listener)
        awaitClose { playerRef.removeEventListener(listener) }
    }

    fun onPlaySelected() {
        player.value?.let {
            val currentPlayer = it.copy(play = !it.play!!)
            realtimeDatabase.reference.child("player").setValue(currentPlayer)
        }
    }

    fun onCancelSelected() {
        realtimeDatabase.reference.child("player").setValue(null)
    }

    fun addPlayer(artist: Artist) {
        val player = Player(artist = artist, play = true)
        realtimeDatabase.reference.child("player").setValue(player)
    }
}
