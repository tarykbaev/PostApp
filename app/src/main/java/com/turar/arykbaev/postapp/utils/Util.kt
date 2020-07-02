package com.turar.arykbaev.postapp.utils

import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Util {
    companion object {
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun showSnackBar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        fun progressBarStop(progressBar: ProgressBar) {
            progressBar.visibility = ProgressBar.INVISIBLE
        }

        fun progressBarStart(progressBar: ProgressBar) {
            progressBar.visibility = ProgressBar.VISIBLE
        }
    }
}