package com.psr.quizapp.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.psr.quizapp.MainActivity
import com.psr.quizapp.sign_in
import com.psr.quizapp.sign_up
import com.psr.quizapp.models.User
import utils.Constants

class FireStore {
    private val Firestore=FirebaseFirestore.getInstance()
    fun registerUser(activity: sign_up, user_info: User){
        Firestore.collection(Constants.Users)
            .document(getCurrentUserId())
                .set(user_info, SetOptions.merge())
            .addOnSuccessListener {
                activity.registrationSucces()
            }.addOnFailureListener{
                e->
                Log.e(activity.javaClass.simpleName,"error writing document")
            }

    }
    fun CreateGUser(activity: sign_in, user_info: User){
        Firestore.collection(Constants.Users)
            .document(getCurrentUserId())
            .set(user_info, SetOptions.mergeFields(
                mutableListOf("id","name","email","image")
            ))
            .addOnSuccessListener {
                activity.signInSucces(user_info)
            }.addOnFailureListener{
                    e->
                Log.e(activity.javaClass.simpleName,"error writing document")
            }

    }


    fun getCurrentUserId():String{
        val currentUser= FirebaseAuth.getInstance().currentUser
        var currentUserId:String=""
        if (currentUser!=null) {currentUserId=currentUser.uid}
        return currentUserId
    }

    fun loadUser(activity:Activity){
        Firestore.collection(Constants.Users)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {document->
                val loggedInUser=document.toObject(User::class.java)!!
                when (activity){
                    is sign_in ->activity.signInSucces(loggedInUser)
                }

            }.addOnFailureListener{
                    e->
                Log.e(activity.javaClass.simpleName,"error reading document")
            }

    }
    /*
    fun updateUserProfileData(activity: MyProfile, userHashMap: HashMap<String, Any>) {
        Firestore.collection(Constants.Users) // Collection Name
            .document(getCurrentUserId()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                // Notify the success result.
                activity.profileUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while Updating Profile.",
                    e
                )
                Toast.makeText(activity, "Error while Updating Profile!", Toast.LENGTH_SHORT).show()
            }
    }*/
}