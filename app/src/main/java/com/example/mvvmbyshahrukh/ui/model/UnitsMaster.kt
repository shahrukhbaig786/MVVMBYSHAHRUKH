package com.example.mvvmbyshahrukh.ui.model

class UnitsMaster {
    var unitId = 0
    var isTakaActive = false
    var unitName: String? = null
    var shortName: String? = null
    var orgId: String? = null
    var isCheck = false

    override fun toString(): String {
        return unitName!!
    }

}