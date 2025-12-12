package com.example.haleharmony.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * App container for Dependency Injection.
 */
interface AppContainer {
    val visitorsRepository: VisitorsRepository
    val eventsRepository: EventsRepository
    val billsRepository: BillsRepository
    val choresRepository: ChoresRepository
    val authRepository: AuthRepository
    // val householdRepository: HouseholdRepository
    val firebaseAuth: FirebaseAuth
}

/**
 * [AppContainer] implementation that provides instances of repositories.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [VisitorsRepository]
     */
    override val visitorsRepository: VisitorsRepository by lazy {
        OfflineVisitorsRepository(AppDatabase.getDatabase(context).visitorDao())
    }

    /**
     * Implementation for [EventsRepository]
     */
    override val eventsRepository: EventsRepository by lazy {
        OfflineEventsRepository(AppDatabase.getDatabase(context).eventDao())
    }

    /**
     * Implementation for [BillsRepository]
     */
    override val billsRepository: BillsRepository by lazy {
        OfflineBillsRepository(AppDatabase.getDatabase(context).billDao())
    }

    /**
     * Implementation for [ChoresRepository]
     */
    override val choresRepository: ChoresRepository by lazy {
        OfflineChoresRepository(AppDatabase.getDatabase(context).choreDao())
    }

    /**
     * Implementation for [AuthRepository]
     */
    override val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }

    override val firebaseAuth: FirebaseAuth by lazy {
        Firebase.auth
    }

    /**
     * Implementation for [HouseholdRepository]
     */
    /*
    override val householdRepository: HouseholdRepository by lazy {
        FirestoreHouseholdRepository(firebaseAuth)
    }
    */
}
