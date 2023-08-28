package com.harash1421.payment_integration.data

import android.content.Context
import com.harash1421.payment_integration.util.Info
import com.harash1421.payment_integration.data.api.NetworkModule
import com.harash1421.payment_integration.data.api.StripeApiService
import com.harash1421.payment_integration.data.repositories.StripeRepository
import com.harash1421.payment_integration.util.BillingManager
import com.stripe.android.Stripe
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideBillingManager(@ApplicationContext context: Context): BillingManager {
        return BillingManager(context)
    }

    @Singleton
    @Provides
    fun provideStripe(@ApplicationContext context: Context): Stripe {
        return Stripe(context, Info.stripePublisherKey)
    }

    @Singleton
    @Provides
    fun provideStripeApiService(): StripeApiService {
        return NetworkModule.stripeApiService
    }

    @Singleton
    @Provides
    fun providePaymentRepository(stripeApiService: StripeApiService): StripeRepository {
        return StripeRepository(stripeApiService)
    }
}
