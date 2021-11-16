package io.github.collins993.deliveryapp

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(
    context: Context
) {

    private val dataStore: DataStore<Preferences> = context.createDataStore("user_prefs")

    companion object{
        val FIRST_NAME_KEY = preferencesKey<String>("FIRST_NAME")
        val LAST_NAME_KEY = preferencesKey<String>("LAST_NAME")
        val USER_EMAIL_KEY = preferencesKey<String>("USER_EMAIL")
        val USER_PHONE_NO_KEY = preferencesKey<String>("USER_PHONE_NO")
        val DESCRIPTION_KEY = preferencesKey<String>("DESCRIPTION")
        val ADDRESS_KEY = preferencesKey<String>("ADDRESS")
        //val USER_IS_FINISH_ONBOARDING_KEY = preferencesKey<Boolean>("ONBOARDING_FINISH")
    }

    suspend fun storeUser(firstname: String,
                          lastname: String,
                          email: String, phoneNo: String, description: String, address: String){
        dataStore.edit {
            it[FIRST_NAME_KEY] = firstname
            it[LAST_NAME_KEY] = lastname
            it[USER_EMAIL_KEY] = email
            it[USER_PHONE_NO_KEY] = phoneNo
            it[DESCRIPTION_KEY] = description
            it[ADDRESS_KEY] = address
        }
    }
//    suspend fun onboardingFinished(isFinished: Boolean){
//        dataStore.edit {
//            it[USER_IS_FINISH_ONBOARDING_KEY] = isFinished
//        }
//    }
    val firstnameFlow: Flow<String> = dataStore.data.map {
        it[FIRST_NAME_KEY] ?: ""
    }
    val emailFlow: Flow<String> = dataStore.data.map {
        it[USER_EMAIL_KEY] ?: ""
    }
    val lastnameFlow: Flow<String> = dataStore.data.map {
        it[LAST_NAME_KEY] ?: ""
    }
    val descriptionFlow: Flow<String> = dataStore.data.map {
        it[DESCRIPTION_KEY] ?: ""
    }
    val addressFlow: Flow<String> = dataStore.data.map {
        it[ADDRESS_KEY] ?: ""
    }
    val phoneNoFlow: Flow<String> = dataStore.data.map {
        it[USER_PHONE_NO_KEY] ?: ""
    }
//    val isFinishedFlow: Flow<Boolean> = dataStore.data.map {
//        it[USER_IS_FINISH_ONBOARDING_KEY] ?: false
//    }
}