package com.yerayyas.spotifymvvmhilt.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yerayyas.spotifymvvmhilt.R
import com.yerayyas.spotifymvvmhilt.presentation.model.Player
import com.yerayyas.spotifymvvmhilt.ui.theme.Purple40

@Composable
fun PlayerComponent(
    player: Player?,
    onPlaySelected: () -> Unit,
    onCancelSelected: () -> Unit
) {
    val icon = if (player?.play == true) R.drawable.ic_pause else R.drawable.ic_play

    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(Purple40),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = player?.artist?.name.orEmpty(),
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Play/Pause icon",
            modifier = Modifier
                .size(40.dp)
                .clickable { onPlaySelected() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "Close icon",
            modifier = Modifier
                .size(40.dp)
                .clickable { onCancelSelected() }
        )
    }
}