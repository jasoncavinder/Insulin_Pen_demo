package com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint

enum class DataPointType {
    TEMPERATURE,    // data = temperature, period = how long
    DOSE_GIVEN,     // data = dosage, doseID = foreignKey
    CARTRIDGE_NEW  // time = when it will expire
}
