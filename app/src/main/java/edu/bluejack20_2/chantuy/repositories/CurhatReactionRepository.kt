package edu.bluejack20_2.chantuy.repositories

import com.google.firebase.firestore.*
import edu.bluejack20_2.chantuy.models.CurhatReaction

class CurhatReactionRepository {
    companion object {
        private val COLLECTION_NAME = "curhats"

        fun addLikeReaction(curhatId: String, type: CurhatReaction, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val curhatRef = db.collection(COLLECTION_NAME).document(curhatId)
            val userId = UserRepository.getCurrentUserId()

            db.runTransaction { transaction ->
                removeUserFromLikeDislikeIfExist(transaction, userId, curhatRef)
                addUserToLikeList(transaction, curhatRef, userId, type)
            }.addOnSuccessListener {
                curhatRef.get().addOnSuccessListener {
                    it.reference.update("likeCount", getLikeCount(it))
                    callback()
                }
            }
        }

        fun addDislikeReaction(curhatId: String, type: CurhatReaction, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val curhatRef = db.collection(COLLECTION_NAME).document(curhatId)
            val userId = UserRepository.getCurrentUserId()

            db.runTransaction { transaction ->
                removeUserFromLikeDislikeIfExist(transaction, userId, curhatRef)
                addUserToDislikeList(transaction, curhatRef, userId, type)
            }.addOnSuccessListener {
                curhatRef.get().addOnSuccessListener {
                    it.reference.update("dislikeCount", getDislikeCount(it))
                    callback()
                }
            }
        }

        fun addUserToLikeList(transaction: Transaction, curhatRef: DocumentReference, userId: String, type: CurhatReaction) {
            if (type == CurhatReaction.THUMB_UP) {
                transaction.update(curhatRef,"usersGiveThumbUp", FieldValue.arrayUnion(userId))
            } else if (type == CurhatReaction.COOL) {
                transaction.update(curhatRef,"usersGiveCool", FieldValue.arrayUnion(userId))
            } else  {
                transaction.update(curhatRef,"usersGiveLove", FieldValue.arrayUnion(userId))
            }
        }

        fun addUserToDislikeList(transaction: Transaction, curhatRef: DocumentReference, userId: String, type: CurhatReaction) {
            if (type == CurhatReaction.THUMB_DOWN) {
                transaction.update(curhatRef,"usersGiveThumbDowns", FieldValue.arrayUnion(userId))
            } else  {
                transaction.update(curhatRef,"usersGiveAngry", FieldValue.arrayUnion(userId))
            }
        }

        fun removeUserFromLikeDislikeIfExist(transaction: Transaction, userId: String, curhatRef: DocumentReference) {
            transaction.update(curhatRef,"usersGiveThumbUp", FieldValue.arrayRemove(userId))
            transaction.update(curhatRef,"usersGiveLove", FieldValue.arrayRemove(userId))
            transaction.update(curhatRef,"usersGiveCool", FieldValue.arrayRemove(userId))
            transaction.update(curhatRef,"usersGiveThumbDowns", FieldValue.arrayRemove(userId))
            transaction.update(curhatRef,"usersGiveAngry", FieldValue.arrayRemove(userId))
        }

        fun getLikeCount(snap: DocumentSnapshot): Int {
            val thumbCount = (snap.get("usersGiveThumbUp") as List<String>).size
            val coolCount = (snap.get("usersGiveCool") as List<String>).size
            val loveCount = (snap.get("usersGiveLove") as List<String>).size
            return thumbCount + coolCount + loveCount
        }

        fun getDislikeCount(snap: DocumentSnapshot): Int {
            val thumbDownCount = (snap.get("usersGiveThumbDowns") as List<String>).size
            val angryCount = (snap.get("usersGiveAngry") as List<String>).size
            return thumbDownCount + angryCount
        }
    }
}