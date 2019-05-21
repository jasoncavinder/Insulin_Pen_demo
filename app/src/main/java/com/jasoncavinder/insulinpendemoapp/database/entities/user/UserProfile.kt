/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.user

import androidx.room.Embedded
import androidx.room.Relation
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenWithDataPoints
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider

class UserProfile {

    @Embedded
    var user: User? = null

    @Relation(
        parentColumn = "providerId",
        entityColumn = "id",
        entity = Provider::class
    )
    var provider: Set<Provider>? = null

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    var paymentMethod: Set<Payment>? = null

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = Pen::class
    )
    var pen: Set<PenWithDataPoints>? = null

}
