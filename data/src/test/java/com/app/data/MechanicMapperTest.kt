package com.app.data

import com.app.data.mapper.toDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MechanicMapperTest {

    @Test
    fun `toDomain maps dto correctly`() {
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

        val result = dto.toDomain("m_01")

        assertEquals("m_01", result?.id)
        assertEquals("Sharma Auto Works", result?.name)
        assertEquals(4.6, result?.rating)
        assertEquals(128, result?.reviewCount)
        assertEquals(2.3, result?.distanceKm)
        assertEquals("Indirapuram, Ghaziabad", result?.location)
        assertEquals("+919876543210", result?.phoneNumber)
    }

    @Test
    fun `toDomain returns null when name is missing`() {
        val dto = MechanicDto(
            name = null
        )

        val result = dto.toDomain("m_01")

        assertNull(result)
    }
}