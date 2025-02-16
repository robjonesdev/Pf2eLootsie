package com.robjonesdev.pf2elootsie.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.robjonesdev.pf2elootsie.data.datasource.EquipmentDatabase
import com.robjonesdev.pf2elootsie.data.models.Equipment
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import com.google.gson.*
import com.robjonesdev.pf2elootsie.data.models.Trait
import java.lang.reflect.Type

/**
 * A WorkManager which has the purpose of parsing local json files
 * for [com.robjonesdev.pf2elootsie.data.models.Equipment] objects
 * and storing them in a local database
 */
@HiltWorker
class JsonEquipmentDatabaseBuilderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val db: EquipmentDatabase
) : CoroutineWorker(context, workerParams) {
    companion object {
        const val CLASS_TAG = "JSON_EQUIPMENT_DATABASE_BUILDER_WORKER"
        const val ASSET_EQUIPMENT_PATH = "equipment"
    }
    private val assetManager = context.assets

    override suspend fun doWork(): Result {
        Log.d(CLASS_TAG, "Populating equipment DB from asset files")
        val dao = db.equipmentDao()

        return withContext(Dispatchers.IO) {
            val files = assetManager.list(ASSET_EQUIPMENT_PATH) ?: run {
                Log.w(CLASS_TAG, "Application was unable to migrate equipment assets due to being unable to find an asset path")
                return@withContext Result.failure()
            }
            try{
                val gson = GsonBuilder()
                    .registerTypeAdapter(Equipment::class.java, EquipmentDeserializer())
                    .create()

                if (files.count() != dao.getEquipmentCount()) {
                    for (fileName in files) {
                        val json = assetManager.open("$ASSET_EQUIPMENT_PATH/$fileName")
                            .bufferedReader()
                            .use { it.readText() }

                        Log.d(CLASS_TAG, "Processing File: $fileName")

                        val equipment = gson.fromJson(json, Equipment::class.java)

                        dao.insertEquipment(equipment)
                        for (traitName in equipment.traits) {
                            dao.insertTrait(Trait(name = traitName, equipmentOwnerId = equipment.equipmentId))
                        }

                        Log.d(CLASS_TAG, "Inserted into DB: $fileName")
                    }
                    Log.d(CLASS_TAG, "Worker Success")
                    Result.success()
                } else {
                    Log.d(CLASS_TAG, "Equipment DB is already populated, no work necessary")
                    Result.success()
                }
            } catch (e: IOException) {
                Log.e(CLASS_TAG, "Worker failed due to IOException:", e)
                Result.failure()
            }
        }
    }
}

class EquipmentDeserializer : JsonDeserializer<Equipment> {
    private fun JsonElement?.safeAsString(): String {
        return if (this is JsonNull || this == null) "" else this.asString
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Equipment {
        val jsonObject = json.asJsonObject

        val equipmentId = jsonObject["_id"].safeAsString()
        val name = jsonObject["name"].safeAsString()
        val img = jsonObject.get("img").safeAsString()
        val type = jsonObject.get("type").safeAsString()

        val system = jsonObject.getAsJsonObject("system")

        val baseItem = system.get("baseItem").safeAsString()
        val containerId = system.get("containerId").safeAsString()
        val description = system.getAsJsonObject("description")?.get("value").safeAsString()
        val category = system.get("category").safeAsString()
        val hardness = system.get("hardness")?.asInt ?: 0
        val maxHP = system.getAsJsonObject("hp")?.get("max")?.asInt ?: 0
        val level = system.getAsJsonObject("level")?.get("value")?.asInt ?: 0
        val materialGrade = system.getAsJsonObject("material")?.get("grade").safeAsString()
        val materialType = system.getAsJsonObject("material")?.get("type").safeAsString()
        val usage = system.getAsJsonObject("usage")?.get("value")?.asString ?: ""
        val price = system.getAsJsonObject("price")?.getAsJsonObject("value")?.get("gp")?.asFloat ?: 0.00f
        val sizeJson = system.get("size")
        val size = when {
            sizeJson == null -> ""
            sizeJson.isJsonObject -> sizeJson.asJsonObject.get("value")?.asString ?: ""
            sizeJson.isJsonPrimitive -> sizeJson.asString
            else -> ""
        }
        val license = system.getAsJsonObject("publication")?.get("license").safeAsString()
        val remaster = system.getAsJsonObject("publication")?.get("remaster")?.asBoolean ?: false
        val title = system.getAsJsonObject("publication")?.get("title").safeAsString()

        val rarity = system.getAsJsonObject("traits")?.get("rarity").safeAsString()

        val traitsArray = system.getAsJsonObject("traits")?.getAsJsonArray("value")
        val traits = traitsArray?.mapNotNull { it.safeAsString() } ?: emptyList()

        return Equipment(
            equipmentId = equipmentId,
            name = name,
            img = img,
            type = type,
            baseItem = baseItem,
            containerId = containerId,
            description = description,
            hardness = hardness,
            maxHP = maxHP,
            level = level,
            materialGrade = materialGrade,
            materialType = materialType,
            price = price,
            license = license,
            remaster = remaster,
            title = title,
            rarity = rarity,
            traits = traits,
            usage = usage,
            category = category,
            size = size,
        )
    }
}

