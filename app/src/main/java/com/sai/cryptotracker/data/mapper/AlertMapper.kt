package com.sai.cryptotracker.data.mapper

import com.sai.cryptotracker.data.local.entity.AlertEntity
import com.sai.cryptotracker.domain.model.PriceAlert

fun AlertEntity.toDomain(): PriceAlert {
    return PriceAlert(
        id = id,
        coinId = coinId,
        coinName = coinName,
        targetPrice = targetPrice,
        isAbove = isAbove,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun PriceAlert.toEntity(): AlertEntity {
    return AlertEntity(
        id = id,
        coinId = coinId,
        coinName = coinName,
        targetPrice = targetPrice,
        isAbove = isAbove,
        isActive = isActive,
        createdAt = createdAt
    )
}
