package com.example.shoppingmall_app.contentList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingmall_app.R
import com.example.shoppingmall_app.utils.FBAuth
import com.example.shoppingmall_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {
    lateinit var myRef: DatabaseReference
    //북마크 key값을 담을 리스트
    val bookmarkIdList = mutableListOf<String>()
    lateinit var rvAdapter: ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()
        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

        // Write a message to the database
        val database = Firebase.database

        val category = intent.getStringExtra("category")
        if (category == "categoryALl") {
            myRef = FBRef.conetent.child("categoryALl")
        } else if (category == "categoryLip") {
            myRef = FBRef.conetent.child("categoryLip")
        } else if (category == "categoryBlusher") {
            myRef = FBRef.conetent.child("categoryBlusher")
        } else if (category == "categoryMascara") {
            myRef = FBRef.conetent.child("categoryMascara")
        } else if (category == "categoryNail") {
            myRef = FBRef.conetent.child("categoryNail")
        } else if (category == "categoryShadow") {
            myRef = FBRef.conetent.child("categoryShadow")
        } else if (category == "categorySkin") {
            myRef = FBRef.conetent.child("categorySkin")
        } else if (category == "categorySun") {
            myRef = FBRef.conetent.child("categorySun")
        }

        //파이어베이스 데이터 읽기
        val postListener = object : ValueEventListener {
            //Content 데이터읽기
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    //contents id값 가져오기
                    Log.d("ContentListActivity", dataModel.key.toString())
                    //ContentModel의 형태로 데이터를 받아옴
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    //contentKey 를 데이터베이스에 넣어줌
                    itemKeyList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

        //RecyclerView와 Adapter 연결하기
        val rv: RecyclerView = findViewById(R.id.rv)
        rv.adapter = rvAdapter
        //RecyclerView 3줄로 나오게 하기
        rv.layoutManager = GridLayoutManager(this, 2)
        getBookmarkData()

        //데이터 넣기
//        val categoryLip = FBRef.conetent
//        categoryLip.child("categoryLip").push().setValue(
//            ContentModel("매트립스틱 잘 바르는 법",
//                "https://post-phinf.pstatic.net/MjAxOTExMTVfMTI2/MDAxNTczNzg5NjIzMTI5.pQ1e21r1deZwNkakjcY1wJT8lmqf4vIpQpleMWoTVLkg.mWYGqd5ChB7LtLqROtTcYC7c067V63vur4W5mCvqsG8g.JPEG/mug_obj_157378962327073497.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.nhn?volumeNo=26876588&memberNo=19207333&vType=VERTICAL")
//        )
//        categoryLip.child("categoryLip").push().setValue(
//            ContentModel("오버립 연출하는 방법",
//                "https://post-phinf.pstatic.net/MjAxOTA5MjVfMjE0/MDAxNTY5Mzc0NzI3NzE4.e8jXkfzZjcrqChlQ8bOdfwN29n-fZXD92LniMhqDowsg.a4w8qbUpZuvVQB8xqDe6bEowIhvlZyn6Mg_rkqGL-dkg.PNG/%EA%B7%B8%EB%A6%BC5.png?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=25731052&memberNo=19207333&searchKeyword=%EB%A6%BD&searchRank=1")
//        )
//        categoryLip.child("categoryLip").push().setValue(
//            ContentModel("누드립 6종 추천",
//                "https://post-phinf.pstatic.net/MjAyMTAyMThfMjM1/MDAxNjEzNjI5Njg2MzM4.zLd5Wf-P4By1cRfoRFGVcoUneW4IrVLGPiT2XPkEgVUg.vAv_s2PQjHQhQiC67cLBdEjvNLl9QtQg1hXp7ATdRicg.PNG/%EA%B7%B8%EB%A6%BC4.png?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=30746632&memberNo=19207333&searchRank=51")
//        )
//        categoryLip.child("categoryLip").push().setValue(
//            ContentModel("분위기 좋은 립 추천",
//                "https://post-phinf.pstatic.net/MjAyMjA1MTJfMTk2/MDAxNjUyMzM4NTYzODQy.08d77pQ0s9eVfrh3o2cmZwTP6K-7C4VncKSD6kyY3GYg.p-KNCNGnAUfAaaHnh2ywwbfzUpjcD7VL5CVXbw2lzpMg.JPEG/2_3ce.jpg?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=33786606&memberNo=19207333&searchKeyword=%EB%A6%BD&searchRank=1")
//        )
//
//        val categoryBlusher = FBRef.conetent
//        categoryBlusher.child("categoryBlusher").push().setValue(
//            ContentModel("블러셔 위치별 이미지",
//                "https://post-phinf.pstatic.net/MjAxNzA1MDJfNTgg/MDAxNDkzNzEzNjk1MzQ2.xFSswW-ptzt7LfoNCl-9V7AOkhOzZ9NCCyc8bfBJossg.m3hRvEQmIRLQIClvHm0wO30HAKoLuCX44AbZu1p2E_Ig.PNG/mug_obj_149371398747024225.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=7368749&memberNo=19207333&clipNo=10&searchKeyword=%EB%B8%94%EB%9F%AC%EC%85%94&searchRank=10")
//        )
//        categoryBlusher.child("categoryBlusher").push().setValue(
//            ContentModel("여름 블러셔 추천",
//                "https://post-phinf.pstatic.net/MjAyMDA4MDZfMjM1/MDAxNTk2Njk4MTk3OTE4.7VCdLQxUi0LPQpeIRG3B2KuTM85n47e72HVJeSLraawg.vJTHigIrFlDNQ2oR2jumL7vL8GPqNjPAoeQl5VScCM8g.JPEG/mug_obj_15966981983577400.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=29005450&memberNo=19207333&searchKeyword=%EB%B8%94%EB%9F%AC%EC%85%94&searchRank=14")
//        )
//        categoryBlusher.child("categoryBlusher").push().setValue(
//            ContentModel("블러셔 발색 방법",
//                "https://post-phinf.pstatic.net/MjAxODAyMDlfMjI4/MDAxNTE4MTUzNDIxMTQ0.IKJjrx5D_vKebj_QUbTLWwZh5wokHaf2DfAo8dmn77sg.9tA6oPYFCevuN4nyuroyz8ccuted0S097ggaHUtNdBog.JPEG/mug_obj_151815342353475235.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=12905090&memberNo=19207333&searchKeyword=%EB%B8%94%EB%9F%AC%EC%85%94&searchRank=15")
//        )
//        categoryBlusher.child("categoryBlusher").push().setValue(
//            ContentModel("핑크 블러셔 9종 추천",
//                "https://post-phinf.pstatic.net/MjAxNzA5MTRfMTg4/MDAxNTA1Mzc3OTk3NDA4.N9zDJs5FGZjGrgr5ftQn433f71JbHYUeAxDrM31kjhMg.tl5knBh4sCAnt7MxmnSq1w4VvTlGdAOkZ2c1S9ixB7Mg.GIF/2_%EC%95%84%EB%A6%AC%ED%95%91%ED%81%AC.gif?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=9596436&memberNo=19207333&searchKeyword=%EB%B8%94%EB%9F%AC%EC%85%94&searchRank=17")
//        )
//
//
//        val categoryMascara = FBRef.conetent
//        categoryMascara.child("categoryMascara").push().setValue(
//            ContentModel("마스카라 정복하는 법",
//                "https://post-phinf.pstatic.net/MjAyMDA5MjJfMjEw/MDAxNjAwNzQxOTIyMjg2.7nc5U6FDPa8zuNomhuLakDHWC43f9E1HIMJXkLZxp44g.boSXEWZsh_PLMF5JFGp4_KmeubO9mQbh9vg-P7HuYzIg.JPEG/video_1599793655.00_01_17_20.jpg?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=29515652&memberNo=19207333&searchKeyword=%EB%A7%88%EC%8A%A4%EC%B9%B4%EB%9D%BC&searchRank=2")
//        )
//        categoryMascara.child("categoryMascara").push().setValue(
//            ContentModel("속눈썹 픽서 추천",
//                "https://post-phinf.pstatic.net/MjAxNzAyMTRfMTM3/MDAxNDg3MDMxODM4NjY0.8HSA55ASVeKwbRUldGGpOgPG_vmwlLEVkk_6NiueDmEg.LbmTy4GQ008CP5lUT9WheyC1f7DTOi-SoSecfJwhfRUg.PNG/mug_obj_148703201095161712.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=6451867&memberNo=19207333&clipNo=4&searchKeyword=%EB%A7%88%EC%8A%A4%EC%B9%B4%EB%9D%BC&searchRank=3")
//        )
//        categoryMascara.child("categoryMascara").push().setValue(
//            ContentModel("인기 마스카라 3종 비교",
//                "https://post-phinf.pstatic.net/20150710_50/markers7_14365220720090obQ1_PNG/mug_obj_143652207423983019.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=1861804&memberNo=19207333&clipNo=4&searchKeyword=%EB%A7%88%EC%8A%A4%EC%B9%B4%EB%9D%BC&searchRank=5")
//        )
//        categoryMascara.child("categoryMascara").push().setValue(
//            ContentModel("뷰러, 마스카라 사용법",
//                "https://post-phinf.pstatic.net/MjAxODA1MzBfNDcg/MDAxNTI3NjY3OTMzMDkw.a1v64PLAVRbE23uCgDjoYYLJ_6-f2lQ-xKqLbMQ1RV0g.yU-EIRwFbEOXiAvu6cHoAwk5GDolPBcW4tzgrbl-E2sg.JPEG/mug_obj_152766793341028620.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=15873260&memberNo=19207333&searchKeyword=%EB%A7%88%EC%8A%A4%EC%B9%B4%EB%9D%BC&searchRank=1")
//        )
//
//        val categoryNail = FBRef.conetent
//        categoryNail.child("categoryNail").push().setValue(
//            ContentModel("여름에 핫한 디자인",
//                "https://post-phinf.pstatic.net/MjAyMDA2MDJfMzIg/MDAxNTkxMDU2NzAwOTA4._ouHqfv6HVk1d-QZKhdlTL5h44b6OyU7oxptWivdID4g.pLuyvy7e9xGfbqfOye8Ijrcv9cEoD23N7emiaoM5nk0g.JPEG/%EB%8C%80%ED%91%9C%EC%82%AC%EC%A7%84.jpg?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=28426340&memberNo=19207333&searchKeyword=%EB%84%A4%EC%9D%BC&searchRank=1")
//        )
//        categoryNail.child("categoryNail").push().setValue(
//            ContentModel("셀프 젤네일 하는 법",
//                "https://post-phinf.pstatic.net/MjAyMDA5MDlfNzYg/MDAxNTk5NjE0MzM4OTUy.vIxDKLPccmsg8x5lj3AlPMxP0K8jhKQTtZkWezbwgoog.PfkX_n-E4qcanEddjsQarMfN1XHGJB4tmw9Vm4wX4qwg.JPEG/0_%EC%85%80%ED%94%84%EB%84%A4%EC%9D%BC_%EB%96%BC%EC%83%B7.jpg?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=29382659&memberNo=19207333&searchKeyword=%EB%84%A4%EC%9D%BC&searchRank=3")
//        )
//        categoryNail.child("categoryNail").push().setValue(
//            ContentModel("연예인이 하고 온 네일",
//                "https://post-phinf.pstatic.net/20160824_6/markers7_1472027171895hWKUk_JPEG/mug_obj_14720274007362378.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=4901346&memberNo=19207333&searchKeyword=%EB%84%A4%EC%9D%BC&searchRank=5")
//        )
//        categoryNail.child("categoryNail").push().setValue(
//            ContentModel("트와이스 셀프네일",
//                "https://post-phinf.pstatic.net/20160628_183/markers7_1467100103570Kp5yY_JPEG/mug_obj_146710032381099872.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=4543338&memberNo=19207333&clipNo=12&searchKeyword=%EB%84%A4%EC%9D%BC&searchRank=6")
//        )
//
//        val categoryShadow = FBRef.conetent
//        categoryShadow.child("categoryShadow").push().setValue(
//            ContentModel("애교살 섀도우 8종 발색",
//                "https://post-phinf.pstatic.net/MjAxNzA4MjFfMjMw/MDAxNTAzMjgyMTQ3OTA2.l5I7PKnv4mw6fiWTWhwUD9LJqWrjQkrPTZw3iGIryo0g.x644rsN6LPAe_B4GxJdTsbj0EoRNN8ZA6YBZJ13MwIwg.JPEG/mug_obj_150328215446456734.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=28426340&memberNo=19207333&searchKeyword=%EB%84%A4%EC%9D%BC&searchRank=1")
//        )
//        categoryShadow.child("categoryShadow").push().setValue(
//            ContentModel("음영 섀도우 추천",
//                "https://post-phinf.pstatic.net/20160325_53/markers7_1458888514245CfACe_PNG/mug_obj_145888854651568540.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=3880900&memberNo=19207333&clipNo=4&searchKeyword=%EC%84%80%EB%8F%84%EC%9A%B0&searchRank=2")
//        )
//        categoryShadow.child("categoryShadow").push().setValue(
//            ContentModel("소장가치 있는 섀도우",
//                "https://post-phinf.pstatic.net/MjAxODAxMzBfMjE2/MDAxNTE3MjkyNTU4NjE4.6iu55FdImapCTJBdtRvJNTSehGVYFslbUFoLxyCkQVQg.1kdt-F73y5CbVa0d-Ei-emx_2zACcvUDH7YtTlqd-_Ug.JPEG/mug_obj_151729256077638363.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=29467752&memberNo=19207333&searchKeyword=%EC%84%80%EB%8F%84%EC%9A%B0&searchRank=4")
//        )
//        categoryShadow.child("categoryShadow").push().setValue(
//            ContentModel("글리터 섀도우 12종",
//                "https://post-phinf.pstatic.net/20160628_183/markers7_1467100103570Kp5yY_JPEG/mug_obj_146710032381099872.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=12598688&memberNo=19207333&searchKeyword=%EC%84%80%EB%8F%84%EC%9A%B0&searchRank=5")
//        )
//
//        val categorySkin = FBRef.conetent
//        categorySkin.child("categorySkin").push().setValue(
//            ContentModel("수부지 모닝케어 팁",
//                "https://post-phinf.pstatic.net/MjAxNzA0MDRfMTk3/MDAxNDkxMjkzNjAwNTQ5.Ps-4IdURi-Et_5-pzZ2UaRgd2bQzPOAZfu1vie7-Osgg.3i4xomIs_tYn62RN14NyuSuNyEK8sDz4ykHpEq5GWwkg.PNG/mug_obj_149129380894468463.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=6963169&memberNo=19207333&searchKeyword=%EC%8A%A4%ED%82%A8&searchRank=1")
//        )
//        categorySkin.child("categorySkin").push().setValue(
//            ContentModel("남성 스킨 루틴",
//                "https://post-phinf.pstatic.net/20151221_285/markers7_1450691453868I9m5S_PNG/mug_obj_145069149185948289.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=3215030&memberNo=19207333&clipNo=4&searchKeyword=%EC%8A%A4%ED%82%A8&searchRank=2")
//        )
//        categorySkin.child("categorySkin").push().setValue(
//            ContentModel("수분크림 사용법",
//                "https://post-phinf.pstatic.net/20150925_114/markers7_1443141881158MoJ43_PNG/mug_obj_144314187660855779.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=2464298&memberNo=19207333&clipNo=3&searchKeyword=%EC%8A%A4%ED%82%A8&searchRank=4")
//        )
//        categorySkin.child("categorySkin").push().setValue(
//            ContentModel("환절기 스킨케어 루틴",
//                "https://post-phinf.pstatic.net/MjAxOTA0MDRfMTYx/MDAxNTU0Mzc2ODI0NTEy.sNZHc1Xkp6LkhhoCemIQYnDuh1jTg0ZLftD4xnGp1WAg.KtWjhWs_k5I0HpJf9qbV-pUTgqPw6pfqTPLcsSu8xc4g.PNG/mug_obj_155437682500346145.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=18911020&memberNo=19207333&searchKeyword=%EC%8A%A4%ED%82%A8&searchRank=8")
//        )
//
//        val categorySun = FBRef.conetent
//        categorySun.child("categorySun").push().setValue(
//            ContentModel("여름 선크림 추천",
//                "https://post-phinf.pstatic.net/MjAyMDA0MDdfNDEg/MDAxNTg2MjM3ODA2NjQ0.KWq9J_uoZ7ErGfOAQnljwkLcfIusPcFI0rGHf6iI274g.D2HQ0182nWNAN2pCpLZsQrR1erJoQPCfKXmb0i7Y8SYg.PNG/0_%ED%91%9C%EC%A7%80.png?type=w1200" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=27928524&memberNo=19207333&searchKeyword=%EC%84%A0%ED%81%AC%EB%A6%BC&searchRank=2")
//        )
//        categorySun.child("categorySun").push().setValue(
//            ContentModel("나에게 맞는 선크림 찾기",
//                "https://post-phinf.pstatic.net/MjAxNzA3MTlfMTU3/MDAxNTAwNDI5ODQyODU3.QsiRYhQOus9RYzrA1A58oqDdmjX_Grevg48Oee7lpE4g.j63QLA2EogN-BqBpo-mo6kDTRaFeBZmb1ku8w52d8Mgg.PNG/mug_obj_150043011939035341.png?type=w10800" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=8711232&memberNo=19207333&searchKeyword=%EC%84%A0%ED%81%AC%EB%A6%BC&searchRank=3")
//        )
//        categorySun.child("categorySun").push().setValue(
//            ContentModel("미세먼지 막는 선크림",
//                "https://post-phinf.pstatic.net/MjAxNzA2MTJfMTg5/MDAxNDk3MjUwNDgwNjI4.5J8xt-7xH6ilx4U-G9L2bnD341zeQ2ZsVSayiyBhiH8g.dNvE2vz1HvXdxwA7RsNqPn9SBK99QhcpJppeJfMPltgg.JPEG/mug_obj_149725073410020528.jpg?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=8096247&memberNo=19207333&searchKeyword=%EC%84%A0%ED%81%AC%EB%A6%BC&searchRank=14")
//        )
//        categorySun.child("categorySun").push().setValue(
//            ContentModel("톤업 선크림 추천",
//                "https://post-phinf.pstatic.net/MjAxOTA4MDZfMTU1/MDAxNTY1MDcwMTYxMzQ3.v5b_BncB1-xDR_5Ef2qxPLZhaODl9sTJdEsFGAXjtPUg.QQRyI1t9hU4-ssDAE4b8u3LGP4ffbvx336X2Sm7tlbAg.PNG/mug_obj_156507016218854324.png?type=w1080" ,
//                "https://post.naver.com/viewer/postView.naver?volumeNo=23217599&memberNo=19207333&searchRank=25")
//        )



    }
    private fun getBookmarkData(){
        //파이어베이스 데이터 읽기(북마크)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //데이터가 변경될 때 기존에 데이터는 clear 해주고, 다시 값을 받아줌
                bookmarkIdList.clear()

                for (dataModel in dataSnapshot.children) {
                    bookmarkIdList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged()
                Log.d("북마크데이터", bookmarkIdList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}