package com.jasoncavinder.inpen.demo.data.entities.pendata

enum class DataPointType {
    TEMPERATURE,    // data = temperature, period = how long
    DOSE_SCHEDULED, // data = dosage, time = scheduled time, dose_id = foreignKey
    DOSE_GIVEN,     // data = dosage, dose_id = foreignKey
    CARTRIDGE_NEW  // time = when it will expire
}
