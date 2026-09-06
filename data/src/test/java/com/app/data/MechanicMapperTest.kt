package com.app.data

import com.app.data.local.mechanic.MechanicEntity
import com.app.data.mapper.toDomain
import com.app.data.mapper.toEntity
import com.app.data.mechanic.MechanicDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MechanicMapperTest {

    @Test
    fun `toEntity maps valid dto correctly`() {
        val dto = MechanicDto(
            name = "Sharma Auto Works",
            rating = 4.6,
            reviewCount = 128,
            distanceKm = 2.3,
            locality = "Indirapuram, Ghaziabad",
            address = "Shop 14",
            phone = "+919876543210",
            openTime = "09:00",
            closeTime = "20:00",
            services = listOf(
                "Battery",
                "Engine Repair"
            )
        )

        val entity = dto.toEntity("m_01")

        requireNotNull(entity)

        assertEquals("m_01", entity.id)
        assertEquals("Sharma Auto Works", entity.name)
        assertEquals(4.6, entity.rating, 0.0)
        assertEquals(128, entity.reviewCount)
        assertEquals(2.3, entity.distanceKm, 0.0)
        assertEquals("Indirapuram, Ghaziabad", entity.location)
        assertEquals("+919876543210", entity.phoneNumber)
        assertEquals(
            listOf("Battery", "Engine Repair"),
            entity.services
        )
    }

    @Test
    fun `toEntity returns null when name is missing`() {
        val dto = MechanicDto(
            name = null
        )

        val result = dto.toEntity("m_01")

        assertNull(result)
    }

    @Test
    fun `toDomain maps cached entity correctly`() {
        val entity = MechanicEntity(
            id = "m_01",
            name = "Sharma Auto Works",
            rating = 4.6,
            reviewCount = 128,
            distanceKm = 2.3,
            location = "Indirapuram, Ghaziabad",
            address = "Shop 14",
            services = listOf("Battery", "Engine Repair"),
            openTime = "09:00",
            closeTime = "20:00",
            phoneNumber = "+919876543210"
        )

        val mechanic = entity.toDomain()

        assertEquals(entity.id, mechanic.id)
        assertEquals(entity.name, mechanic.name)
        assertEquals(entity.services, mechanic.services)
        assertEquals(entity.phoneNumber, mechanic.phoneNumber)
    }
}