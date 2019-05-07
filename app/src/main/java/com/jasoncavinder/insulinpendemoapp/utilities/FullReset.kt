/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.utilities

class FullReset {

    fun run(
        all: Boolean = false,
        clearPhotos: Boolean = false,
        loadRawAssets: Boolean = false
    ) {
        if (all || clearPhotos) clearPhotos()
        if (all || loadRawAssets) loadRawAssets()


    }

    fun clearPhotos() {
        // TODO: Compose function to erase app internal storage
    }

    fun loadRawAssets() {

    }
}