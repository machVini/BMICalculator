package com.mach.apps.imccalculatorapp.android.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.mach.apps.imccalculatorapp.android.BuildConfig

@Composable
fun AdBanner(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = BuildConfig.AD_UNIT_ID
                loadAd(AdRequest.Builder().build())
                adListener = object : AdListener(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d("ADMOB", "Ad failed to load: ${p0.code} - ${p0.message}")
                    }

                    override fun onAdLoaded() {
                        Log.d("ADMOB", "Ad loaded")
                    }
                }
            }
        }
    )
}