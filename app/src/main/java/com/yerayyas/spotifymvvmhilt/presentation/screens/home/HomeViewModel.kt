package com.yerayyas.spotifymvvmhilt.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yerayyas.spotifymvvmhilt.domain.usecases.CanAccessToAppUseCase
import com.yerayyas.spotifymvvmhilt.presentation.model.Artist
import com.yerayyas.spotifymvvmhilt.presentation.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
        /* repeat(20){
             loadData()
         }*/
        checkUserVersion()
        getArtists()
        getPlayer()
    }

    private fun checkUserVersion() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                canAccessToAppUseCase()
            }
            _blockVersion.value = !result
        }
    }

    private fun getPlayer() {
        viewModelScope.launch {
            collectPlayer().collect { snapshot ->
                val player = snapshot.getValue(Player::class.java)
                _player.value = player
                Log.i("OULLEA", "Player: $player")
            }
        }
    }

    /*   private fun loadData() {
           val random = (0..10000).random()
           val artist = Artist(
               name = "Random $random",
               description = "Random description number $random",
               image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwyXeKDN29AmZgZPLS7n0Bepe8QmVappBwZCeA3XWEbWNdiDFB")
           db.collection("artists").add(artist)
       }*/

    private fun getArtists() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getAllArtists()
            }
            _artist.value = result
        }
    }

    private suspend fun getAllArtists(): List<Artist> {
        return try {
            db.collection("artists")
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    snapshot.toObject(Artist::class.java)
                }
        } catch (e: Exception) {
            Log.i("OULLEA", e.toString())
            emptyList()
        }
    }

    private fun collectPlayer(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("OULLEA", "Error: ${error.message}")
                close() // Si queremos que pete, podemos incluir error.toException() dentro de los parámetros de close
            }
        }

        val ref = realtimeDatabase.reference.child("player")
        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    fun onPlaySelected() {
        if (player.value != null) {
            val currentPlayer = _player.value?.copy(play = !player.value?.play!!)
            val ref = realtimeDatabase.reference.child("player")
            ref.setValue(currentPlayer)
        }
    }

    fun onCancelSelected() {
        val ref = realtimeDatabase.reference.child("player")
        ref.setValue(null)

    }

    fun addPlayer(artist: Artist) {
        val ref = realtimeDatabase.reference.child("player")
        val player = Player(artist = artist, play = true)
        ref.setValue(player)
    }
}

