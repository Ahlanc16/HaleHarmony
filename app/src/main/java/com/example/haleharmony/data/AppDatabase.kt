package com.example.haleharmony.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitorDao {
    @Query("SELECT * FROM visitors ORDER BY arrivalDateTime DESC")
    fun getAllVisitors(): Flow<List<Visitor>>

    @Insert
    suspend fun insert(visitor: Visitor)

    @Query("DELETE FROM visitors WHERE id = :visitorId")
    suspend fun delete(visitorId: String)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY dateTime DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Insert
    suspend fun insert(event: Event)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun delete(eventId: String)
}

@Dao
interface BillDao {
    @Query("SELECT * FROM bills ORDER BY dueDate DESC")
    fun getAllBills(): Flow<List<Bill>>

    @Insert
    suspend fun insert(bill: Bill)

    @Query("UPDATE bills SET isPaid = :isPaid WHERE id = :billId")
    suspend fun updateBillPaidStatus(billId: String, isPaid: Boolean)

    @Query("DELETE FROM bills WHERE id = :billId")
    suspend fun delete(billId: String)
}

@Dao
interface ChoreDao {
    @Query("SELECT * FROM chores ORDER BY name ASC")
    fun getAllChores(): Flow<List<Chore>>

    @Insert
    suspend fun insert(chore: Chore)

    @Query("UPDATE chores SET isCompleted = :isCompleted WHERE id = :choreId")
    suspend fun updateChoreCompletion(choreId: String, isCompleted: Boolean)

    @Query("UPDATE chores SET assignedTo = :assignedTo WHERE id = :choreId")
    suspend fun reassignChore(choreId: String, assignedTo: String)

    @Query("DELETE FROM chores WHERE id = :choreId")
    suspend fun delete(choreId: String)
}

@Database(entities = [Visitor::class, Event::class, Bill::class, Chore::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun visitorDao(): VisitorDao
    abstract fun eventDao(): EventDao
    abstract fun billDao(): BillDao
    abstract fun choreDao(): ChoreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hale_harmony_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
