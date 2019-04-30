package com.jasoncavinder.inpen.demo.data.entities.pendatapoint

enum class DataPointType {
    TEMPERATURE,    // data = temperature, period = how long
    DOSE_SCHEDULED, // data = dosage, time = scheduled time, doseID = foreignKey
    DOSE_GIVEN,     // data = dosage, doseID = foreignKey
    CARTRIDGE_NEW  // time = when it will expire
}
