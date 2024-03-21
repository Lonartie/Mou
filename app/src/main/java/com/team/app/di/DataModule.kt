package com.team.app.di


import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

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
//    @Provides
//    fun providesSettingsRepo(@ApplicationContext context: Context): SettingsRepository {
//        val dataStore = context.dataStore
//        return SettingsRepository(dataStore)
//    }
//
//    @Provides
//    fun providesWorkmanager(@ApplicationContext context: Context): WorkManager {
//        return WorkManager.getInstance(context)
//    }
}