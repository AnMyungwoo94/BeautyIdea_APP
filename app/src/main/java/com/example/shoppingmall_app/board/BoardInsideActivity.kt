package com.example.shoppingmall_app.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.shoppingmall_app.R
import com.example.shoppingmall_app.comment.CommentLVAdapter
import com.example.shoppingmall_app.comment.CommentModel
import com.example.shoppingmall_app.contentList.ContentModel
import com.example.shoppingmall_app.databinding.ActivityBoardInsideBinding
import com.example.shoppingmall_app.utils.FBAuth
import com.example.shoppingmall_app.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {
    private val TAG = BoardInsideActivity::class.java.simpleName
    private  lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String
    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside )

        //1번째 방법으로 실행했을때
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text = time

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }
        //2번째 방법으로 값 받아오기
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }
        getCommentData(key)

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter

    }

    //comment 데이터값 가져오기
    fun getCommentData(key: String){
        val postListener = object : ValueEventListener {
            //Content 데이터읽기
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()

                for (dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
            }
                commentAdapter.notifyDataSetChanged()
        }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key: String) {
        FBRef.commentRef.child(key).push()
            .setValue(CommentModel(binding.commentArea.text.toString(), FBAuth.getTime() ))
        Toast.makeText(this, "댓글 작성완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }
    //수정,삭제 다이얼로그 띄우기
    private fun showDialog(){

        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = builder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정버튼을 눌렀습니다." ,Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            finish()
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료" ,Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private  fun getImageData(key: String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(
            OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(this)
                        .load(task.result)
                        .into(imageViewFromFB)
                }else{
                    //이미지가 없으면 이미지 칸은 나오지 않음
                        binding.getImageArea.isVisible = false
                }
            }
        )
    }
    private fun getBoardData(key: String){
        val postListener = object : ValueEventListener {
            //Content 데이터읽기
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel.title
                    binding.textArea.text = dataModel.content
                    binding.timeArea.text = dataModel.time

                    val myUid = FBAuth.getUid()
                    val writeUid = dataModel.uid

                    if(myUid.equals(writeUid)){
                    binding.boardSettingIcon.isVisible = true

                    }else{

                    }
                }catch(e:Exception){
                    Log.d(TAG, "삭제완료")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
}