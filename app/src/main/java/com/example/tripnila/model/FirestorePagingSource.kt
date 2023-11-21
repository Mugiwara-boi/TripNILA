package com.example.tripnila.model
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.tripnila.data.StaycationBooking
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.QueryDocumentSnapshot
//import com.google.firebase.firestore.QuerySnapshot
//import kotlinx.coroutines.tasks.await
//
//class FirestorePagingSource(private val db: FirebaseFirestore, private val touristId: String) : PagingSource<QuerySnapshot, StaycationBooking>() {
//
//    override fun getRefreshKey(state: PagingState<QuerySnapshot, StaycationBooking>): QuerySnapshot? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, StaycationBooking> {
//        return try {
//            val currentPage = params.key ?: db.collection("staycationBookings")
//                .whereEqualTo("touristId", touristId)
//                .orderBy("bookingDate")
//                .limit(params.loadSize.toLong())
//                .get()
//                .await()
//
//            val nextPage = db.collection("staycationBookings")
//                .whereEqualTo("touristId", touristId)
//                .orderBy("bookingDate")
//                .startAfter(currentPage.documents[currentPage.size() - 1])
//                .limit(params.loadSize.toLong())
//                .get()
//                .await()
//
//            val data = nextPage.documents.map { it.toStaycationBooking() }
//
//            LoadResult.Page(
//                data = data,
//                prevKey = null,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    private fun QueryDocumentSnapshot.toStaycationBooking(): StaycationBooking {
//        // Convert the QueryDocumentSnapshot to a StaycationBooking object
//        // Implement the conversion logic based on your StaycationBooking model
//    }
//}
//
//private fun DocumentSnapshot.toStaycationBooking() {
//    TODO("Not yet implemented")
//}
