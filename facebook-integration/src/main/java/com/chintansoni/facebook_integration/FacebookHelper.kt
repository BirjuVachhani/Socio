package com.chintansoni.facebook_integration

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object FacebookHelper {

    init {
        Timber.plant(Timber.DebugTree())
    }


//
//    fun getAlbums(onApiStatus: (AlbumApiStatus) -> Unit) {
//        onApiStatus.invoke(AlbumApiStatus.Loading)
//        val request = GraphRequest.newGraphPathRequest(
//            AccessToken.getCurrentAccessToken(),
//            "/me/albums"
//        ) { graphResponse ->
//            graphResponse.error?.exception?.let {
//                onApiStatus.invoke(AlbumApiStatus.Failure(it))
//            }
//            graphResponse.rawResponse?.let {
//                val albumsResponse =
//                    Gson().fromJson(graphResponse.rawResponse, AlbumsResponse::class.java)
//                onApiStatus.invoke(AlbumApiStatus.Success(albumsResponse))
//            }
//        }
//        val parameters = Bundle()
//        parameters.putString("fields", "id,name,count")
//        request.parameters = parameters
//        request.executeAsync()
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
//    }
//
//    fun getAlbumCoverPhoto(dataItem: DataItem, onCoverApi: () -> Unit) {
//        if (dataItem.albumCoverApiStatus == AlbumCoverApiStatus.Idle) {
//            dataItem.albumCoverApiStatus = AlbumCoverApiStatus.Loading
//            val params = Bundle()
//            params.putString("type", "album")
//            params.putBoolean("redirect", false)
//            GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/${dataItem.id}/picture",
//                params,
//                HttpMethod.GET,
//                GraphRequest.Callback { graphResponse ->
//                    graphResponse.error?.exception?.let {
//                        dataItem.albumCoverApiStatus = AlbumCoverApiStatus.Failure(it)
//                    }
//                    graphResponse.rawResponse?.let {
//                        val albumPictureResponse =
//                            Gson().fromJson(
//                                graphResponse.rawResponse,
//                                AlbumPictureResponse::class.java
//                            )
//                        dataItem.url = albumPictureResponse.data.url
//                        dataItem.albumCoverApiStatus =
//                            AlbumCoverApiStatus.Success(albumPictureResponse)
//                        onCoverApi()
//                    }
//                }
//            ).executeAsync()
//        }
//    }
//
//    fun getAlbumImages(dataItem: DataItem, onCoverApi: () -> Unit) {
//        if (dataItem.albumCoverApiStatus == AlbumCoverApiStatus.Idle) {
//            dataItem.albumCoverApiStatus = AlbumCoverApiStatus.Loading
//            val params = Bundle()
//            params.putString("type", "album")
//            params.putBoolean("redirect", false)
//            GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/${dataItem.id}/picture",
//                params,
//                HttpMethod.GET,
//                GraphRequest.Callback { graphResponse ->
//                    graphResponse.error?.exception?.let {
//                        dataItem.albumCoverApiStatus = AlbumCoverApiStatus.Failure(it)
//                    }
//                    graphResponse.rawResponse?.let {
//                        val albumPictureResponse =
//                            Gson().fromJson(
//                                graphResponse.rawResponse,
//                                AlbumPictureResponse::class.java
//                            )
//                        dataItem.url = albumPictureResponse.data.url
//                        dataItem.albumCoverApiStatus =
//                            AlbumCoverApiStatus.Success(albumPictureResponse)
//                        onCoverApi()
//                    }
//                }
//            ).executeAsync()
//        }
//    }
//
//    fun getUserImages(onUserImagesResponse: (UserImagesResponse) -> Unit) {
//        val request = GraphRequest.newGraphPathRequest(
//            AccessToken.getCurrentAccessToken(),
//            "/me/photos"
//        ) {
//            userImagesGraphResponse = it
//            val response = Gson().fromJson(it.rawResponse, UserImagesResponse::class.java)
//            onUserImagesResponse(response)
//        }
//
//        val parameters = Bundle()
//        parameters.putString("fields", "images")
//        parameters.putString("limit", "20")
//        request.parameters = parameters
//        request.executeAsync()
//    }
//
//    fun pageUserImages(onUserImagesResponse: (UserImagesResponse) -> Unit) {
//        userImagesGraphResponse?.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT)
//            ?.apply {
//                callback = GraphRequest.Callback {
//                    userImagesGraphResponse = it
//                    val response = Gson().fromJson(it.rawResponse, UserImagesResponse::class.java)
//                    onUserImagesResponse(response)
//                }
//                executeAsync()
//            }
//    }

    fun generateHashKey(context: Context) {
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }
}