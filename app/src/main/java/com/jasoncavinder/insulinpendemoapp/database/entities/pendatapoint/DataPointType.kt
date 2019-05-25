/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint

enum class DataPointType {
    TEMPERATURE,    // data = temperature, period = how long
    DOSE_GIVEN,     // data = dosage, doseId = foreignKey
    CARTRIDGE_NEW  // time = when it will expire
}
