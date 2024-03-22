package com.team.app.di


import android.content.Context
import androidx.room.Room
import com.team.app.data.database.InventoryDatabase
import com.team.app.data.repositories.ShopRepository
import com.lonartie.bookdiary.data.repositories.SettingsRepository
import com.team.app.data.database.AttributesDatabase
import com.team.app.data.repositories.AttributesRepository
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
    fun provideItemDatabase(@ApplicationContext context: Context): InventoryDatabase {
        return Room.databaseBuilder(context, InventoryDatabase::class.java, "inventory_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAttributesRepository(inventoryDb: InventoryDatabase): ShopRepository {
        return ShopRepository(inventoryDb.itemDao())
    }
    @Provides
    @Singleton
    fun provideAttributesDatabase(@ApplicationContext context: Context): AttributesDatabase {
        return Room.databaseBuilder(context, AttributesDatabase::class.java, "attributes_database")
            .createFromAsset("database/prefilled_attributes.db")
            .build()
    }

    @Provides
    fun provideItemRepository(attributesDatabase: AttributesDatabase): AttributesRepository {
        return AttributesRepository(attributesDatabase.attributesDao())
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
    @Provides
    fun providesSettingsRepo(@ApplicationContext context: Context): SettingsRepository {
        val dataStore = context.dataStore
        return SettingsRepository(dataStore)
    }

//    @Provides
//    fun providesWorkmanager(@ApplicationContext context: Context): WorkManager {
//        return WorkManager.getInstance(context)
//    }
}