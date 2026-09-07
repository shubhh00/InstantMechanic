package com.app.instantmechanic.video

import android.content.Context
import android.view.SurfaceView
import com.app.instantmechanic.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface VideoCallEvent {

    data class LocalUserJoined(
        val uid: Int
    ) : VideoCallEvent

    data class RemoteUserJoined(
        val uid: Int
    ) : VideoCallEvent

    data class RemoteUserLeft(
        val uid: Int
    ) : VideoCallEvent

    data class Error(
        val code: Int
    ) : VideoCallEvent
}

@Singleton
class AgoraVideoClient @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private var rtcEngine: RtcEngine? = null

    private val _events = MutableSharedFlow<VideoCallEvent>(
        extraBufferCapacity = 10
    )

    val events: SharedFlow<VideoCallEvent> =
        _events.asSharedFlow()

    private val eventHandler =
        object : IRtcEngineEventHandler() {

            override fun onJoinChannelSuccess(
                channel: String?,
                uid: Int,
                elapsed: Int
            ) {
                _events.tryEmit(
                    VideoCallEvent.LocalUserJoined(uid)
                )
            }

            override fun onUserJoined(
                uid: Int,
                elapsed: Int
            ) {
                _events.tryEmit(
                    VideoCallEvent.RemoteUserJoined(uid)
                )
            }

            override fun onUserOffline(
                uid: Int,
                reason: Int
            ) {
                _events.tryEmit(
                    VideoCallEvent.RemoteUserLeft(uid)
                )
            }

            override fun onError(errorCode: Int) {
                _events.tryEmit(
                    VideoCallEvent.Error(errorCode)
                )
            }
        }

    fun initialize() {
        if (rtcEngine != null) {
            return
        }

        check(BuildConfig.AGORA_APP_ID.isNotBlank()) {
            "AGORA_APP_ID is missing from local.properties"
        }

        val configuration = RtcEngineConfig().apply {
            mContext = appContext
            mAppId = BuildConfig.AGORA_APP_ID
            mEventHandler = eventHandler
        }

        rtcEngine = RtcEngine.create(configuration).apply {
            setChannelProfile(
                Constants.CHANNEL_PROFILE_COMMUNICATION
            )

            enableVideo()
        }
    }

    fun setupLocalVideo(surfaceView: SurfaceView) {
        val engine = requireNotNull(rtcEngine) {
            "Agora must be initialized before setting up video"
        }

        engine.setupLocalVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                LOCAL_VIDEO_CANVAS_UID
            )
        )

        engine.startPreview()
    }

    fun setupRemoteVideo(
        surfaceView: SurfaceView,
        remoteUid: Int
    ) {
        val engine = requireNotNull(rtcEngine) {
            "Agora must be initialized before setting up video"
        }

        engine.setupRemoteVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                remoteUid
            )
        )
    }

    fun joinChannel(
        channelName: String,
        uid: Int,
        token: String? = null
    ) {
        val engine = requireNotNull(rtcEngine) {
            "Agora must be initialized before joining a channel"
        }

        val mediaOptions = ChannelMediaOptions().apply {
            clientRoleType =
                Constants.CLIENT_ROLE_BROADCASTER

            publishCameraTrack = true
            publishMicrophoneTrack = true
            autoSubscribeAudio = true
            autoSubscribeVideo = true
        }

        val result = engine.joinChannel(
            token,
            channelName,
            uid,
            mediaOptions
        )

        if (result < 0) {
            _events.tryEmit(
                VideoCallEvent.Error(result)
            )
        }
    }

    fun setMicrophoneEnabled(enabled: Boolean) {
        rtcEngine?.muteLocalAudioStream(!enabled)
    }

    fun setCameraEnabled(enabled: Boolean) {
        rtcEngine?.enableLocalVideo(enabled)

        if (enabled) {
            rtcEngine?.startPreview()
        }
    }

    fun switchCamera() {
        rtcEngine?.switchCamera()
    }

    fun leaveChannel() {
        rtcEngine?.stopPreview()
        rtcEngine?.leaveChannel()
    }

    fun release() {
        val engineExists = rtcEngine != null

        if (!engineExists) {
            return
        }

        leaveChannel()
        RtcEngine.destroy()
        rtcEngine = null
    }

    private companion object {
        const val LOCAL_VIDEO_CANVAS_UID = 0
    }
}