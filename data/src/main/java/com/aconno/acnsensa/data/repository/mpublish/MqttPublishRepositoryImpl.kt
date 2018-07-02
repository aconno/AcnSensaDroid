package com.aconno.acnsensa.data.repository.mpublish

import com.aconno.acnsensa.data.mapper.MqttPublishDataMapper
import com.aconno.acnsensa.domain.ifttt.BasePublish
import com.aconno.acnsensa.domain.ifttt.MqttPublish
import com.aconno.acnsensa.domain.ifttt.MqttPublishRepository
import io.reactivex.Maybe
import io.reactivex.Single

class MqttPublishRepositoryImpl(
    private val mqttPublishDao: MqttPublishDao,
    private val mqttPublishDataMapper: MqttPublishDataMapper
) : MqttPublishRepository {
    override fun addMqttPublish(mqttPublish: MqttPublish): Long {
        return mqttPublishDao.insert(mqttPublishDataMapper.toMqttPublishEntity(mqttPublish))
    }

    override fun updateMqttPublish(mqttPublish: MqttPublish) {
        mqttPublishDao.update(mqttPublishDataMapper.toMqttPublishEntity(mqttPublish))
    }

    override fun deleteMqttPublish(mqttPublish: MqttPublish) {
        mqttPublishDao.delete(mqttPublishDataMapper.toMqttPublishEntity(mqttPublish))
    }

    override fun getAllMqttPublish(): Single<List<BasePublish>> {
        return mqttPublishDao.all.map(mqttPublishDataMapper::toMqttPublishList)
    }

    override fun getAllEnabledMqttPublish(): Single<List<BasePublish>> {
        return mqttPublishDao.getEnabledMqttPublish()
            .map(mqttPublishDataMapper::toMqttPublishList)
    }

    override fun getMqttPublishById(mqttPublishId: Long): Maybe<MqttPublish> {
        return mqttPublishDao.getMqttPublishById(mqttPublishId)
            .map(mqttPublishDataMapper::toMqttPublish)
    }
}