package com.robjonesdev.pf2elootsie.data.injection

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.google.gson.Gson
import com.robjonesdev.pf2elootsie.data.datasource.EquipmentDao
import com.robjonesdev.pf2elootsie.data.datasource.EquipmentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class InjectionModule {
    companion object {
        private const val APP_DATABASE_NAME = "equipment_database"
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ): EquipmentDatabase = Room.databaseBuilder(
        app,
        EquipmentDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    @Provides
    fun provideEquipmentDao(database: EquipmentDatabase): EquipmentDao {
        return database.equipmentDao()
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): CoroutineContext = Dispatchers.Default
}