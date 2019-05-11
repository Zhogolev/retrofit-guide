package com.zhogolev.data.provider

import com.zhogolev.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}