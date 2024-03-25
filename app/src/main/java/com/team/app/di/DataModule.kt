package com.team.app.di


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.room.Room
import com.team.app.data.repositories.SettingsRepository
import com.team.app.data.database.StepCounterDatabase
import com.team.app.data.database.StepsDao
import com.team.app.data.repositories.StepCounterRepository
import com.team.app.data.database.AttributesDatabase
import com.team.app.data.database.ItemsDatabase
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.ItemsRepository
import com.team.app.service.NotificationService
import com.team.app.service.SoundService
import com.team.app.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideItemsDatabase(@ApplicationContext context: Context): ItemsDatabase {
        return Room.databaseBuilder(context, ItemsDatabase::class.java, "items_database")
            .createFromAsset("database/prefilled_items.db")
            .build()
    }

    @Provides
    fun provideItemsRepository(itemsDb: ItemsDatabase): ItemsRepository {
        return ItemsRepository(itemsDb.itemsDao())
    }

    @Provides
    fun provideInventoryRepository(
        itemsDb: ItemsDatabase, itemsRepo: ItemsRepository, attributesRepo: AttributesRepository
    ): InventoryRepository {
        return InventoryRepository(itemsDb.inventoryDao(), itemsRepo, attributesRepo)
    }

    @Provides
    fun provideHotbarRepository(itemsDb: ItemsDatabase, inventoryRepo: InventoryRepository): HotbarRepository {
        return HotbarRepository(itemsDb.hotbarDao(), inventoryRepo)
    }

    @Provides
    @Singleton
    fun provideAttributesDatabase(@ApplicationContext context: Context): AttributesDatabase {
        return Room.databaseBuilder(context, AttributesDatabase::class.java, "attributes_database")
            .createFromAsset("database/prefilled_attributes.db")
            .build()
    }

    @Provides
    fun provideAttributesRepository(attributesDatabase: AttributesDatabase): AttributesRepository {
        return AttributesRepository(attributesDatabase.attributesDao())
    }

    @Provides
    fun providesNotificationService(@ApplicationContext context: Context): NotificationService {
        return NotificationService(context)
    }

    @Provides
    fun provideSoundService(@ApplicationContext context: Context): SoundService {
        return SoundService(context)
    }

//    @Provides
//    fun providesNetworkRepository(@ApplicationContext context: Context): NetworkRepository {
//        return NetworkRepository(context)
//    }
//
//    @Provides
//    fun providesRetroFit(): Retrofit {
//        return Retrofit.Builder().baseUrl(GOOGLE_BOOKS_API_BASE).addConverterFactory(
//            MoshiConverterFactory.create(
//                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//            )
//        ).build()
//    }
//
//    @Provides
//    fun provideBooksAPI(retrofit: Retrofit): BooksService {
//        return retrofit.create(BooksService::class.java)
//    }
//
//    @Provides
//    fun provideBooksRepo(api: BooksService, db: BooksDatabase): BooksRepository {
//        return BooksRepository(api, db.booksDao())
//    }
//
//    @Provides
//    fun providesDatabase(@ApplicationContext context: Context): BooksDatabase {
//        return Room.databaseBuilder(context, BooksDatabase::class.java, "booksdatabase")
//            .addMigrations(
//                BooksDatabase.MIGRATION_1_2,
//                BooksDatabase.MIGRATION_2_3
//            )
//            .build()
//    }
//
    // provide StepCounterDao
    @Provides
    fun providesStepDatabase(@ApplicationContext context: Context) : StepCounterDatabase {
        return Room.databaseBuilder(context, StepCounterDatabase::class.java, "stepcounterdatabase")
            .build()
    }

    @Provides
    fun providesStepCounterDao(database: StepCounterDatabase) = database.stepsDao()
    @Provides
    fun providesSettingsRepo(@ApplicationContext context: Context): SettingsRepository {
        val dataStore = context.dataStore
        return SettingsRepository(dataStore)
    }

    @Provides
    fun providesStepCounterRepository(sensorManager: SensorManager, stepsDao: StepsDao) : StepCounterRepository {
        return StepCounterRepository(sensorManager, stepsDao)
    }

    @Provides
    fun providesSensorManager(@ApplicationContext context: Context) : SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @Provides
    fun providesStepCounterSensor(sensorManager: SensorManager) : Sensor? {
        return sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

//    @Provides
//    fun providesWorkmanager(@ApplicationContext context: Context): WorkManager {
//        return WorkManager.getInstance(context)
//    }
}