package com.example.maapp.presentation.di

import android.content.Context
import com.example.maapp.presentation.ui.leaderboard.LeaderboardFragment
import com.example.maapp.presentation.ui.tests.bandwidthTest.BandwidthTestFragment
import com.example.maapp.presentation.ui.tests.searchTest.SearchTestFragment
import com.example.maapp.presentation.ui.tests.wallTest.WallTestFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            NetworkModule::class
        ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(application: Context): Builder

        fun build(): AppComponent

    }

    interface ComponentProvider {
        val appComponent: AppComponent
    }

    /* Fragment */
    fun inject(fragment: LeaderboardFragment)

    fun inject2(fragment: WallTestFragment)

    fun inject3(fragment: BandwidthTestFragment)

    fun injectSearchTest(fragment: SearchTestFragment)

}