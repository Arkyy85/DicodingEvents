package com.ammar.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ammar.core.data.EventsRepository
import com.ammar.core.data.source.local.LocalDataSource
import com.ammar.core.data.source.local.preference.SettingPreferences
import com.ammar.core.data.source.local.preference.dataStore
import com.ammar.core.data.source.local.room.EventsDatabase
import com.ammar.core.data.source.remote.RemoteDataSource
import com.ammar.core.data.source.remote.retrofit.ApiService
import com.ammar.core.domain.repository.IEventsRepository
import com.ammar.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.ammar.core.BuildConfig
import okhttp3.CertificatePinner

val repositoryModule = module {
    single { LocalDataSource(get(), get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IEventsRepository> {
        EventsRepository(
            get(),
            get(),
        )
    }
}

val networkModule = module {
    single {
        val hostname = "event-api.dicoding.dev"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname,"sha256/IP3deCdJNWm0Ae27av8Odv7gpd7Z1jL6dKVGnJDOpDM=")
            .add(hostname,"sha256/Jfy7JYGSTPqS9mo2VZhc/2epZhPp3mGNSSJTN/JJjO8=")
            .add(hostname,"sha256/K7rZOrXHknnsEhUH8nLL4MZkejquUuIvOIr6tCa0rbo=")
            .add(hostname,"sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
            .build()
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        } else {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE }
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}



val databaseModule = module {
    factory { get<EventsDatabase>().eventsDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("dicoding".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            EventsDatabase::class.java, "EventsDb"
        ) .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .openHelperFactory(factory)
            .build()
    }
}

val preferenceModule = module {
    single { provideDataStore(androidContext()) }
    single { SettingPreferences(get()) }
}


val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE events ADD COLUMN isFav INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE events ADD COLUMN eventType TEXT NOT NULL DEFAULT 'all'")
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE events ADD COLUMN summary TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN registrants INTEGER")
        db.execSQL("ALTER TABLE events ADD COLUMN imageLogo TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN link TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN description TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN ownerName TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN cityName TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN quota INTEGER")
        db.execSQL("ALTER TABLE events ADD COLUMN beginTime TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN endTime TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN category TEXT")
    }
}

fun provideDataStore(context: Context): DataStore<Preferences> {
    return context.dataStore
}

