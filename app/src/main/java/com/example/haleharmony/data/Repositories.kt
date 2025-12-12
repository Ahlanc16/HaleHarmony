package com.example.haleharmony.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.Firebase
import com.google.firebase.firestore.snapshots

interface VisitorsRepository {
    fun getAllVisitorsStream(): Flow<List<Visitor>>
    suspend fun insertVisitor(visitor: Visitor)
    suspend fun deleteVisitor(visitorId: String)
}

class OfflineVisitorsRepository(private val visitorDao: VisitorDao) : VisitorsRepository {
    override fun getAllVisitorsStream(): Flow<List<Visitor>> = visitorDao.getAllVisitors()
    override suspend fun insertVisitor(visitor: Visitor) = visitorDao.insert(visitor)
    override suspend fun deleteVisitor(visitorId: String) = visitorDao.delete(visitorId)
}

interface EventsRepository {
    fun getAllEventsStream(): Flow<List<Event>>
    suspend fun insertEvent(event: Event)
    suspend fun deleteEvent(eventId: String)
}

class OfflineEventsRepository(private val eventDao: EventDao) : EventsRepository {
    override fun getAllEventsStream(): Flow<List<Event>> = eventDao.getAllEvents()
    override suspend fun insertEvent(event: Event) = eventDao.insert(event)
    override suspend fun deleteEvent(eventId: String) = eventDao.delete(eventId)
}

interface BillsRepository {
    fun getAllBillsStream(): Flow<List<Bill>>
    suspend fun insertBill(bill: Bill)
    suspend fun updateBillPaidStatus(billId: String, isPaid: Boolean)
    suspend fun deleteBill(billId: String)
}

class OfflineBillsRepository(private val billDao: BillDao) : BillsRepository {
    override fun getAllBillsStream(): Flow<List<Bill>> = billDao.getAllBills()
    override suspend fun insertBill(bill: Bill) = billDao.insert(bill)
    override suspend fun updateBillPaidStatus(billId: String, isPaid: Boolean) = billDao.updateBillPaidStatus(billId, isPaid)
    override suspend fun deleteBill(billId: String) = billDao.delete(billId)
}

interface ChoresRepository {
    fun getAllChoresStream(): Flow<List<Chore>>
    suspend fun insertChore(chore: Chore)
    suspend fun updateChoreCompletion(choreId: String, isCompleted: Boolean)
    suspend fun reassignChore(choreId: String, assignedTo: String)
    suspend fun deleteChore(choreId: String)
}

class OfflineChoresRepository(private val choreDao: ChoreDao) : ChoresRepository {
    override fun getAllChoresStream(): Flow<List<Chore>> = choreDao.getAllChores()
    override suspend fun insertChore(chore: Chore) = choreDao.insert(chore)
    override suspend fun updateChoreCompletion(choreId: String, isCompleted: Boolean) = choreDao.updateChoreCompletion(choreId, isCompleted)
    override suspend fun reassignChore(choreId: String, assignedTo: String) = choreDao.reassignChore(choreId, assignedTo)
    override suspend fun deleteChore(choreId: String) = choreDao.delete(choreId)
}

interface AuthRepository {
    suspend fun createUser(email: String, pass: String, householdId: String? = null): FirebaseUser?
    suspend fun signIn(email: String, pass: String): FirebaseUser?
    fun getCurrentUser(): FirebaseUser?
}

class FirebaseAuthRepository: AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    override suspend fun createUser(email: String, pass: String, householdId: String?): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        result.user?.let { user ->
            val userDocument = User(uid = user.uid, email = user.email ?: "", householdId = householdId)
            firestore.collection("users").document(user.uid).set(userDocument).await()
        }
        return result.user
    }

    override suspend fun signIn(email: String, pass: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return result.user
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}

/*
sealed interface HouseholdResult {
    object Loading : HouseholdResult
    object NotLoggedIn : HouseholdResult
    object NoHousehold : HouseholdResult
    data class HasHousehold(val household: Household) : HouseholdResult
}

interface HouseholdRepository {
    suspend fun createHousehold(name: String): String
    suspend fun joinHousehold(householdId: String)
    fun getHousehold(): Flow<HouseholdResult>
}

@OptIn(ExperimentalCoroutinesApi::class)
class FirestoreHouseholdRepository(private val auth: FirebaseAuth) : HouseholdRepository {
    private val firestore: FirebaseFirestore = Firebase.firestore

    override suspend fun createHousehold(name: String): String {
        val currentUser = auth.currentUser ?: throw IllegalStateException("User not logged in")
        val household = Household(name = name, members = listOf(currentUser.uid))
        val householdRef = firestore.collection("households").add(household).await()
        firestore.collection("users").document(currentUser.uid).update("householdId", householdRef.id).await()
        return householdRef.id
    }

    override suspend fun joinHousehold(householdId: String) {
        val currentUser = auth.currentUser ?: throw IllegalStateException("User not logged in")
        firestore.collection("households").document(householdId).update("members", FieldValue.arrayUnion(currentUser.uid)).await()
        firestore.collection("users").document(currentUser.uid).update("householdId", householdId).await()
    }

    override fun getHousehold(): Flow<HouseholdResult> {
        return authStateFlow().flatMapLatest { user ->
            if (user == null) {
                flowOf(HouseholdResult.NotLoggedIn)
            } else {
                firestore.collection("users").document(user.uid)
                    .snapshots()
                    .flatMapLatest { userDoc ->
                        val householdId = userDoc.toObject<User>()?.householdId
                        if (householdId == null) {
                            flowOf(HouseholdResult.NoHousehold)
                        } else {
                            firestore.collection("households").document(householdId)
                                .snapshots()
                                .map { householdDoc ->
                                    householdDoc.toObject<Household>()?.let {
                                        HouseholdResult.HasHousehold(it)
                                    } ?: HouseholdResult.NoHousehold
                                }
                        }
                    }
            }
        }
    }

    private fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser).isSuccess
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }
}
*/
