package com.aydar.demandi

import android.app.Application
import com.aydar.demandi.common.di.bluetoothServiceModule
import com.aydar.demandi.data.di.repositoryModule
import com.aydar.demandi.data.di.roomModule
import com.aydar.demandi.di.firebaseModule
import com.aydar.demandi.di.routerModule
import com.aydar.demandi.featurecreateroom.di.createRoomModule
import com.aydar.demandi.featurestudentroom.di.studentsRoomModule
import com.aydar.demandi.teacherrooms.di.teacherRoomsUseCaseModule
import com.aydar.demandi.teacherrooms.di.teacherRoomsViewModelModule
import com.aydar.featureroomdetails.di.roomDetailsUseCaseModule
import com.aydar.featureroomdetails.di.roomDetailsViewModelModule
import com.aydar.featureteacherroom.di.roomsViewModelModule
import com.aydar.featureteacherroom.di.teacherRoomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                routerModule,
                repositoryModule,
                createRoomModule,
                teacherRoomsViewModelModule,
                teacherRoomsUseCaseModule,
                studentsRoomModule,
                bluetoothServiceModule,
                roomModule,
                roomDetailsUseCaseModule,
                roomDetailsViewModelModule,
                roomsViewModelModule,
                teacherRoomModule,
                firebaseModule
            )
        }
    }
}