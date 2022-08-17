package io.cgisca.godot.gpgs.accountinfo

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.gson.Gson
import io.cgisca.godot.gpgs.ConnectionController
import io.cgisca.godot.gpgs.model.PlayerInfo
import io.cgisca.godot.gpgs.model.PlayerLevel
import io.cgisca.godot.gpgs.model.PlayerLevelInfo

class PlayerInfoController(
    private val activity: Activity,
    private val playerInfoListener: PlayerInfoListener,
    private val connectionController: ConnectionController
) {

    fun fetchPlayerInfo() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity)
        if (connectionController.isConnected().first && googleSignInAccount != null) {
            Games.getPlayersClient(activity, googleSignInAccount).currentPlayer
                .addOnCompleteListener { task ->
                    val player = task.result
                    if (task.isSuccessful && player != null) {
                        playerInfoListener.onPlayerInfoLoaded(Gson().toJson(player))
                    } else {
                        playerInfoListener.onPlayerInfoLoadingFailed()
                    }
                }
        } else {
            playerInfoListener.onPlayerInfoLoadingFailed()
        }
    }
}