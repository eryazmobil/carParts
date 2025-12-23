package eryaz.software.carParts.data.di

import eryaz.software.carParts.data.api.client.ZeusClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModuleApis = module {

    single { ZeusClient.provideApi(androidContext()) }

    single { ZeusClient.provideUserApi(androidContext()) }

    single { ZeusClient.provideWorkActivityApi(androidContext()) }

    single { ZeusClient.provideBarcodeApi(androidContext()) }

    single { ZeusClient.providePlacementApi(androidContext()) }

    single { ZeusClient.provideOrderApi(androidContext()) }

    single { ZeusClient.provideCountingApi(androidContext()) }
}