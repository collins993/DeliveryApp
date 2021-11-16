package io.github.collins993.deliveryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.github.collins993.deliveryapp.models.DeliveryModel
import io.github.collins993.deliveryapp.utils.Resource
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {

    private var  auth: FirebaseAuth? = null
    private var  orderCollectionReference: CollectionReference? = null
    private var firebaseUserId: String = ""


    //
    private val _registrationStatus = MutableLiveData<Resource<String>>()
    val registrationStatus: LiveData<Resource<String>> = _registrationStatus
    //
    private val _signInStatus = MutableLiveData<Resource<String>>()
    val signInStatus: LiveData<Resource<String>> = _signInStatus
    //
    private val _signOutStatus = MutableLiveData<Resource<String>>()
    //
    private val _resetPasswordStatus = MutableLiveData<Resource<String>>()
    val  resetPasswordStatus: LiveData<Resource<String>> = _resetPasswordStatus
    //
    private val _addOrderStatus = MutableLiveData<Resource<String>>()
    val  addOrderStatus: LiveData<Resource<String>> = _addOrderStatus
    //
    private val _getOrderStatus = MutableLiveData<List<DeliveryModel?>>()
    val getOrderStatus: LiveData<List<DeliveryModel?>> = _getOrderStatus
    //
    private val _getDriverDeliveryOrderStatus = MutableLiveData<List<DeliveryModel?>>()
    val getDriverDeliveryOrderStatus: LiveData<List<DeliveryModel?>> = _getDriverDeliveryOrderStatus
    //
    private val _getDriverScheduleOrderStatus = MutableLiveData<List<DeliveryModel?>>()
    val getDriverScheduleOrderStatus: LiveData<List<DeliveryModel?>> = _getDriverScheduleOrderStatus

    init {
        auth = FirebaseAuth.getInstance()
        orderCollectionReference = Firebase.firestore.collection("delivery_orders")
    }

    fun signUp(email:String, password:String){
        viewModelScope.launch{
            val  errorCode = -1
            _registrationStatus.postValue(Resource.Loading())
            try{
                auth?.let { authentication ->
                    authentication.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task: Task<AuthResult> ->
                            if(!task.isSuccessful){
                                println("Registration Failed with ${task.exception}")
                                _registrationStatus.postValue(Resource.Error("Registration Failed with ${task.exception}"))
                            }else{
                                _registrationStatus.postValue(Resource.Success("UserCreated"))

                            }
                        }

                }
            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _registrationStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _registrationStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun signIn(email:String, password:String){
        viewModelScope.launch{
            var  errorCode = -1
            _signInStatus.postValue(Resource.Loading())
            try{
                auth?.let{ login->
                    login.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task: Task<AuthResult> ->
                            if(!task.isSuccessful){
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(Resource.Error("Login Failed with ${task.exception}"))
                            }else{
                                _signInStatus.postValue(Resource.Success("Login Successful"))

                            }
                        }

                }

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _signInStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _signInStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun signOut(){
        viewModelScope.launch{
            var  errorCode = -1
            _signOutStatus.postValue(Resource.Loading())
            try{
                auth?.currentUser?.let {
                    it.let { auth!!.signOut() }
                    _signOutStatus.postValue(Resource.Success("Signed Out Successfully"))
                }
            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _signOutStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _signOutStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            var  errorCode = -1
            _resetPasswordStatus.postValue(Resource.Loading())
            try{
                auth?.let{ resetPassword->
                    if (resetPassword.currentUser != null){
                        resetPassword.sendPasswordResetEmail(email)
                            .addOnCompleteListener {task ->
                                if(!task.isSuccessful){
                                    //println("Login Failed with ${task.exception}")
                                    _resetPasswordStatus.postValue(Resource.Error("Password Reset Failed with ${task.exception}"))
                                }else{
                                    _resetPasswordStatus.postValue(Resource.Success("Password Reset Successful"))

                                }
                            }
                    }
                }

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _resetPasswordStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _resetPasswordStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun addOrder(deliveryModel: DeliveryModel){
        viewModelScope.launch {
            var errorCode = -1
            _addOrderStatus.postValue(Resource.Loading())
            try {
                auth?.currentUser?.let {
                    orderCollectionReference?.add(deliveryModel)?.addOnSuccessListener {
                        _addOrderStatus.postValue(Resource.Success("Order Added"))
                    }?.addOnFailureListener {
                        _addOrderStatus.postValue(Resource.Error("Error Adding Order"))
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _addOrderStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _addOrderStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }
            }
        }
    }

    fun retrieveAllOrders(){
        viewModelScope.launch {
            var errorCode = -1
            try {

                firebaseUserId = auth?.currentUser?.uid.toString()
                orderCollectionReference?.whereEqualTo("userId",firebaseUserId)
                    ?.addSnapshotListener { value, error ->
                        if (value != null){
                            val order = value.toObjects(DeliveryModel::class.java)
                            _getOrderStatus.postValue(order)
                        }
                    }


            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getOrderStatus.postValue(Resource.Error(e.toString()))
                } else {
                    //_getOrderStatus.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun retrieveAllDriverDeliveryOrders(){
        viewModelScope.launch {
            var errorCode = -1
            try {

                orderCollectionReference?.whereEqualTo("scheduleOrder","Immediately")
                    ?.addSnapshotListener { value, error ->
                        if (value != null){
                            val allOrders = value.toObjects(DeliveryModel::class.java)
                            _getDriverDeliveryOrderStatus.postValue(allOrders)

//                            for (order in allOrders){
//                                val scheduleOrder = order.scheduleOrder
//                                if (scheduleOrder == "Immediately") {
//
//                                    val list = ArrayList<DeliveryModel?>()
//                                    list.add(order)
//
//
//                                }
//                                else {
//                                    val scheduledList = ArrayList<DeliveryModel?>()
//                                    scheduledList.add(order)
//                                    _getDriverScheduleOrderStatus.postValue(scheduledList)
//                                }
//                            }

                        }
                    }


            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getOrderStatus.postValue(Resource.Error(e.toString()))
                } else {
                    //_getOrderStatus.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun retrieveAllDriverScheduledDelivery(){
        viewModelScope.launch {
            var errorCode = -1
            try {

                orderCollectionReference?.whereEqualTo("scheduleOrder","Scheduled")
                    ?.addSnapshotListener { value, error ->
                        if (value != null){
                            val allOrders = value.toObjects(DeliveryModel::class.java)
                            _getDriverScheduleOrderStatus.postValue(allOrders)

                        }
                    }


            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getOrderStatus.postValue(Resource.Error(e.toString()))
                } else {
                    //_getOrderStatus.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }


}