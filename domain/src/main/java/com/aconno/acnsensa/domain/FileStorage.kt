package com.aconno.acnsensa.domain

import com.aconno.acnsensa.domain.model.Reading

interface FileStorage {

    fun storeReading(reading: Reading, fileName: String)
}