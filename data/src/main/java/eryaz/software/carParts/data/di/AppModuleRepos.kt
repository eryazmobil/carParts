package eryaz.software.carParts.data.di

import eryaz.software.carParts.data.repositories.AuthRepo
import eryaz.software.carParts.data.repositories.BarcodeRepo
import eryaz.software.carParts.data.repositories.CountingRepo
import eryaz.software.carParts.data.repositories.OrderRepo
import eryaz.software.carParts.data.repositories.PlacementRepo
import eryaz.software.carParts.data.repositories.UserRepo
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import org.koin.dsl.module

val appModuleRepos = module {

    factory { AuthRepo(get()) }

    factory { UserRepo(get()) }

    factory { WorkActivityRepo(get()) }

    factory { BarcodeRepo(get()) }

    factory { PlacementRepo(get()) }

    factory { OrderRepo(get()) }

    factory { CountingRepo(get()) }

}