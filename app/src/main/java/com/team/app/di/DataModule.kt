package com.team.app.di


import android.content.Context
import androidx.room.Room
import com.lonartie.bookdiary.data.repositories.SettingsRepository
import com.team.app.data.database.AttributesDatabase
import com.team.app.data.database.ItemsDatabase
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.ItemsRepository
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