package de.htwBerlin.ai.mediAlarm.data.medicine

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicine")
    fun getAll(): List<Medicine>

    @Query("SELECT * FROM medicine WHERE id = :id")
    fun get(id: Long): Medicine?

    @Query("SELECT * FROM medicine WHERE id IN (:medicineIds)")
    fun loadAllByIds(medicineIds: List<Long>): List<Medicine>

    @Query("SELECT * FROM medicine WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Medicine

    @Insert
    fun insertAll(vararg medicines: Medicine)

    @Insert
    fun insert(medicine: Medicine): Long

    @Delete
    fun delete(medicine: Medicine)

    @Query("DELETE FROM medicine WHERE id = :id")
    fun deleteById(id: Int)

    @Update
    fun update(medicine: Medicine)
}