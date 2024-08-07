package com.joheba.hotelbediax.domain.usecase

import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import javax.inject.Inject

//TODO: implement worker use case to fetch the whole data at once and store it in Room
class DestinationWorkerUseCase @Inject constructor(
    private val externalDestinationRepository: ExternalDestinationRepository,
    private val localDestinationRepository: LocalDestinationRepository
) {
}