package com.team.app.di


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.app.data.database.AttributesDatabase
import com.team.app.data.database.InvestmentsDatabase
import com.team.app.data.database.ItemsDatabase
import com.team.app.data.database.StartTimestampDao
import com.team.app.data.database.StepCounterDatabase
import com.team.app.data.database.StepsDao
import com.team.app.data.network.StocksService
import com.team.app.data.network.interceptors.RateLimitInterceptor
import com.team.app.data.network.interceptors.StockApiKeyInterceptor
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.ItemsRepository
import com.team.app.data.repositories.NetworkRepository
import com.team.app.data.repositories.SettingsRepository
import com.team.app.data.repositories.StepCounterRepository
import com.team.app.data.repositories.StocksRepository
import com.team.app.ui.common.Dialog
import com.team.app.ui.common.Notification
import com.team.app.service.SoundService
import com.team.app.service.StepCounterService
import com.team.app.utils.Constants.Companion.STOCKS_API_BASE
import com.team.app.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideHotbarRepository(
        itemsDb: ItemsDatabase,
        inventoryRepo: InventoryRepository
    ): HotbarRepository {
        return HotbarRepository(itemsDb.hotbarDao(), inventoryRepo)
    }

    @Provides
    @Singleton
    fun provideAttributesDatabase(@ApplicationContext context: Context): AttributesDatabase {
        return Room
            .databaseBuilder(context, AttributesDatabase::class.java, "attributes_database")
            .createFromAsset("database/prefilled_attributes.db")
            .build()
    }

    @Provides
    fun provideAttributesRepository(attributesDatabase: AttributesDatabase): AttributesRepository {
        return AttributesRepository(attributesDatabase.attributesDao())
    }

    @Provides
    fun providesNotificationService(@ApplicationContext context: Context): Notification {
        return Notification(context)
    }

    @Provides
    fun providesNetworkRepository(@ApplicationContext context: Context): NetworkRepository {
        return NetworkRepository(context)
    }

    @Provides
    fun providesRetroFit(): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(STOCKS_API_BASE).addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )

        val client = OkHttpClient.Builder()
            .addInterceptor(StockApiKeyInterceptor())
            .addInterceptor(RateLimitInterceptor())
            .build()

        retrofit.client(client)

        return retrofit.build()
    }

    @Provides
    fun provideStocksAPI(retrofit: Retrofit): StocksService {
        return retrofit.create(StocksService::class.java)
    }

    @Provides
    fun provideStocksRepository(stocksService: StocksService): StocksRepository {
        return StocksRepository(stocksService)
    }

    @Provides
    fun provideSoundService(@ApplicationContext context: Context): SoundService {
        return SoundService(context)
    }

    @Provides
    fun provideDialogService(@ApplicationContext context: Context): Dialog {
        return Dialog(context)
    }

    // provide StepCounterDao
    @Provides
    fun providesStepDatabase(@ApplicationContext context: Context): StepCounterDatabase {
        return Room
            .databaseBuilder(context, StepCounterDatabase::class.java, "stepcounterdatabase")
            .build()
    }

    @Provides
    fun providesStartTimestampDao(database: StepCounterDatabase) = database.startTimestampDao()

    @Provides
    fun providesStepCounterDao(database: StepCounterDatabase) = database.stepsDao()

    @Provides
    fun providesSettingsRepo(@ApplicationContext context: Context): SettingsRepository {
        val dataStore = context.dataStore
        return SettingsRepository(dataStore)
    }

    @Provides
    fun providesStepCounterRepository(
        stepCounterService: StepCounterService,
        stepsDao: StepsDao,
        timestampDao: StartTimestampDao
    ): StepCounterRepository {
        return StepCounterRepository(stepCounterService, stepsDao, timestampDao)
    }

    @Provides
    fun providesStepCounterService(sensorManager: SensorManager): StepCounterService {
        return StepCounterService(sensorManager)
    }

    @Provides
    fun providesSensorManager(@ApplicationContext context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @Provides
    fun providesStepCounterSensor(sensorManager: SensorManager): Sensor? {
        return sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    @Provides
    fun providesInvestmentsDatabase(@ApplicationContext context: Context): InvestmentsDatabase {
        return Room.databaseBuilder(
            context,
            InvestmentsDatabase::class.java,
            "investments_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesInvestmentsRepository(
        investmentsDatabase: InvestmentsDatabase
    ): InvestmentsRepository {
        return InvestmentsRepository(investmentsDatabase.investmentsDao())
    }
}