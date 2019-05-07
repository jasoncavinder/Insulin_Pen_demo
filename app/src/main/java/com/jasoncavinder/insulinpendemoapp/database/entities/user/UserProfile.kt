/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.user

import androidx.room.Embedded
import androidx.room.Relation
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
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
    var pen: Set<Pen>? = null

}
/*
  @Relation(parentColumn = "creatorId", entityColumn = "remoteId",     entity = User::class)
  var user: List<User>? = null

  @Relation(parentColumn = "remoteId", entityColumn = "tripId", entity = PlanitiList::class)
  var lists: List<ListAndListItems>? = null
}
*/
