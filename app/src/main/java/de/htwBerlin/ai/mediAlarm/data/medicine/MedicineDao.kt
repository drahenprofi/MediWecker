package de.htwBerlin.ai.mediAlarm.data.medicine

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicine")
    fun getAll(): List<Medicine>

    @Query("SELECT * FROM medicine WHERE id IN (:medicineIds)")
    fun loadAllByIds(medicineIds: IntArray): List<Medicine>

    @Query("SELECT * FROM medicine WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Medicine

    @Insert
    fun insertAll(vararg medicines: Medicine)

    @Delete
    fun delete(medicine: Medicine)
}