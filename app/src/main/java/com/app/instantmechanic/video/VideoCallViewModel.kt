package com.app.instantmechanic.video

import android.view.SurfaceView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.app.instantmechanic.BuildConfig

enum class VideoCallPhase {
    PREPARING,
    JOINING,
    WAITING_FOR_MECHANIC,
    CONNECTED,
    ENDED,
    ERROR
}

data class VideoCallUiState(
    val phase: VideoCallPhase = VideoCallPhase.PREPARING,
    val remoteUid: Int? = null,
    val isMicrophoneEnabled: Boolean = true,
    val isCameraEnabled: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class VideoCallViewModel @Inject constructor(
    private val videoClient: AgoraVideoClient
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(VideoCallUiState())

    val uiState: StateFlow<VideoCallUiState> =
        _uiState.asStateFlow()

    init {
        observeAgoraEvents()
        initializeAgora()
    }

    private fun initializeAgora() {
        try {
            videoClient.initialize()
        } catch (exception: Exception) {
            moveToError(
                exception.message
                    ?: "Unable to initialize video calling"
            )
        }
    }

    private fun observeAgoraEvents() {
        viewModelScope.launch {
            videoClient.events.collect { event ->
                handleAgoraEvent(event)
            }
        }
    }

    fun setupLocalVideo(surfaceView: SurfaceView) {
        try {
            videoClient.setupLocalVideo(surfaceView)
        } catch (exception: Exception) {
            moveToError(
                exception.message
                    ?: "Unable to start camera preview"
            )
        }
    }

    fun setupRemoteVideo(
        surfaceView: SurfaceView,
        remoteUid: Int
    ) {
        try {
            videoClient.setupRemoteVideo(
                surfaceView = surfaceView,
                remoteUid = remoteUid
            )
        } catch (exception: Exception) {
            moveToError(
                exception.message
                    ?: "Unable to display mechanic video"
            )
        }
    }

    fun startCall() {
        val currentPhase = _uiState.value.phase

        if (
            currentPhase == VideoCallPhase.JOINING ||
            currentPhase == VideoCallPhase.WAITING_FOR_MECHANIC ||
            currentPhase == VideoCallPhase.CONNECTED
        ) {
            return
        }

        _uiState.update {
            it.copy(
                phase = VideoCallPhase.JOINING,
                errorMessage = null
            )
        }

        try {
            videoClient.joinChannel(
                channelName = DEMO_CHANNEL_NAME,
                uid = BuildConfig.AGORA_RTC_UID,
                token = BuildConfig.AGORA_TEMP_TOKEN.takeIf {
                    it.isNotBlank()
                }
            )
        } catch (exception: Exception) {
            moveToError(
                exception.message
                    ?: "Unable to join video consultation"
            )
        }
    }

    fun toggleMicrophone() {
        val microphoneEnabled =
            !_uiState.value.isMicrophoneEnabled

        videoClient.setMicrophoneEnabled(
            microphoneEnabled
        )

        _uiState.update {
            it.copy(
                isMicrophoneEnabled = microphoneEnabled
            )
        }
    }

    fun toggleCamera() {
        val cameraEnabled =
            !_uiState.value.isCameraEnabled

        videoClient.setCameraEnabled(cameraEnabled)

        _uiState.update {
            it.copy(
                isCameraEnabled = cameraEnabled
            )
        }
    }

    fun switchCamera() {
        if (_uiState.value.isCameraEnabled) {
            videoClient.switchCamera()
        }
    }

    fun endCall() {
        videoClient.leaveChannel()

        _uiState.update {
            it.copy(
                phase = VideoCallPhase.ENDED,
                remoteUid = null
            )
        }
    }

    private fun handleAgoraEvent(event: VideoCallEvent) {
        when (event) {
            is VideoCallEvent.LocalUserJoined -> {
                _uiState.update {
                    it.copy(
                        phase = VideoCallPhase.WAITING_FOR_MECHANIC,
                        errorMessage = null
                    )
                }
            }

            is VideoCallEvent.RemoteUserJoined -> {
                _uiState.update {
                    it.copy(
                        phase = VideoCallPhase.CONNECTED,
                        remoteUid = event.uid,
                        errorMessage = null
                    )
                }
            }

            is VideoCallEvent.RemoteUserLeft -> {
                _uiState.update {
                    it.copy(
                        phase = VideoCallPhase.WAITING_FOR_MECHANIC,
                        remoteUid = null
                    )
                }
            }

            is VideoCallEvent.Error -> {
                moveToError(
                    "Video call failed with code ${event.code}"
                )
            }
        }
    }

    private fun moveToError(message: String) {
        _uiState.update {
            it.copy(
                phase = VideoCallPhase.ERROR,
                errorMessage = message
            )
        }
    }

    override fun onCleared() {
        videoClient.release()
        super.onCleared()
    }

    private companion object {
        const val DEMO_CHANNEL_NAME =
            "instant_mechanic_consultation"
    }
}